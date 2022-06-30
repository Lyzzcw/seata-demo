package com.lzy.seata.tcc;

import com.lzy.seata.constant.FeignCodes;
import com.lzy.seata.constant.OrderCodes;
import com.lzy.seata.constant.TCCCodes;
import com.lzy.seata.entity.Order;
import com.lzy.seata.entity.Result;
import com.lzy.seata.entity.TransactionRecord;
import com.lzy.seata.mapper.OrderMapper;
import com.lzy.seata.openfeign.TransactionRecordFeignService;
import com.lzy.seata.util.IdempotentUtil;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/30
 * Time: 16:09
 * Description: No Description
 */
@Component
@Slf4j
public class TCCOrderServiceImpl implements TCCOrderService{

    @Autowired
    private TransactionRecordFeignService transactionRecordFeignService;
    @Resource
    private OrderMapper orderMapper;

    @Transactional
    @Override
    public boolean tryCreate(BusinessActionContext businessActionContext,
                             String userId, Long productId, String orderId,Long num) {
        log.info("-------开始第一阶段的事务，事务组XID：{}-------",businessActionContext.getXid());
        //判断cancel阶段是否已经执行过 --- 事务日志
        //FIXME 防止悬挂异常，从事务日志表中获取全局事务ID的状态，如果是cancel状态则不执行。
        Result<TransactionRecord> result = transactionRecordFeignService
                .getByXid(businessActionContext.getXid());
        if(StringUtils.equals(result.getCode(), FeignCodes.SUCCESS.getStatus())){
            throw new RuntimeException("事务日志查询失败！");
        }
        // 1 try 2 commit 3 cancel
        TransactionRecord record = result.getData();
        if(record != null && record.getStatus() == TCCCodes.TRY.getStatus()){
            throw new RuntimeException("已经进入了TCC 二阶段 cancel,不允许try阶段");
        }

        //开始try ---- 冻结库存
        //1.冻结库存
        //2.冻结资金
        //3.生成订单 -- 未完成状态
        Order order = Order.builder()
                .orderId(orderId)
                .productId(productId)
                .createTime(LocalDateTime.now())
                .num(num)
                .status(OrderCodes.PAID.getStatus())
                .build();
        orderMapper.insert(order);

        //保证幂等性 (在cancel或commit二阶段 后移除)
        //FIXME 向幂等工具类中添加一个标记，key为当前类和全局事务ID，value为当前时间戳。
        IdempotentUtil.add(getClass(), businessActionContext.getXid(), System.nanoTime());
        return false;
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
        //释放冻结库存
        //修改订单状态为已完成


        //提交成功,移除幂等校验
        //FIXME 从幂等工具类中移出try方法中添加的值。
        IdempotentUtil.remove(getClass(), businessActionContext.getXid());
        return true;
    }

    @Transactional
    @Override
    public boolean cancel(BusinessActionContext businessActionContext) {
        return false;
    }
}
