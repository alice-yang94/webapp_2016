import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Calendar;

public class TestimonialServlet extends HttpServlet {

    private final String dbConnString = "jdbc:postgresql://db.doc.ic.ac.uk/g1427101_u";
    private final String dbUsername   = "g1427101_u";
    private final String dbPassword   = "ZfOfLyHLTA";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String res = "";

        try {
            Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);
            
            Statement statement = conn.createStatement();
            
            ResultSet rs = statement.executeQuery("SELECT * FROM submittedTestimonials ORDER BY dateSubmitted DESC");

            int i = 0;
            while (rs.next() && i < 3) {
                res += "<div class=\"col-md-4\"><div class=\"well well-sm\"><b>" + rs.getString("username") + ": - "
                    + rs.getString("dateSubmitted") + "</b><br />" + rs.getString("testimonial") + "</div></div>";
                i++;
            }

        } catch(Exception e) {
            for (int i = 0; i < 3; i++) {
                res += "<div class=\"col-md-4\"><div class=\"well well-sm\"><b>player: - 1/1/1970</b><br />texttexttexttexttexttexttexttext</div></div>";
            }
        }

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println(res);
        return;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String username = request.getParameter("username");
        String thoughts = request.getParameter("thoughts");

        try {
            Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);
            
            Statement statement = conn.createStatement();
            
            PreparedStatement prep = conn.prepareStatement("INSERT INTO submittedTestimonials VALUES ( DEFAULT, ?, ?, ? )");

            Calendar cal = Calendar.getInstance();
            Date date = new Date(cal.getTime().getTime());

            prep.setString(1, username);
            prep.setDate(2, date);
            prep.setString(3, thoughts);

            prep.executeUpdate();
        } catch(Exception e) {
            //
        }

        response.sendRedirect("main.html");
    }
}