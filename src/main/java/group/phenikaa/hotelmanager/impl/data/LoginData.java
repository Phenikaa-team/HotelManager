package group.phenikaa.hotelmanager.impl.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import group.phenikaa.hotelmanager.api.info.impl.customer.User;
import group.phenikaa.hotelmanager.api.utility.interfaces.IDataStorage;
import group.phenikaa.hotelmanager.asm.DatabaseConnection;
import group.phenikaa.hotelmanager.impl.data.adapter.UserAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//TODO: upload everything to database
public class LoginData implements IDataStorage<User> {
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter())
            .setPrettyPrinting().create();

    @Override
    public void save(List<User> list, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(list, writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> load(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File not found: " + fileName);
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type bookingListType = TypeToken.getParameterized(List.class, User.class).getType();
            return gson.fromJson(reader, bookingListType);
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Example here
//    public List<User> load() throws SQLException {
//        List<User> users = new ArrayList<>();
//        String query = "SELECT username, password FROM users";
//
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query);
//             ResultSet resultSet = preparedStatement.executeQuery()) {
//
//            while (resultSet.next()) {
//                String username = resultSet.getString("username");
//                String password = resultSet.getString("password");
//                users.add(new User(username, password));
//            }
//        }
//
//        return users;
//    }
//
//    public void save(User newUser) throws SQLException {
//        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
//
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setString(1, newUser.getUsername());
//            preparedStatement.setString(2, newUser.getPassword());
//            preparedStatement.executeUpdate();
//        }
//    }
}
