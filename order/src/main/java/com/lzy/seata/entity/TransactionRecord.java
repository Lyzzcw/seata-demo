package com.lzy.seata.entity;

import lombok.Builder;

@Builder
public class TransactionRecord {
    private Long id;

    private String xid;

    private Integer status;

    public TransactionRecord(Long id, String xid, Integer status) {
        this.id = id;
        this.xid = xid;
        this.status = status;
    }

    public TransactionRecord() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid == null ? null : xid.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}