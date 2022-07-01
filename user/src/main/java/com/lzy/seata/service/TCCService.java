package com.lzy.seata.service;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/7/1
 * Time: 11:21
 * Description: No Description
 */
public interface TCCService {

    String frozen(String userId,Long money);


    String cancelFrozen(String userId,Long money);


    String commitFrozen(String userId,Long money);
}
