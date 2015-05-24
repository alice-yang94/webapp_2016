import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

  private static String TEST_UNAME = "test123";
  private static String TEST_PW = "test321";

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    String action = request.getParameter("action");

    if ("login".equals(action)) { //Login
      String username = request.getParameter("username");
      String password = request.getParameter("password");

      //password encryption
      //lookup details in database

      if (username.equals(TEST_UNAME) && password.equals(TEST_PW)) {
        response.sendRedirect("main.html");
      } else {
        response.sendRedirect("login.html");
      }

    } else if ("create".equals(action)) { //Account Creation
      String username = request.getParameter("username");
      String password = request.getParameter("password");      
      String password2 = request.getParameter("password2");

      //check username doesn't already exist

      if (!password.equals(password2)) {
        response.sendRedirect("create.html");
      }

      //add details to database
      response.sendRedirect("login.html");

    } else {
      //Not valid action
    }
  }
} 