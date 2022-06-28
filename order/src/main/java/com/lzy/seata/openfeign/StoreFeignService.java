package com.lzy.seata.openfeign;

import com.lzy.seata.entity.Store;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/28
 * Time: 16:22
 * Description: No Description
 */
@FeignClient(value = "seata-store")
public interface StoreFeignService {

    @PostMapping("/seata-store/deduct")
    int deduct(@RequestParam("id") Long id,@RequestParam("num") Long num);

    @PostMapping("/seata-store/detail")
    Store detail(@RequestParam("id") Long id);
}
