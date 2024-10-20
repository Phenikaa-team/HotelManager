package group.phenikaa.hotelmanager.asm.database;

import group.phenikaa.hotelmanager.api.info.impl.customer.User;
import group.phenikaa.hotelmanager.asm.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoginDatabase extends DataBaseConnection {

    public static void register(User newCustomer) throws SQLException {
        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO customer (username, password) VALUES (?, ?)")) {

            pstmt.setString(1, newCustomer.username());
            pstmt.setString(2, newCustomer.password());
            pstmt.executeUpdate();
            System.out.println("Registration successful!");
        } catch (SQLException e) {
            System.out.println("An error occurred during registration: " + e.getMessage());
        }
    }

    public static User login(String username, String password) {
        User user = null;
        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM customer WHERE username = ? AND password = ?")) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful! Welcome, " + username);
                user = new User(rs.getString("username"), rs.getString("password"));
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred during login: " + e.getMessage());
        }
        return user;
    }

    private static List<User> userList() {
        List<User> list = new ArrayList<>();
        try (Connection connection = connect();
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

    public static void main(String[] args) throws SQLException {
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
                case 1 -> {
                    System.out.print("Enter a username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter a password: ");
                    String password = scanner.nextLine();

                    var newCustomer = new User(username, password);
                    register(newCustomer);
                }
                case 2 -> {
                    System.out.print("Enter your username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();

                    login(username, password);
                }
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
