package com.lzy.seata.entity;

import java.time.LocalDateTime;

public class Store {
    private Long id;

    private String name;

    private Long num;

    private LocalDateTime createTime;

    private Long price;

    public Store(Long id, String name, Long num, LocalDateTime createTime, Long price) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.createTime = createTime;
        this.price = price;
    }

    public Store() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}