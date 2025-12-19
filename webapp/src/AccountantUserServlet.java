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
import java.sql.CallableStatement;


public class AccountantUserServlet extends HttpServlet {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/project4";

    @Override
    protected void doGet(HttpServletRequest request,
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

        String op = request.getParameter("operation");
        String result;

        if (op == null) {
            result = "No operation selected.";
        } else {
            try (Connection conn = DriverManager.getConnection(DB_URL, dbUser, dbPassword)) {

                switch (op) {
                    case "maxStatus":
                        result = singleValueQuery(
                                conn,
                                "SELECT MAX(status) FROM suppliers",
                                "Maximum supplier status"
                        );
                        break;

                    case "sumWeight":
                        result = singleValueQuery(
                                conn,
                                "SELECT SUM(weight) FROM parts",
                                "Total weight of all parts"
                        );
                        break;

                    case "countShipments":
                        result = singleValueQuery(
                                conn,
                                "SELECT COUNT(*) FROM shipments",
                                "Total number of shipments"
                        );
                        break;

                    case "maxWorkers":
                        result = tableQuery(
                                conn,
                                "SELECT jname, numworkers " +
                                        "FROM jobs " +
                                        "WHERE numworkers = (SELECT MAX(numworkers) FROM jobs)"
                        );
                        break;

                    case "listSupplierStatus":
                        result = tableQuery(
                                conn,
                                "SELECT sname, status FROM suppliers"
                        );
                        break;

                    default:
                        result = "Unknown operation: " + op;
                }

            } catch (SQLException e) {
                result = "Database error: " + e.getMessage();
            }

        }

        request.setAttribute("result", result);
        RequestDispatcher rd =
                request.getRequestDispatcher("accountantHome.jsp");
        rd.forward(request, response);
    }

    // Helper: for queries that return a single numeric value
    // Helper: for queries that return a single numeric value
    private String singleValueQuery(Connection conn, String sql, String label)
            throws SQLException {

        try (CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            if (rs.next()) {
                return label + ": " + rs.getString(1);
            } else {
                return label + ": (no rows returned)";
            }
        }
    }

    // Helper: for table-style results
    private String tableQuery(Connection conn, String sql)
            throws SQLException {

        StringBuilder sb = new StringBuilder();

        try (CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();

            // header row
            for (int i = 1; i <= cols; i++) {
                sb.append(meta.getColumnName(i)).append("\t");
            }
            sb.append("\n");

            // data rows
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    sb.append(rs.getString(i)).append("\t");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

}
