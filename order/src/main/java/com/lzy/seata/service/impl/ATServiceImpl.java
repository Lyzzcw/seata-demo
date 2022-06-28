package com.lzy.seata.service.impl;

import com.google.j2objc.annotations.AutoreleasePool;
import com.lzy.seata.entity.Order;
import com.lzy.seata.entity.Store;
import com.lzy.seata.mapper.OrderMapper;
import com.lzy.seata.openfeign.StoreFeignService;
import com.lzy.seata.openfeign.UserFeignService;
import com.lzy.seata.service.ATService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/28
 * Time: 13:45
 * Description: No Description
 */
@Service
@Slf4j
public class ATServiceImpl implements ATService {

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    private StoreFeignService storeFeignService;
    @Autowired
    private UserFeignService userFeignService;

    /**
     * 下订单的接口就是一个事务发起方，作为TM，需要发起一个全局事务
     */
    @GlobalTransactional
    @Override
    public int create(String userId, Long productId, Long num) {
        //1.扣减库存
        log.info("扣库存开始....................");
        int result_store = storeFeignService.deduct(productId,num);
        log.info("扣库存结束...................."+result_store);


        //2.扣减余额
        log.info("扣余额开始....................");
        Store store = storeFeignService.detail(productId);
        int result_user = userFeignService.deduct(userId,store.getPrice()*num);
        log.info("扣余额结束...................."+result_user);

        //3.创建订单
        log.info("创建订单....................");
        Order order = Order.builder()
                .userId(userId)
                .createTime(LocalDateTime.now())
                .num(num)
                .productId(productId)
                .status(2)
                .build();
        return orderMapper.insertSelective(order);
    }
}
