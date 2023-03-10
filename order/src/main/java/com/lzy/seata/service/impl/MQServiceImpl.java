package com.lzy.seata.service.impl;

import com.lzy.seata.entity.Order;
import com.lzy.seata.entity.TransactionLog;
import com.lzy.seata.mapper.OrderMapper;
import com.lzy.seata.mapper.TransactionLogMapper;
import com.lzy.seata.service.MQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/7/4
 * Time: 13:23
 * Description: No Description
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MQServiceImpl implements MQService {

    final OrderMapper orderMapper;
    final TransactionLogMapper transactionLogMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOrder(final Order order, String transactionId) {

        //1.扣除库存
        //1.1 数据库乐观锁模式:尝试扣减库存，利用update的排他性确保事务性，避免超卖
        // update store set store = store - #{deductStore } where (store-#{deductStore }) >= 0
        // 行级锁的原因存在性能瓶颈，高并发会出现请求堵塞超时问题
        // 优化方式：对于潜在的热点商品，可以将库存平均分为多行（比如M行），扣减库存时，随机在M行中选取一行库存数据进行扣减。该方案突破了数据库单行锁的瓶颈限制，解决了爆款商品的热点问题。

        //1.2 redis+lua脚本
        //1.3 redis watch命令 乐观锁


        //2.创建订单
        orderMapper.insert(order);
        //3.写入事务日志
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setTransactionId(transactionId);
        transactionLog.setBusiness("order");
        transactionLog.setForeignKey(order.getOrderId());
        transactionLogMapper.insert(transactionLog);
        log.info("订单创建完成。{}",order);
    }
}
