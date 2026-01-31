package com.revpay.main;

import java.util.Scanner;

import com.revpay.exception.RevPayException;
import com.revpay.model.User;
import com.revpay.service.UserService;

public class RevPayApplication {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();

        while (true) {
            try {
                System.out.println("\n===============================");
                System.out.println("        Welcome to RevPay      ");
                System.out.println("===============================");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Forgot Password");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                case 1:
                    userService.registerUser(sc);
                    break;

                case 2:
                    User loggedInUser = userService.loginUser(sc);
                    if (loggedInUser != null) {
                        walletMenu(sc, userService, loggedInUser);
                    }
                    break;

                case 3:
                    userService.forgotPassword(sc);
                    break;

                case 4:
                    System.out.println("Thank you for using RevPay");
                    sc.close();
                    System.exit(0);

                default:
                	System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    // ================= WALLET MENU =================
    private static void walletMenu(Scanner sc, UserService userService, User user) {

        while (true) {
            try {
                System.out.println("\n----------- Wallet Menu -----------");
                System.out.println("1. View Balance");
                System.out.println("2. Add Money");
                System.out.println("3. Withdraw Money");
                System.out.println("4. Send Money");
                System.out.println("5. Request Money");
                System.out.println("6. View Requests");
                System.out.println("7. Add Card");
                System.out.println("8. View Cards");
                System.out.println("9. Transaction History");
                System.out.println("10. View Notifications");

                if ("BUSINESS".equalsIgnoreCase(user.getAccountType())) {
                    System.out.println("11. Business Dashboard");
                    System.out.println("12. Logout");
                } else {
                    System.out.println("11. Logout");
                }

                System.out.print("Enter option: ");
                int option = sc.nextInt();
                sc.nextLine();

                switch (option) {

                    case 1:
                        userService.showWalletBalance(user);
                        break;

                    case 2:
                        System.out.print("Enter amount to add: ");
                        userService.addMoney(user, sc.nextDouble());
                        sc.nextLine();
                        break;

                    case 3:
                        System.out.print("Enter amount to withdraw: ");
                        userService.withdrawMoney(user, sc.nextDouble());
                        sc.nextLine();
                        break;

                    case 4:
                        System.out.print("Enter receiver email: ");
                        String receiverEmail = sc.nextLine();

                        System.out.print("Enter amount: ");
                        double amount = sc.nextDouble();
                        sc.nextLine();

                        System.out.print("Enter note: ");
                        String note = sc.nextLine();

                        userService.sendMoney(user, receiverEmail, amount, note);
                        break;

                    case 5:
                        System.out.print("Enter receiver email: ");
                        String reqEmail = sc.nextLine();

                        System.out.print("Enter amount: ");
                        double reqAmt = sc.nextDouble();
                        sc.nextLine();

                        System.out.print("Enter note: ");
                        String reqNote = sc.nextLine();

                        userService.requestMoney(user, reqEmail, reqAmt, reqNote);
                        break;

                    case 6:
                        userService.viewRequests(user, sc);
                        break;

                    case 7:
                        userService.addCard(user, sc);
                        break;

                    case 8:
                        userService.viewCards(user);
                        break;

                    case 9:
                        userService.viewTransactionHistory(user);
                        break;

                    case 10:
                        userService.viewNotifications(user);
                        break;

                    case 11:
                        if ("BUSINESS".equalsIgnoreCase(user.getAccountType())) {
                            businessMenu(sc, userService, user);
                            break;
                        }
                        System.out.println("Logged out successfully.");
                        return;

                    case 12:
                        System.out.println("Logged out successfully.");
                        return;

                    default:
                        System.out.println("Invalid option. Try again.");
                }

            } catch (RevPayException e) {
                System.out.println("❌ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Please try again.");
                sc.nextLine(); // clear buffer
            }
        }
    }

    // ================= BUSINESS MENU =================
    private static void businessMenu(Scanner sc, UserService userService, User user) {

        while (true) {
            try {
                System.out.println("\n======= BUSINESS DASHBOARD =======");
                System.out.println("1. Create Invoice");
                System.out.println("2. View Invoices");
                System.out.println("3. Accept Payments");
                System.out.println("4. Apply for Loan");
                System.out.println("5. View Loan Applications");
                System.out.println("6. Back");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1:
                        userService.createInvoice(user, sc);
                        break;

                    case 2:
                        userService.viewInvoices(user);
                        break;

                    case 3:
                        userService.acceptPayment(user, sc);
                        break;

                    case 4:
                        userService.applyForLoan(user, sc);
                        break;

                    case 5:
                        userService.viewLoans(user);
                        break;

                    case 6:
                        return;

                    default:
                        System.out.println("Invalid option. Try again.");
                }

            } catch (RevPayException e) {
                System.out.println("❌ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Please try again.");
                sc.nextLine();
            }
        }
    }
}

