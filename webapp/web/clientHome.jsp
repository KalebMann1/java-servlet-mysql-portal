<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Client User Console</title>
</head>
<body>
<h2>Client User Console</h2>

<p>Logged in as:
    <strong><%= (String) session.getAttribute("dbUser") %></strong>
</p>

<!-- Free-form SELECT area (still useful and matches spec) -->
<form method="post" action="ClientUserServlet">
    <p>Enter SQL <strong>SELECT</strong> command for project4 database:</p>
    <textarea name="sqlCommand" rows="6" cols="80"></textarea><br><br>

    <input type="submit" name="action" value="Execute Command">
    <input type="submit" name="action" value="Clear Command">
    <input type="submit" name="action" value="Clear Results">
</form>

<hr>

<!-- Convenience BUTTONS with predefined queries (your “option B”) -->
<h3>Quick Queries</h3>

<form method="post" action="ClientUserServlet" style="display:inline;">
    <input type="hidden" name="sqlCommand"
           value="SELECT * FROM suppliers;">
    <input type="submit" name="action" value="Show All Suppliers">
</form>

<form method="post" action="ClientUserServlet" style="display:inline; margin-left:10px;">
    <input type="hidden" name="sqlCommand"
           value="SELECT * FROM parts;">
    <input type="submit" name="action" value="Show All Parts">
</form>

<form method="post" action="ClientUserServlet" style="display:inline; margin-left:10px;">
    <input type="hidden" name="sqlCommand"
           value="SELECT * FROM jobs;">
    <input type="submit" name="action" value="Show All Jobs">
</form>

<form method="post" action="ClientUserServlet" style="display:inline; margin-left:10px;">
    <input type="hidden" name="sqlCommand"
           value="SELECT * FROM shipments;">
    <input type="submit" name="action" value="Show All Shipments">
</form>

<hr>

<h3>Results:</h3>
<pre>
<%= (request.getAttribute("results") == null)
        ? "" : (String) request.getAttribute("results") %>
</pre>

<p>Status:
    <strong><%= (request.getAttribute("message") == null)
            ? "" : (String) request.getAttribute("message") %></strong>
</p>

</body>
</html>
