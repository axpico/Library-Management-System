// File: PasswordUtils.java

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for handling password hashing and verification.
 */
public class PasswordUtils {

    private static final int SALT_LENGTH = 16; // 128 bits
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Hashes a password using SHA-256 with a random salt.
     *
     * @param password The password to hash
     * @return A string containing the salt and hashed password, Base64 encoded
     */
    public static String hashPassword(String password) {
        try {
            byte[] salt = generateSalt();
            byte[] hashedPassword = hashWithSalt(password, salt);

            // Combine salt and hashed password
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            // Encode as Base64 string
            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verifies a password against a stored hash.
     *
     * @param password The password to verify
     * @param storedHash The stored hash to verify against
     * @return true if the password matches the hash, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Decode the stored hash
            byte[] combined = Base64.getDecoder().decode(storedHash);

            // Extract salt and hash
            byte[] salt = new byte[SALT_LENGTH];
            byte[] hash = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(combined, SALT_LENGTH, hash, 0, hash.length);

            // Hash the input password with the extracted salt
            byte[] hashedPassword = hashWithSalt(password, salt);

            // Compare the hashed input with the stored hash
            return MessageDigest.isEqual(hashedPassword, hash);
        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Generates a random salt.
     *
     * @return A byte array containing the salt
     */
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hashes a password with a given salt.
     *
     * @param password The password to hash
     * @param salt The salt to use
     * @return The hashed password
     * @throws NoSuchAlgorithmException If the hashing algorithm is not available
     */
    private static byte[] hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        md.update(salt);
        return md.digest(password.getBytes());
    }
}