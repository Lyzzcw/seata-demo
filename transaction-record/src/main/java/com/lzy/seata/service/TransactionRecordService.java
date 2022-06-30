package com.lzy.seata.service;

import com.lzy.seata.entity.Result;
import com.lzy.seata.entity.TransactionRecord;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/30
 * Time: 17:08
 * Description: No Description
 */
public interface TransactionRecordService {

    Result<TransactionRecord> add(TransactionRecord record);

    Result<TransactionRecord> getByXid(String xid);

}
