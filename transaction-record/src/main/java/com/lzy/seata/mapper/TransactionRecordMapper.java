package com.lzy.seata.mapper;

import com.lzy.seata.entity.TransactionRecord;

public interface TransactionRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TransactionRecord record);

    int insertSelective(TransactionRecord record);

    TransactionRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TransactionRecord record);

    int updateByPrimaryKey(TransactionRecord record);

    TransactionRecord findByXid(String xid);
}