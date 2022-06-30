package com.lzy.seata.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lzy
 * @version 1.0
 * Date: 2022/6/30
 * Time: 23:23
 * Created with IntelliJ IDEA
 * Description: No Description
 */
@AllArgsConstructor
@Getter
public enum TCCCodes {

    TRY(1,"try"),
    COMMIT(2,"commit"),
    CANCEL(3,"cancel");

    private int status;
    private String msg;
}
