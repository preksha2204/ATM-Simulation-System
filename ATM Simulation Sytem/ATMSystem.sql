CREATE DATABASE ATMSystem;
USE ATMSystem;

CREATE TABLE login (
    cardno VARCHAR(16) PRIMARY KEY,
    pin VARCHAR(4) NOT NULL
);

SELECT * FROM login;

CREATE TABLE signup (
    formno INT PRIMARY KEY,
    cname VARCHAR(100),
    father_name VARCHAR(100),
    dob DATE,
    gender VARCHAR(10),
    email VARCHAR(100),
    marital_status VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(100),
    pin VARCHAR(10),
    state VARCHAR(100)
);

SELECT * FROM signup;

CREATE TABLE signup2 (
    form_no INT PRIMARY KEY,
    religion VARCHAR(20),
    category VARCHAR(20),
    income VARCHAR(20),
    education VARCHAR(20),
    occupation VARCHAR(20),
    pan VARCHAR(10),
    aadhar VARCHAR(12),
    senior_citizen ENUM('Yes', 'No'),
    existing_account ENUM('Yes', 'No'),
    FOREIGN KEY (form_no) REFERENCES signup(formno)
);

SELECT * FROM signup2;

CREATE TABLE Signup3 (
    form_no INT PRIMARY KEY,
    account_type VARCHAR(30),
    card_number VARCHAR(19),
    pin VARCHAR(4),
    facilities TEXT,
    FOREIGN KEY (form_no) REFERENCES signup(formno)
);

SELECT * FROM signup3;

CREATE TABLE deposit (
    cardno VARCHAR(16),
    amount DECIMAL(10, 2),
    deposit_date TIMESTAMP,
    transaction_type VARCHAR(20), 
    PRIMARY KEY (cardno, deposit_date)
);

SELECT * FROM deposit;


