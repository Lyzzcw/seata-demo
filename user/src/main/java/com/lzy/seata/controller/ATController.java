package com.lzy.seata.controller;

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
    public int deduct(@RequestParam("userId") String userId,@RequestParam("money") Long money){
        try {
            atService.deduct(userId,money);
            return 0;
        }catch (Exception e){
            log.error("扣除余额失败",e);
            return -1;
        }
    }
}
