package com.lzy.seata.entity;

import java.time.LocalDateTime;

public class User {
    private Long id;

    private String userId;

    private Long money;

    private Long frozen;

    private LocalDateTime createTime;

    public User(Long id, String userId, Long money, Long frozen, LocalDateTime createTime) {
        this.id = id;
        this.userId = userId;
        this.money = money;
        this.frozen = frozen;
        this.createTime = createTime;
    }

    public User() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getFrozen() {
        return frozen;
    }

    public void setFrozen(Long frozen) {
        this.frozen = frozen;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}