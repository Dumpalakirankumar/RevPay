package com.revpay.service;

import java.util.ArrayList;


import java.util.List;
import java.util.Scanner;

import com.revpay.dao.MoneyRequestDao;
import com.revpay.dao.NotificationDao;
import com.revpay.dao.PaymentMethodDao;
import com.revpay.dao.TransactionDao;
import com.revpay.dao.UserDao;
import com.revpay.dao.impl.MoneyRequestDaoImpl;
import com.revpay.dao.impl.NotificationDaoImpl;
import com.revpay.dao.impl.PaymentMethodDaoImpl;
import com.revpay.dao.impl.TransactionDaoImpl;
import com.revpay.dao.impl.UserDaoImpl;
import com.revpay.model.MoneyRequest;
import com.revpay.model.Notification;
import com.revpay.model.PaymentMethod;
import com.revpay.model.User;
import com.revpay.security.SessionManager;

import com.revpay.exception.*;


/**
 * FINAL UserService – Wallet + Notifications + Business Features
 */
public class UserService {

    private UserDao userDao = new UserDaoImpl();
    private TransactionDao transactionDao = new TransactionDaoImpl();
    private MoneyRequestDao moneyRequestDao = new MoneyRequestDaoImpl();
    private PaymentMethodDao cardDao = new PaymentMethodDaoImpl();
    private NotificationDao notificationDao = new NotificationDaoImpl();

    // ================= REGISTER =================
 // ================= REGISTER =================
    public void registerUser(Scanner sc) throws Exception {

        User user = new User();

        System.out.print("Enter Full Name: ");
        user.setFullName(sc.nextLine());

        System.out.print("Enter Email: ");
        String email = sc.nextLine().trim().toLowerCase();

        // ✅ Email validation
        if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            throw new InvalidEmailException(
                    "Email must be a valid @gmail.com address");
        }

        // ✅ Duplicate email check
        if (userDao.isEmailExists(email)) {
            throw new EmailAlreadyExistsException(
                    "Email already registered. Please login.");
        }

        user.setEmail(email);

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        if (!phone.matches("\\d{10}")) {
            throw new InvalidPhoneException(
                    "Mobile number must be exactly 10 digits");
        }
        user.setPhone(phone);

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (password.length() < 6) {
            throw new InvalidPasswordException(
                    "Password must be at least 6 characters");
        }
        user.setPassword(password);

        System.out.print("Account Type (PERSONAL/BUSINESS): ");
        String accountType = sc.nextLine().trim().toUpperCase();

        if (!accountType.equals("PERSONAL") &&
            !accountType.equals("BUSINESS")) {
            throw new IllegalArgumentException(
                    "Invalid account type. Choose PERSONAL or BUSINESS");
        }

        user.setAccountType(accountType);

        // ✅ Single registration
        if (userDao.registerUser(user)) {
            System.out.println("✅ Registration Successful");
            return; // 🔥 MOST IMPORTANT LINE
        }

