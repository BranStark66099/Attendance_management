package main;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class hash_pass {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());


        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString().toUpperCase();
    }
}
