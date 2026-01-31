package com.revpay.dao;

import java.util.List;
import com.revpay.model.Transaction;

public interface TransactionDao {

    boolean sendMoney(int senderId, int receiverId, double amount, String note);

    // STEP 6  Transaction History
    List<Transaction> getTransactionsByUser(int userId);

	void sendMoney1(int userId, int userId2, double amount, String note);
}
