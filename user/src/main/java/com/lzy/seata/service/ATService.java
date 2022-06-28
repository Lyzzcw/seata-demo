package com.lzy.seata.service;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/28
 * Time: 13:45
 * Description: No Description
 */
public interface ATService {
    void deduct(String userId, Long money);
}
