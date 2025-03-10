import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://localhost:3306/StudentPortal";
    private static final String USER = "root";  
    private static final String PASSWORD = "";  

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String studentName = request.getParameter("student_name");
        String rollNumber = request.getParameter("roll_number");
        String attendanceDate = request.getParameter("attendance_date");
        String status = request.getParameter("status");

        try {
           
            Class.forName("com.mysql.cj.jdbc.Driver");

          
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO attendance (student_name, roll_number, attendance_date, status) VALUES (?, ?, ?, ?)"
            );
            pstmt.setString(1, studentName);
            pstmt.setString(2, rollNumber);
            pstmt.setString(3, attendanceDate);
            pstmt.setString(4, status);
            
            int rowsInserted = pstmt.executeUpdate();
            conn.close();

            if (rowsInserted > 0) {
                out.println("<h2>Attendance recorded successfully!</h2>");
                out.println("<a href='attendance.jsp'>Go Back</a>");
            } else {
                out.println("<h2>Error in recording attendance.</h2>");
            }
        } catch (Exception e) {
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
            e.printStackTrace();
        }
    }
}
