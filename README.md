# ATM Simulation System Using JavaFX



## Project Description
The ATM Simulation System is a JavaFX-based desktop application that simulates the functionality of an ATM. Users can log in, check their account balance, withdraw money, and perform other banking operations. The application integrates various concepts of Object-Oriented Programming(OOP), JavaFX for the user interface, MySQL for database management, and JDBC for database connectivity.



## Features
* User Authentication: Secure login system for users.
* Account Management: View account balance and transaction history.
* Deposit & Withdrawal: Perform basic banking transactions.
* Graphical User Interface: Interactive UI using JavaFX.
* Database Integration: MySQL for storing user data and transactions.



## Technologies Used
* Object-Oriented Programming (OOP): Ensures modular, reusable, and maintainable code through encapsulation, inheritance, and polymorphism.
* JavaFX: Used for building the graphical user interface (GUI).
* MySQL: Chosen for its efficiency in handling structured financial data.
* JDBC: Enables database connectivity between Java and MySQL.



## Challenges Faced
* Configuring MySQL authentication and resolving Access Denied errors.
* Integrating JavaFX with database operations.
* Handling concurrent user transactions efficiently.



## Future Enhancements
* Implement multi-factor authentication for security.
* Add a transaction history feature.
* Develop a mobile-friendly version of the application.



## Installation and Running the Project
### Prerequisites
* Install Java Development Kit (JDK 17 or later).
* Install MySQL Server and create a database named ATMSystem.
* Add the MySQL Connector JAR to the project dependencies.
### Setup Instructions
* Clone the repository:
  ```sh
  git clone https://github.com/preksha2204/ATM-Simulation-System.git
* Open the project in an IDE (VS Code, IntelliJ, or Eclipse). I would recommend to use BlueJ
* Configure the database connection in Conn.java:
  ```java
  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATMSystem", "root", "your_password");
* Run StartingPage.java to launch the application.



## Troubleshooting
If you get the error Access denied for user 'root'@'localhost', ensure:
MySQL is running.
Credentials in Conn.java are correct.
The user has proper permissions in MySQL.



## Contributors
Preksha K
