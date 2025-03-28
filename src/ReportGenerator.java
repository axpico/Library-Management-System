// File: ReportGenerator.java

import enums.TransactionStatus;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generates various reports for the library management system.
 */
public class ReportGenerator {

    private final CSVBookDAO bookDAO;
    private final CSVUserDAO userDAO;
    private final CSVTransactionDAO transactionDAO;

    /**
     * Constructor for ReportGenerator.
     *
     * @param bookDAO The data access object for books
     * @param userDAO The data access object for users
     * @param transactionDAO The data access object for transactions
     */
    public ReportGenerator(CSVBookDAO bookDAO, CSVUserDAO userDAO, CSVTransactionDAO transactionDAO) {
        this.bookDAO = bookDAO;
        this.userDAO = userDAO;
        this.transactionDAO = transactionDAO;
    }

    /**
     * Generates an inventory report of all books in the library.
     *
     * @throws IOException If there's an error reading the book data
     */
    public void generateInventoryReport() throws IOException {
        List<Book> books = CSVBookDAO.loadBooks();

        System.out.println("=== Library Inventory Report ===");
        System.out.printf("%-15s %-40s %-20s %-10s %-10s%n", "ISBN", "Title", "Author", "Available", "Total");
        System.out.println("--------------------------------------------------------------------------------");

        for (Book book : books) {
            System.out.printf("%-15s %-40s %-20s %-10d %-10d%n",
                    book.getISBN(),
                    truncate(book.getTitle(), 37),
                    truncate(book.getAuthor(), 17),
                    book.getAvailableCopies(),
                    book.getTotalCopies());
        }

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Total Books: " + books.size());
    }

    /**
     * Generates a report of all overdue books.
     *
     * @throws IOException If there's an error reading the transaction data
     */
    public void generateOverdueReport() throws IOException {
            List<Transaction> transactions = transactionDAO.loadTransactions();
        LocalDate today = LocalDate.now();

        List<Transaction> overdueTransactions = transactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.ACTIVE && t.getDueDate().isBefore(today))
                .toList();

        System.out.println("=== Overdue Books Report ===");
        System.out.printf("%-15s %-15s %-15s %-12s%n", "ISBN", "User ID", "Due Date", "Days Overdue");
        System.out.println("----------------------------------------------------------");

        for (Transaction t : overdueTransactions) {
            long daysOverdue = t.getDueDate().until(today).getDays();
            System.out.printf("%-15s %-15s %-15s %-12d%n",
                    t.getISBN(),
                    t.getUserId(),
                    t.getDueDate(),
                    daysOverdue);
        }

        System.out.println("----------------------------------------------------------");
        System.out.println("Total Overdue Books: " + overdueTransactions.size());
    }

    /**
     * Generates a report of a user's borrowing activity.
     *
     * @param userId The ID of the user
     * @throws IOException If there's an error reading the user or transaction data
     */
    public void generateUserReport(String userId) throws IOException {
        User user = userDAO.findUserById(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        List<Transaction> userTransactions = transactionDAO.loadTransactions().stream()
                .filter(t -> t.getUserId().equals(userId))
                .toList();

        System.out.println("=== User Activity Report ===");
        System.out.println("User: " + user.getName() + " (ID: " + user.getUserId() + ")");
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-15s %-12s %-12s %-10s%n", "ISBN", "Borrow Date", "Return Date", "Status");
        System.out.println("----------------------------------------------------------");

        for (Transaction t : userTransactions) {
            System.out.printf("%-15s %-12s %-12s %-10s%n",
                    t.getISBN(),
                    t.getBorrowDate(),
                    t.getReturnDate() != null ? t.getReturnDate() : "-",
                    t.getStatus());
        }

        System.out.println("----------------------------------------------------------");
        System.out.println("Total Transactions: " + userTransactions.size());
    }

    /**
     * Generates a report of the most popular books.
     *
     * @param topN The number of top books to include in the report
     * @throws IOException If there's an error reading the transaction data
     */
    public void generatePopularBooksReport(int topN) throws IOException {
        List<Transaction> transactions = transactionDAO.loadTransactions();

        Map<String, Long> bookBorrowCounts = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getISBN, Collectors.counting()));

        List<Map.Entry<String, Long>> sortedBooks = bookBorrowCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .toList();

        System.out.println("=== Most Popular Books Report ===");
        System.out.printf("%-15s %-40s %-10s%n", "ISBN", "Title", "Borrows");
        System.out.println("----------------------------------------------------------");

        for (Map.Entry<String, Long> entry : sortedBooks) {
            Book book = CSVBookDAO.findBookByISBN(entry.getKey());
            if (book != null) {
                System.out.printf("%-15s %-40s %-10d%n",
                        book.getISBN(),
                        truncate(book.getTitle(), 37),
                        entry.getValue());
            }
        }

        System.out.println("----------------------------------------------------------");
    }

    /**
     * Truncates a string to a specified length, adding an ellipsis if truncated.
     *
     * @param str The string to truncate
     * @param length The maximum length
     * @return The truncated string
     */
    private String truncate(String str, int length) {
        if (str.length() <= length) {
            return str;
        }
        return str.substring(0, length - 3) + "...";
    }
}
