import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

public class Encryption {

  private static final String hardkey = "E8726B4EDEBA34F9";

  public static String Encrypt(String text) {
    byte[] encryptedText = null;
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      byte[] key = hardkey.getBytes();
      key = Arrays.copyOf(md.digest(key), 16);

      SecretKey secretKey = new SecretKeySpec(key, "AES");

      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);

      encryptedText = cipher.doFinal(text.getBytes("UTF-8"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new String(encryptedText);
  }

}
