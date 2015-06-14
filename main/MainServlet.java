import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class MainServlet extends HttpServlet {

  private final String dbConnString = "jdbc:postgresql://db.doc.ic.ac.uk/g1427101_u";
  private final String dbUsername   = "g1427101_u";
  private final String dbPassword   = "ZfOfLyHLTA";

  /**
   * handles https GET messages for scoreboard data
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    String res = "";

    String action = request.getParameter("action");

    if ("userGames".equals(action)) {
      Cookie ck[] = request.getCookies();
      String username = "";
      for (Cookie c : ck) {
        if (c.getName().equals("username")) {
          username = c.getValue();
          break;
        }
      }


      res += "<h1>" + username + "'s recent games: </h1><br />";

      try {
        Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);
        
        Statement statement = conn.createStatement();
        
        ResultSet rs = statement.executeQuery("SELECT * FROM completedGames WHERE username='" + 
                            username + "' ORDER BY dateCompleted DESC");

        int i = 1;
        while (rs.next() && i < 10) {
          if (i % 3 == 1) res += "<div class=\"col-md-4\">";

          res += "<div class=\"well well-sm\"><b>" + rs.getString("dateCompleted") + 
                  "</b><br />" + rs.getString("score") + "</div>";

          if (i % 3 == 0) res += "</div>";
          i++;
        }
      } catch (Exception e) {
        for (int x = 0; x < 3; x++) {
          res += "<div class=\"row\">";

          for (int y = 0; y < 3; y++) {
            res += "<div class=\"col-md-4\"><div class=\"well well-sm\"><b>1/1/1970</b><br />999999</div></div>";
          }

          res += "</div>";
        }
      }

      response.setContentType("text/html");

      PrintWriter out = response.getWriter();

      out.println(res);
      return;

    } else if ("highScores".equals(action)) {
      try {
        Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);
        
        Statement statement = conn.createStatement();
        
        ResultSet rs = statement.executeQuery("SELECT * FROM completedGames ORDER BY score ASC");
        int i = 1;
        while (rs.next() && i < 10) {
          res += "<div class=\"well well-sm\"><b>" + i + ": " + rs.getString("username") +
                  " - " + rs.getString("dateCompleted") + "</b><br />" + rs.getString("score") + "</div>";
          i++;
        }

        conn.close();
      } catch (Exception e) { //print dummy data as database could not be accessed
        for (int x = 1; x < 10; x++) {
          res += "<div class=\"well well-sm\"><b>" + x + ": player - 1/1/1970</b><br />999999</div>";
        }
      }

      response.setContentType("text/html");

      PrintWriter out = response.getWriter();

      out.println(res);
      return;
    }

  }
}