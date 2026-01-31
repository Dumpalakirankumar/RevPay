package com.revpay.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.revpay.dao.UserDao;
import com.revpay.exception.EmailAlreadyExistsException;
import com.revpay.model.User;
import com.revpay.security.PasswordUtil;
import com.revpay.util.DBUtil;

public class UserDaoImpl implements UserDao {

    // ================= REGISTER =================
    @Override
    public boolean registerUser(User user) throws Exception {

        String sql =
            "INSERT INTO users (user_id, full_name, email, phone, password, account_type) " +
            "VALUES (user_seq.NEXTVAL, ?, ?, ?, ?, ?)";

        try {Connection con = null;
		try {
			con = DBUtil.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, PasswordUtil.hashPassword(user.getPassword()));
            ps.setString(5, user.getAccountType());

            return ps.executeUpdate() > 0;
             }

        }
        // ✅ STEP 5: HANDLE UNIQUE CONSTRAINT
        catch (SQLException e) {

            // ORA-00001 → UNIQUE constraint violated (duplicate email)
            if (e.getErrorCode() == 1) {
                throw new EmailAlreadyExistsException(
                    "Email already registered. Please login."
                );
            }

            throw new RuntimeException("Database error during registration", e);
        }
    }


    // ================= LOGIN CHECK =================
    @Override
    public boolean loginUser(String email, String password) {

        String sql =
            "SELECT user_id FROM users WHERE email=? AND password=?";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setString(1, email);
            ps.setString(2, PasswordUtil.hashPassword(password));

            ResultSet rs = ps.executeQuery();
            return rs.next();
           }

        } catch (Exception e) {
            return false;
        }
    }

    // ================= GET USER =================
    @Override
    public User getUserByEmail(String email) {

        String sql =
            "SELECT user_id, full_name, email, phone, account_type, " +
            "failed_attempts, account_locked, lock_time " +
            "FROM users WHERE email=?";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                u.setAccountType(rs.getString("account_type"));
                u.setFailedAttempts(rs.getInt("failed_attempts"));
                u.setAccountLocked("Y".equals(rs.getString("account_locked")));
                u.setLockTime(rs.getTimestamp("lock_time"));
                return u;
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= WALLET =================
    @Override
    public boolean updateWalletBalance(int userId, double amount) {

        String sql =
            "UPDATE users SET wallet_balance = wallet_balance + ? WHERE user_id=?";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
             }

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public double getWalletBalance(int userId) {

        String sql =
            "SELECT wallet_balance FROM users WHERE user_id=?";

        try { Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
             }
        } catch (Exception e) {}
        return 0;
    }

    // ================= ACCOUNT LOCKOUT =================
    @Override
    public void increaseFailedAttempts(int userId) {

        String sql =
            "UPDATE users SET failed_attempts = failed_attempts + 1 WHERE user_id=?";

        try { Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, userId);
            ps.executeUpdate();
             }
        } catch (Exception e) {}
    }

    @Override
    public void resetFailedAttempts(int userId) {

        String sql =
            "UPDATE users SET failed_attempts = 0 WHERE user_id=?";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, userId);
            ps.executeUpdate();
             }
        } catch (Exception e) {}
    }

    @Override
    public void lockAccount(int userId) {

        String sql =
            "UPDATE users SET account_locked='Y', lock_time=SYSTIMESTAMP WHERE user_id=?";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, userId);
            ps.executeUpdate();
             }
        } catch (Exception e) {}
    }

    @Override
    public void unlockAccount(int userId) {

        String sql =
            "UPDATE users SET account_locked='N', failed_attempts=0, lock_time=NULL WHERE user_id=?";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setInt(1, userId);
            ps.executeUpdate();
             }
        } catch (Exception e) {}
    }

    // ================= PASSWORD RESET =================
    @Override
    public boolean updatePassword(String email, String newPassword) {

        String sql =
            "UPDATE users SET password=? WHERE email=?";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setString(1, PasswordUtil.hashPassword(newPassword));
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
             }

        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public boolean isEmailExists(String email) {

        String sql = "SELECT 1 FROM users WHERE email = ?";

        try {Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            return rs.next(); // true if email exists
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
