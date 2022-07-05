package com.lzy.seata.entity;


import lombok.*;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/7/4
 * Time: 10:37
 * Description: No Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderMessage extends AbstractMessage{
    private  String id;
}
