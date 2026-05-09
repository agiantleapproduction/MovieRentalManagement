package movierentalmanagement;

import java.util.Scanner;
import java.sql.Connection;

/**
 *
 * @author georg & Edow
 */
public class MovieRentalManagement {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Connection connection = DBManager.getConnection();

        if (connection == null) {
            System.out.println("Failed to connect to database. Exiting...");
            return;
        }

        boolean running = true;
        while (running) {

            System.out.println("\n== Movie Rental System ===");
            System.out.println("1. Customer Management");
            System.out.println("2. Movie Inventory");
            System.out.println("3. Rental & Return");
            System.out.println("4. Reports");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> customerMenu(scanner, connection);
                case 2 -> movieMenu(scanner, connection);
                case 3 -> rentalMenu(scanner, connection);
                case 4 -> reportsMenu(scanner, connection);
                case 5 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    } 

    // Helper method to check for cancel command
    private static boolean isCancel(String input) {
        return input.trim().isEmpty();
    }

    // Customer management sub-menu
    private static void customerMenu(Scanner scanner, Connection connection) {
        boolean running = true;
        while (running) {
            System.out.println("\n-- Customer Management --");
            System.out.println("1. List All Customers");
            System.out.println("2. Add Customer");
            System.out.println("3. Update Customer Email");
            System.out.println("4. Delete Customer");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> new CustomerManager().displayAllCustomers();
                case 2 -> addCustomer(scanner, connection);
                case 3 -> updateCustomerEmail(scanner, connection);
                case 4 -> deleteCustomer(scanner, connection);
                case 5 -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    } 

    // Movie inventory sub-menu
    private static void movieMenu(Scanner scanner, Connection connection) {
        boolean running = true;
        while (running) {
            System.out.println("\n-- Movie Inventory --");
            System.out.println("1. List All Movies");
            System.out.println("2. Add Movie");
            System.out.println("3. Remove Movie");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> new MovieManager().displayAllMovies();
                case 2 -> addMovie(scanner, connection);
                case 3 -> removeMovie(scanner, connection);
                case 4 -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    } 

    // Rental & Return sub-menu
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
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> processRental(scanner, rentalManager);
                case 2 -> processReturn(scanner, rentalManager);
                case 3 -> rentalManager.displayActiveRentals();
                case 4 -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    } 

    // Reports sub-menu
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
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> employeeManager.displayEmployeeRentalStats();
                case 2 -> {
                    System.out.print("First name (or press Enter to cancel): ");
                    String fname = scanner.nextLine();
                    if (isCancel(fname)) break;

                    System.out.print("Last name (or press Enter to cancel): ");
                    String lname = scanner.nextLine();
                    if (isCancel(lname)) break;

                    System.out.print("Role (or press Enter to cancel): ");
                    String role = scanner.nextLine();
                    if (isCancel(role)) break;

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
        System.out.print("First name (or press Enter to cancel): ");
        String fname = scanner.nextLine();
        if (isCancel(fname)) return;

        System.out.print("Last name (or press Enter to cancel): ");
        String lname = scanner.nextLine();
        if (isCancel(lname)) return;

        System.out.print("Email (or press Enter to cancel): ");
        String email = scanner.nextLine();
        if (isCancel(email)) return;

        customerManager.addCustomer(fname, lname, email);
    }

    private static void updateCustomerEmail(Scanner scanner, Connection connection) {
        CustomerManager customerManager = new CustomerManager();
        customerManager.displayAllCustomers();
        
        System.out.print("Enter current email to update (or press Enter to cancel): ");
        String currentEmail = scanner.nextLine();
        if (isCancel(currentEmail)) return;

        System.out.print("Enter new email (or press Enter to cancel): ");
        String newEmail = scanner.nextLine();
        if (isCancel(newEmail)) return;

        customerManager.updateCustomerEmail(currentEmail, newEmail);
    }

    private static void deleteCustomer(Scanner scanner, Connection connection) {
        CustomerManager customerManager = new CustomerManager();
        customerManager.displayAllCustomers();
        
        System.out.print("Enter customer email to delete (or press Enter to cancel): ");
        String email = scanner.nextLine();
        if (isCancel(email)) return;

        customerManager.deleteCustomer(email);
    }

    // Movie actions
    private static void addMovie(Scanner scanner, Connection connection) {
        MovieManager movieManager = new MovieManager();
        
        try {
            System.out.print("Title (or press Enter to cancel): ");
            String title = scanner.nextLine();
            if (isCancel(title)) return;

            System.out.print("Release year (or press Enter to cancel): ");
            String yearInput = scanner.nextLine();
            if (isCancel(yearInput)) return;
            int year = Integer.parseInt(yearInput);

            System.out.print("Rental rate (or press Enter to cancel): ");
            String rateInput = scanner.nextLine();
            if (isCancel(rateInput)) return;
            double rate = Double.parseDouble(rateInput);

            System.out.print("Category ID (or press Enter to cancel): ");
            String catInput = scanner.nextLine();
            if (isCancel(catInput)) return;
            int categoryId = Integer.parseInt(catInput);

            movieManager.addMovie(title, year, rate, categoryId);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Action canceled.");
        }
    }

    private static void removeMovie(Scanner scanner, Connection connection) {
        MovieManager movieManager = new MovieManager();
        movieManager.displayAllMovies();
        
        System.out.print("Movie ID to remove (or press Enter to cancel): ");
        String input = scanner.nextLine();
        if (isCancel(input)) return;
        
        try {
            int id = Integer.parseInt(input);
            movieManager.removeMovie(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Action canceled.");
        }
    }

    // Rental actions
    private static void processRental(Scanner scanner, RentalManager rentalManager) {
        EmployeeManager employeeManager = new EmployeeManager();
        MovieManager movieManager = new MovieManager();

        try {
            employeeManager.displayAllEmployees();
            System.out.print("Employee ID (or press Enter to cancel): ");
            String empInput = scanner.nextLine();
            if (isCancel(empInput)) return;
            int employeeId = Integer.parseInt(empInput);

            new CustomerManager().displayAllCustomers();
            System.out.print("Customer email (or press Enter to cancel): ");
            String customerEmail = scanner.nextLine();
            if (isCancel(customerEmail)) return;

            movieManager.displayAllMovies();
            System.out.print("Movie ID (or press Enter to cancel): ");
            String movInput = scanner.nextLine();
            if (isCancel(movInput)) return;
            int movieId = Integer.parseInt(movInput);

            rentalManager.processRental(customerEmail, movieId, employeeId);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Action canceled.");
        }
    }

    private static void processReturn(Scanner scanner, RentalManager rentalManager) {
        try {
            new CustomerManager().displayAllCustomers();
            System.out.print("Customer email (or press Enter to cancel): ");
            String customerEmail = scanner.nextLine();
            if (isCancel(customerEmail)) return;

            // Display ONLY this customer's active rentals
            boolean hasRentals = rentalManager.displayCustomerActiveRentals(customerEmail);
            
            // If they have nothing to return, stop the process
            if (!hasRentals) {
                return; 
            }

            System.out.print("Enter Movie ID to return (or press Enter to cancel): ");
            String movInput = scanner.nextLine();
            if (isCancel(movInput)) return;
            int movieId = Integer.parseInt(movInput);

            rentalManager.processReturn(customerEmail, movieId);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Action canceled.");
        }
    }
}