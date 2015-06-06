import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

'<div class=\"well well-sm\"><b>' + i + ': '+ scorers[i-1].uname + '</b><br />' + scorers[i-1].score + '</div>'

public class MainServlet extends HttpServlet {

  private final String dbConnString = "jdbc:postgresql://db.doc.ic.ac.uk/g1427101_u";
  private final String dbUsername   = "g1427101_u";
  private final String dbPassword   = "ZfOfLyHLTA";

  /**
   * handles https GET messages relating to the login system
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    String res;

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
    } catch (Exception e) {
      //return;
    }

    response.setContentType("text/html");

    PrintWriter out = response.getWriter();

    out.println(res);
  }
}