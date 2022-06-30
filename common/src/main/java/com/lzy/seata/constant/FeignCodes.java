package com.lzy.seata.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lzy
 * @version 1.0
 * Date: 2022/6/30
 * Time: 23:17
 * Created with IntelliJ IDEA
 * Description: 消息状态码
 */
@AllArgsConstructor
@Getter
public enum FeignCodes {
    /*

     */
    SUCCESS("200","成功！");

    private String status;

    private String msg;
}
