package com.digicash.paymentkitlibrary.model;

import java.io.Serializable;

/**
 * Created by hechmi on 12/12/17.
 */

public class TransactionStatus implements Serializable {

    private String nym;
    private String requestid;
    private String amount;
    private String operationType;
    private String State;
    private String createdAt;
    private String updatedAt;
    private String TransactionId;

    public TransactionStatus() {
    }

    public String getNym() {
        return nym;
    }

    public void setNym(String nym) {
        this.nym = nym;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Status getState() {
        return Status.valueOf(this.State.toUpperCase());
    }

    public void setState(String state) {
        State = state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }
}