package movierentalmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    static String jdbcURL = "jdbc:postgresql://localhost:5432/movie_rental";
    static String username = "postgres";
    static String password = "password";

    private static Connection connection;

    public DBManager() throws ClassNotFoundException {
    }

    public static Connection getConnection() {
        if (connection == null) {
            setConnection();
        }
        return connection;
    }

    private static void setConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Database connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot load the PostgreSQL driver.");
        } catch (SQLException e) {
            System.out.println("Got a SQL exception.");
            e.printStackTrace();
        }
    }
}
