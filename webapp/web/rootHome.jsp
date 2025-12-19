<%@ page import="javax.servlet.http.HttpSession" %>
<%
    HttpSession sess = request.getSession(false);
    String currentUser = (sess != null) ? (String) sess.getAttribute("dbUser") : null;
    if (currentUser == null) {
        // Not logged in – bounce back to login page
        response.sendRedirect("authentication.html");
        return;
    }

    String sqlCommand = (String) request.getAttribute("sqlCommand");
    if (sqlCommand == null) sqlCommand = "";

    String message    = (String) request.getAttribute("message");
    String resultTable = (String) request.getAttribute("resultTable");
%>
<html>
<head>
    <title>Root SQL Console</title>
</head>
<body>
<h2>Root User SQL Console</h2>
<p>Logged in as: <b><%= currentUser %></b></p>

<form method="post" action="RootUserServlet">
    <p><b>Enter SQL command for project4 database:</b></p>
    <textarea name="sqlCommand" rows="8" cols="100"><%= sqlCommand %></textarea>
    <br/><br/>
    <input type="submit" name="action" value="Execute Command"/>
    <input type="submit" name="action" value="Clear Command"/>
    <input type="submit" name="action" value="Clear Results"/>
</form>

<hr/>

<% if (message != null) { %>
    <p><b>Status:</b> <%= message %></p>
<% } %>

<% if (resultTable != null) { %>
    <h3>Results:</h3>
    <%= resultTable %>
<% } %>

</body>
</html>
