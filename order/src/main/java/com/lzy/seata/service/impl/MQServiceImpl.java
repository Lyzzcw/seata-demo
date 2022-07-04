package com.lzy.seata.service.impl;

import com.lzy.seata.entity.Order;
import com.lzy.seata.entity.TransactionLog;
import com.lzy.seata.mapper.OrderMapper;
import com.lzy.seata.mapper.TransactionLogMapper;
import com.lzy.seata.service.MQService;
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
public class MQServiceImpl implements MQService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private TransactionLogMapper transactionLogMapper;

    @Transactional
    @Override
    public void createOrder(final Order order, String transactionId) {
        //1.创建订单
        orderMapper.insert(order);
        //2.写入事务日志
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setTransactionId(transactionId);
        transactionLog.setBusiness("order");
        transactionLog.setForeignKey(order.getOrderId());
        transactionLogMapper.insert(transactionLog);
        log.info("订单创建完成。{}",order);
    }
}
