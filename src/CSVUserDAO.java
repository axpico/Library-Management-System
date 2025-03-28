import enums.UserRole;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entities using CSV file storage.
 */
public class CSVUserDAO {
    private static final String FILE_PATH = "data/users.csv";
    private static final String CSV_HEADER = "UserId,Name,Email,PasswordHash,Role,IsActive";

    /**
     * Saves a list of users to the CSV file.
     *
     * @param users The list of users to save
     * @throws IOException If an I/O error occurs
     */
    public void saveUsers(List<User> users) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (User user : users) {
                writer.write(userToCSV(user));
                writer.newLine();
            }
        }
    }

    /**
     * Loads all users from the CSV file.
     *
     * @return A list of all users
     * @throws IOException If an I/O error occurs
     */
    public List<User> loadUsers() throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                users.add(csvToUser(line));
            }
        }
        return users;
    }

    /**
     * Adds a new user to the CSV file.
     *
     * @param user The user to add
     * @throws IOException If an I/O error occurs
     */
    public void addUser(User user) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(userToCSV(user));
            writer.newLine();
        }
    }

    /**
     * Updates an existing user in the CSV file.
     *
     * @param updatedUser The updated user information
     * @throws IOException If an I/O error occurs
     */
    public void updateUser(User updatedUser) throws IOException {
        List<User> users = loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(updatedUser.getUserId())) {
                users.set(i, updatedUser);
                break;
            }
        }
        saveUsers(users);
    }

    /**
     * Finds a user by their ID.
     *
     * @param userId The ID of the user to find
     * @return The found user, or null if not found
     * @throws IOException If an I/O error occurs
     */
    public User findUserById(String userId) throws IOException {
        List<User> users = loadUsers();
        return users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Converts a User object to a CSV string.
     *
     * @param user The user to convert
     * @return A CSV string representation of the user
     */
    private String userToCSV(User user) {
        return String.join(",",
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole().toString(),
                String.valueOf(user.isActive())
        );
    }

    /**
     * Converts a CSV string to a User object.
     *
     * @param csv The CSV string to convert
     * @return A User object
     */
    private User csvToUser(String csv) {
        String[] parts = csv.split(",");
        User user = new User(parts[1], parts[2], "", UserRole.valueOf(parts[4]));
        user.setUserId(parts[0]);
        user.setPasswordHash(parts[3]); // Set hashed password directly from CSV
        user.setActive(Boolean.parseBoolean(parts[5]));
        return user;
    }
}
