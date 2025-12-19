<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Data Entry Application</title>
</head>
<body>
<h1>Welcome to the Fall 2025 Project 4 Enterprise System</h1>
<h2>Data Entry Application</h2>

<p>You are connected to the Project 4 Enterprise System database as a
   <strong>data-entry-level</strong> user.</p>
<p>Enter the data values in a form below to add a new record to the corresponding
   database table.</p>

<hr>

<!-- =================== SUPPLIERS RECORD INSERT =================== -->
<h3>Suppliers Record Insert</h3>

<form method="post" action="DataEntryServlet">
    <table border="1" cellpadding="4" cellspacing="0">
        <tr>
            <th>snum</th>
            <th>sname</th>
            <th>status</th>
            <th>city</th>
        </tr>
        <tr>
            <td><input type="text" name="snum"></td>
            <td><input type="text" name="sname"></td>
            <td><input type="text" name="status"></td>
            <td><input type="text" name="scity"></td>
        </tr>
    </table>
    <br>
    <input type="submit" name="operation" value="Enter Supplier Record">
    <input type="submit" name="operation" value="Clear Data and Results">
</form>

<hr>

<!-- =================== PARTS RECORD INSERT =================== -->
<h3>Parts Record Insert</h3>

<form method="post" action="DataEntryServlet">
    <table border="1" cellpadding="4" cellspacing="0">
        <tr>
            <th>pnum</th>
            <th>pname</th>
            <th>color</th>
            <th>weight</th>
            <th>city</th>
        </tr>
        <tr>
            <td><input type="text" name="pnum"></td>
            <td><input type="text" name="pname"></td>
            <td><input type="text" name="color"></td>
            <td><input type="text" name="weight"></td>
            <td><input type="text" name="pcity"></td>
        </tr>
    </table>
    <br>
    <input type="submit" name="operation" value="Enter Part Record">
    <input type="submit" name="operation" value="Clear Data and Results">
</form>

<hr>

<!-- =================== JOBS RECORD INSERT =================== -->
<h3>Jobs Record Insert</h3>

<form method="post" action="DataEntryServlet">
    <table border="1" cellpadding="4" cellspacing="0">
        <tr>
            <th>jnum</th>
            <th>jname</th>
            <th>numworkers</th>
            <th>city</th>
        </tr>
        <tr>
            <td><input type="text" name="jnum"></td>
            <td><input type="text" name="jname"></td>
            <td><input type="text" name="numworkers"></td>
            <td><input type="text" name="jcity"></td>
        </tr>
    </table>
    <br>
    <input type="submit" name="operation" value="Enter Job Record">
    <input type="submit" name="operation" value="Clear Data and Results">
</form>

<hr>

<!-- =================== SHIPMENTS RECORD INSERT =================== -->
<h3>Shipments Record Insert</h3>

<form method="post" action="DataEntryServlet">
    <table border="1" cellpadding="4" cellspacing="0">
        <tr>
            <th>snum</th>
            <th>pnum</th>
            <th>jnum</th>
            <th>quantity</th>
        </tr>
        <tr>
            <td><input type="text" name="ssnum"></td>
            <td><input type="text" name="spnum"></td>
            <td><input type="text" name="sjnum"></td>
            <td><input type="text" name="quantity"></td>
        </tr>
    </table>
    <br>
    <input type="submit" name="operation" value="Enter Shipment Record">
    <input type="submit" name="operation" value="Clear Data and Results">
</form>

<hr>

<h3>Execution Results:</h3>
<p>
    <strong><%= (request.getAttribute("message") == null)
            ? "" : (String) request.getAttribute("message") %></strong>
</p>

</body>
</html>
