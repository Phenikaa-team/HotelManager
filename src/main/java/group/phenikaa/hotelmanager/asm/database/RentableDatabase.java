package group.phenikaa.hotelmanager.asm.database;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.utility.enums.Country;
import group.phenikaa.hotelmanager.api.utility.enums.IDProof;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

import group.phenikaa.hotelmanager.asm.DataBaseConnection;
import group.phenikaa.hotelmanager.api.info.Booking;

import group.phenikaa.hotelmanager.api.info.impl.rentable.ConcreteRentable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static group.phenikaa.hotelmanager.api.utility.Utility.getRentable;

public class RentableDatabase extends DataBaseConnection {

    public static List<AbstractRentable> rentableList() {
        List<AbstractRentable> list = new ArrayList<>();
        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM rentables")) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String type = rs.getString("type");
                String status = rs.getString("status");
                String number = rs.getString("number");
                long price = rs.getLong("price");

                RentableType rentableType = RentableType.valueOf(type);
                RentableStatus rentableStatus = RentableStatus.valueOf(status);

                AbstractRentable rentable = getRentable(rentableType, rentableStatus, price, number);
                list.add(rentable);
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while fetching rentables: " + e.getMessage());
        }
        return list;
    }

    public static List<Booking> loadRentable() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM rentables";

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String number = rs.getString("number");
                long price = rs.getLong("price");
                RentableType type = RentableType.valueOf(rs.getString("type"));
                RentableStatus status = RentableStatus.valueOf(rs.getString("status"));

                AbstractRentable rentable = getRentable(type, status, price, number);

                Booking booking = new Booking(rentable, null);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while loading rentable data: " + e.getMessage());
            e.printStackTrace();
        }

        return bookings;
    }

    public static void saveRentable(AbstractRentable rentable) {
        RentableType type = rentable.getType();
        RentableStatus status = rentable.getStatus();
        String number = rentable.getNumber();
        long price = rentable.getPrice();

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO rentables (type, status, number, price) VALUES (?, ?, ?, ?)")) {

            pstmt.setString(1, type.name());
            pstmt.setString(2, status.name());
            pstmt.setString(3, number);
            pstmt.setLong(4, price);

            pstmt.executeUpdate();
            System.out.println("Rentable saved successfully!");
        } catch (SQLException e) {
            System.out.println("An error occurred while saving the rentable: " + e.getMessage());
        }
    }

    public static void updateRentable(AbstractRentable rentable) {
        String query = "UPDATE rentables SET type = ?, status = ?, number = ? WHERE number = ?";

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, rentable.getType().name());
            pstmt.setString(2, rentable.getStatus().name());
            pstmt.setString(3, rentable.getNumber());
            pstmt.setString(4, rentable.getNumber());

            pstmt.executeUpdate();
            System.out.println("Rentable updated successfully!");
        } catch (SQLException e) {
            System.out.println("An error occurred while updating the rentable: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void removeRentable(String roomNumber) {
        String query = "DELETE FROM rentables WHERE number = ?";

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, roomNumber);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Rentable removed successfully!");
            } else {
                System.out.println("No rentable found with the given number.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while removing the rentable: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void updateRentableCustomer(AbstractRentable rentable, int customerIdNumber) {
        String query = "UPDATE rentables SET type = ?, status = ?, number = ?, idNumber = ? WHERE number = ?";

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, rentable.getType().name());
            pstmt.setString(2, rentable.getStatus().name());
            pstmt.setString(3, rentable.getNumber());
            pstmt.setInt(4, customerIdNumber);
            pstmt.setString(5, rentable.getNumber());

            pstmt.executeUpdate();
            System.out.println("Rentable and customer idNumber updated successfully!");
        } catch (SQLException e) {
            System.out.println("An error occurred while updating the rentable with customer idNumber: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void updateRentableCustomer(AbstractRentable rentable, Integer idNumber) {
        String query = "UPDATE rentables SET idNumber = ? WHERE number = ?";

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            if (idNumber == null) {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(1, idNumber);
            }

            pstmt.setString(2, rentable.getNumber());

            pstmt.executeUpdate();
            System.out.println("Rentable customer updated successfully!");
        } catch (SQLException e) {
            System.out.println("An error occurred while updating rentable customer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
