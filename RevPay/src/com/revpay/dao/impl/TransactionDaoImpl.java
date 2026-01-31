package com.revpay.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revpay.dao.TransactionDao;
import com.revpay.exception.InvalidAmountException;
import com.revpay.model.Transaction;
import com.revpay.util.DBUtil;

public class TransactionDaoImpl implements TransactionDao {

    // ---------------- SEND MONEY ----------------
    @Override
    public boolean sendMoney(int senderId, int receiverId, double amount, String note) {
    	
    	if (amount <= 0) {
    	    throw new InvalidAmountException("Amount must be greater than zero");
    	}
        String sql =
            "INSERT INTO transactions " +
            "(txn_id, sender_id, receiver_id, amount, note, txn_type, txn_date) " +
            "VALUES (txn_seq.NEXTVAL, ?, ?, ?, ?, 'SEND', SYSTIMESTAMP)";

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.setDouble(3, amount);
            ps.setString(4, note);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("❌ Transaction failed: " + e.getMessage());
            return false;
        }
    }
    

    // ---------------- TRANSACTION HISTORY (STEP 6) ----------------
    @Override
    public List<Transaction> getTransactionsByUser(int userId) {

        List<Transaction> list = new ArrayList<Transaction>();

        String sql =
            "SELECT * FROM transactions " +
            "WHERE sender_id = ? OR receiver_id = ? " +
            "ORDER BY txn_date DESC";

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction();

                t.setTxnId(rs.getInt("txn_id"));
                t.setSenderId(rs.getInt("sender_id"));
                t.setReceiverId(rs.getInt("receiver_id"));
                t.setAmount(rs.getDouble("amount"));
                t.setNote(rs.getString("note"));
                t.setTxnType(rs.getString("txn_type"));
                t.setTxnDate(rs.getTimestamp("txn_date"));

                list.add(t);
            }
            } catch (Exception e){
                System.out.println("❌ Error fetching transactions: " + e.getMessage());
            }

        return list;
    }

	@Override
	public void sendMoney1(int userId, int userId2, double amount, String note) {
		// TODO Auto-generated method stub
		
	}
}
