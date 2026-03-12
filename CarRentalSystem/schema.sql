-- ============================================================
--  Car Rental System – MySQL Schema
--  Run this file BEFORE launching the Java application.
--  Usage: mysql -u root -p < schema.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS car_rental_system;
USE car_rental_system;

-- ----------------------------------------------------------
-- Table: cars
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS cars (
    car_id      INT            NOT NULL AUTO_INCREMENT,
    brand       VARCHAR(50)    NOT NULL,
    model       VARCHAR(50)    NOT NULL,
    rent_per_day DECIMAL(10,2) NOT NULL,
    available   ENUM('YES','NO') NOT NULL DEFAULT 'YES',
    PRIMARY KEY (car_id)
);

-- ----------------------------------------------------------
-- Table: customers
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT         NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    phone       VARCHAR(20)  NOT NULL,
    PRIMARY KEY (customer_id)
);

-- ----------------------------------------------------------
-- Table: rentals
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS rentals (
    rental_id   INT            NOT NULL AUTO_INCREMENT,
    car_id      INT            NOT NULL,
    customer_id INT            NOT NULL,
    rent_date   DATE           NOT NULL,
    days        INT            NOT NULL,
    total_price DECIMAL(10,2)  NOT NULL,
    return_date DATE           DEFAULT NULL,
    PRIMARY KEY (rental_id),
    CONSTRAINT fk_rental_car      FOREIGN KEY (car_id)      REFERENCES cars(car_id),
    CONSTRAINT fk_rental_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- ----------------------------------------------------------
-- Sample data (optional – remove if not needed)
-- ----------------------------------------------------------
INSERT INTO cars (brand, model, rent_per_day, available) VALUES
    ('Toyota',  'Camry',    55.00, 'YES'),
    ('Honda',   'Civic',    45.00, 'YES'),
    ('Ford',    'Mustang',  90.00, 'YES'),
    ('BMW',     '3 Series', 120.00,'YES'),
    ('Tesla',   'Model 3',  150.00,'YES');

INSERT INTO customers (name, phone) VALUES
    ('Alice Johnson', '555-1001'),
    ('Bob Smith',     '555-1002'),
    ('Carol White',   '555-1003');
