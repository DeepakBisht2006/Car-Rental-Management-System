# 🚗 Car Rental Management System
**Academic Project | Object Oriented Programming with Java**
**Institution: Bennett University | Batch: Batch 33**

---

## 🎯 Overview

The **Car Rental Management System** is a fully functional desktop application built with Java that supports complete car rental operations including vehicle management, customer registration, rental processing with UPI payment integration, and return management. Designed for car rental businesses, college projects, and learning Java OOP concepts with real-world database integration.

### Key Highlights
- 🔐 **Role-Based Login** — Separate Admin and Customer portals with password protection
- 🚗 **Complete Rental Flow** — Add cars, register customers, rent, pay, and return
- 💳 **UPI Payment Integration** — QR code payment screen before rental confirmation
- 📊 **Live Data Tables** — Real-time JTable views for cars and customers
- 🗄️ **MySQL Backend** — Full JDBC integration with prepared statements
- 🎨 **Modern Dark UI** — Custom styled Java Swing interface

---

## ✨ Features

### Admin Panel
| Feature | Description |
|---|---|
| ➕ Add Car | Add new vehicles with brand, model, and daily rate |
| 🔍 View Cars | Color-coded table — green = available, red = rented |
| 👥 View Customers | See all registered customers with IDs and contact info |
| 🚪 Exit | Clean database disconnection on exit |

### Customer Portal
| Feature | Description |
|---|---|
| 🔍 View Available Cars | Browse all available vehicles before renting |
| 👤 Register | Register with name and phone — Customer ID shown instantly |
| 🚘 Rent a Car | Select car, enter days, scan QR, confirm payment |
| ↩️ Return a Car | Enter Rental ID to process return |

### Smart Features
- **Live Lookup** — Car and customer details load automatically as you type
- **Auto Cost Calculation** — Total price calculated instantly based on days
- **QR Payment Screen** — UPI deep link QR generated with exact amount
- **Customer ID on Register** — Shown immediately after registration
- **Rental ID on Rent** — Shown after confirmed payment
- **Availability Tracking** — Car status updates automatically on rent/return

---

## 🛠️ Tech Stack

| Component | Technology | Purpose |
|---|---|---|
| Language | Java 11+ | Core application logic |
| GUI | Java Swing | Desktop interface |
| Database | MySQL 8.0 | Data persistence |
| DB Connection | JDBC | Java-MySQL bridge |
| Driver | MySQL Connector/J 9.6 | JDBC implementation |
| IDE | VS Code | Development environment |

---

## 📁 Project Structure

```
CarRentalSystem/
├── schema.sql                         ← Run this first to set up database
├── run.bat                            ← Double click to launch app
├── README.md                          ← This file
├── lib/
│   └── mysql-connector-j-9.6.0.jar   ← MySQL JDBC driver
└── src/
    ├── model/                         ← Data classes (OOP entities)
    │   ├── Car.java                   ← Car entity with getters/setters
    │   ├── Customer.java              ← Customer entity
    │   └── Rental.java                ← Rental record entity
    ├── database/                      ← Database connectivity
    │   └── DBConnection.java          ← Singleton JDBC connection manager
    ├── service/                       ← Business logic layer
    │   ├── CarService.java            ← Car CRUD operations
    │   ├── CustomerService.java       ← Customer CRUD operations
    │   └── RentalService.java         ← Rental operations
    ├── ui/                            ← All GUI forms
    │   ├── LoginForm.java             ← Role-based login screen
    │   ├── Dashboard.java             ← Main navigation panel
    │   ├── AddCarForm.java            ← Add new car form
    │   ├── ViewCarsForm.java          ← Cars table with availability colors
    │   ├── ViewCustomersForm.java     ← Customers table
    │   ├── RegisterCustomerForm.java  ← Registration with ID display
    │   ├── RentCarForm.java           ← Rental form with cost calculator
    │   ├── ReturnCarForm.java         ← Return processing form
    │   └── PaymentDialog.java         ← UPI QR code payment screen
    └── main/
        └── Main.java                  ← Application entry point
```

---

## 🗄️ Database Structure

