package com.lzy.seata.controller;

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
    public String frozen(@RequestParam("userId") String userId,@RequestParam("money") Long money){

        return tccService.frozen(userId,money);
    }

    @PostMapping("cancelFrozen")
    public String cancelFrozen(@RequestParam("userId") String userId,@RequestParam("money") Long money){

        return tccService.cancelFrozen(userId,money);
    }

    @PostMapping("commitFrozen")
    public String commitFrozen(@RequestParam("userId") String userId,@RequestParam("money") Long money){

        return tccService.commitFrozen(userId,money);
    }
}
