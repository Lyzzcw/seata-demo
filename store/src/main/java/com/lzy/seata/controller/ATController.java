package com.lzy.seata.controller;

import com.lzy.seata.entity.Store;
import com.lzy.seata.service.ATService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public int deduct(@RequestParam("id") Long id,@RequestParam("num") Long num){
        try {
            atService.deduct(id,num);
            return 0;
        }catch (Exception e){
            log.error("扣出库存失败",e);
            return -1;
        }
    }

    @PostMapping("detail")
    public Store detail(@RequestParam("id") Long id){
        return atService.detail(id);
    }
}
