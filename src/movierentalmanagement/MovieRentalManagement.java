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
 * @author georg & Edow
 */
public class MovieRentalManagement {

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
            System.out.print("Enter choice: ");

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
                case 2 -> movieMenu(scanner, connection);    // movie sub-menu
                case 3 -> rentalMenu(scanner, connection);   // rental sub-menu
                case 4 -> reportsMenu(scanner, connection);  // reports sub-menu
                case 5 -> {
                    System.out.println("Goodbye!"); // exit message
                    running = false; // stops while loop
                }
                default -> System.out.println("Invalid choice. Try again."); // any choice outside 1-5
            }
        }

        scanner.close(); 

    } 


    // Customer management submenu
    private static void customerMenu(Scanner scanner, Connection connection) {
        boolean running = true;
        while (running) {
            System.out.println("\n-- Customer Management --");
            System.out.println("1. Add Customer");
            System.out.println("2. Update Customer Email");
            System.out.println("3. Delete Customer");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice = 0;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> addCustomer(scanner, connection);
                case 2 -> updateCustomerEmail(scanner, connection);
                case 3 -> deleteCustomer(scanner, connection);
                case 4 -> running = false; // back to main menu
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    } 


    // Movie inventory submenu
    private static void movieMenu(Scanner scanner, Connection connection) {
        boolean running = true;
        while (running) {
            System.out.println("\n-- Movie Inventory --");
            System.out.println("1. Add Movie");
            System.out.println("2. Remove Movie");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            int choice = 0;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> addMovie(scanner, connection);
                case 2 -> removeMovie(scanner, connection);
                case 3 -> running = false; // back to main menu
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    } 


    // Rental & Return submenu
    private static void rentalMenu(Scanner scanner, Connection connection) {
        RentalManager rentalManager = new RentalManager();
        boolean running = true;
        while (running) {
            System.out.println("\n-- Rental & Return --");
            System.out.println("1. Rent a Movie");
            System.out.println("2. Return a Movie");
            System.out.println("3. View Active Rentals");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice = 0;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Customer ID: ");
                    int customerId = scanner.nextInt();
                    System.out.print("Movie ID: ");
                    int movieId = scanner.nextInt();
                    System.out.print("Employee ID: ");
                    int employeeId = scanner.nextInt();
                    scanner.nextLine();
                    rentalManager.processRental(customerId, movieId, employeeId);
                }
                case 2 -> {
                    System.out.print("Customer ID: ");
                    int customerId = scanner.nextInt();
                    System.out.print("Movie ID: ");
                    int movieId = scanner.nextInt();
                    scanner.nextLine();
                    rentalManager.processReturn(customerId, movieId);
                }
                case 3 -> rentalManager.displayActiveRentals();
                case 4 -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    } 


    // Reports submenu
    private static void reportsMenu(Scanner scanner, Connection connection) {
        EmployeeManager employeeManager = new EmployeeManager();
        boolean running = true;
        while (running) {
            System.out.println("\n-- Reports --");
            System.out.println("1. Employee Rental Stats");
            System.out.println("2. Add Employee");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            int choice = 0;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> employeeManager.displayEmployeeRentalStats();
                case 2 -> {
                    System.out.print("First name: ");
                    String fname = scanner.nextLine();
                    System.out.print("Last name: ");
                    String lname = scanner.nextLine();
                    System.out.print("Role: ");
                    String role = scanner.nextLine();
                    employeeManager.addEmployee(fname, lname, role);
                }
                case 3 -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    } 


    // Customer actions
    private static void addCustomer(Scanner scanner, Connection connection) {
        CustomerManager customerManager = new CustomerManager();
        System.out.print("First name: ");
        String fname = scanner.nextLine();
        System.out.print("Last name: ");
        String lname = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        customerManager.addCustomer(fname, lname, email);
    }

    private static void updateCustomerEmail(Scanner scanner, Connection connection) {
        CustomerManager customerManager = new CustomerManager();
        System.out.print("Customer ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("New email: ");
        String email = scanner.nextLine();
        customerManager.updateCustomerEmail(id, email);
    }

    private static void deleteCustomer(Scanner scanner, Connection connection) {
        CustomerManager customerManager = new CustomerManager();
        System.out.print("Customer ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        customerManager.deleteCustomer(id);
    }


    // Movie actions
    private static void addMovie(Scanner scanner, Connection connection) {
        MovieManager movieManager = new MovieManager();
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Release year: ");
        int year = scanner.nextInt();
        System.out.print("Rental rate: ");
        double rate = scanner.nextDouble();
        System.out.print("Category ID: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();
        movieManager.addMovie(title, year, rate, categoryId);
    }

    private static void removeMovie(Scanner scanner, Connection connection) {
        MovieManager movieManager = new MovieManager();
        System.out.print("Movie ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        movieManager.removeMovie(id);
    }

} 