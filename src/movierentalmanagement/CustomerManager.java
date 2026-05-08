package movierentalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerManager {

    // Add a new customer 
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

    // Update a customer's email 
    public void updateCustomerEmail(int customerId, String newEmail) {
        String sql = "UPDATE customer SET email = ? WHERE customer_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, customerId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Email updated.");
            } else {
                System.out.println("Error: No customer found with ID " + customerId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating email: " + e.getMessage());
        }
    }

    // Delete a customer 
    public void deleteCustomer(int customerId) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Customer deleted.");
            } else {
                System.out.println("Error: No customer found with ID " + customerId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }

} 