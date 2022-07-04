package com.lzy.seata.mapper;

import com.lzy.seata.entity.TransactionLog;

public interface TransactionLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TransactionLog record);

    int insertSelective(TransactionLog record);

    TransactionLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TransactionLog record);

    int updateByPrimaryKey(TransactionLog record);

    TransactionLog selectByTransactionId(String transactionId);
}