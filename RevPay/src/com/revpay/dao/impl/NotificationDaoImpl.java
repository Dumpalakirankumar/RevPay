package com.revpay.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.revpay.dao.NotificationDao;
import com.revpay.model.Notification;
import com.revpay.util.DBUtil;

public class NotificationDaoImpl implements NotificationDao {

    @Override
    public boolean createNotification(int userId, String message) {

        String sql =
            "INSERT INTO notifications (notif_id, user_id, message) " +
            "VALUES (notif_seq.NEXTVAL, ?, ?)";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, userId);
            ps.setString(2, message);
            return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.out.println("Notification error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Notification> getNotifications(int userId) {

        List<Notification> list = new ArrayList<Notification>();
        String sql =
            "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setNotifId(rs.getInt("notif_id"));
                n.setUserId(rs.getInt("user_id"));
                n.setMessage(rs.getString("message"));
                n.setRead("Y".equals(rs.getString("is_read")));
                n.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(n);
            }
            }

        } catch (Exception e) {
            System.out.println("Fetch notification error");
        }
        return list;
    }

    @Override
    public boolean markAsRead(int notifId) {

        String sql =
            "UPDATE notifications SET is_read = 'Y' WHERE notif_id = ?";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, notifId);
            return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            return false;
        }
    }
}
