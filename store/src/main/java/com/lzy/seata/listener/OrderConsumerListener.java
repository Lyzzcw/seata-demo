package com.lzy.seata.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/7/4
 * Time: 11:07
 * Description: No Description
 */
@Component
@Slf4j
public class OrderConsumerListener implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        log.info("消费者线程监听到消息。");
        try{
            for (MessageExt message:list) {
//                log.info("开始处理订单数据，准备扣除库存....");

                //增加redis锁防止账户并发
                log.info("扣除库存成功，开始扣除账户余额....");
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("处理消费者数据发生异常。{}",e);
            //mq事务消息 这里失败之后会一直重试，所以要求业务上不允许异常。
            //同时 在订单生成的时候 也要保证 库存和用户余额 充足
            //在生成订单 时，可以尝试用redis锁来保证库存，即订单生成一定会存在余量库存
            //同样在冻结账户余额时，未防止用户并发问题，在冻结和释放冻结用户操作时，可以用锁来保证线程安全
            //因为存在重试的可能性，所有一定要做幂等性的校验
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}

