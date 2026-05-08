/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package movierentalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieManager {

    // Add a new movie 
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

    // Remove a movie 
    public void removeMovie(int movieId) {
        String sql = "DELETE FROM movie WHERE movie_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Movie removed.");
            } else {
                System.out.println("Error: No movie found with ID " + movieId);
            }
        } catch (SQLException e) {
            System.out.println("Error removing movie: " + e.getMessage());
        }
    }

} 