package com.lzy.seata.entity;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class Order {
    private Long id;

    private Long productId;

    private Long num;

    private String userId;

    private LocalDateTime createTime;

    private Integer status;

    public Order(Long id, Long productId, Long num, String userId, LocalDateTime createTime, Integer status) {
        this.id = id;
        this.productId = productId;
        this.num = num;
        this.userId = userId;
        this.createTime = createTime;
        this.status = status;
    }

    public Order() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}