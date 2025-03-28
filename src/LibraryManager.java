// File: LibraryManager.java

import enums.TransactionStatus;
import enums.UserRole;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core class managing library operations and coordinating data access.
 */
public class LibraryManager {
    private final AuthService authService;
    private final ReportGenerator reportGenerator;
    private final CSVUserDAO csvUserDAO = new CSVUserDAO();
    private final CSVTransactionDAO csvTransactionDAO = new CSVTransactionDAO();

    public LibraryManager() {
        this.authService = new AuthService(csvUserDAO);
        this.reportGenerator = new ReportGenerator(new CSVBookDAO(), csvUserDAO, csvTransactionDAO);
    }

    // Authentication operations
    public User authenticateUser(String username, String password) throws IOException, AuthenticationException {
        return authService.authenticate(username, password);
    }

    // Book operations
    public void addBook(Book book) throws IOException {
        CSVBookDAO.addBook(book);
    }

    public void updateBook(Book updatedBook) throws IOException {
        CSVBookDAO.updateBook(updatedBook);
    }

    public void deleteBook(String ISBN) throws IOException {
        CSVBookDAO.deleteBook(ISBN);
    }

    public List<Book> searchBooks(String query) throws IOException {
        return CSVBookDAO.loadBooks().stream()
                .filter(book -> book.getTitle().contains(query) ||
                        book.getAuthor().contains(query) ||
                        book.getISBN().equals(query))
                .collect(Collectors.toList());
    }

    // Transaction operations
    public void borrowBook(String userId, String ISBN, int loanDays) throws IOException {
        User user = csvUserDAO.findUserById(userId);
        Book book = CSVBookDAO.findBookByISBN(ISBN);

        if (user == null || !user.isActive()) {
            throw new IllegalArgumentException("Invalid or inactive user");
        }

        if (book == null || !book.isAvailable()) {
            throw new IllegalArgumentException("Book not available");
        }

        Transaction transaction = new Transaction(userId, ISBN, loanDays);
        ArrayList<Transaction> temp = new ArrayList<>();
        temp.add(transaction);
        csvTransactionDAO.saveTransactions(temp);

        book.updateAvailableCopies(-1);
        CSVBookDAO.updateBook(book);
    }

    public void returnBook(String transactionId) throws IOException {
        Transaction transaction = csvTransactionDAO.findTransactionById(transactionId);
        if (transaction == null || transaction.getStatus() != TransactionStatus.ACTIVE) {
            throw new IllegalArgumentException("Invalid transaction");
        }

        transaction.completeTransaction();
        csvTransactionDAO.updateTransaction(transaction);

        Book book = CSVBookDAO.findBookByISBN(transaction.getISBN());
        book.updateAvailableCopies(1);
        CSVBookDAO.updateBook(book);
    }

    public List<Transaction> getUserTransactions(String userId) throws IOException {
        return csvTransactionDAO.loadTransactions().stream()
                .filter(t -> t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // Reporting operations
    public void generateInventoryReport() throws IOException {
        reportGenerator.generateInventoryReport();
    }

    public void generateOverdueReport() throws IOException {
        reportGenerator.generateOverdueReport();
    }

    public void generateUserActivityReport(String userId) throws IOException {
        reportGenerator.generateUserReport(userId);
    }

    // User management
    public void addUser(User user) throws IOException {
        csvUserDAO.addUser(user);
    }

    public void updateUser(User updatedUser) throws IOException {
        csvUserDAO.updateUser(updatedUser);
    }

    public void deactivateUser(String userId) throws IOException {
        User user = csvUserDAO.findUserById(userId);
        if (user != null) {
            user.setActive(false);
            csvUserDAO.updateUser(user);
        }
    }

    // Utility methods
    public List<Book> getAllBooks() throws IOException {
        return CSVBookDAO.loadBooks();
    }

    public List<User> getAllUsers() throws IOException {
        return csvUserDAO.loadUsers();
    }

    public List<Transaction> getAllTransactions() throws IOException {
        return csvTransactionDAO.loadTransactions();
    }

    // Additional business logic
    public boolean canUserBorrow(User user) throws IOException {
        if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.LIBRARIAN) {
            return true;
        }

        long activeLoans = csvTransactionDAO.loadTransactions().stream()
                .filter(t -> t.getUserId().equals(user.getUserId()))
                .filter(t -> t.getStatus() == TransactionStatus.ACTIVE ||
                        t.getStatus() == TransactionStatus.RENEWED)
                .count();

        return activeLoans < 5; // Maximum 5 loans for regular members
    }

    public void sendOverdueNotifications() throws IOException {
        List<Transaction> overdueTransactions = csvTransactionDAO.loadTransactions().stream()
                .filter(Transaction::isOverdue)
                .toList();

        // Integration with email service would go here
        System.out.println("Sending notifications for " + overdueTransactions.size() + " overdue books");
    }
}
