import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class ScoresServlet extends HttpServlet {

    private final String dbConnString = "jdbc:postgresql://db.doc.ic.ac.uk/g1427101_u";
    private final String dbUsername = "g1427101_u";
    private final String dbPassword = "ZfOfLyHLTA";

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String res = "";

        String username = request.getParameter("username");
        int level = Integer.valueOf(request.getParameter("level"));
        int jumps = Integer.valueOf(request.getParameter("jumps"));

        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                //
            }

            Connection conn = DriverManager.getConnection(dbConnString,
                    dbUsername, dbPassword);

            Statement statement = conn.createStatement();

            statement.executeUpdate("DELETE FROM currentGame WHERE username = '" + username + "'");

            statement.executeUpdate("INSERT INTO currentGame VALUES ( '" + username + "', " + level + ", " + jumps + ")");

            res = "should have stored";

            if ("complete".equals(request.getParameter("action"))) {
                Date date = new Date(Long.valueOf(request.getParameter("date")));
                Float score = Float.valueOf(request.getParameter("score"));

                PreparedStatement prep = conn.prepareStatement("INSERT INTO completedGames VALUES ( DEFAULT, ?, ?, ? )");

                prep.setString(1, username);
                prep.setDate(2, date);
                prep.setFloat(3, score);

                prep.executeUpdate();

                res = "stored completed";
            }

            conn.close();
        } catch (Exception e) {
            res = "connection failed";
            e.printStackTrace();
        }


        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println(res);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String action = request.getParameter("action");
        String username = request.getParameter("username");

        String res = "0";

        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                //
            }

            Connection conn = DriverManager.getConnection(dbConnString,
                    dbUsername, dbPassword);

            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery("SELECT level, jumps FROM currentGame WHERE username='" +
                    username + "'");

            if ("jumps".equals(action)) {
                while (rs.next()) {
                    res = String.valueOf(rs.getInt("jumps"));
                }
            } else {
                while (rs.next()) {
                    res = "1";
                    res = String.valueOf(rs.getInt("level"));
                }
            }

            conn.close();
        } catch (Exception e) {
            //
        }

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println(res);
    }
}
