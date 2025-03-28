import enums.UserRole;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.List;

public class AuthService {
    private final CSVUserDAO userDAO;

    public AuthService(CSVUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Authenticates a user based on their username (email) and password.
     *
     * @param username The user's email address
     * @param password The user's password
     * @return The authenticated User object if successful, null otherwise
     * @throws IOException If there's an error reading the user data
     */
    public User authenticate(String username, String password) throws IOException, AuthenticationException {
        List<User> users = userDAO.loadUsers();

        for (User user : users) {
            if (user.getEmail().equals(username) && user.verifyPassword(password)) {
                if (user.isActive()) {
                    return user;
                } else {
                    throw new AuthenticationException("User account is inactive");
                }
            }
        }

        return null; // Authentication failed
    }

    /**
     * Changes the password for a given user.
     *
     * @param userId The ID of the user
     * @param oldPassword The current password
     * @param newPassword The new password
     * @return true if the password was changed successfully, false otherwise
     * @throws IOException If there's an error updating the user data
     */
    public boolean changePassword(String userId, String oldPassword, String newPassword) throws IOException {
        User user = userDAO.findUserById(userId);
        if (user != null && user.verifyPassword(oldPassword)) {
            user.setPasswordHash(PasswordUtils.hashPassword(newPassword));
            userDAO.updateUser(user);
            return true;
        }
        return false;
    }

    /**
     * Checks if a user has a specific role.
     *
     * @param user The user to check
     * @param role The role to check for
     * @return true if the user has the specified role, false otherwise
     */
    public boolean hasRole(User user, UserRole role) {
        return user.getRole() == role;
    }
}
