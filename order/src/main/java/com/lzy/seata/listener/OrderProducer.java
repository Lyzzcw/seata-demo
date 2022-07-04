package com.lzy.seata.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/7/4
 * Time: 10:36
 * Description: No Description
 */
@Component
@Slf4j
public class OrderProducer  {
    private String producerGroup = "order_trans_group";
    private String topic = "order_trans_topic";
    private TransactionMQProducer producer;

    //执行任务的线程池
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));

    @Autowired
    private OrderTransactionListener orderTransactionListener;
    @PostConstruct
    public void init(){
        producer = new TransactionMQProducer(producerGroup);
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setSendMsgTimeout(Integer.MAX_VALUE);
        producer.setExecutorService(executor);
        producer.setTransactionListener(orderTransactionListener);
        this.start();
    }
    private void start(){
        try {
            this.producer.start();
            log.info("start order mq producer success");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    //事务消息发送
    public TransactionSendResult send(String data, String topic,String tag) throws MQClientException {
        Message message = new Message(this.topic,tag,data.getBytes(StandardCharsets.UTF_8));
        return this.producer.sendMessageInTransaction(message, null);
    }

}
