package com.lzy.seata.service;

import com.lzy.seata.entity.Store;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/28
 * Time: 13:45
 * Description: No Description
 */
public interface ATService {

    String deduct(Long productId,Long num);

    Store detail(Long productId);

}
