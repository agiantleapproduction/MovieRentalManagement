package movierentalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RentalManager {

    // Process a new rental using customer email
    public void processRental(String customerEmail, int movieId, int employeeId) {
        String sql = "INSERT INTO rental (customer_id, movie_id, employee_id) " +
                     "VALUES ((SELECT customer_id FROM customer WHERE email = ?), ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerEmail);
            pstmt.setInt(2, movieId);
            pstmt.setInt(3, employeeId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Rental processed.");
            } else {
                System.out.println("Error: No customer found with that email.");
            }
        } catch (SQLException e) {
            System.out.println("Error processing rental: " + e.getMessage());
        }
    }

    // Process a return using customer email
    public void processReturn(String customerEmail, int movieId) {
        String sql = "UPDATE rental SET return_date = CURRENT_DATE " +
                     "WHERE rental_id = (" +
                     "    SELECT rental_id FROM rental " +
                     "    WHERE customer_id = (SELECT customer_id FROM customer WHERE email = ?) " +
                     "    AND movie_id = ? AND return_date IS NULL " +
                     "    ORDER BY rent_date ASC LIMIT 1" +
                     ")";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerEmail);
            pstmt.setInt(2, movieId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Movie returned.");
            } else {
                System.out.println("Error: No active rental found for this customer and movie.");
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