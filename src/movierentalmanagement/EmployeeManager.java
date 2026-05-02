package movierentalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeManager {

    // 1. Write Operation: Insert a new employee using Parameterized SQL
    public void addEmployee(String firstName, String lastName, String role) {
        String sql = "INSERT INTO employee (first_name, last_name, role) VALUES (?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // The ? parameters prevent SQL injection attacks
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

    // 2. Read Operation with Aggregation: Show total rentals processed by each employee
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
            System.out.printf("%-15s %-15s %-15s\n", "First Name", "Last Name", "Total Rentals");
            System.out.println("-----------------------------------------------");

            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int totalRentals = rs.getInt("total_rentals");

                System.out.printf("%-15s %-15s %-15d\n", firstName, lastName, totalRentals);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching employee stats: " + e.getMessage());
        }
    }
}