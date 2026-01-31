package com.revpay.model;

import java.sql.Timestamp;

public class Loan {

    private int loanId;
    private int userId;
    private double amount;
    private String status; // PENDING / APPROVED / REJECTED
    private Timestamp appliedDate;

    public int getLoanId() {
        return loanId;
    }
    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getAppliedDate() {
        return appliedDate;
    }
    public void setAppliedDate(Timestamp appliedDate) {
        this.appliedDate = appliedDate;
    }
}