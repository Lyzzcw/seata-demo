package com.lzy.seata.tcc;

import com.lzy.seata.constant.FeignCodes;
import com.lzy.seata.constant.OrderCodes;
import com.lzy.seata.constant.TCCCodes;
import com.lzy.seata.entity.Order;
import com.lzy.seata.entity.Result;
import com.lzy.seata.entity.TransactionRecord;
import com.lzy.seata.mapper.OrderMapper;
import com.lzy.seata.openfeign.StoreFeignService;
import com.lzy.seata.openfeign.TransactionRecordFeignService;
import com.lzy.seata.openfeign.UserFeignService;
import com.lzy.seata.util.IdempotentUtil;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/30
 * Time: 16:09
 * Description: tcc
 *
 * TCC事务模型的三个异常
 * 实现TCC事务模型涉及到的三个异常是不可避免的，实际生产中必须要规避这三大异常。
 *
 * 1、空回滚
 *
 * 定义：在未调用try方法或try方法未执行成功的情况下，就执行了cancel方法进行了回滚。
 *
 * 怎么理解呢？未调用try方法就执行了cancel方法，这个很容易理解，既然没有预留资源，那么肯定是不能回滚。
 *
 * try方法未执行成功是什么意思？
 *
 * 可以看上节中的第一阶段try方法的伪代码，由于try方法开启了本地事务，一旦try方法执行过程中出现了异常，将会导致try方法的本地事务回滚（注意这里不是cancel方法回滚，而是try方法的本地事务回滚），这样其实try方法中的所有操作都将会回滚，也就没有必要调用cancel方法。
 *
 * 但是实际上一旦try方法抛出了异常，那么必定是要调用cancel方法进行回滚，这样就导致了空回滚。
 *
 * 解决方案：
 *
 * 解决逻辑很简单：在cancel方法执行操作之前，必须要知道try方法是否执行成功。
 *
 * 2、幂等性
 *
 * TCC模式定义中提到：如果confirm或者cancel方法执行失败，要一直重试直到成功。
 *
 * 这里就涉及了幂等性，confirm和cancel方法必须保证同一个全局事务中的幂等性。
 *
 * 解决方案：
 *
 * 解决逻辑很简单：对付幂等，自然是要利用幂等标识进行防重操作。
 *
 * 3、悬挂
 *
 * 事务协调器在调用 TCC 服务的一阶段 Try 操作时，可能会出现因网络拥堵而导致的超时，此时事务管理器会触发二阶段回滚，调用 TCC 服务的 Cancel 操作，Cancel 调用未超时；
 *
 * 在此之后，拥堵在网络上的一阶段 Try 数据包被 TCC 服务收到，出现了二阶段 Cancel 请求比一阶段 Try 请求先执行的情况，此 TCC 服务在执行晚到的 Try 之后，将永远不会再收到二阶段的 Confirm 或者 Cancel ，造成 TCC 服务悬挂。
 *
 * 解决方案：
 *
 * 解决逻辑很简单：在执行try方法操作资源之前判断cancel方法是否已经执行；同样的在cancel方法执行后要记录执行的状态。
 *
 * 4、总结
 *
 * 针对以上三个异常，落地的解决方案很多，比如维护一个事务状态表，每个事务的执行阶段全部记录下来。
 *
 * 幂等：在执行confirm或者cancel之前根据事务状态表查询当前全局事务是否已经执行过confirm或者cancel方法
 * 空回滚：在执行cancel之前才能根据事务状态表查询当前全局事务是否已经执行成功try方法
 * 悬挂：在执行try方法之前，根据事务状态表查询当前全局事务是否已经执行过cancel方法
 */
@Service
@Slf4j
public class TCCOrderServiceImpl implements TCCOrderService{

    @Autowired
    private TransactionRecordFeignService transactionRecordFeignService;
    @Autowired
    private StoreFeignService storeFeignService;
    @Autowired
    private UserFeignService userFeignService;
    @Resource
    private OrderMapper orderMapper;

    @Transactional
    @Override
    public boolean tryCreate(BusinessActionContext businessActionContext,
                             String userId, Long productId, String orderId,Long num,Long money) {
        log.info("-------开始第一阶段的事务，事务组XID：{}-------",businessActionContext.getXid());
        //判断cancel阶段是否已经执行过 --- 事务日志
        //FIXME 防止悬挂异常，从事务日志表中获取全局事务ID的状态，如果是cancel状态则不执行。
        Result<TransactionRecord> result = transactionRecordFeignService
                .getByXid(businessActionContext.getXid());
        if(!StringUtils.equals(result.getCode(), FeignCodes.SUCCESS.getStatus())){
            throw new RuntimeException("事务日志查询失败！");
        }
        // 1 try 2 commit 3 cancel
        TransactionRecord record = result.getData();
        if(record != null && record.getStatus() == TCCCodes.TRY.getStatus()){
            throw new RuntimeException("已经进入了TCC 二阶段 cancel,不允许try阶段");
        }

        //开始try ---- 冻结库存
        //1.冻结库存
        String frozenStoreResult = storeFeignService.frozen(productId,num);
        if(!StringUtils.equals(frozenStoreResult,FeignCodes.SUCCESS.getStatus())){
            throw new RuntimeException("冻结库存失败");
        }
        //2.冻结资金
        String frozenUserResult = userFeignService.frozen(userId,money);
        if(!StringUtils.equals(frozenUserResult,FeignCodes.SUCCESS.getStatus())){
            throw new RuntimeException("冻结资金失败");
        }
        //3.生成订单 -- 未完成状态
        Order order = Order.builder()
                .orderId(orderId)
                .productId(productId)
                .createTime(LocalDateTime.now())
                .num(num)
                .status(OrderCodes.UNCONFIRMED.getStatus())
                .build();
        orderMapper.insert(order);

        //保证幂等性 (在cancel或commit二阶段 后移除)
        //FIXME 向幂等工具类中添加一个标记，key为当前类和全局事务ID，value为当前时间戳。
        IdempotentUtil.add(getClass(), businessActionContext.getXid(), System.nanoTime());
        return true;
    }

