// File: Book.java

import enums.Genre;

import java.util.Objects;

/**
 * Represents a book in the library management system.
 */
public class Book {
    private String ISBN;
    private String title;
    private String author;
    private Genre genre;
    private boolean isAvailable;
    private int totalCopies;
    private int availableCopies;

    /**
     * Constructor for creating a new book.
     *
     * @param ISBN The International Standard Book Number
     * @param title The title of the book
     * @param author The author of the book
     * @param genre The genre of the book
     * @param totalCopies The total number of copies of this book
     */
    public Book(String ISBN, String title, String author, Genre genre, int totalCopies) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.isAvailable = totalCopies > 0;
    }

    // Getters and setters
    public String getISBN() { return ISBN; }
    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }

    public boolean isAvailable() { return isAvailable; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
        updateAvailability();
    }

    public int getAvailableCopies() { return availableCopies; }

    /**
     * Updates the number of available copies and the availability status.
     *
     * @param change The number of copies to add (positive) or remove (negative)
     */
    public void updateAvailableCopies(int change) {
        this.availableCopies += change;
        updateAvailability();
    }

    private void updateAvailability() {
        this.isAvailable = this.availableCopies > 0;
    }

    /**
     * Creates a CSV representation of the book.
     *
     * @return A string containing the book's data in CSV format
     */
    public String toCSV() {
        return String.join(",", ISBN, title, author, genre.name(),
                String.valueOf(isAvailable), String.valueOf(totalCopies),
                String.valueOf(availableCopies));
    }

    /**
     * Creates a Book object from a CSV string.
     *
     * @param csv The CSV string containing book data
     * @return A new Book object
     */
    public static Book fromCSV(String csv) {
        String[] parts = csv.split(",");
        Book book = new Book(parts[0], parts[1], parts[2], Genre.valueOf(parts[3]), Integer.parseInt(parts[5]));
        book.availableCopies = Integer.parseInt(parts[6]);
        book.updateAvailability();
        return book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(ISBN, book.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN);
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre=" + genre +
                ", isAvailable=" + isAvailable +
                ", totalCopies=" + totalCopies +
                ", availableCopies=" + availableCopies +
                '}';
    }
}
