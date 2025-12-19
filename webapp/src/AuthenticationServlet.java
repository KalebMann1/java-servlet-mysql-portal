/* Name: Kaleb Manning
Course: CNT 4714 – Fall 2025 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: December 1, 2025
*/
import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationServlet extends HttpServlet {

    // Used only to read credentialsDB
    private static final String DB_DRIVER   = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/credentialsDB";
    private static final String DB_USER     = "systemapp";
    private static final String DB_PASSWORD = "systemapp";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC driver not found", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            String sql = "SELECT * FROM usercredentials " +
                    "WHERE login_username=? AND login_password=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        // invalid credentials
                        response.sendRedirect("errorpage.html");
                        return;
                    }
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error in AuthenticationServlet", e);
        }

        // ✅ Valid credentials – store in session for later DB connections
        HttpSession session = request.getSession(true);
        session.setAttribute("dbUser", username);
        session.setAttribute("dbPassword", password);

        // Route to the correct home page based on username
        String u = username.toLowerCase();
        if ("root".equals(u)) {
            response.sendRedirect("rootHome.jsp");
        } else if ("client".equals(u)) {
            response.sendRedirect("clientHome.jsp");
        } else if ("dataentry".equals(u)) {
            response.sendRedirect("dataEntryHome.jsp");
        } else if ("theaccountant".equals(u)) {
            response.sendRedirect("accountantHome.jsp");
        } else {
            response.sendRedirect("errorpage.html");
        }
    }
}
