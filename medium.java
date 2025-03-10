//Java servlet code

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database Credentials
    private static final String URL = "jdbc:mysql://localhost:3306/EmployeeDB";
    private static final String USER = "root";  // Change as per your MySQL config
    private static final String PASSWORD = "";  // Change as per your MySQL config

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String empId = request.getParameter("empId");

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish Database Connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            if (empId != null && !empId.isEmpty()) {
                // Search Employee by ID
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM employees WHERE id = ?");
                pstmt.setInt(1, Integer.parseInt(empId));
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    out.println("<h2>Employee Details</h2>");
                    out.println("<p>ID: " + rs.getInt("id") + "</p>");
                    out.println("<p>Name: " + rs.getString("name") + "</p>");
                    out.println("<p>Department: " + rs.getString("department") + "</p>");
                    out.println("<p>Salary: $" + rs.getDouble("salary") + "</p>");
                } else {
                    out.println("<h3>No Employee found with ID " + empId + "</h3>");
                }
            } else {
                // Display All Employees
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM employees");
                ResultSet rs = pstmt.executeQuery();
                
                out.println("<h2>Employee List</h2>");
                out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Department</th><th>Salary</th></tr>");
                
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("id") + "</td>");
                    out.println("<td>" + rs.getString("name") + "</td>");
                    out.println("<td>" + rs.getString("department") + "</td>");
                    out.println("<td>$" + rs.getDouble("salary") + "</td></tr>");
                }
                
                out.println("</table>");
            }

            conn.close();
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
}
