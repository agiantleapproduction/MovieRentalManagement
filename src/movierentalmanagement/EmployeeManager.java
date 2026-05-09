package movierentalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeManager {

    // Add a new employee (Insert)
    public void addEmployee(String firstName, String lastName, String role) {
        String sql = "INSERT INTO employee (first_name, last_name, role) VALUES (?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, role);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Employee " + firstName + " " + lastName + " added.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    // Display all employees in table format
    public void displayAllEmployees() {
        String sql = "SELECT employee_id, first_name, last_name, role FROM employee ORDER BY last_name ASC";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n--- All Employees ---");
            System.out.printf("%-5s %-25s %-15s\n", "ID", "Name", "Role");
            System.out.println("--------------------------------------------");
            while (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                System.out.printf("%-5d %-25s %-15s\n",
                    rs.getInt("employee_id"),
                    fullName,
                    rs.getString("role"));
            }
            System.out.println("--------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error fetching employees: " + e.getMessage());
        }
    }

    // Display employee rental stats with GROUP BY
    public void displayEmployeeRentalStats() {
        String sql = "SELECT e.first_name, e.last_name, COUNT(r.rental_id) AS total_rentals " +
                     "FROM employee e " +
                     "LEFT JOIN rental r ON e.employee_id = r.employee_id " +
                     "GROUP BY e.employee_id, e.first_name, e.last_name " +
                     "ORDER BY total_rentals DESC";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n--- Employee Rental Statistics ---");
            System.out.printf("%-25s %-15s\n", "Name", "Total Rentals");
            System.out.println("-----------------------------------------------");
            while (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                int totalRentals = rs.getInt("total_rentals");
                System.out.printf("%-25s %-15d\n", fullName, totalRentals);
            }
            System.out.println("-----------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error fetching employee stats: " + e.getMessage());
        }
    }

} 