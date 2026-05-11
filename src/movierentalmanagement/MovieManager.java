package movierentalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieManager {
    
    // Add a new category (Insert)
    public void addCategory(String name) {
        String sql = "INSERT INTO category (name) VALUES (?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Category \"" + name + "\" added.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    // Update an existing category
    public void updateCategory(int categoryId, String newName) {
        String sql = "UPDATE category SET name = ? WHERE category_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, categoryId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Success: Category updated.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating category: " + e.getMessage());
        }
    }

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

    // Display all movies in table format (Joined with category for meaningful info)
    public void displayAllMovies() {
        String sql = "SELECT m.movie_id, m.title, m.release_year, m.rental_rate, c.name AS category_name " +
                     "FROM movie m JOIN category c ON m.category_id = c.category_id ORDER BY m.movie_id ASC";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n--- All Movies ---");
            System.out.printf("%-5s %-30s %-10s %-10s %-15s\n", "ID", "Title", "Year", "Rate", "Category");
            System.out.println("--------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-30s %-10d $%-9.2f %-15s\n",
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getInt("release_year"),
                    rs.getDouble("rental_rate"),
                    rs.getString("category_name"));
            }
            System.out.println("--------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error fetching movies: " + e.getMessage());
        }
    }
    
    // Display all available categories
    public void displayAllCategories() {
        String sql = "SELECT category_id, name FROM category ORDER BY category_id ASC";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n--- Categories ---");
            System.out.printf("%-5s %-20s\n", "ID", "Category Name");
            System.out.println("-------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s\n", rs.getInt("category_id"), rs.getString("name"));
            }
            System.out.println("-------------------------");
        } catch (SQLException e) {
            System.out.println("Error fetching categories: " + e.getMessage());
        }
    }
    
    // Check if a category exists by ID
    public boolean categoryExists(int categoryId) {
        String sql = "SELECT 1 FROM category WHERE category_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking category: " + e.getMessage());
            return false;
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