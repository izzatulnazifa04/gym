package gym.auth;

import java.security.GeneralSecurityException; //handle security errors
import java.security.MessageDigest; //nk create hash
import java.security.SecureRandom; //generate random salt
import java.util.Base64; //decode/decode
import java.crypto.SecretKeyFactory; //generate secret code
import java.crypto.spec.PBEKeySpec; //utk simpan pass n setting tuk encrypt

public final class PasswordHasher {
    public static final int ITERATIONS = 120_000; // total iteration untuk secure password
    public static final int KEY_LENGTH = 256; // panjang key untuk secure password
    private static final SecureRandom random = new SecureRandom();

    private PasswordHasher() {
        // private constructor to prevent instantiation
    }

    public static String newSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hash(char[] password, String saltBase64) {
        if (password == null || password.length == 0) { // Check password sebelum proses hashing
            throw new IllegalArgumentException("Password is required.");
        }
        
        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64); // Decode salt daripada Base64
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
                password, salt, ITERATIONS, KEY_BITS);

                try {
                SecretKeyFactory factory = SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256");
                byte[] result = factory.generateSecret(keySpec).getEncoded();
                return Base64.getEncoder().encodeToString(result);
            } finally {
                keySpec.clearPassword();
            }
        } catch (GeneralSecurityException | IllegalArgumentException ex) {
            throw new IllegalStateException("Unable to hash password.", ex);
        }
    }

}