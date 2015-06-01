package encryption;

/* Encryption imports */
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

/* Linking */
import encryption.PasswordPair;


public class Encryption {

  private static final String hardKey = "E8726B4EDEBA34F9";

  public static PasswordPair Encrypt(String text) {
    text = EncryptString(text, hardKey);

    String salt = String.valueOf(System.currentTimeMillis());
    text = EncryptString(text, salt);

    return new PasswordPair(text, salt);
  }

  public static String EncryptString(String text, String key) {
    byte[] encryptedText = null;
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      byte[] keyBytes = key.getBytes();
      keyBytes = Arrays.copyOf(md.digest(keyBytes), 16);

      SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);

      encryptedText = cipher.doFinal(text.getBytes("UTF-8"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new String(encryptedText);
  }

}
