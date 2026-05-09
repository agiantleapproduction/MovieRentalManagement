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

            System.out.println("\n== Movie Rental System ===");
            System.out.println("1. Customer Management");
            System.out.println("2. Movie Inventory");
            System.out.println("3. Rental & Return");
            System.out.println("4. Reports");
            System.out.println("5. Exit");
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
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
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
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
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
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
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


    // Handles collecting input and calling RentalManager.processRental
    private static void processRental(Scanner scanner, RentalManager rentalManager) {
        EmployeeManager employeeManager = new EmployeeManager();
        MovieManager movieManager = new MovieManager();

        employeeManager.displayAllEmployees();
        System.out.print("Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        new CustomerManager().displayAllCustomers();
        System.out.print("Customer email: ");
        String customerEmail = scanner.nextLine();

        movieManager.displayAllMovies();
        System.out.print("Movie ID: ");
        int movieId = scanner.nextInt();
        scanner.nextLine();

        rentalManager.processRental(customerEmail, movieId, employeeId);
    }


    // Handles collecting input and calling RentalManager.processReturn
    private static void processReturn(Scanner scanner, RentalManager rentalManager) {
        MovieManager movieManager = new MovieManager();

        new CustomerManager().displayAllCustomers();
        System.out.print("Customer email: ");
        String customerEmail = scanner.nextLine();

        movieManager.displayAllMovies();
        System.out.print("Movie ID: ");
        int movieId = scanner.nextInt();
        scanner.nextLine();

        rentalManager.processReturn(customerEmail, movieId);
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
        customerManager.displayAllCustomers();
        System.out.print("Enter current email to update: ");
        String currentEmail = scanner.nextLine();
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();
        customerManager.updateCustomerEmail(currentEmail, newEmail);
    }

    private static void deleteCustomer(Scanner scanner, Connection connection) {
        CustomerManager customerManager = new CustomerManager();
        customerManager.displayAllCustomers();
        System.out.print("Enter customer email to delete: ");
        String email = scanner.nextLine();
        customerManager.deleteCustomer(email);
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
        movieManager.displayAllMovies();
        System.out.print("Movie ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        movieManager.removeMovie(id);
    }

} 