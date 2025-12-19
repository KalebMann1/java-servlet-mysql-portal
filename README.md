# Java Servlet + MySQL Web Application (CNT 4714 Project)

This project is a small multi-user web application built with **Java Servlets**
and **MySQL** for a Computer Networks / Enterprise Computing course. It
demonstrates a classic 3-tier architecture: HTML forms → Java servlet layer →
relational database.

The app enforces different permissions for **data-entry users** and
**accountant / admin users**, and it uses parameterized queries and stored
procedures for all database access.

---

## Tech Stack

- **Backend:** Java Servlets (Jakarta/Java EE style)
- **Web server:** Apache Tomcat
- **Database:** MySQL
- **Data access:**
  - `PreparedStatement` for data-entry operations
  - `CallableStatement` for accountant / reporting operations
- **Frontend:** HTML forms + basic JSP/servlet responses

---

## Features

- **Login & role selection**
  - Users log in with credentials mapped to specific roles.
  - Role determines which servlet/endpoints they may access.

- **Data-entry user functions**
  - Insert or update records in the database (e.g. new records, updates,
    or transactions) via HTML forms.
  - All inserts/updates use `PreparedStatement` to prevent SQL injection.
  - Input validation and clear error messages on invalid data.

- **Accountant / admin functions**
  - Trigger stored procedures via `CallableStatement` to generate summary
    information or reports (e.g. totals, balances, or flagged records).
  - Results displayed in a formatted HTML table.

- **Database script**
  - `sql/project4DBscript.sql` creates the schema and loads the initial data
    required by the application (tables, constraints, sample rows).

---

## Project Structure

```text
src/
  <package>/
    AuthenticationServlet.java
    DataEntryServlet.java
    AccountantServlet.java
    DBUtil.java               # helper for database connections

web/
  index.html                  # landing page / login
  dataentry.html              # form(s) for inserts/updates
  accountant.html             # reporting UI
  WEB-INF/
    web.xml                   # servlet mappings

sql/
  project4DBscript.sql        # schema + seed data