    @Transactional
    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        //检验幂等性，防止重复提交
        //FIXME 处的代码从幂等工具类中根据当前类和全局事务ID获取值，由于try阶段执行成功会向其中添加值，confirm方法执行成功会移出这个值，因此在confirm开头判断这个值是否存在就起到了幂等效果，防止重试的效果。
        if(IdempotentUtil.get(getClass(),businessActionContext.getXid()) == null){
            //注意返回值，返回false时将会重试
            return true;
        }

        log.info("-------开始第二阶段的commit事务，事务组XID：{}-------",businessActionContext.getXid());
        Long productId = Long.parseLong(businessActionContext.getActionContext("productId").toString());
        Long num = Long.parseLong(businessActionContext.getActionContext("num").toString());
        //释放冻结库存
        String commitFrozenStoreResult = storeFeignService.commitFrozen(productId,num);
        if(!StringUtils.equals(commitFrozenStoreResult,FeignCodes.SUCCESS.getStatus())){
            //二阶段逻辑上不允许失败 ,返回false重试 这里其实也要做一下幂等 防止后续操作失败重复释放
            return false;
        }
        //释放冻结资金
        String userId = businessActionContext.getActionContext("userId").toString();
        Long money = Long.parseLong(businessActionContext.getActionContext("money").toString());
        String commitFrozenUserResult = userFeignService.commitFrozen(userId,money);
        if(!StringUtils.equals(commitFrozenUserResult,FeignCodes.SUCCESS.getStatus())){
            //二阶段逻辑上不允许失败 ,返回false重试
            return false;
        }
        //修改订单状态为已完成
        String orderId = businessActionContext.getActionContext("orderId").toString();
        Order order = orderMapper.findByOrderId(orderId);
        order.setStatus(OrderCodes.COMPLETED.getStatus());
        orderMapper.updateByPrimaryKeySelective(order);
        //提交成功,移除幂等校验
        //FIXME 从幂等工具类中移出try方法中添加的值。
        IdempotentUtil.remove(getClass(), businessActionContext.getXid());
        return true;
    }

    @Transactional
    @Override
    public boolean cancel(BusinessActionContext businessActionContext) {
        //防止悬挂 ,只要cancel 就插入一条事务日志
        TransactionRecord record = TransactionRecord.builder()
                .xid(businessActionContext.getXid())
                .status(TCCCodes.CANCEL.getStatus())
                .build();
        //这个操作也必须保证幂等性，添加多次和一次效果相同
        Result<TransactionRecord> result = transactionRecordFeignService.add(record);
        if(!StringUtils.equals(result.getCode(),FeignCodes.SUCCESS.getStatus())){
            return false;
        }
        //校验幂等性，空回滚
        if(IdempotentUtil.get(getClass(),businessActionContext.getXid()) == null){
            return true;
        }

        log.info("-------开始第二阶段的cancel事务，事务组XID：{}-------",businessActionContext.getXid());
        //恢复冻结的库存
        Long productId = Long.parseLong(businessActionContext.getActionContext("productId").toString());
        Long num = Long.parseLong(businessActionContext.getActionContext("num").toString());
        //释放冻结库存
        String cancelFrozenStoreResult = storeFeignService.cancelFrozen(productId,num);
        if(!StringUtils.equals(cancelFrozenStoreResult,FeignCodes.SUCCESS.getStatus())){
            //二阶段逻辑上不允许失败 ,返回false重试  这里其实也要做一下幂等 防止后续操作失败重复释放
            return false;
        }
        //释放冻结资金
        String userId = businessActionContext.getActionContext("userId").toString();
        Long money = Long.parseLong(businessActionContext.getActionContext("money").toString());
        String cancelFrozenUserResult = userFeignService.cancelFrozen(userId,money);
        if(!StringUtils.equals(cancelFrozenUserResult,FeignCodes.SUCCESS.getStatus())){
            //二阶段逻辑上不允许失败 ,返回false重试
            return false;
        }
        //删除该订单 --- 逻辑删除
        String orderId = businessActionContext.getActionContext("orderId").toString();
        Order order = orderMapper.findByOrderId(orderId);
        order.setStatus(OrderCodes.DELETE.getStatus());
        orderMapper.updateByPrimaryKeySelective(order);
        //回滚成功 则移除幂等校验
        IdempotentUtil.remove(getClass(),businessActionContext.getXid());
        return true;
    }
}