```sql
Database: car_rental_system

┌─────────────────────────────────┐
│           cars                  │
├─────────────────────────────────┤
│ car_id       INT (PK)           │
│ brand        VARCHAR(50)        │
│ model        VARCHAR(50)        │
│ rent_per_day DECIMAL(10,2)      │
│ available    ENUM('YES','NO')   │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│         customers               │
├─────────────────────────────────┤
│ customer_id  INT (PK)           │
│ name         VARCHAR(100)       │
│ phone        VARCHAR(20)        │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│           rentals               │
├─────────────────────────────────┤
│ rental_id    INT (PK)           │
│ car_id       INT (FK → cars)    │
│ customer_id  INT (FK → customers│
│ rent_date    DATE               │
│ days         INT                │
│ total_price  DECIMAL(10,2)      │
│ return_date  DATE (nullable)    │
└─────────────────────────────────┘
```

---

## 🚀 Quick Start

### Prerequisites
| Requirement | Version |
|---|---|
| Java JDK | 11 or later |
| MySQL Server | 8.0 or later |
| MySQL Connector/J | 9.6.0 |
| VS Code | Latest |
| Extension Pack for Java | Latest |

### Installation

**Step 1 — Set up the database**
```
Open MySQL Workbench
File → Open SQL Script → select schema.sql
Press Ctrl+Shift+Enter to run
```

**Step 2 — Configure credentials**

Open src/database/DBConnection.java and update:
```java
private static final String DB_PASSWORD = "your_mysql_password";
```

**Step 3 — Run the application**
```
Simply double click run.bat
```

---

## 📖 User Guide

### Admin Login
```
Role     → Admin
Password → admin123
```

### Customer Login
```
Role     → Customer
Password → (none required)
```

### Renting a Car — Step by Step
```
1. Login as Customer
2. Click "View Available Cars" → note the Car ID
3. Click "Register as Customer" → save your Customer ID
4. Click "Rent a Car"
5. Enter Car ID and Customer ID
6. Enter number of days → total cost calculated automatically
7. Click "Confirm Rental" → QR code payment screen appears
8. Scan QR with Google Pay / PhonePe / Paytm
9. Click "I Have Paid" → Rental ID shown → save it!
```

### Returning a Car
```
1. Login as Customer
2. Click "Return a Car"
3. Enter your Rental ID
4. Review rental details
5. Click "Confirm Return" → car marked available again
```

---

## 🎓 OOP Concepts Demonstrated

| Concept | Where Used |
|---|---|
| **Classes & Objects** | Car, Customer, Rental model classes |
| **Encapsulation** | Private fields with public getters/setters |
| **Constructors** | Overloaded constructors in all model classes |
| **Inheritance** | All forms extend JFrame |
| **Abstraction** | Service layer abstracts DB operations from UI |
| **Packages** | Code organized into model, database, service, ui, main |
| **Exception Handling** | Try-catch in all DB operations |
| **Interfaces** | ActionListener, WindowAdapter implementations |

---

## 🔒 Security Features

- Admin password protected login
- Prepared statements prevent SQL injection
- Role-based access control
- Input validation on all forms
- Graceful error handling with user-friendly messages

---

## 👥 Developed By

**Team | Institution: Bennett University | Batch: 33 | B.Tech CSE | 2025-2026**

| Member | Student ID | Role | Key Contributions |
|---|---|---|---|
| Shimon Pandey | S25CSEU0993 | 🎖️ Team Lead | System architecture, project coordination, JDBC integration, final testing & presentation |
| Deepak Bisht | S25CSEU0986 | 🎨 UI/UX Lead | Complete Swing interface design, login system, dashboard, payment QR dialog, dark theme |
| Adityan | S25CSEU0977 | 🗄️ Database Engineer | MySQL schema design, service layer, prepared statements, DB connection management |

---

## 🔮 Future Enhancements

- 🌐 **Web Interface** — PHP/HTML website version
- 📧 **Email Receipts** — Send rental confirmation to customer email
- 📊 **Revenue Reports** — Admin dashboard with earnings analytics
- 🔍 **Search & Filter** — Search cars by brand, model, or price
- 🖼️ **Car Images** — Add photos for each vehicle
- 💰 **Real Payment Gateway** — Razorpay/PayPal integration
- 📱 **Mobile App** — Android version using Java

---

## 📊 Project Statistics

| Metric | Count |
|---|---|
| Total Java Files | 14 |
| Packages | 5 |
| Classes | 14 |
| Lines of Code | ~1500+ |
| Database Tables | 3 |
| GUI Forms | 9 |
| Development Time | 4 weeks |

---

## 🙏 Acknowledgments

- **Bennett University** — For the learning opportunity
- **Java & Swing Documentation** — Oracle official docs
- **MySQL** — Database engine
- **Stack Overflow Community** — For debugging support

---

*Built with ❤️ | Shimon Pandey, Deepak Bisht, Adityan | Bennett University | 2025-2026*
