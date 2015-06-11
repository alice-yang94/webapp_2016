/* Encryption imports */
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

public class Encryption {

  private static final String hardKey = "E8726B4EDEBA34F9";

  private String password;
  private String salt;

  public Encryption(String password, String salt) {
    this.password = password;
    this.salt = salt;
  }

  public String getPassword() {
    return password;
  }

  public String getSalt() {
    return salt;
  }

  public static String encryptLoginAttempt(String text, String salt) {
    text = encryptString(text, hardKey);
    text = encryptString(text, salt); 
    
    return text;   
  }

  /**
   * returns the given string, encrypted using the hard key and a
   * generated salt value, and the salt value used
   */
  public static Encryption encrypt(String text) {
    text = encryptString(text, hardKey);

    String newSalt = String.valueOf(System.currentTimeMillis());
    text = encryptString(text, newSalt);

    return new Encryption(text, newSalt);
  }

  /**
   * returns the given string, encrypted with the given key
   */
  public static String encryptString(String text, String key) {
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
