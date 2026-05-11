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
            System.out.println("2. Employee Management");
            System.out.println("3. Movie Inventory/Management");
            System.out.println("4. Rental & Return");
            System.out.println("5. Reports");
            System.out.println("6. Exit");
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
                case 2 -> employeeMenu(scanner, connection);
                case 3 -> movieMenu(scanner, connection);
                case 4 -> rentalMenu(scanner, connection);
                case 5 -> reportsMenu(scanner, connection);
                case 6 -> {
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
    
    // Employee management sub-menu
    private static void employeeMenu(Scanner scanner, Connection connection) {
        boolean running = true;
        while (running) {
            System.out.println("\n-- Employee Management --");
            System.out.println("1. List All Employees");
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
                case 1 -> new EmployeeManager().displayAllEmployees();
                case 2 -> addEmployee(scanner, connection);
                case 3 -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Movie inventory sub-menu
    private static void movieMenu(Scanner scanner, Connection connection) {
        boolean running = true;
        while (running) {
            System.out.println("\n-- Movie Inventory/Management --");
            System.out.println("1. List All Movies");
            System.out.println("2. Add Movie");
            System.out.println("3. Remove Movie");
            System.out.println("4. List All Categories");
            System.out.println("5. Add Category");
            System.out.println("6. Update Category");
            System.out.println("7. Back");
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
                case 4 -> new MovieManager().displayAllCategories();
                case 5 -> addCategory(scanner, connection);
                case 6 -> updateCategory(scanner, connection);
                case 7 -> running = false;
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
            System.out.println("2. Back");
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
                case 2 -> running = false;
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

        // Check if the email already exists before inserting
        if (customerManager.customerExists(email)) {
            System.out.println("Error: A customer with that email already exists.");
            return;
        }

        customerManager.addCustomer(fname, lname, email);
    }

    private static void updateCustomerEmail(Scanner scanner, Connection connection) {
        CustomerManager customerManager = new CustomerManager();
        customerManager.displayAllCustomers();
        
        System.out.print("Enter current email to update (or press Enter to cancel): ");
        String currentEmail = scanner.nextLine();
        if (isCancel(currentEmail)) return;

        // Check if the current email exists before proceeding
        if (!customerManager.customerExists(currentEmail)) {
            System.out.println("Error: No customer found with that email.");
            return;
        }

        System.out.print("Enter new email (or press Enter to cancel): ");
        String newEmail = scanner.nextLine();
        if (isCancel(newEmail)) return;

        // Check if the new email is already taken by someone else
        if (customerManager.customerExists(newEmail)) {
            System.out.println("Error: A customer with that email already exists.");
            return;
        }

        customerManager.updateCustomerEmail(currentEmail, newEmail);
    }

    private static void deleteCustomer(Scanner scanner, Connection connection) {
        CustomerManager customerManager = new CustomerManager();
        customerManager.displayAllCustomers();
        
        System.out.print("Enter customer email to delete (or press Enter to cancel): ");
        String email = scanner.nextLine();
        if (isCancel(email)) return;

        // Check if the email exists before proceeding
        if (!customerManager.customerExists(email)) {
            System.out.println("Error: No customer found with that email.");
            return;
        }

        customerManager.deleteCustomer(email);
    }
    
    // Employee actions
    private static void addEmployee(Scanner scanner, Connection connection) {
        EmployeeManager employeeManager = new EmployeeManager();
        System.out.print("First name (or press Enter to cancel): ");
        String fname = scanner.nextLine();
        if (isCancel(fname)) return;

        System.out.print("Last name (or press Enter to cancel): ");
        String lname = scanner.nextLine();
        if (isCancel(lname)) return;

        System.out.print("Role (or press Enter to cancel): ");
        String role = scanner.nextLine();
        if (isCancel(role)) return;

        employeeManager.addEmployee(fname, lname, role);
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

            // Display available categories before asking for ID
            movieManager.displayAllCategories();
            System.out.print("Category ID (or press Enter to cancel): ");
            String catInput = scanner.nextLine();
            if (isCancel(catInput)) return;
            int categoryId = Integer.parseInt(catInput);

            // Check if the category exists before proceeding
            if (!movieManager.categoryExists(categoryId)) {
                System.out.println("Error: No category found with ID " + categoryId + ".");
                return;
            }

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
            
            // Check if the movie exists before proceeding
            if (!movieManager.movieExists(id)) {
                System.out.println("Error: No movie found with ID " + id + ".");
                return;
            }
            
            movieManager.removeMovie(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Action canceled.");
        }
    }
    
    // Category actions
    private static void addCategory(Scanner scanner, Connection connection) {
        MovieManager movieManager = new MovieManager();
        System.out.print("Category name (or press Enter to cancel): ");
        String name = scanner.nextLine();
        if (isCancel(name)) return;
        
        movieManager.addCategory(name);
    }
    
    private static void updateCategory(Scanner scanner, Connection connection) {
        MovieManager movieManager = new MovieManager();
        movieManager.displayAllCategories();
        
        System.out.print("Enter Category ID to update (or press Enter to cancel): ");
        String idInput = scanner.nextLine();
        if (isCancel(idInput)) return;
        
        try {
            int id = Integer.parseInt(idInput);
            
            if (!movieManager.categoryExists(id)) {
                System.out.println("Error: No category found with ID " + id + ".");
                return;
            }
            
            System.out.print("Enter new category name (or press Enter to cancel): ");
            String newName = scanner.nextLine();
            if (isCancel(newName)) return;
            
            movieManager.updateCategory(id, newName);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Action canceled.");
        }
    }

    // Rental actions
    private static void processRental(Scanner scanner, RentalManager rentalManager) {
        EmployeeManager employeeManager = new EmployeeManager();
        MovieManager movieManager = new MovieManager();
        CustomerManager customerManager = new CustomerManager();

        try {
            employeeManager.displayAllEmployees();
            System.out.print("Employee ID (or press Enter to cancel): ");
            String empInput = scanner.nextLine();
            if (isCancel(empInput)) return;
            int employeeId = Integer.parseInt(empInput);

            // Check if the employee exists before proceeding
            if (!employeeManager.employeeExists(employeeId)) {
                System.out.println("Error: No employee found with ID " + employeeId + ".");
                return;
            }

            customerManager.displayAllCustomers();
            System.out.print("Customer email (or press Enter to cancel): ");
            String customerEmail = scanner.nextLine();
            if (isCancel(customerEmail)) return;

            // Check if the customer exists before proceeding
            if (!customerManager.customerExists(customerEmail)) {
                System.out.println("Error: No customer found with that email.");
                return;
            }

            movieManager.displayAllMovies();
            System.out.print("Movie ID (or press Enter to cancel): ");
            String movInput = scanner.nextLine();
            if (isCancel(movInput)) return;
            int movieId = Integer.parseInt(movInput);

            // Check if the movie exists before proceeding
            if (!movieManager.movieExists(movieId)) {
                System.out.println("Error: No movie found with ID " + movieId + ".");
                return;
            }

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