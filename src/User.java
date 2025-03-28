// File: User.java

import enums.UserRole;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user in the library management system.
 */
public class User {
    private String userId;
    private String name;
    private String email;
    private String passwordHash;
    private UserRole role;
    private boolean isActive;

    /**
     * Constructor for creating a new user.
     *
     * @param name The name of the user
     * @param email The email address of the user
     * @param password The plain text password (will be hashed)
     * @param role The role of the user
     */
    public User(String name, String email, String password, UserRole role) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.passwordHash = hashPassword(password);
        this.role = role;
        this.isActive = true;
    }

    // Getters and setters
    public String getUserId() { return userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Verifies the provided password against the stored hash.
     *
     * @param password The password to verify
     * @return true if the password is correct, false otherwise
     */
    public boolean verifyPassword(String password) {
        return PasswordUtils.verifyPassword(password, this.passwordHash);
    }

    /**
     * Changes the user's password.
     *
     * @param oldPassword The current password
     * @param newPassword The new password
     * @return true if the password was changed successfully, false otherwise
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (verifyPassword(oldPassword)) {
            this.passwordHash = hashPassword(newPassword);
            return true;
        }
        return false;
    }

    /**
     * Hashes the provided password.
     *
     * @param password The plain text password
     * @return The hashed password
     */
    private String hashPassword(String password) {
        return PasswordUtils.hashPassword(password);
    }

    /**
     * Creates a CSV representation of the user.
     *
     * @return A string containing the user's data in CSV format
     */
    public String toCSV() {
        return String.join(",", userId, name, email, passwordHash, role.name(), String.valueOf(isActive));
    }

    /**
     * Creates a User object from a CSV string.
     *
     * @param csv The CSV string containing user data
     * @return A new User object
     */
    public static User fromCSV(String csv) {
        String[] parts = csv.split(",");
        User user = new User(parts[1], parts[2], "", UserRole.valueOf(parts[4]));
        user.userId = parts[0];
        user.passwordHash = parts[3];
        user.isActive = Boolean.parseBoolean(parts[5]);
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }
}
