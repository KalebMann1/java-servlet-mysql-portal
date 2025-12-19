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

public class DataEntryServlet extends HttpServlet {

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

        String op = request.getParameter("operation");
        String message = "";

        // "Clear Data and Results" just wipes the message.
        if ("Clear Data and Results".equals(op)) {
            message = "";
        } else {
            try (Connection conn = DriverManager.getConnection(DB_URL, dbUser, dbPassword)) {

                if ("Enter Supplier Record".equals(op)) {
                    message = insertSupplier(request, conn);
                } else if ("Enter Part Record".equals(op)) {
                    message = insertPart(request, conn);
                } else if ("Enter Job Record".equals(op)) {
                    message = insertJob(request, conn);
                } else if ("Enter Shipment Record".equals(op)) {
                    message = insertShipment(request, conn);
                } else {
                    message = "Unknown operation.";
                }

            } catch (SQLException e) {
                message = "Database error: " + e.getMessage();
            }
        }

        request.setAttribute("message", message);
        RequestDispatcher rd =
                request.getRequestDispatcher("dataEntryHome.jsp");
        rd.forward(request, response);
    }

    // ===== Helper methods for each table insert =====

    private String insertSupplier(HttpServletRequest request, Connection conn)
            throws SQLException {

        String snum = request.getParameter("snum");
        String sname = request.getParameter("sname");
        String statusStr = request.getParameter("status");
        String city = request.getParameter("scity");

        int status = Integer.parseInt(statusStr.trim());

        String sql = "INSERT INTO suppliers (snum, sname, status, city) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, snum);
            ps.setString(2, sname);
            ps.setInt(3, status);
            ps.setString(4, city);
            ps.executeUpdate();
        }

        return "New suppliers record: (" + snum + ", " + sname + ", " +
                status + ", " + city + ") - successfully entered into database.";
    }

    private String insertPart(HttpServletRequest request, Connection conn)
            throws SQLException {

        String pnum = request.getParameter("pnum");
        String pname = request.getParameter("pname");
        String color = request.getParameter("color");
        String weightStr = request.getParameter("weight");
        String city = request.getParameter("pcity");

        int weight = Integer.parseInt(weightStr.trim());

        String sql = "INSERT INTO parts (pnum, pname, color, weight, city) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pnum);
            ps.setString(2, pname);
            ps.setString(3, color);
            ps.setInt(4, weight);
            ps.setString(5, city);
            ps.executeUpdate();
        }

        return "New parts record: (" + pnum + ", " + pname + ", " + color +
                ", " + weight + ", " + city + ") - successfully entered into database.";
    }

    private String insertJob(HttpServletRequest request, Connection conn)
            throws SQLException {

        String jnum = request.getParameter("jnum");
        String jname = request.getParameter("jname");
        String numworkersStr = request.getParameter("numworkers");
        String city = request.getParameter("jcity");

        int numworkers = Integer.parseInt(numworkersStr.trim());

        String sql = "INSERT INTO jobs (jnum, jname, numworkers, city) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jnum);
            ps.setString(2, jname);
            ps.setInt(3, numworkers);
            ps.setString(4, city);
            ps.executeUpdate();
        }

        return "New jobs record: (" + jnum + ", " + jname + ", " +
                numworkers + ", " + city + ") - successfully entered into database.";
    }

    private String insertShipment(HttpServletRequest request, Connection conn)
            throws SQLException {

        String snum = request.getParameter("ssnum");
        String pnum = request.getParameter("spnum");
        String jnum = request.getParameter("sjnum");
        String qtyStr = request.getParameter("quantity");

        int qty = Integer.parseInt(qtyStr.trim());

        String sql = "INSERT INTO shipments (snum, pnum, jnum, quantity) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, snum);
            ps.setString(2, pnum);
            ps.setString(3, jnum);
            ps.setInt(4, qty);
            ps.executeUpdate();
        }

        return "New shipments record: (" + snum + ", " + pnum + ", " +
                jnum + ", " + qty + ") - successfully entered into database.";
    }
}
