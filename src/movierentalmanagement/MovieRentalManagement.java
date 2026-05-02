/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package movierentalmanagement;


import java.util.Scanner;
import java.sql.Connection;
import java.util.InputMismatchException;
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
        
        // controls the main menu loop; when false program exits
        boolean running = true;
        
        // Keep showing the main menu until user exits
        while (running) {
            
            // Print main menu options
            System.out.println("\n== Movie Rental System ===");
            System.out.println("1. Customer Management");
            System.out.println("2. Movie Inventory");
            System.out.println("3. Rental & Return");
            System.out.println("4. Reports");
            System.out.println("5. Exit");
            System.out.println("Enter choice: ");
            
            // Read user input, catch non-number input
            int choice = 0;
            try {
                choice = scanner.nextInt(); // reads the number the user typed
                scanner.nextLine(); // clears the leftover newline from the buffer
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number."); // If user types word
                scanner.nextLine(); // clears the bad input so the loop doesn't freeze
                continue; // jump back to top of while loop
                
            }
            
            // Route to the correct sub-menu based on user choice
            switch (choice) {
                case 1 -> customerMenu(scanner, connection); // customer sub-menu
                case 2 -> movieMenu(scanner, connection); // movie sub-menu
                case 3 -> rentalMenu(scanner, connection); // rental sub-menu
                case 4 -> reportsMenu(scanner, connection); // reports sub-menu
                case 5 -> {
                    System.out.println("Goodbye!"); // exit message
                    running = false; // stops while loop
                }
                default -> System.out.println("Invalid choice. Try again."); // any choice outside 1-5
            }
        }
        
        scanner.close();
    }
    
}
