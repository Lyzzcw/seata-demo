package com.lzy.seata.service.impl;

import com.lzy.seata.entity.Result;
import com.lzy.seata.entity.TransactionRecord;
import com.lzy.seata.mapper.TransactionRecordMapper;
import com.lzy.seata.service.TransactionRecordService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/30
 * Time: 17:10
 * Description: No Description
 */
@Service
public class TransactionRecordServiceImpl implements TransactionRecordService {
    @Resource
    private TransactionRecordMapper transactionRecordMapper;

    @Override
    public Result<TransactionRecord> add(TransactionRecord record) {
        //保证幂等性
        TransactionRecord res = transactionRecordMapper.findByXid(record.getXid());
        if (Objects.nonNull(res))
            return Result.resultSuccess("200","请求成功",res);
        transactionRecordMapper.insert(record);
        return Result.resultSuccess("200","请求成功",record);
    }

    @Override
    public Result<TransactionRecord> getByXid(String xid) {
        return Result.resultSuccess("200","请求成功",transactionRecordMapper.findByXid(xid));
    }
}
