package enums;

// File: TransactionStatus.java

import java.util.Arrays;

/**
 * Represents the different statuses a library transaction can have.
 * This enum is used to track the lifecycle of book loans and returns.
 */
public enum TransactionStatus {
    ACTIVE("Active", "The book is currently checked out"),
    COMPLETED("Completed", "The book has been returned"),
    OVERDUE("Overdue", "The book is past its due date"),
    RENEWED("Renewed", "The loan period has been extended"),
    LOST("Lost", "The book has been reported as lost"),
    RESERVED("Reserved", "The book is reserved for pickup");

    private final String displayName;
    private final String description;

    /**
     * Constructor for TransactionStatus enum.
     *
     * @param displayName The human-readable name of the status.
     * @param description A brief description of what the status means.
     */
    TransactionStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Gets the display name of the status.
     *
     * @return The human-readable name of the status.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the description of the status.
     *
     * @return A brief description of what the status means.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Converts a string to a TransactionStatus enum value, ignoring case.
     *
     * @param statusString The string representation of the status.
     * @return The corresponding TransactionStatus enum value.
     * @throws IllegalArgumentException if the input string doesn't match any TransactionStatus.
     */
    public static TransactionStatus fromString(String statusString) {
        String normalizedStatus = statusString.toUpperCase().replace(" ", "_");
        return Arrays.stream(TransactionStatus.values())
                .filter(status -> status.name().equals(normalizedStatus))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction status: " + statusString));
    }

    /**
     * Returns a string representation of the status (its display name).
     *
     * @return The display name of the status.
     */
    @Override
    public String toString() {
        return displayName;
    }
}
