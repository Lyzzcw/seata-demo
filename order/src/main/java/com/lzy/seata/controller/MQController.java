package com.lzy.seata.controller;

import com.alibaba.fastjson.JSON;
import com.lzy.seata.constant.FeignCodes;
import com.lzy.seata.constant.OrderCodes;
import com.lzy.seata.entity.Order;
import com.lzy.seata.entity.Result;
import com.lzy.seata.listener.OrderProducer;
import com.lzy.seata.service.MQService;
import com.lzy.seata.util.NanoIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/7/4
 * Time: 14:45
 * Description: No Description
 */
@RestController
@Slf4j
public class MQController {
    @Autowired
    private MQService mqService;
    @Autowired
    private OrderProducer orderProducer;

    @PostMapping("mqOrder")
    public Result<String> create(@RequestParam("userId") String userId,
                                 @RequestParam("productId") Long productId,
                                 @RequestParam("num") Long num){
        // 先校验 库存，用户余额是否满足


        // 生成正式订单
        Order order = Order.builder()
                .orderId(NanoIdUtils.randomNanoId())
                .productId(productId)
                .userId(userId)
                .num(num)
                .createTime(LocalDateTime.now())
                .status(OrderCodes.COMPLETED.getStatus())
                .build();

        try {
            //如果transactionSendResult = RollBack 则订单创建失败
            TransactionSendResult transactionSendResult =
                    orderProducer.send(JSON.toJSONString(order),"order",null);
        }catch (MQClientException mqClientException){
            log.error("send mq error:{}",mqClientException);
        }

        //finally 释放redis锁

        return Result.resultSuccess(FeignCodes.SUCCESS.getStatus()
                ,FeignCodes.SUCCESS.getMsg(),userId);
    }
}