        throw new RevPayException("Registration failed. Please try again.");
    }



    // ================= LOGIN =================
    public User loginUser1(Scanner sc) {

        System.out.print("Enter Email: ");
        String email = sc.nextLine().trim().toLowerCase();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (userDao.loginUser(email, password)) {
            System.out.println("✅ Login Successful");
            return userDao.getUserByEmail(email);
        }

        System.out.println("❌ Invalid credentials");
        return null;
    }

    // ================= WALLET =================
    public void showWalletBalance(User user) {
        System.out.println("💰 Wallet Balance: ₹" +
                userDao.getWalletBalance(user.getUserId()));
    }

    public void addMoney(User user, double amount) {
        if (amount <= 0) {
            System.out.println("❌ Invalid amount");
            return;
        }
        userDao.updateWalletBalance(user.getUserId(), amount);
        System.out.println("₹" + amount + " added successfully");
    }

    public void withdrawMoney(User user, double amount) {
        double balance = userDao.getWalletBalance(user.getUserId());
        if (amount <= 0 || amount > balance) {
            System.out.println("❌ Insufficient balance");
            return;
        }
        userDao.updateWalletBalance(user.getUserId(), -amount);
        System.out.println("₹" + amount + " withdrawn successfully");
    }

    // ================= SEND MONEY =================
    public void sendMoney(User sender, String receiverEmail,
                          double amount, String note) {

        receiverEmail = receiverEmail.trim().toLowerCase();
        User receiver = userDao.getUserByEmail(receiverEmail);

        if (receiver == null) {
            System.out.println("❌ Receiver not found");
            return;
        }

        if (amount <= 0 ||
                userDao.getWalletBalance(sender.getUserId()) < amount) {
            System.out.println("❌ Insufficient balance");
            return;
        }

        userDao.updateWalletBalance(sender.getUserId(), -amount);
        userDao.updateWalletBalance(receiver.getUserId(), amount);

        transactionDao.sendMoney(
                sender.getUserId(),
                receiver.getUserId(),
                amount,
                note
        );

        notificationDao.createNotification(
                receiver.getUserId(),
                "You received ₹" + amount + " from " + sender.getFullName()
        );

        System.out.println("✅ Money sent successfully");
    }

    // ================= REQUEST MONEY =================
    public void requestMoney(User sender, String receiverEmail,
                             double amount, String note) {

        receiverEmail = receiverEmail.trim().toLowerCase();
        User receiver = userDao.getUserByEmail(receiverEmail);

        if (receiver == null) {
            System.out.println("❌ Receiver not found");
            return;
        }

        MoneyRequest req = new MoneyRequest();
        req.setSenderId(sender.getUserId());
        req.setReceiverId(receiver.getUserId());
        req.setAmount(amount);
        req.setNote(note);

        if (moneyRequestDao.createRequest(req)) {
            notificationDao.createNotification(
                    receiver.getUserId(),
                    sender.getFullName() + " requested ₹" + amount
            );
            System.out.println("✅ Money request sent");
        }
    }

    // ================= VIEW REQUESTS =================
    public void viewRequests(User user, Scanner sc) {

        List<MoneyRequest> requests =
                moneyRequestDao.getPendingRequests(user.getUserId());

        if (requests.isEmpty()) {
            System.out.println("No pending requests");
            return;
        }

        for (MoneyRequest r : requests) {
            System.out.println(
                    "ID: " + r.getRequestId() +
                    " | Amount: ₹" + r.getAmount() +
                    " | Note: " + r.getNote()
            );
        }

        System.out.print("Enter request ID: ");
        int id = sc.nextInt();

        System.out.print("1. Accept  2. Decline : ");
        int choice = sc.nextInt();

        moneyRequestDao.updateRequestStatus(
                id, choice == 1 ? "ACCEPTED" : "DECLINED"
        );
    }

    // ================= CARDS =================
    public void addCard(User user, Scanner sc) {
        PaymentMethod card = new PaymentMethod();
        card.setUserId(user.getUserId());

        System.out.print("Card Holder Name: ");
        card.setCardHolder(sc.nextLine());

        System.out.print("Card Number: ");
        card.setCardNumber(sc.nextLine());

        System.out.print("Expiry Month: ");
        card.setExpiryMonth(sc.nextInt());

        System.out.print("Expiry Year: ");
        card.setExpiryYear(sc.nextInt());
        sc.nextLine();

        System.out.print("Card Type (CREDIT/DEBIT): ");
        card.setCardType(sc.nextLine().toUpperCase());

        cardDao.addCard(card);
        System.out.println("✅ Card added successfully");
    }

    public void viewCards(User user) {
        List<PaymentMethod> cards =
                cardDao.getCardsByUser(user.getUserId());

        if (cards.isEmpty()) {
            System.out.println("No cards found");
            return;
        }

        for (PaymentMethod c : cards) {
            System.out.println(
                    "Card ID: " + c.getCardId() +
                    " | " + c.getCardType() +
                    " | Exp: " + c.getExpiryMonth() + "/" + c.getExpiryYear()
            );
        }
    }

    // ================= TRANSACTIONS =================
    public void viewTransactionHistory(User user) {

        List<com.revpay.model.Transaction> list =
                transactionDao.getTransactionsByUser(user.getUserId());

        if (list.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n====== Transaction History ======");
        for (com.revpay.model.Transaction t : list) {
            String type =
                    (t.getSenderId() == user.getUserId()) ? "DEBIT" : "CREDIT";

            System.out.println(
                    "Txn ID: " + t.getTxnId() +
                    " | " + type +
                    " | ₹" + t.getAmount() +
                    " | " + t.getNote() +
                    " | " + t.getTxnDate()
            );
        }
    }

    // ================= NOTIFICATIONS =================
    public void viewNotifications(User user) {

        List<Notification> list =
                notificationDao.getNotifications(user.getUserId());

        if (list.isEmpty()) {
            System.out.println("No new notifications.");
            return;
        }

        for (Notification n : list) {
            System.out.println("- " + n.getMessage());
            notificationDao.markAsRead(n.getNotifId());
        }
    }

    // ================= FORGOT PASSWORD =================
    public void forgotPassword(Scanner sc) {

        System.out.print("Enter registered email: ");
        String email = sc.nextLine().trim().toLowerCase();

        User user = userDao.getUserByEmail(email);
        if (user == null) {
            System.out.println("❌ User not found");
            return;
        }

        System.out.print("Enter new password: ");
        String newPassword = sc.nextLine();

        userDao.updatePassword(email, newPassword);
        System.out.println("✅ Password updated successfully");
    }

    // =================================================
    // =============== BUSINESS FEATURES ===============
    // =================================================

    public void createInvoice(User user, Scanner sc) {
        System.out.print("Enter invoice amount: ");
        double amt = sc.nextDouble();
        sc.nextLine();

        System.out.print("Enter invoice description: ");
        String desc = sc.nextLine();

        System.out.println("✅ Invoice created for ₹" + amt + " (" + desc + ")");
    }

    public void viewInvoices(User user) {
        System.out.println("📄 Invoice List (demo)");
        System.out.println("Invoice #101 | ₹5000 | PENDING");
    }

    public void acceptPayment(User user, Scanner sc) {
        System.out.println("💳 Payment accepted successfully (demo)");
    }

    public void applyForLoan(User user, Scanner sc) {
        System.out.print("Enter loan amount: ");
        double amt = sc.nextDouble();
        sc.nextLine();

        System.out.println("🏦 Loan application submitted for ₹" + amt);
    }

    public void viewLoans(User user) {
        System.out.println("🏦 Loan Status");
        System.out.println("Loan #201 | ₹1,00,000 | UNDER REVIEW");
    }
 // ================= LOGIN (WITH ACCOUNT LOCKOUT) =================
    public User loginUser(Scanner sc) {

        System.out.print("Enter Email: ");
        String email = sc.nextLine().trim().toLowerCase();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = userDao.getUserByEmail(email);

        if (user == null) {
            System.out.println("❌ User not found");
            return null;
        }

        // 🔒 Check if account is locked
        if (user.isAccountLocked()) {

            long diff =
                System.currentTimeMillis() - user.getLockTime().getTime();

            long minutes = diff / (60 * 1000);

            if (minutes < 15) {
                System.out.println("🔒 Account locked. Try again after "
                        + (15 - minutes) + " minutes");
                return null;
            } else {
                userDao.unlockAccount(user.getUserId());
            }
        }

        // ❌ Wrong password
        if (!userDao.loginUser(email, password)) {

            userDao.increaseFailedAttempts(user.getUserId());

            if (user.getFailedAttempts() + 1 >= 3) {
                userDao.lockAccount(user.getUserId());
                System.out.println("🔒 Account locked due to multiple failed attempts");
            } else {
                System.out.println("❌ Invalid credentials");
            }
            return null;
        }

        // ✅ Success
        userDao.resetFailedAttempts(user.getUserId());
        System.out.println("✅ Login Successful");
        return user;
    }
    
    public User loginUser2(Scanner sc) {

        System.out.print("Enter Email: ");
        String email = sc.nextLine().trim().toLowerCase();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = userDao.getUserByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not registered");
        }

        if (user.isAccountLocked()) {
            throw new AccountLockedException("Account is locked. Try later.");
        }

        if (!userDao.loginUser(email, password)) {
            throw new AuthenticationException("Invalid credentials");
        }

        SessionManager.startSession();
        System.out.println("✅ Login Successful");
        return user;
    }
    public void sendMoney1(User sender, String receiverEmail, double amount, String note) {
    	if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        User receiver = userDao.getUserByEmail(receiverEmail);
        if (receiver == null) {
            throw new UserNotFoundException("Receiver not found");
        }

        double balance = userDao.getWalletBalance(sender.getUserId());
        if (balance < amount) {
            throw new InsufficientBalanceException("Insufficient wallet balance");
        }

        userDao.updateWalletBalance(sender.getUserId(), -amount);
        userDao.updateWalletBalance(receiver.getUserId(), amount);

        transactionDao.sendMoney1(sender.getUserId(), receiver.getUserId(), amount, note);
        notificationDao.createNotification(
                receiver.getUserId(),
                "You received ₹" + amount + " from " + sender.getFullName()
        );

        System.out.println("✅ Money sent successfully");
    }
    public void addMoney1(User user, double amount) {

        if (amount <= 0) {
            throw new InvalidAmountException("Invalid amount entered");
        }

        userDao.updateWalletBalance(user.getUserId(), amount);
        System.out.println("₹" + amount + " added successfully");
    }
    public void validateEmail(String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            throw new InvalidEmailException("Invalid Gmail address");
        }
    }

    public void validatePhone(String phone) {
        if (!phone.matches("\\d{10}")) {
            throw new InvalidPhoneException("Invalid mobile number");
        }
    }

    public void validatePassword(String password) {
        if (password.length() < 6) {
            throw new InvalidPasswordException("Password too short");
        }
    }

}