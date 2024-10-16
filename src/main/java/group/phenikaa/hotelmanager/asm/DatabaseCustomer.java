package group.phenikaa.hotelmanager.asm;

import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.utility.enums.Country;
import group.phenikaa.hotelmanager.api.utility.enums.IDProof;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseCustomer {
    private static final String DB_URL = "jdbc:mysql://avnadmin:AVNS_fLXK_Ki8PUpk5ejsf0Z@mysql-177353fb-st-3589.i.aivencloud.com:12362/defaultdb?ssl-mode=REQUIRED";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to the MySQL database successfully!");
        } catch (SQLException e) {
            System.out.println("Connection to MySQL database failed.");
            e.printStackTrace();
        }
        return connection;
    }

    public static void saveCustomer(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        // Example for IDProof and Country, you can customize it based on your enum structure
        System.out.print("Enter your ID proof type (e.g., PASSPORT, ID_CARD): ");
        String idProofType = scanner.nextLine(); // You might want to convert this to the Enum
        IDProof idProof = IDProof.valueOf(idProofType.toUpperCase());

        System.out.print("Enter your ID number: ");
        int idNumber = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter the number of guests: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter number of nights: ");
        int night = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter your country (e.g., USA, VIETNAM): ");
        String countryType = scanner.nextLine(); // You might want to convert this to the Enum
        Country country = Country.valueOf(countryType.toUpperCase());

        System.out.print("Enter your budget: ");
        long money = Long.parseLong(scanner.nextLine());

        System.out.print("Do you have a kid (true/false): ");
        boolean kid = Boolean.parseBoolean(scanner.nextLine());

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO customers (name, id_proof, id_number, quantity, night, country, money, kid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            pstmt.setString(1, name);
            pstmt.setString(2, idProof.name());
            pstmt.setInt(3, idNumber);
            pstmt.setInt(4, quantity);
            pstmt.setInt(5, night);
            pstmt.setString(6, country.name());
            pstmt.setLong(7, money);
            pstmt.setBoolean(8, kid);
            pstmt.executeUpdate();
            System.out.println("Customer registration successful!");
        } catch (SQLException e) {
            System.out.println("An error occurred during registration: " + e.getMessage());
        }
    }



    public static List<Customer> customerList() {
        List<Customer> list = new ArrayList<>();
        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM customers")) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String idProofType = rs.getString("id_proof");
                int idNumber = rs.getInt("id_number");
                int quantity = rs.getInt("quantity");
                int night = rs.getInt("night");
                String countryType = rs.getString("country");
                long money = rs.getLong("money");
                boolean kid = rs.getBoolean("kid");

                // Construct Customer object with fetched data
                Customer customer = new Customer(name, IDProof.valueOf(idProofType), idNumber, quantity, night, Country.valueOf(countryType), money, kid);
                list.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while fetching customers: " + e.getMessage());
        }
        return list;
    }

    public static List<Customer> loadCustomer() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers";

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String idProof = rs.getString("idProof");
                int idNumber = rs.getInt("idNumber");
                int quantity = rs.getInt("quantity");
                int night = rs.getInt("night");
                String country = rs.getString("country");
                long money = rs.getLong("money");
                boolean hasKids = rs.getBoolean("hasKids");

                // Create a new Customer object and add it to the list
                Customer customer = new Customer(name, IDProof.valueOf(idProof), idNumber, quantity, night, Country.valueOf(country), money, hasKids);
                customers.add(customer);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while loading customers: " + e.getMessage());
            e.printStackTrace();
        }

        return customers;
    }
    public static void deleteCustomer(int idNumber) {
        String query = "DELETE FROM customers WHERE idNumber = ?";

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, idNumber);
            pstmt.executeUpdate();
            System.out.println("Customer deleted successfully!");
        } catch (SQLException e) {
            System.out.println("An error occurred while deleting the customer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}