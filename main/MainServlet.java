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

    try {
      Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);
      
      Statement statement = conn.createStatement();
      
      ResultSet rs = statement.executeQuery("SELECT * FROM completedGames ORDER BY score DESC");
      int i = 1;
      while (rs.next() && i < 10) {
        res += "<div class=\"well well-sm\"><b>" + i + ": " + rs.getString("username") 
                + "</b><br />" + rs.getString("score") + "</div>";
        i++;
      }

      conn.close();
    } catch (Exception e) { //print dummy data as database could not be accessed
      for (int x = 1; x < 10; x++) {
        res += "<div class=\"well well-sm\"><b>" + x + ": player</b><br />999999</div>";
      }
    }

    response.setContentType("text/html");

    PrintWriter out = response.getWriter();

    out.println(res);
  }
}