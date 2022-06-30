package com.lzy.seata.openfeign;

import com.lzy.seata.entity.Result;
import com.lzy.seata.entity.Store;
import com.lzy.seata.entity.TransactionRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/28
 * Time: 16:22
 * Description: No Description
 */
@FeignClient(value = "seata-transaction-record")
public interface TransactionRecordFeignService {

    @PostMapping("/seata-transaction-record/add")
    Result<TransactionRecord> add(@SpringQueryMap TransactionRecord record);

    @PostMapping("/seata-transaction-record/getByXid")
    Result<TransactionRecord> getByXid(@RequestParam(value = "xid") String xid);
}
