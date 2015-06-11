import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {

  private final String dbConnString = "jdbc:postgresql://db.doc.ic.ac.uk/g1427101_u";
  private final String dbUsername   = "g1427101_u";
  private final String dbPassword   = "ZfOfLyHLTA";

  /**
   * handles https POST messages relating to the login system
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    loadDBDriver();
    String action = request.getParameter("action");

    if ("login".equals(action)) { //Login
      String username = request.getParameter("username");
      String password = request.getParameter("password");
      
      Encryption storedPasswordPair = getUserPasswordPair(username);

      if (usernameNotUsed(username)) {
        //response.sendError(response.SC_BAD_REQUEST, "The username doesn't exist");
        response.sendRedirect("login.html");
        return;
      }

      String storedPassword = storedPasswordPair.getPassword();
      String storedSalt = storedPasswordPair.getSalt();

      //should password and storedPassword be swapped here? storedPassword should already be encrypted?
      if (storedPassword.equals(Encryption.encryptLoginAttempt(password, storedSalt))) {
        Cookie ck = new Cookie("username", username);
        ck.setPath("/");

        response.addCookie(ck);
        response.sendRedirect("/main/main.html");
      } else {
        //response.sendError(response.SC_BAD_REQUEST, "The password is incorrect");
        response.sendRedirect("login.html");
      }

    } else if ("create".equals(action)) { //Account Creation
      String username = request.getParameter("username");
      String password = request.getParameter("password");      
      String password2 = request.getParameter("password2");

      if (!usernameNotUsed(username)) {
        //response.sendError(response.SC_BAD_REQUEST, "Username already in use");
        response.sendRedirect("create.html");
        return;
      }

      if (!password.equals(password2)) {
        //response.sendError(response.SC_BAD_REQUEST, "The passwords don't match");
        response.sendRedirect("create.html");
        return;
      }

      Encryption encryptedPasswordPair = Encryption.encrypt(password);
      String salt = encryptedPasswordPair.getSalt();
      String encryptedPassword = encryptedPasswordPair.getPassword();

      addUserToDB(username, encryptedPassword, salt);

      response.sendRedirect("login.html");

    } else {
      //Not valid action
    }
  }

  /**
   * adds a new row to the database containing the new user details
   */
  private void addUserToDB(String username, String password, String salt) {
    try {
      Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);
      
      Statement statement = conn.createStatement();

      statement.executeUpdate("INSERT INTO users VALUES ('" + username + "', '" + password + "', '" + salt + "')");

      conn.close();
    } catch (Exception e) {
      assert(false);
    }
  }

  /**
   * returns true iff the given username is not in the database
   * returns false on exception
   */
  private boolean usernameNotUsed(String username) {
    try {
      Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);
      
      Statement statement = conn.createStatement();
      
      ResultSet rs = statement.executeQuery("SELECT username FROM users");

      while (rs.next()) {
        if (username.equals(rs.getString("username"))) {
          conn.close();
          return false;
        }
      }

      conn.close();
    } catch (Exception e) {
      //
      return false;
    }

    return true;
  }

  /**
   * returns the password associated with the given username
   * returns null if the username is not in the database or on exception
   */
  private Encryption getUserPasswordPair(String username) {
    try {
      Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);
      
      Statement statement = conn.createStatement();
      
      ResultSet rs = statement.executeQuery("SELECT password, salt FROM users WHERE username='" + username + "'");

      if (rs.next()) {
        String password = rs.getString("password");
        String salt = rs.getString("salt");
        conn.close();
        return new Encryption(password, salt);
      } else { // username not in database
        conn.close();
        return null;
      }
    } catch (Exception e) {
      //
    }
    return null;
  }

  /**
   * attempts to load the database driver
   */
  private void loadDBDriver() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      //
    }
  }
} 
