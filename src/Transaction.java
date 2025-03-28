// File: Transaction.java

import enums.TransactionStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a transaction in the library management system.
 * A transaction records the borrowing and returning of books by users.
 */
public class Transaction {
    private String transactionId;
    private String userId;
    private String ISBN;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private TransactionStatus status;

    /**
     * Constructor for creating a new transaction.
     *
     * @param userId The ID of the user borrowing the book
     * @param ISBN The ISBN of the borrowed book
     * @param loanPeriodDays The number of days the book can be borrowed
     */
    public Transaction(String userId, String ISBN, int loanPeriodDays) {
        this.transactionId = UUID.randomUUID().toString();
        this.userId = userId;
        this.ISBN = ISBN;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(loanPeriodDays);
        this.status = TransactionStatus.ACTIVE;
    }

    // Getters and setters
    public String getTransactionId() { return transactionId; }

    public String getUserId() { return userId; }

    public String getISBN() { return ISBN; }

    public LocalDate getBorrowDate() { return borrowDate; }

    public LocalDate getDueDate() { return dueDate; }

    public LocalDate getReturnDate() { return returnDate; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Completes the transaction by setting the return date and updating the status.
     */
    public void completeTransaction() {
        this.returnDate = LocalDate.now();
        this.status = TransactionStatus.COMPLETED;
    }

    /**
     * Renews the transaction by extending the due date.
     *
     * @param extensionDays The number of days to extend the loan
     * @return true if the renewal was successful, false if the transaction is not eligible for renewal
     */
    public boolean renewTransaction(int extensionDays) {
        if (this.status == TransactionStatus.ACTIVE || this.status == TransactionStatus.RENEWED) {
            this.dueDate = this.dueDate.plusDays(extensionDays);
            this.status = TransactionStatus.RENEWED;
            return true;
        }
        return false;
    }

    /**
     * Checks if the transaction is overdue.
     *
     * @return true if the transaction is overdue, false otherwise
     */
    public boolean isOverdue() {
        return LocalDate.now().isAfter(this.dueDate) && this.status != TransactionStatus.COMPLETED;
    }

    /**
     * Creates a CSV representation of the transaction.
     *
     * @return A string containing the transaction's data in CSV format
     */
    public String toCSV() {
        return String.join(",",
                transactionId,
                userId,
                ISBN,
                borrowDate.toString(),
                dueDate.toString(),
                returnDate != null ? returnDate.toString() : "",
                status.name()
        );
    }

    /**
     * Creates a Transaction object from a CSV string.
     *
     * @param csv The CSV string containing transaction data
     * @return A new Transaction object
     */
    public static Transaction fromCSV(String csv) {
        String[] parts = csv.split(",");
        Transaction transaction = new Transaction(parts[1], parts[2], 0);
        transaction.transactionId = parts[0];
        transaction.borrowDate = LocalDate.parse(parts[3]);
        transaction.dueDate = LocalDate.parse(parts[4]);
        if (!parts[5].isEmpty()) {
            transaction.returnDate = LocalDate.parse(parts[5]);
        }
        transaction.status = TransactionStatus.valueOf(parts[6]);
        return transaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", userId='" + userId + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                '}';
    }
}
