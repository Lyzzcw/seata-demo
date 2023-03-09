package com.lzy.seata.listener;

import com.alibaba.fastjson.JSONObject;
import com.lzy.seata.entity.Order;
import com.lzy.seata.mapper.OrderMapper;
import com.lzy.seata.mapper.TransactionLogMapper;
import com.lzy.seata.service.MQService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/7/4
 * Time: 11:07
 * Description: No Description
 */
@Component
@Slf4j
public class OrderTransactionListener implements TransactionListener {

    /**
     * 消息状态 事务消息有三种状态：
     * TransactionStatus.CommitTransaction：提交事务消息，消费者可以消费此消息
     * TransactionStatus.RollbackTransaction：回滚事务，它代表该消息将被删除，不允许被消费。
     * TransactionStatus.Unknown ：中间状态，它代表需要检查消息队列来确定状态。
     */
    @Autowired
    private MQService mqService;
    @Resource
    private TransactionLogMapper transactionLogMapper;

    /**
     * 事务消息在一阶段对用户不可见
     * 事务消息相对普通消息最大的特点就是一阶段发送的消息对用户是不可见的，
     * 也就是说消费者不能直接消费。这里RocketMQ的实现方法是原消息的主题与消息消费队列，
     * 然后把主题改成 RMQ_SYS_TRANS_HALF_TOPIC ，这样由于消费者没有订阅这个主题，所以不会被消费。
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("开始执行本地事务....");
        LocalTransactionState state;
        try {
            String body = new String(message.getBody());
            Order order = JSONObject.parseObject(body, Order.class);
            mqService.createOrder(order, message.getTransactionId());
            state = LocalTransactionState.COMMIT_MESSAGE;
            log.info("本地事务已提交。{}", message.getTransactionId());
        } catch (Exception e) {
            log.error("执行本地事务失败。{}", e);
            state = LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return state;
    }

    /**
     * 如何处理第二阶段的失败消息？
     * 在本地事务执行完成后会向MQServer发送Commit或Rollback操作，
     * 此时如果在发送消息的时候生产者出故障了，那么要保证这条消息最终被消费，
     * MQServer会像服务端发送回查请求，确认本地事务的执行状态。
     * 当然了rocketmq并不会无休止的的信息事务状态回查，默认回查15次，
     * 如果15次回查还是无法得知事务状态，RocketMQ默认回滚该消息。
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        log.info("开始回查本地事务状态。{}", messageExt.getTransactionId());
        LocalTransactionState state;
        String transactionId = messageExt.getTransactionId();
        if (transactionLogMapper.selectByTransactionId(transactionId) != null) {
            state = LocalTransactionState.COMMIT_MESSAGE;
        } else {
            //回查生成订单失败之后，这里直接回滚该消息 需要释放被冻结的订单,通知用户等操作
            state = LocalTransactionState.ROLLBACK_MESSAGE;
        }
        log.info("结束本地事务状态查询：{}", state);
        return state;
    }
}

