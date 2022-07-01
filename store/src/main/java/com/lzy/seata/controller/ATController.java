package com.lzy.seata.controller;

import com.lzy.seata.entity.Store;
import com.lzy.seata.service.ATService;
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
public class ATController {
    @Autowired
    private ATService atService;

    @PostMapping("deduct")
    public String deduct(@RequestParam("id") Long id,@RequestParam("num") Long num){
        return atService.deduct(id,num);
    }

    @PostMapping("detail")
    public Store detail(@RequestParam("id") Long id){
        return atService.detail(id);
    }

}
