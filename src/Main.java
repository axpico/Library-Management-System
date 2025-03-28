import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize DAOs
            CSVBookDAO bookDAO = new CSVBookDAO();
            CSVUserDAO userDAO = new CSVUserDAO();
            CSVTransactionDAO transactionDAO = new CSVTransactionDAO();

            // Initialize LibraryManager and ReportGenerator
            LibraryManager libraryManager = new LibraryManager();
            ReportGenerator reportGenerator = new ReportGenerator(bookDAO, userDAO, transactionDAO);

            // Demonstrate user authentication
            User user = libraryManager.authenticateUser("john.smith@library.com", "password123");
            if (user != null) {
                System.out.println("Authenticated user: " + user.getName());
            } else {
                System.out.println("Authentication failed");
            }

            // Demonstrate book borrowing
            String userId = "MEM001";
            String ISBN = "9780060850524";
            libraryManager.borrowBook(userId, ISBN, 14);
            System.out.println("Book borrowed successfully");

            // Demonstrate book returning
            List<Transaction> userTransactions = libraryManager.getUserTransactions(userId);
            if (!userTransactions.isEmpty()) {
                String transactionId = userTransactions.getFirst().getTransactionId();
                libraryManager.returnBook(transactionId);
                System.out.println("Book returned successfully");
            }

            // Generate reports
            System.out.println("\nInventory Report:");
            reportGenerator.generateInventoryReport();

            System.out.println("\nOverdue Report:");
            reportGenerator.generateOverdueReport();

            System.out.println("\nUser Activity Report:");
            reportGenerator.generateUserReport(userId);

            System.out.println("\nPopular Books Report:");
            reportGenerator.generatePopularBooksReport(5);

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }
}
