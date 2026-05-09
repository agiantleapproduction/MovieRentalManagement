package movierentalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RentalManager {

    // Process a new rental - finds customer_id by email then inserts into rental table
    public void processRental(String customerEmail, int movieId, int employeeId) {
        
        // First find the customer_id by their email
        String findCustomerSql = "SELECT customer_id FROM customer WHERE email = ?";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement findCustomer = conn.prepareStatement(findCustomerSql)) {
            
            findCustomer.setString(1, customerEmail);
            ResultSet rs = findCustomer.executeQuery();
            
            // If no customer found with that email, stop here
            if (!rs.next()) {
                System.out.println("Error: No customer found with that email.");
                return;
            }
            
            // Store the customer_id found by email
            int customerId = rs.getInt("customer_id");
            
            // Now insert into rental table using the three IDs
            String insertSql = "INSERT INTO rental (customer_id, movie_id, employee_id) VALUES (?, ?, ?)";
            try (PreparedStatement insertRental = conn.prepareStatement(insertSql)) {
                insertRental.setInt(1, customerId); // customer_id found from email lookup
                insertRental.setInt(2, movieId);
                insertRental.setInt(3, employeeId);
                int rowsAffected = insertRental.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Success: Rental processed.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error processing rental: " + e.getMessage());
        }
    }

    // Process a return - finds customer_id by email then updates rental record
    public void processReturn(String customerEmail, int movieId) {
        
        // First find the customer_id by their email
        String findCustomerSql = "SELECT customer_id FROM customer WHERE email = ?";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement findCustomer = conn.prepareStatement(findCustomerSql)) {
            
            findCustomer.setString(1, customerEmail);
            ResultSet rs = findCustomer.executeQuery();
            
            // If no customer found with that email, stop here
            if (!rs.next()) {
                System.out.println("Error: No customer found with that email.");
                return;
            }
            
            // Store the customer_id found by email
            int customerId = rs.getInt("customer_id");
            
            // Update the oldest active rental for this customer and movie
            String updateSql = "UPDATE rental SET return_date = CURRENT_DATE " +
                               "WHERE rental_id = (" +
                               "    SELECT rental_id FROM rental " +
                               "    WHERE customer_id = ? AND movie_id = ? AND return_date IS NULL " +
                               "    ORDER BY rent_date ASC LIMIT 1" +
                               ")";
            try (PreparedStatement updateRental = conn.prepareStatement(updateSql)) {
                updateRental.setInt(1, customerId);
                updateRental.setInt(2, movieId);
                int rowsAffected = updateRental.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Success: Movie returned.");
                } else {
                    System.out.println("Error: No active rental found for this customer and movie.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error processing return: " + e.getMessage());
        }
    }

    // Display all active rentals from view
    public void displayActiveRentals() {
        String sql = "SELECT * FROM vw_active_rentals";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n--- Currently Active Rentals ---");
            System.out.printf("%-20s %-25s %-15s %-15s\n", "Customer", "Movie Title", "Rent Date", "Processed By");
            System.out.println("--------------------------------------------------------------------------------");
            while (rs.next()) {
                String customerName = rs.getString("customer_name");
                String movieTitle = rs.getString("movie_title");
                java.sql.Date rentDate = rs.getDate("rent_date");
                String processedBy = rs.getString("processed_by");
                System.out.printf("%-20s %-25s %-15s %-15s\n", customerName, movieTitle, rentDate, processedBy);
            }
            System.out.println("--------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error fetching active rentals: " + e.getMessage());
        }
    }

} 