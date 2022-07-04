package com.lzy.seata.service;

import com.lzy.seata.entity.Order;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/7/4
 * Time: 13:22
 * Description: No Description
 */
public interface MQService {
    void createOrder(final Order order, String transactionId);
}
