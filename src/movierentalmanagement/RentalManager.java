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
            
            if (rs.next()) {
                int customerId = rs.getInt("customer_id");
                
                // Now insert into rental table using the three IDs
                String insertSql = "INSERT INTO rental (customer_id, movie_id, employee_id) VALUES (?, ?, ?)";
                try (PreparedStatement insertRental = conn.prepareStatement(insertSql)) {
                    insertRental.setInt(1, customerId);
                    insertRental.setInt(2, movieId);
                    insertRental.setInt(3, employeeId);
                    int rowsAffected = insertRental.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Success: Rental processed.");
                    }
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
            
            if (rs.next()) {
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
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error processing return: " + e.getMessage());
        }
    }

    // Display all active rentals from view
    // Display all active rentals from view
    public void displayActiveRentals() {
        // Sorts by rent_date ASC to show oldest rentals first
        String sql = "SELECT * FROM vw_active_rentals ORDER BY rent_date ASC";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n--- Currently Active Rentals ---");
            System.out.printf("%-20s %-25s %-25s %-15s %-15s\n", "Customer", "Email", "Movie Title", "Rent Date", "Processed By");
            System.out.println("-----------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                String customerName = rs.getString("customer_name");
                String customerEmail = rs.getString("customer_email");
                String movieTitle = rs.getString("movie_title");
                java.sql.Date rentDate = rs.getDate("rent_date");
                String processedBy = rs.getString("processed_by");
                System.out.printf("%-20s %-25s %-25s %-15s %-15s\n", customerName, customerEmail, movieTitle, rentDate, processedBy);
            }
            System.out.println("-----------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error fetching active rentals: " + e.getMessage());
        }
    }
    
    // Display unreturned movies for a specific customer
    public boolean displayCustomerActiveRentals(String customerEmail) {
        String sql = "SELECT m.movie_id, m.title, r.rent_date " +
                     "FROM rental r " +
                     "JOIN movie m ON r.movie_id = m.movie_id " +
                     "JOIN customer c ON r.customer_id = c.customer_id " +
                     "WHERE c.email = ? AND r.return_date IS NULL " +
                     "ORDER BY r.rent_date ASC";
        
        boolean hasRentals = false;
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerEmail);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\n--- Customer's Active Rentals ---");
            while (rs.next()) {
                if (!hasRentals) {
                    System.out.printf("%-10s %-30s %-15s\n", "Movie ID", "Title", "Rent Date");
                    System.out.println("-------------------------------------------------------");
                    hasRentals = true;
                }
                System.out.printf("%-10d %-30s %-15s\n", 
                    rs.getInt("movie_id"), 
                    rs.getString("title"), 
                    rs.getDate("rent_date"));
            }
            
            if (!hasRentals) {
                System.out.println("No active rentals found for this email.");
            } else {
                System.out.println("-------------------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching customer rentals: " + e.getMessage());
        }
        return hasRentals;
    }
}