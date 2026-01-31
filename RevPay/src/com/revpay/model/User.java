package com.revpay.model;

public class User {

    private int userId;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String accountType; // PERSONAL / BUSINESS
    private double walletBalance;
    private int failedAttempts;
    private boolean accountLocked;
    private java.sql.Timestamp lockTime;

    // 🔹 Business fields
    private boolean businessAccount;
    private String businessName;
    private String gstNumber;

    // -------- getters & setters --------

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getWalletBalance() {
        return walletBalance;
    }
    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public boolean isBusinessAccount() {
        return businessAccount;
    }
    public void setBusinessAccount(boolean businessAccount) {
        this.businessAccount = businessAccount;
    }

    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getGstNumber() {
        return gstNumber;
    }
    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }
    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public java.sql.Timestamp getLockTime() {
        return lockTime;
    }
    public void setLockTime(java.sql.Timestamp lockTime) {
        this.lockTime = lockTime;
    }
    
}
