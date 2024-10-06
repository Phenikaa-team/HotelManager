package group.phenikaa.hotelmanager.impl.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import group.phenikaa.hotelmanager.api.info.impl.customer.User;
import group.phenikaa.hotelmanager.asm.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginData {

    // Login method that throws SQLException
    public User login(String username, String password) throws SQLException {
        User user = null;
        String query = "SELECT * FROM customer WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"));
            }
        }
        return user; // Return the found user or null
    }

    // Registration method that throws SQLException
    public void register(User newCustomer) throws SQLException {
        String query = "INSERT INTO customer (username, password) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, newCustomer.getUsername());
            pstmt.setString(2, newCustomer.getPassword());
            pstmt.executeUpdate();
        }
    }
}
