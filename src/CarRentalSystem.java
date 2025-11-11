import java.sql.*;
import java.util.Scanner;
public class CarRentalSystem {
    private static final String URL = "jdbc:postgresql://localhost:5433/CarRental";// it's mine the port is diff to others
    private static final String USER = "postgres";
    private static final String PASSWORD = "Varma@123";//it's mine change what you put in db password
    private Connection conn;
    public CarRentalSystem() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" DATABASE CONNECTED SUCCESSFULLY!");
        } catch (SQLException e) {
            System.out.println(" DATABASE CONNECTION FAILED: " + e.getMessage());
        }
    }
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== CAR RENTAL SYSTEM =====");
            System.out.println("1. RENT A CAR");
            System.out.println("2. RETURN A CAR");
            System.out.println("3. VIEW AVAILABLE CARS");
            System.out.println("4. EXIT");
            System.out.print("ENTER YOUR CHOICE: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> rentCar(scanner);
                case 2 -> returnCar(scanner);
                case 3 -> viewCars();
                case 4 -> {
                    System.out.println("\n THANK YOU! NEED IT - IT'S OURS - EXPERIENCE IT");
                    return;
                }
                default -> System.out.println("INVALID CHOICE. TRY AGAIN!");
            }
        }
    }
    private void rentCar(Scanner scanner) {
        try {
            System.out.println("\n== RENT A CAR ==\n");
            System.out.print("ENTER YOUR NAME: ");
            String name = scanner.nextLine();
            System.out.print("ENTER PHONE NUMBER: ");
            long phone = scanner.nextLong();
            System.out.print("ENTER ADHAR NUMBER: ");
            long adhar = scanner.nextLong();
            scanner.nextLine();
            int nextId = getNextCustomerId();
            String formattedId = String.format("C%03d", nextId);
            PreparedStatement insertCustomer = conn.prepareStatement(
                    "INSERT INTO customers (customer_id, name, phone_number, adhar_card) VALUES (?, ?, ?, ?)");
            insertCustomer.setInt(1, nextId);
            insertCustomer.setString(2, name);
            insertCustomer.setLong(3, phone);
            insertCustomer.setLong(4, adhar);
            insertCustomer.executeUpdate();
            System.out.println("\n-- AVAILABLE CARS --");
            viewCars();
            System.out.print("\nENTER CAR ID TO RENT: ");
            String carId = scanner.nextLine();
            String selectCarSQL = "SELECT * FROM cars WHERE LOWER(car_id) = LOWER(?) AND is_available = TRUE";
            PreparedStatement selectCar = conn.prepareStatement(selectCarSQL);
            selectCar.setString(1, carId);
            ResultSet rs = selectCar.executeQuery();
            if (rs.next()) {
                System.out.print("ENTER NUMBER OF DAYS: ");
                int days = scanner.nextInt();
                scanner.nextLine();
                double totalPrice = rs.getDouble("base_price_per_day") * days;
                System.out.println("\n--- RENTAL DETAILS ---");
                System.out.println("CUSTOMER ID: " + formattedId);
                System.out.println("NAME: " + name);
                System.out.println("CAR: " + rs.getString("brand") + " " + rs.getString("model"));
                System.out.println("DAYS: " + days);
                System.out.printf("TOTAL PRICE: ₹%.2f%n", totalPrice);
                System.out.print("\nCONFIRM RENTAL (Y/N): ");
                String confirm = scanner.nextLine();
                if (confirm.equalsIgnoreCase("Y")) {
                    String insertRentalSQL = "INSERT INTO rentals (car_id, customer_id, days) VALUES (?, ?, ?)";
                    PreparedStatement insertRental = conn.prepareStatement(insertRentalSQL);
                    insertRental.setString(1, rs.getString("car_id"));
                    insertRental.setInt(2, nextId);
                    insertRental.setInt(3, days);
                    insertRental.executeUpdate();
                    PreparedStatement updateCar = conn.prepareStatement("UPDATE cars SET is_available = FALSE WHERE LOWER(car_id) = LOWER(?)");
                    updateCar.setString(1, carId);
                    updateCar.executeUpdate();
                    System.out.println("\nCAR RENTED SUCCESSFULLY!");
                } else {
                    System.out.println("\n RENTAL CANCELLED!");
                }
            } else {
                System.out.println("\n INVALID CAR ID OR NOT AVAILABLE!");
            }
        } catch (SQLException e) {
            System.out.println("ERROR DURING RENTAL: " + e.getMessage());
        }
    }
    private int getNextCustomerId() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COALESCE(MAX(customer_id), 0) + 1 FROM customers");
        rs.next();
        return rs.getInt(1);
    }
    private void returnCar(Scanner scanner) {
        try {
            System.out.println("\n-- RETURN A CAR --");
            System.out.print("ENTER CAR ID TO RETURN: ");
            String carId = scanner.nextLine();
            String checkRentalSQL = """
                    SELECT rentals.rental_id, customers.name 
                    FROM rentals 
                    JOIN customers ON rentals.customer_id = customers.customer_id 
                    WHERE LOWER(rentals.car_id) = LOWER(?)""";
            PreparedStatement checkRental = conn.prepareStatement(checkRentalSQL);
            checkRental.setString(1, carId);
            ResultSet rs = checkRental.executeQuery();
            if (rs.next()) {
                PreparedStatement deleteRental = conn.prepareStatement("DELETE FROM rentals WHERE LOWER(car_id) = LOWER(?)");
                deleteRental.setString(1, carId);
                deleteRental.executeUpdate();
                PreparedStatement updateCar = conn.prepareStatement("UPDATE cars SET is_available = TRUE WHERE LOWER(car_id) = LOWER(?)");
                updateCar.setString(1, carId);
                updateCar.executeUpdate();
                System.out.println("CAR RETURNED SUCCESSFULLY BY: " + rs.getString("name"));
            } else {
                System.out.println(" INVALID CAR ID OR CAR NOT RENTED!");
            }
        } catch (SQLException e) {
            System.out.println("ERROR RETURNING CAR: " + e.getMessage());
        }
    }
    private void viewCars() {
        try {
            String query = "SELECT * FROM cars WHERE is_available = TRUE ORDER BY car_id";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println("\nCAR ID | BRAND | MODEL | PRICE/DAY | AVAILABLE");
            System.out.println("---------------------------------------------");
            while (rs.next()) {
                System.out.printf("%s | %s | %s | ₹%.2f | %s%n",
                        rs.getString("car_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getDouble("base_price_per_day"),
                        rs.getBoolean("is_available") ? "YES" : "NO");
            }
        } catch (SQLException e) {
            System.out.println("ERROR FETCHING CARS: " + e.getMessage());
        }
    }
}
