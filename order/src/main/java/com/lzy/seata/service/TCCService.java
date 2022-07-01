package com.lzy.seata.service;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/7/1
 * Time: 10:29
 * Description: No Description
 */
public interface TCCService {
    void create(String userId,Long productId,Long num);
}
