package com.lzy.seata.controller;

import com.lzy.seata.constant.FeignCodes;
import com.lzy.seata.entity.Result;
import com.lzy.seata.service.ATService;
import com.lzy.seata.service.TCCService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/28
 * Time: 14:06
 * Description: No Description
 */
@RestController
@Slf4j
public class TCCController {
    @Autowired
    private TCCService tccService;

    @PostMapping("tccOrder")
    public Result<String> create(@RequestParam("userId") String userId,
                         @RequestParam("productId") Long productId,
                         @RequestParam("num") Long num){
        tccService.create(userId,productId,num);
        return Result.resultSuccess(FeignCodes.SUCCESS.getStatus()
                ,FeignCodes.SUCCESS.getMsg(),userId);
    }
}
