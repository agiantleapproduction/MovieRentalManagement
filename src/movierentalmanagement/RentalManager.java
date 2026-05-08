package movierentalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RentalManager {

    // 1. Process a new rental (Insert)
    public void processRental(int customerId, int movieId, int employeeId) {
        String sql = "INSERT INTO rental (customer_id, movie_id, employee_id) VALUES (?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            pstmt.setInt(2, movieId);
            pstmt.setInt(3, employeeId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Rental processed.");
            }

        } catch (SQLException e) {
            System.out.println("Error processing rental: " + e.getMessage());
        }
    }

    // 2. Process a return (Update complex logic)
    public void processReturn(int customerId, int movieId) {
        // Updates the oldest active rental for this specific customer and movie
        String sql = "UPDATE rental SET return_date = CURRENT_DATE " +
                     "WHERE rental_id = (" +
                     "    SELECT rental_id FROM rental " +
                     "    WHERE customer_id = ? AND movie_id = ? AND return_date IS NULL " +
                     "    ORDER BY rent_date ASC LIMIT 1" +
                     ")";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
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

    // 3. Complex Query: Read from the View
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

        } catch (SQLException e) {
            System.out.println("Error fetching active rentals: " + e.getMessage());
        }
    }
}