package com.revpay.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revpay.dao.MoneyRequestDao;
import com.revpay.model.MoneyRequest;
import com.revpay.util.DBUtil;

public class MoneyRequestDaoImpl implements MoneyRequestDao {

    // ---------------- CREATE REQUEST ----------------
    @Override
    public boolean createRequest(MoneyRequest req) {

        String sql =
            "INSERT INTO money_requests " +
            "(request_id, sender_id, receiver_id, amount, status, note) " +
            "VALUES (request_seq.NEXTVAL, ?, ?, ?, 'PENDING', ?)";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, req.getSenderId());
            ps.setInt(2, req.getReceiverId());
            ps.setDouble(3, req.getAmount());
            ps.setString(4, req.getNote());

            return ps.executeUpdate() > 0;
             }

        } catch (Exception e) {
            System.out.println("Error creating money request: " + e.getMessage());
            return false;
        }
    }

    // ---------------- GET PENDING REQUESTS ----------------
    @Override
    public List<MoneyRequest> getPendingRequests(int userId) {

        List<MoneyRequest> list = new ArrayList<MoneyRequest>();

        String sql =
            "SELECT request_id, sender_id, receiver_id, amount, status, note " +
            "FROM money_requests WHERE receiver_id = ? AND status = 'PENDING'";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MoneyRequest r = new MoneyRequest();
                r.setRequestId(rs.getInt("request_id"));
                r.setSenderId(rs.getInt("sender_id"));
                r.setReceiverId(rs.getInt("receiver_id"));
                r.setAmount(rs.getDouble("amount"));
                r.setStatus(rs.getString("status"));
                r.setNote(rs.getString("note"));
                list.add(r);
            }
            }

        } catch (Exception e) {
            System.out.println("Error fetching requests: " + e.getMessage());
        }

        return list;
    }

    // ---------------- UPDATE REQUEST STATUS ----------------
    @Override
    public boolean updateRequestStatus(int requestId, String status) {

        String sql =
            "UPDATE money_requests SET status = ? WHERE request_id = ?";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setString(1, status);
            ps.setInt(2, requestId);

            return ps.executeUpdate() > 0;
             }

        } catch (Exception e) {
            System.out.println("Error updating request status: " + e.getMessage());
            return false;
        }
    }
}