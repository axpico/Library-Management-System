import enums.TransactionStatus;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Transaction entities using CSV file storage.
 */
public class CSVTransactionDAO {
    private static final String FILE_PATH = "data/transactions.csv";
    private static final String CSV_HEADER = "TransactionId,UserId,ISBN,BorrowDate,DueDate,ReturnDate,Status";

    /**
     * Saves a list of transactions to the CSV file.
     *
     * @param transactions The list of transactions to save
     * @throws IOException If an I/O error occurs
     */
    public void saveTransactions(List<Transaction> transactions) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Transaction transaction : transactions) {
                writer.write(transactionToCSV(transaction));
                writer.newLine();
            }
        }
    }

    /**
     * Loads all transactions from the CSV file.
     *
     * @return A list of all transactions
     * @throws IOException If an I/O error occurs
     */
    public List<Transaction> loadTransactions() throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                transactions.add(csvToTransaction(line));
            }
        }
        return transactions;
    }

    /**
     * Adds a new transaction to the CSV file.
     *
     * @param transaction The transaction to add
     * @throws IOException If an I/O error occurs
     */
    public void addTransaction(Transaction transaction) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(transactionToCSV(transaction));
            writer.newLine();
        }
    }

    /**
     * Updates an existing transaction in the CSV file.
     *
     * @param updatedTransaction The updated transaction information
     * @throws IOException If an I/O error occurs
     */
    public void updateTransaction(Transaction updatedTransaction) throws IOException {
        List<Transaction> transactions = loadTransactions();
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getTransactionId().equals(updatedTransaction.getTransactionId())) {
                transactions.set(i, updatedTransaction);
                break;
            }
        }
        saveTransactions(transactions);
    }

    /**
     * Finds a transaction by its ID.
     *
     * @param transactionId The ID of the transaction to find
     * @return The found transaction, or null if not found
     * @throws IOException If an I/O error occurs
     */
    public Transaction findTransactionById(String transactionId) throws IOException {
        List<Transaction> transactions = loadTransactions();
        return transactions.stream()
                .filter(t -> t.getTransactionId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Converts a Transaction object to a CSV string.
     *
     * @param transaction The transaction to convert
     * @return A CSV string representation of the transaction
     */
    private String transactionToCSV(Transaction transaction) {
        return String.join(",",
                transaction.getTransactionId(),
                transaction.getUserId(),
                transaction.getISBN(),
                transaction.getBorrowDate().toString(),
                transaction.getDueDate().toString(),
                transaction.getReturnDate() != null ? transaction.getReturnDate().toString() : "",
                transaction.getStatus().toString()
        );
    }

    /**
     * Converts a CSV string to a Transaction object.
     *
     * @param csv The CSV string to convert
     * @return A Transaction object
     */
    private Transaction csvToTransaction(String csv) {
        String[] parts = csv.split(",");
        Transaction transaction = new Transaction(parts[1], parts[2], (int) ChronoUnit.DAYS.between(LocalDate.parse(parts[3]), LocalDate.parse(parts[4])));
        transaction.setTransactionId(parts[0]);
        if (!parts[5].isEmpty()) {
            transaction.setReturnDate(LocalDate.parse(parts[5]));
        }
        transaction.setStatus(TransactionStatus.fromString(parts[6]));
        return transaction;
    }
}
