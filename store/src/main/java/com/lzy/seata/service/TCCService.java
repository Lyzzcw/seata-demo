package com.lzy.seata.service;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/7/1
 * Time: 11:21
 * Description: No Description
 */
public interface TCCService {

    String frozen(Long productId,Long num);


    String cancelFrozen(Long productId,Long num);


    String commitFrozen(Long productId,Long num);
}
