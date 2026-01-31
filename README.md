<img width="2160" height="1440" alt="Screenshot 2026-01-31 093245" src="https://github.com/user-attachments/assets/cf14e3fc-191a-4b5c-9f09-708c8bea4e8e" /><img width="2160" height="1440" alt="Screenshot 2026-01-31 093245" src="https://github.com/user-attachments/assets/44d41538-3d32-4947-ad9f-32b498dfb11b" /># 🚀 RevPay - Digital Banking System
RevPay is a console-based banking application built with Java. It simulates a core financial platform allowing users to send money, pay invoices, request funds, and manage their digital wallets securely.
# 🛠 Tech Stack
Language: Java 7
Database: Oracle 10g
Architecture: Layered Architecture (DAO → Service → Main)
Testing: JUnit 4, Mockito
Build Tool: Manual JAR-based setup
IDE: Eclipse

# ✨ Key Features
👤 User Management

User Registration with validations
Login with account lock after 3 failed attempts
Forgot password functionality
Session handling

💰 Wallet Operations

View wallet balance
Add money
Withdraw money
Send money to other users
Request money

🔔 Notifications

Real-time notifications for:
Money received
Money requested
Mark notifications as read

💳 Card Management

Add debit/credit cards
View saved cards

📜 Transaction History

View complete transaction history
Debit / Credit classification

🏢 Business Features

Business dashboard (BUSINESS users only)
Create invoices
View invoices
Accept payments
Apply for loans
View loan status

🔐 Security & Validation

Email validation (@gmail.com only)
Phone number validation (10 digits)
Password validation (minimum length)
Custom exception handling
Account lock & unlock mechanism

# ⚙️ Setup Instructions

Clone Project
git clone https://github.com/Dumpalakirankumar/RevPay.git
Open in Eclipse
File → Import → Existing Projects into Workspace
Configure Database
Start Oracle 10g
Update DB details in com.revpay.util.DBUtil
Add JARs
ojdbc14.jar
junit-4.x.jar
mockito-all-1.10.19.jar
Run Application
Run com.revpay.main.RevPayApplication
Run Tests (Optional)
Right-click com.revpay.test → Run as JUnit Test
