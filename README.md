# Library Management System

## Overview
This Library Management System is a Java-based application designed to manage books, users, and lending operations for a small to medium-sized library. It features user authentication, book inventory management, transaction tracking, and report generation.

## Features
- User Management: Add, update, and authenticate users (Admin, Librarian, Member)
- Book Management: Add, update, and track books in the library inventory
- Transaction Handling: Borrow and return books, manage due dates
- Reporting: Generate inventory, overdue, user activity, and popular books reports
- Data Persistence: Store all data in CSV files for easy management and portability

## Project Structure
- `src/`: Source code directory
  - `Main.java`: Entry point of the application
  - `LibraryManager.java`: Core class managing library operations
  - `ReportGenerator.java`: Handles generation of various reports
  - `CSVBookDAO.java`: Data Access Object for Book entities
  - `CSVUserDAO.java`: Data Access Object for User entities
  - `CSVTransactionDAO.java`: Data Access Object for Transaction entities
  - `Book.java`: Represents a book in the library
  - `User.java`: Represents a user of the library system
  - `Transaction.java`: Represents a lending transaction
  - `PasswordUtils.java`: Utility class for password hashing and verification
- `data/`: Directory containing CSV files for data storage
  - `books.csv`: Stores book information
  - `users.csv`: Stores user information
  - `transactions.csv`: Stores transaction records

## Setup and Running
1. Ensure you have Java Development Kit (JDK) installed on your system.
2. Clone this repository to your local machine.
3. Navigate to the project directory in your terminal.
4. Compile the Java files:
    ```
    javac src/*.java
    ```
5.Run the application:
```
java -cp src Main
```

## CSV File Formats
### books.csv
```
ISBN,Title,Author,Genre,PublishDate,TotalCopies,AvailableCopies
```

### users.csv
```
UserId,Name,Email,PasswordHash,Role,IsActive
```

### transactions.csv
```
TransactionId,UserId,ISBN,BorrowDate,DueDate,ReturnDate,Status
```

## Contributors
- Picone Alessandro

## License
This project is licensed under the MIT License