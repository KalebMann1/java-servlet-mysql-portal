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

public class RootUserServlet extends HttpServlet {

    private static final String PROJECT4_URL = "jdbc:mysql://localhost:3306/project4";
    private static final String DB_DRIVER    = "com.mysql.cj.jdbc.Driver";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC driver not found in RootUserServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sess = request.getSession(false);
        if (sess == null) {
            response.sendRedirect("authentication.html");
            return;
        }

        String dbUser = (String) sess.getAttribute("dbUser");
        String dbPassword = (String) sess.getAttribute("dbPassword");

        if (dbUser == null || dbPassword == null || !"root".equalsIgnoreCase(dbUser)) {
            // Only allow root to use this servlet
            response.sendRedirect("authentication.html");
            return;
        }

        String action = request.getParameter("action");
        String sqlCommand = request.getParameter("sqlCommand");
        if (sqlCommand == null) sqlCommand = "";
        sqlCommand = sqlCommand.trim();

        // Always send the current command back to the JSP
        request.setAttribute("sqlCommand", sqlCommand);

        if ("Clear Command".equals(action)) {
            request.setAttribute("sqlCommand", "");
            request.setAttribute("message", "Command cleared.");
            request.setAttribute("resultTable", null);
            request.getRequestDispatcher("rootHome.jsp").forward(request, response);
            return;
        }

        if ("Clear Results".equals(action)) {
            request.setAttribute("message", "Results cleared.");
            request.setAttribute("resultTable", null);
            request.getRequestDispatcher("rootHome.jsp").forward(request, response);
            return;
        }

        if (sqlCommand.isEmpty()) {
            request.setAttribute("message", "No SQL command entered.");
            request.setAttribute("resultTable", null);
            request.getRequestDispatcher("rootHome.jsp").forward(request, response);
            return;
        }

        // Execute the SQL against project4 DB
        try (Connection conn = DriverManager.getConnection(PROJECT4_URL, dbUser, dbPassword)) {

            // Decide if it's a query or an update command
            boolean isQuery = sqlCommand.toLowerCase().startsWith("select");

            if (isQuery) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sqlCommand)) {

                    StringBuilder tableHtml = new StringBuilder();
                    ResultSetMetaData meta = rs.getMetaData();
                    int colCount = meta.getColumnCount();

                    tableHtml.append("<table border='1' cellspacing='0' cellpadding='4'>");

                    // header row
                    tableHtml.append("<tr>");
                    for (int i = 1; i <= colCount; i++) {
                        tableHtml.append("<th>").append(meta.getColumnName(i)).append("</th>");
                    }
                    tableHtml.append("</tr>");

                    // data rows
                    while (rs.next()) {
                        tableHtml.append("<tr>");
                        for (int i = 1; i <= colCount; i++) {
                            String val = rs.getString(i);
                            if (val == null) val = "";
                            tableHtml.append("<td>").append(val).append("</td>");
                        }
                        tableHtml.append("</tr>");
                    }

                    tableHtml.append("</table>");

                    request.setAttribute("message", "Query executed successfully.");
                    request.setAttribute("resultTable", tableHtml.toString());
                }
            } else {
                try (Statement stmt = conn.createStatement()) {
                    int count = stmt.executeUpdate(sqlCommand);
                    String msg = "Command executed successfully. " + count + " row(s) affected.";
                    request.setAttribute("message", msg);
                    request.setAttribute("resultTable", null);
                }
            }

        } catch (SQLException e) {
            request.setAttribute("message", "Error executing SQL: " + e.getMessage());
            request.setAttribute("resultTable", null);
        }

        request.getRequestDispatcher("rootHome.jsp").forward(request, response);
    }
}
