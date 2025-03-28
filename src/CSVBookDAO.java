import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Data Access Object for Book entities using CSV file storage.
 */
public class CSVBookDAO {
    private static final String FILE_PATH = "data/books.csv";
    private static final String CSV_HEADER = "ISBN,Title,Author,Genre,IsAvailable,TotalCopies,AvailableCopies";

    /**
     * Saves a list of books to the CSV file.
     *
     * @param books The list of books to save
     * @throws IOException If an I/O error occurs
     */
    public static void saveBooks(List<Book> books) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Book book : books) {
                writer.write(book.toCSV());
                writer.newLine();
            }
        }
    }

    /**
     * Loads all books from the CSV file.
     *
     * @return A list of all books
     * @throws IOException If an I/O error occurs
     */
    public static List<Book> loadBooks() throws IOException {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                books.add(Book.fromCSV(line));
            }
        }
        return books;
    }

    /**
     * Adds a new book to the CSV file.
     *
     * @param book The book to add
     * @throws IOException If an I/O error occurs
     */
    public static void addBook(Book book) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH), StandardOpenOption.APPEND)) {
            writer.write(book.toCSV());
            writer.newLine();
        }
    }

    /**
     * Updates an existing book in the CSV file.
     *
     * @param updatedBook The updated book information
     * @throws IOException If an I/O error occurs
     */
    public static void updateBook(Book updatedBook) throws IOException {
        List<Book> books = loadBooks();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getISBN().equals(updatedBook.getISBN())) {
                books.set(i, updatedBook);
                break;
            }
        }
        saveBooks(books);
    }

    /**
     * Deletes a book from the CSV file.
     *
     * @param ISBN The ISBN of the book to delete
     * @throws IOException If an I/O error occurs
     */
    public static void deleteBook(String ISBN) throws IOException {
        List<Book> books = loadBooks();
        books.removeIf(book -> book.getISBN().equals(ISBN));
        saveBooks(books);
    }

    /**
     * Finds a book by its ISBN.
     *
     * @param ISBN The ISBN to search for
     * @return The found book, or null if not found
     * @throws IOException If an I/O error occurs
     */
    public static Book findBookByISBN(String ISBN) throws IOException {
        List<Book> books = loadBooks();
        return books.stream()
                .filter(book -> book.getISBN().equals(ISBN))
                .findFirst()
                .orElse(null);
    }
}
