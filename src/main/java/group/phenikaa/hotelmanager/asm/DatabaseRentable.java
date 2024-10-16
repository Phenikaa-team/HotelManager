package group.phenikaa.hotelmanager.asm;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

import group.phenikaa.hotelmanager.impl.data.BookingData;
import group.phenikaa.hotelmanager.api.info.Booking;

import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.utility.enums.Country;
import group.phenikaa.hotelmanager.api.utility.enums.IDProof;

import group.phenikaa.hotelmanager.api.info.impl.rentable.ConcreteRentable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseRentable {
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

    // Method to save a Rentable
    public static void saveRentable(Scanner scanner) {
        System.out.print("Enter Rentable type (e.g., APARTMENT, HOUSE): ");
        String rentableType = scanner.nextLine().toUpperCase();
        RentableType type = RentableType.valueOf(rentableType);

        System.out.print("Enter Rentable status (e.g., AVAILABLE, BOOKED): ");
        String rentableStatus = scanner.nextLine().toUpperCase();
        RentableStatus status = RentableStatus.valueOf(rentableStatus);

        System.out.print("Enter Rentable number: ");
        String number = scanner.nextLine();

        System.out.print("Enter Rentable price: ");
        long price = Long.parseLong(scanner.nextLine());

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

    // Method to fetch all rentables
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

                // Create an instance of your concrete Rentable class here
                AbstractRentable rentable = new ConcreteRentable(rentableType, rentableStatus, price, number);
                list.add(rentable);
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while fetching rentables: " + e.getMessage());
        }
        return list;
    }

    // Method to load rentable information from database
    public static List<AbstractRentable> loadRentables() {
        List<AbstractRentable> rentables = new ArrayList<>();
        String query = "SELECT * FROM rentables";

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String type = rs.getString("type");
                String status = rs.getString("status");
                String number = rs.getString("number");
                long price = rs.getLong("price");

                RentableType rentableType = RentableType.valueOf(type);
                RentableStatus rentableStatus = RentableStatus.valueOf(status);

                // Create a new Rentable object and add it to the list
                AbstractRentable rentable = new ConcreteRentable(rentableType, rentableStatus, price, number);
                rentables.add(rentable);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while loading rentables: " + e.getMessage());
            e.printStackTrace();
        }

        return rentables;
    }

    public static List<Booking> loadRentable() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM rentables";  // Cần thay đổi nếu cần

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Giả sử bạn có bảng rentables với các trường
                String number = rs.getString("number");
                long price = rs.getLong("price");
                RentableType type = RentableType.valueOf(rs.getString("type"));
                RentableStatus status = RentableStatus.valueOf(rs.getString("status"));

                // Tạo đối tượng ConcreteRentable
                AbstractRentable rentable = new ConcreteRentable(type, status, price, number);

                // Thêm thông tin vào Booking
                Booking booking = new Booking(rentable, null); // Thay null bằng đối tượng Customer nếu cần
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while loading rentable data: " + e.getMessage());
            e.printStackTrace();
        }

        return bookings;
    }

    public static void saveRentable(RentableType type, RentableStatus status, String number, long price) {
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
    public static void saveRentable(AbstractRentable rentable) {
        // Lấy thông tin từ đối tượng rentable
        RentableType type = rentable.getType(); // Giả sử bạn có phương thức getType() trong ConcreteRentable
        RentableStatus status = rentable.getStatus(); // Giả sử bạn có phương thức getStatus()
        String number = rentable.getNumber(); // Giả sử bạn có phương thức getNumber()
        long price = rentable.getPrice(); // Giả sử bạn có phương thức getPrice()

        // Kết nối đến cơ sở dữ liệu và thực hiện câu lệnh INSERT
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

    public static void saveRentable(ConcreteRentable rentable) {
        // Lấy thông tin từ đối tượng rentable
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
        String query = "UPDATE rentables SET type = ?, status = ?, number = ? WHERE number = ?";  // Sử dụng số phòng làm khóa chính hoặc sử dụng ID nếu có

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, rentable.getType().name());
            pstmt.setString(2, rentable.getStatus().name());
            pstmt.setString(3, rentable.getNumber());
            pstmt.setString(4, rentable.getNumber());  // Cập nhật theo số phòng, có thể cần thay đổi nếu sử dụng khóa chính khác

            pstmt.executeUpdate();
            System.out.println("Rentable updated successfully!");
        } catch (SQLException e) {
            System.out.println("An error occurred while updating the rentable: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void removeRentable(String roomNumber) {
        String query = "DELETE FROM rentables WHERE number = ?";  // Sử dụng số phòng làm điều kiện

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, roomNumber);  // Thiết lập điều kiện xóa theo số phòng

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
            pstmt.setInt(4, customerIdNumber);  // Cập nhật idNumber của khách hàng
            pstmt.setString(5, rentable.getNumber());  // Điều kiện cập nhật dựa trên số phòng (number)

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

            // Đặt idNumber là null khi checkout
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
