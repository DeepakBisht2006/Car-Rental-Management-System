# Car Rental Management System
Java + Swing + MySQL Desktop Application

---

## Prerequisites

| Requirement | Version |
|---|---|
| Java (JDK) | 11 or later |
| MySQL Server | 8.0 or later |
| MySQL Connector/J | 8.x |

---

## Step 1 – Database Setup

Open a terminal and run:

```bash
mysql -u root -p < schema.sql
```

This creates the `car_rental_system` database with sample data.

---

## Step 2 – Configure DB Credentials

Open `src/database/DBConnection.java` and update:

```java
private static final String DB_URL      = "jdbc:mysql://localhost:3306/car_rental_system?useSSL=false&serverTimezone=UTC";
private static final String DB_USER     = "root";       // ← your MySQL username
private static final String DB_PASSWORD = "password";   // ← your MySQL password
```

---

## Step 3 – Add the JDBC Driver JAR

Download `mysql-connector-j-x.x.x.jar` from:
https://dev.mysql.com/downloads/connector/j/

### IntelliJ IDEA
1. File → Project Structure → Modules → Dependencies
2. Click `+` → JARs or Directories → select the JAR
3. Apply & OK

### Eclipse
1. Right-click project → Build Path → Add External Archives
2. Select the JAR

### NetBeans
1. Right-click Libraries → Add JAR/Folder
2. Select the JAR

---

## Step 4 – Run the Application

Set `main.Main` as the run configuration and execute.

---

## Project Structure

```
CarRentalSystem/
├── schema.sql                  ← Run this first
├── README.md
└── src/
    ├── model/
    │   ├── Car.java
    │   ├── Customer.java
    │   └── Rental.java
    ├── database/
    │   └── DBConnection.java   ← Set credentials here
    ├── service/
    │   ├── CarService.java
    │   ├── CustomerService.java
    │   └── RentalService.java
    ├── ui/
    │   ├── Dashboard.java
    │   ├── AddCarForm.java
    │   ├── ViewCarsForm.java
    │   ├── RegisterCustomerForm.java
    │   ├── RentCarForm.java
    │   └── ReturnCarForm.java
    └── main/
        └── Main.java           ← Entry point
```

---

## Features

| Feature | Description |
|---|---|
| Add Car | Add new vehicles with daily rate |
| View Cars | Table view with green/red availability colouring |
| Register Customer | Store customer name & phone |
| Rent Car | Auto-lookup, cost calculation, DB update |
| Return Car | Rental lookup + confirmation, marks car available |

---

## Troubleshooting

- **"Communications link failure"** – MySQL server is not running, or URL is wrong.
- **"Access denied for user"** – Wrong username/password in DBConnection.java.
- **"Unknown database"** – schema.sql has not been executed yet.
- **ClassNotFoundException: com.mysql.cj.jdbc.Driver** – JDBC JAR not on classpath.
