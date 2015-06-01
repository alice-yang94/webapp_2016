package encryption;

public class PasswordPair {
  
  private String password;
  private String salt;

  public PasswordPair(String password, String salt) {
    this.password = password;
    this.salt = salt;
  }

  public String getPassword() {
    return password;
  }

  public String getSalt() {
    return salt;
  }

}
