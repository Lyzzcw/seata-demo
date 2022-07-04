package com.lzy.seata.entity;

public class TransactionLog {
    private Long id;

    private String transactionId;

    private String business;

    private String foreignKey;

    public TransactionLog(Long id, String transactionId, String business, String foreignKey) {
        this.id = id;
        this.transactionId = transactionId;
        this.business = business;
        this.foreignKey = foreignKey;
    }

    public TransactionLog() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : transactionId.trim();
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business == null ? null : business.trim();
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey == null ? null : foreignKey.trim();
    }
}