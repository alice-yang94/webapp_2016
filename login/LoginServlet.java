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

      //password encryption
      
      String storedPassword = getUserPassword(username);

      if (password.equals(storedPassword)) {
        response.sendRedirect("main.html");
      } else {
        response.sendRedirect("login.html");
      }

    } else if ("create".equals(action)) { //Account Creation
      String username = request.getParameter("username");
      String password = request.getParameter("password");      
      String password2 = request.getParameter("password2");

      if (!usernameNotUsed(username)) {
        response.sendRedirect("create.html");
        return;
      }

      if (!password.equals(password2)) {
        response.sendRedirect("create.html");
        return;
      }

      //password encryption

      addUserToDB(username, password);

      response.sendRedirect("login.html");

    } else {
      //Not valid action
    }
  }

  /**
   * adds a new row to the database containing the new user details
   */
  private void addUserToDB(String username, String password) {
    try {
      Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);
      
      Statement statement = conn.createStatement();

      statement.executeUpdate("INSERT INTO users VALUES ('" + username + "', '" + password + "')");

      conn.close();
    } catch (Exception e) {
      //
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
  private String getUserPassword(String username) {
    try {
      Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);
      
      Statement statement = conn.createStatement();
      
      ResultSet rs = statement.executeQuery("SELECT password FROM users WHERE username='" + username + "'");

      if (rs.next()) {
        String res = rs.getString("password");
        conn.close();
        return res;
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