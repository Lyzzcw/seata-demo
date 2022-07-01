package com.lzy.seata.service.impl;

import com.lzy.seata.entity.Order;
import com.lzy.seata.entity.Store;
import com.lzy.seata.mapper.OrderMapper;
import com.lzy.seata.openfeign.StoreFeignService;
import com.lzy.seata.openfeign.UserFeignService;
import com.lzy.seata.service.ATService;
import com.lzy.seata.service.TCCService;
import com.lzy.seata.tcc.TCCOrderService;
import com.lzy.seata.util.NanoIdUtils;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class TCCServiceImpl implements TCCService {

    @Autowired
    private StoreFeignService storeFeignService;

    @Autowired
    private TCCOrderService tccOrderService;
    /**
     * @GlobalTransactional这个注解开启了全局事务，是事务的发起方。
     * 内部直接调用的TCC的try方法。
     */
    @GlobalTransactional
    @Override
    public void create(String userId, Long productId, Long num) {
        Store store = storeFeignService.detail(productId);
        tccOrderService.tryCreate(null,userId,productId,
                NanoIdUtils.randomNanoId(),num,num*store.getPrice());
    }
}
