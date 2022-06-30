package com.lzy.seata.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lzy
 * @version 1.0
 * Date: 2022/6/30
 * Time: 23:26
 * Created with IntelliJ IDEA
 * Description:  1 未付款 2 已付款 3 已完成 4 待确认 5 已删除
 */
@AllArgsConstructor
@Getter
public enum OrderCodes {
    UNPAID(1),
    PAID(2),
    COMPLETED(3),
    UNCONFIRMED(4),
    DELETE(5);
    private int status;
}
