package enums;

// File: UserRole.java

/**
 * Represents the different roles a user can have in the library management system.
 * Each role has different levels of access and permissions within the system.
 */
public enum UserRole {
    ADMIN("Administrator"),
    LIBRARIAN("Librarian"),
    MEMBER("Member"),
    GUEST("Guest");

    private final String displayName;

    /**
     * Constructor for UserRole enum.
     *
     * @param displayName The human-readable name of the role.
     */
    UserRole(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the role.
     *
     * @return The human-readable name of the role.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Checks if this role has higher or equal privileges compared to another role.
     * The order of privilege from highest to lowest is: ADMIN > LIBRARIAN > MEMBER > GUEST
     *
     * @param otherRole The role to compare against.
     * @return true if this role has higher or equal privileges, false otherwise.
     */
    public boolean hasPrivilegeOver(UserRole otherRole) {
        return this.ordinal() <= otherRole.ordinal();
    }

    /**
     * Converts a string to a UserRole enum value, ignoring case.
     *
     * @param roleString The string representation of the role.
     * @return The corresponding UserRole enum value.
     * @throws IllegalArgumentException if the input string doesn't match any UserRole.
     */
    public static UserRole fromString(String roleString) {
        try {
            return UserRole.valueOf(roleString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user role: " + roleString, e);
        }
    }

    /**
     * Returns a string representation of the role (its display name).
     *
     * @return The display name of the role.
     */
    @Override
    public String toString() {
        return displayName;
    }
}
