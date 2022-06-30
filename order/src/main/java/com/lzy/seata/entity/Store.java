package com.lzy.seata.entity;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class Store {
    private Long id;

    private Long productId;

    private String name;

    private Long num;

    private LocalDateTime createTime;

    private Long price;

    private Long frozen;

    public Store(Long id, Long productId, String name, Long num, LocalDateTime createTime, Long price, Long frozen) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.num = num;
        this.createTime = createTime;
        this.price = price;
        this.frozen = frozen;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public Long getFrozen() {
        return frozen;
    }

    public void setFrozen(Long frozen) {
        this.frozen = frozen;
    }
}