package com.lzy.seata.controller;

import com.lzy.seata.entity.Store;
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

    @PostMapping("frozen")
    public String frozen(@RequestParam("productId") Long productId,@RequestParam("num") Long num){

        return tccService.frozen(productId,num);
    }

    @PostMapping("cancelFrozen")
    public String cancelFrozen(@RequestParam("productId") Long productId,@RequestParam("num") Long num){

        return tccService.cancelFrozen(productId,num);
    }

    @PostMapping("commitFrozen")
    public String commitFrozen(@RequestParam("productId") Long productId,@RequestParam("num") Long num){

        return tccService.commitFrozen(productId,num);
    }
}
