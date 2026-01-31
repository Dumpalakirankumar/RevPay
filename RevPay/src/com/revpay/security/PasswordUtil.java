package com.revpay.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;



public class PasswordUtil {

    private static final String ALGO = "AES";
    private static final String SECRET_KEY = "RevPaySecretKey"; // 16 chars

    private PasswordUtil() {}

    // ================= PASSWORD HASH =================
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest md =
                    java.security.MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password");
        }
    }

    // ================= CARD ENCRYPT =================
    public static String encrypt(String cardNumber) {
        try {
            SecretKeySpec key =
                    new SecretKeySpec(SECRET_KEY.getBytes(), ALGO);

            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encrypted = cipher.doFinal(cardNumber.getBytes());

            return DatatypeConverter.printBase64Binary(encrypted);


        } catch (Exception e) {
            throw new RuntimeException("Encryption failed");
        }
    }

    // ================= CARD DECRYPT =================
    public static String decrypt(String encryptedCard) {
        try {
            SecretKeySpec key =
                    new SecretKeySpec(SECRET_KEY.getBytes(), ALGO);

            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decoded = DatatypeConverter.parseBase64Binary(encryptedCard);

            return new String(cipher.doFinal(decoded));

        } catch (Exception e) {
            throw new RuntimeException("Decryption failed");
        }
    }
}

