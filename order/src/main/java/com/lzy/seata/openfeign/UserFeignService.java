package com.lzy.seata.openfeign;

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
@FeignClient(value = "seata-user")
public interface UserFeignService {

    @PostMapping("/seata-user/deduct")
    String deduct(@RequestParam("userId") String userId,@RequestParam("money") Long money);

    @PostMapping("/seata-user/frozen")
    String frozen(@RequestParam("userId") String userId,@RequestParam("money") Long money);

    @PostMapping("/seata-user/cancelFrozen")
    String cancelFrozen(@RequestParam("userId") String userId,@RequestParam("money") Long money);

    @PostMapping("/seata-user/commitFrozen")
    String commitFrozen(@RequestParam("userId") String userId,@RequestParam("money") Long money);

}
