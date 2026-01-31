package com.revpay.dao;

import com.revpay.model.User;

public interface UserDao {

    boolean registerUser(User user) throws Exception;

    boolean loginUser(String email, String password);

    User getUserByEmail(String email);

    boolean updateWalletBalance(int userId, double amount);

    double getWalletBalance(int userId);
    
    boolean updatePassword(String email, String newPassword);
    
    void increaseFailedAttempts(int userId);

    void resetFailedAttempts(int userId);

    void lockAccount(int userId);

    void unlockAccount(int userId);
    
    boolean isEmailExists(String email);


}

