package movierentalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieManager {

    // Add a new movie (Insert)
    public void addMovie(String title, int releaseYear, double rentalRate, int categoryId) {
        String sql = "INSERT INTO movie (title, release_year, rental_rate, category_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, releaseYear);
            pstmt.setDouble(3, rentalRate);
            pstmt.setInt(4, categoryId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Movie \"" + title + "\" added.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding movie: " + e.getMessage());
        }
    }

    // Remove a movie (Delete)
    public void removeMovie(int movieId) {
        String sql = "DELETE FROM movie WHERE movie_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Movie removed.");
            }
        } catch (SQLException e) {
            System.out.println("Error removing movie: " + e.getMessage());
        }
    }

    // Display all movies in table format
    public void displayAllMovies() {
        String sql = "SELECT movie_id, title, release_year, rental_rate FROM movie ORDER BY movie_id ASC";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n--- All Movies ---");
            System.out.printf("%-5s %-30s %-10s %-10s\n", "ID", "Title", "Year", "Rate");
            System.out.println("------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-30s %-10d $%-10.2f\n",
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getInt("release_year"),
                    rs.getDouble("rental_rate"));
            }
            System.out.println("------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error fetching movies: " + e.getMessage());
        }
    }
    
    // Check if a movie exists by ID
    public boolean movieExists(int movieId) {
        String sql = "SELECT 1 FROM movie WHERE movie_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking movie: " + e.getMessage());
            return false;
        }
    }
}