package com.revpay.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.revpay.dao.PaymentMethodDao;
import com.revpay.model.PaymentMethod;
import com.revpay.security.PasswordUtil;
import com.revpay.util.DBUtil;

public class PaymentMethodDaoImpl implements PaymentMethodDao {

    @Override
    public boolean addCard(PaymentMethod card) {

        String sql =
            "INSERT INTO payment_methods " +
            "(card_id, user_id, card_holder, card_number, expiry_month, expiry_year, card_type, is_default) " +
            "VALUES (card_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, card.getUserId());
            ps.setString(2, card.getCardHolder());

            // encrypt card number
            ps.setString(3, PasswordUtil.encrypt(card.getCardNumber()));

            ps.setInt(4, card.getExpiryMonth());
            ps.setInt(5, card.getExpiryYear());
            ps.setString(6, card.getCardType());
            ps.setString(7, card.isDefault() ? "Y" : "N");

            return ps.executeUpdate() > 0;
             }

        } catch (Exception e) {
            System.out.println("Error adding card: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<PaymentMethod> getCardsByUser(int userId) {

        List<PaymentMethod> list = new ArrayList<PaymentMethod>();

        String sql = "SELECT * FROM payment_methods WHERE user_id = ?";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PaymentMethod card = new PaymentMethod();
                card.setCardId(rs.getInt("card_id"));
                card.setUserId(userId);
                card.setCardHolder(rs.getString("card_holder"));
                card.setCardNumber("**** **** ****");
                card.setExpiryMonth(rs.getInt("expiry_month"));
                card.setExpiryYear(rs.getInt("expiry_year"));
                card.setCardType(rs.getString("card_type"));
                card.setDefault("Y".equals(rs.getString("is_default")));
                list.add(card);
            }
        }

        } catch (Exception e) {
            System.out.println("Error fetching cards");
        }

        return list;
    }

    @Override
    public boolean setDefaultCard(int userId, int cardId) {

        try {Connection con = DBUtil.getConnection(); {

            // remove previous default
            PreparedStatement ps1 =
                con.prepareStatement(
                    "UPDATE payment_methods SET is_default='N' WHERE user_id=?");
            ps1.setInt(1, userId);
            ps1.executeUpdate();

            // set new default
            PreparedStatement ps2 =
                con.prepareStatement(
                    "UPDATE payment_methods SET is_default='Y' WHERE card_id=?");
            ps2.setInt(1, cardId);

            return ps2.executeUpdate() > 0;
        }

        } catch (Exception e) {
            System.out.println("Error setting default card");
            return false;
        }
    }
}
