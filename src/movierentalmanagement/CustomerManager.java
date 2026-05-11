package movierentalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerManager {

    // Add a new customer (Insert)
    public void addCustomer(String firstName, String lastName, String email) {
        String sql = "INSERT INTO customer (first_name, last_name, email) VALUES (?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Customer " + firstName + " " + lastName + " added.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    // List all customers in table format
    public void displayAllCustomers() {
        String sql = "SELECT first_name, last_name, email FROM customer ORDER BY last_name ASC";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n--- All Customers ---");
            System.out.printf("%-30s %-30s\n", "Name", "Email");
            System.out.println("------------------------------------------------------------");
            while (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                String email = rs.getString("email");
                System.out.printf("%-30s %-30s\n", fullName, email);
            }
            System.out.println("------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error fetching customers: " + e.getMessage());
        }
    }

    // Update customer email by matching current email
    public void updateCustomerEmail(String currentEmail, String newEmail) {
        String sql = "UPDATE customer SET email = ? WHERE email = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setString(2, currentEmail);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Email updated.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating email: " + e.getMessage());
        }
    }

    // Delete a customer by email
    public void deleteCustomer(String email) {
        String sql = "DELETE FROM customer WHERE email = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Customer deleted.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }
    
    // Check if a customer exists by email
    public boolean customerExists(String email) {
        String sql = "SELECT 1 FROM customer WHERE email = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking customer: " + e.getMessage());
            return false;
        }
    }
}