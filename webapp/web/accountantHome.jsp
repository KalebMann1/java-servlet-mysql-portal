<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Accountant User Console</title>
</head>
<body>
<h1>Welcome to the Fall 2025 Project 4 Enterprise System</h1>
<h2>Accountant User Application</h2>

<p>You are connected to the Project 4 Enterprise System database as an
   <b>accountant-level</b> user.</p>
<p>Click an operation below to run the corresponding query.</p>

<ul>
    <li><a href="AccountantUserServlet?operation=maxStatus">
        Get The Maximum Status Value Of All Suppliers
    </a></li>

    <li><a href="AccountantUserServlet?operation=sumWeight">
        Get The Total Weight Of All Parts
    </a></li>

    <li><a href="AccountantUserServlet?operation=countShipments">
        Get The Total Number Of Shipments
    </a></li>

    <li><a href="AccountantUserServlet?operation=maxWorkers">
        Get The Name And Number Of Workers Of The Job With The Most Workers
    </a></li>

    <li><a href="AccountantUserServlet?operation=listSupplierStatus">
        List The Name And Status Of Every Supplier
    </a></li>
</ul>

<p><a href="accountantHome.jsp">Clear Results</a></p>

<hr>
<h3>Execution Results:</h3>

<%
    String result = (String) request.getAttribute("result");
    if (result == null) result = "";
%>

<textarea rows="12" cols="90"><%= result %></textarea>

</body>
</html>
