package com.lzy.seata.entity;

import com.lzy.seata.constant.MQSendTypeEnum;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/1/21
 * Time: 14:10
 * Description: No Description
 */
public abstract class AbstractMessage {
    /** MQ名称 **/
    public  String MQName;

    /** MQ 类型 **/
    public  MQSendTypeEnum MQType;

    /** 构造MQ消息体 String类型 **/
    public String Message;
}
