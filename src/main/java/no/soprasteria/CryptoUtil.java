package no.soprasteria;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CryptoUtil {

    private static final Charset UTF8 = StandardCharsets.UTF_8;
    public static final String AES = "AES";

    /** Simple encryption with given password. Uses AES algorithm with given password. */
    public static String encrypt(String plainText, byte[] passwordBytes) throws GeneralSecurityException {
        SecretKeySpec sks = new SecretKeySpec(makeKey(passwordBytes), AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] kryptert = cipher.doFinal(plainText.getBytes(UTF8));
        return byteArrayToHexString(kryptert);
    }

    private static byte[] makeKey(byte[] password) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] key = sha.digest(password);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            return key;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e); // should never happen
        }
    }

    public static String decrypt(String encryptedString, byte[] passwordBytes) {
        try {
            SecretKeySpec sks = new SecretKeySpec(makeKey(passwordBytes), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, sks);
            byte[] decrypted = cipher.doFinal(hexStringToByteArray(encryptedString));
            return new String(decrypted, UTF8);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    private static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
}
