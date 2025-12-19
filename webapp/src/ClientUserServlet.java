/* Name: Kaleb Manning
Course: CNT 4714 – Fall 2025 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: December 1, 2025
*/
import java.io.IOException;
import java.sql.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ClientUserServlet extends HttpServlet {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/project4";

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null ||
                session.getAttribute("dbUser") == null ||
                session.getAttribute("dbPassword") == null) {

            response.sendRedirect("authentication.html");
            return;
        }

        String dbUser = (String) session.getAttribute("dbUser");
        String dbPassword = (String) session.getAttribute("dbPassword");

        String action = request.getParameter("action");
        String sql = request.getParameter("sqlCommand");

        String message = "";
        String results = "";

        if ("Clear Command".equals(action)) {
            sql = "";
        } else if ("Clear Results".equals(action)) {
            results = "";
        } else if ("Execute Command".equals(action) ||
                (action != null && action.startsWith("Show"))) {

            if (sql == null || sql.trim().isEmpty()) {
                message = "No SQL command entered.";
            } else {
                sql = sql.trim();

                // Optional: remind client to use SELECT
                if (!sql.toLowerCase().startsWith("select")) {
                    message = "Client user should only issue SELECT statements.";
                }

                try (Connection conn = DriverManager.getConnection(DB_URL, dbUser, dbPassword);
                     Statement stmt = conn.createStatement()) {

                    boolean hasResultSet = stmt.execute(sql);

                    if (hasResultSet) {
                        ResultSet rs = stmt.getResultSet();
                        results = formatResultSet(rs);
                        message = "Query executed successfully.";
                    } else {
                        int count = stmt.getUpdateCount();
                        message = "Command executed. Update count = " + count;
                    }

                } catch (SQLException e) {
                    message = "Error executing SQL: " + e.getMessage();
                }
            }
        }

        request.setAttribute("results", results);
        request.setAttribute("message", message);

        RequestDispatcher rd =
                request.getRequestDispatcher("clientHome.jsp");
        rd.forward(request, response);
    }

    private String formatResultSet(ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder();
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();

        // header
        for (int i = 1; i <= cols; i++) {
            sb.append(meta.getColumnName(i)).append("\t");
        }
        sb.append("\n");

        // rows
        while (rs.next()) {
            for (int i = 1; i <= cols; i++) {
                sb.append(rs.getString(i)).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
