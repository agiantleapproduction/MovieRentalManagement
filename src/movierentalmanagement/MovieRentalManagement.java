/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package movierentalmanagement;


import java.util.Scanner;
import java.sql.Connection;
/**
 *
 * @author georg
 */
public class MovieRentalManagement {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Scanner object to listen to user input
        Scanner scanner = new Scanner(System.in);
        
        // Grabs the database connection from DBManager 
        Connection connection = DBManager.getConnection();
        
        // If the connection fails, prints message and exit immediately instead of crashing
        if (connection == null) {
            System.out.println("Failed to connect to database. Exiting...");
            return;
        }
    }
    
}
