package com.revpay.model;

import java.sql.Timestamp;

public class Invoice {

    private int invoiceId;
    private int businessUserId;
    private String customerEmail;
    private double amount;
    private String status; // PAID / UNPAID
    private Timestamp createdAt;

    // getters & setters
    public int getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getBusinessUserId() {
        return businessUserId;
    }
    public void setBusinessUserId(int businessUserId) {
        this.businessUserId = businessUserId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
