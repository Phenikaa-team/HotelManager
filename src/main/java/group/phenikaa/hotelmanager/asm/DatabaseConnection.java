package group.phenikaa.hotelmanager.asm;

import group.phenikaa.hotelmanager.api.info.impl.customer.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseConnection {
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

    private static void register(Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO customer (username, password) VALUES (?, ?)")) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            System.out.println("Registration successful!");
        } catch (SQLException e) {
            System.out.println("An error occurred during registration: " + e.getMessage());
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM customer WHERE username = ? AND password = ?")) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful! Welcome, " + username);
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred during login: " + e.getMessage());
        }
    }

    private static List<User> userList() {
        List<User> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM customer")) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                list.add(new User(username, password));
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while fetching users: " + e.getMessage());
        }
        return list;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome to the Hotel Manager System");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Account List");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> register(scanner);
                case 2 -> login(scanner);
                case 3 -> {
                    List<User> userList = userList();
                    if (userList.isEmpty()) {
                        System.out.println("No users found.");
                    } else {
                        System.out.println("Customer List:");
                        for (User customer : userList) {
                            System.out.println(customer);
                        }
                    }
                }
                case 4 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
