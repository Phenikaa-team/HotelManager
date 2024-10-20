package group.phenikaa.hotelmanager.asm.database;

import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.utility.enums.Country;
import group.phenikaa.hotelmanager.api.utility.enums.IDProof;
import group.phenikaa.hotelmanager.asm.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDatabase extends DataBaseConnection {

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