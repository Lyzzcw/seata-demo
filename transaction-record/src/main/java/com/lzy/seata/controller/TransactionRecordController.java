package com.lzy.seata.controller;

import com.lzy.seata.entity.Result;
import com.lzy.seata.entity.TransactionRecord;
import com.lzy.seata.service.TransactionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/30
 * Time: 16:41
 * Description: No Description
 */
@RestController
public class TransactionRecordController {
    @Autowired
    private TransactionRecordService transactionRecordService;

    @PostMapping("/add")
    Result<TransactionRecord> add(TransactionRecord record){
        return transactionRecordService.add(record);
    }

    @PostMapping("/getByXid")
    Result<TransactionRecord> getByXid(String xid){
        return transactionRecordService.getByXid(xid);
    }
}
