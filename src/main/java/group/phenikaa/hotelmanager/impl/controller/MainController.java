package group.phenikaa.hotelmanager.impl.controller;

import group.phenikaa.hotelmanager.asm.DatabaseConnection;
import group.phenikaa.hotelmanager.asm.DatabaseCustomer;
import group.phenikaa.hotelmanager.asm.DatabaseRentable;

import group.phenikaa.hotelmanager.api.info.impl.rentable.ConcreteRentable;

import group.phenikaa.hotelmanager.impl.data.CustomerData;

import group.phenikaa.hotelmanager.HotelApplication;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.info.impl.customer.Session;
import group.phenikaa.hotelmanager.api.info.impl.customer.User;
import group.phenikaa.hotelmanager.api.utility.enums.*;
import group.phenikaa.hotelmanager.impl.data.CustomerData;
import group.phenikaa.hotelmanager.impl.data.adapter.BookingAdapter;
import group.phenikaa.hotelmanager.impl.data.BookingData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static group.phenikaa.hotelmanager.HotelApplication.DATA;
import static group.phenikaa.hotelmanager.HotelApplication.USER;
import static group.phenikaa.hotelmanager.api.utility.Utility.*;

public class MainController implements Initializable {
    private final BookingData data = new BookingData();
    private final CustomerData customer = new CustomerData();

    private Booking selectedBooking = null;

    // ListView for bookings
    @FXML
    private ListView<Booking> bookingListView;
    @FXML
    private ListView<Booking> user_book_list;

    // ObservableList for bookings
    private ObservableList<Booking> bookingList;

    // Buttons
    @FXML
    private Button dashboard_btn;
    @FXML
    private Button room_details_btn;
    @FXML
    private Button rooms_btn;

    // Panels
    private List<AnchorPane> panels;

    @FXML
    private AnchorPane room_details_panel;
    @FXML
    private AnchorPane dash_board_panel;
    @FXML
    private AnchorPane room_panel;

    //Dashboard
    @FXML
    private Text total_rooms;
    @FXML
    private Text valid_rooms;

    //Room(details)
    @FXML
    private ComboBox<RentableType> hotel_category;
    @FXML
    private ComboBox<RentableStatus> hotel_status;
    @FXML
    private TextField room_number_field;
    @FXML
    private Button add_btn;
    @FXML
    private Button edit_btn;

    //Room(booking)
    @FXML
    private TextField name_field;
    @FXML
    private TextField id_field;
    @FXML
    private TextField submitted_money;

    @FXML
    private ComboBox<Country> citizen_box;
    @FXML
    private ComboBox<IDProof> id_box;
    @FXML
    private ComboBox<String> renter_quantity;
    @FXML
    private ComboBox<String> total_night_box;

    @FXML
    private CheckBox kid_btn;

    @FXML
    private ChoiceBox<RentableStatus> filter_box;

    // Window control buttons
    @FXML
    private Button exit_btn;
    @FXML
    private Button minimize_btn;
    @FXML
    private Text user_name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            User user = Session.getCurrentUser();
            if (user != null) {
                user_name.setText(user.username());
            }
            panels = Arrays.asList(room_details_panel, dash_board_panel, room_panel);

            List<Booking> list = DatabaseRentable.loadRentable();
            bookingList = FXCollections.observableArrayList(list);
            if (bookingListView != null) {
                setupListView();
            }
            setupUserBookList();

            bookingListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateRoomDetails(newSelection);
                    edit_btn.setText("Edit");
                }
            });
            citizen_box.setItems(FXCollections.observableArrayList(Country.values()));
            id_box.setItems(FXCollections.observableArrayList(IDProof.values()));
            renter_quantity.setItems(FXCollections.observableArrayList(convertIntegerListToString(rangeList(1, 12))));
            total_night_box.setItems(FXCollections.observableArrayList(convertIntegerListToString(rangeList(1, 62))));
            hotel_category.setItems(FXCollections.observableArrayList(RentableType.values()));
            hotel_status.setItems(FXCollections.observableArrayList(RentableStatus.values()));
            filter_box.setItems(FXCollections.observableArrayList(RentableStatus.values()));
            filter_box.setAccessibleText("Filter");
            updateRoomCounts();
            edit_btn.setText("Edit");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupUserBookList() {
        ObservableList<Booking> availableRoomsList = FXCollections.observableArrayList(bookingList);
        FilteredList<Booking> filteredList = new FilteredList<>(availableRoomsList, booking -> true);

        user_book_list.setItems(filteredList);
        user_book_list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Booking booking, boolean empty) {
                super.updateItem(booking, empty);
                if (empty || booking == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    var hbox = new HBox(20);

                    // Room Number
                    var roomNumberLabel = new Label(booking.getRentable().getNumber());
                    roomNumberLabel.setPrefWidth(20);

                    // Type
                    var typeLabel = new Label(booking.getRentable().getType().name());
                    typeLabel.setPrefWidth(45);

                    // Price
                    var priceLabel = new Label(booking.getRentable().getPrice() + " VND");
                    priceLabel.setPrefWidth(75);

                    // Can rent
                    var canRent = new Label(booking.getRentable().getStatus() == RentableStatus.Available ? "Yes" : "No");
                    canRent.setPrefWidth(25);

                    hbox.getChildren().addAll(roomNumberLabel, typeLabel, priceLabel, canRent);
                    setGraphic(hbox);
                }
            }
        });

        // Example: Apply filter based on room status
        filter_box.valueProperty().addListener((obs, oldVal, newVal) -> {
            filteredList.setPredicate(booking -> {
                if (newVal == null) {
                    return true;
                }
                if (booking != null) {
                    return booking.getRentable().getStatus() == newVal;
                } else return false;
            });
        });
    }


    private void populateRoomDetails(Booking booking) {
        selectedBooking = booking;
        room_number_field.setText(booking.getRentable().getNumber());
        hotel_category.setValue(booking.getRentable().getType());
        hotel_status.setValue(booking.getRentable().getStatus());
    }

    private void setupListView() {
        bookingListView.setItems(bookingList);
        bookingListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Booking booking, boolean empty) {
                super.updateItem(booking, empty);
                if (empty || booking == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    var hbox = new HBox(20);

                    // Room Number
                    var roomNumberLabel = new Label(booking.getRentable().getNumber());
                    roomNumberLabel.setPrefWidth(85);

                    // Type
                    var typeLabel = new Label(booking.getRentable().getType().name());
                    typeLabel.setPrefWidth(80);

                    // Status
                    var statusLabel = new Label(booking.getRentable().getStatus().toString());
                    statusLabel.setPrefWidth(230);

                    // Price
                    var priceLabel = new Label(booking.getRentable().getPrice() + " VND");
                    priceLabel.setPrefWidth(100);

                    hbox.getChildren().addAll(roomNumberLabel, typeLabel, statusLabel, priceLabel);

                    setGraphic(hbox);
                }
            }
        });
    }

    @FXML
    void getEnterScene() throws IOException {
        HotelApplication.switchToLoginScene();
    }

    @FXML
    void onExit() {
        /*saveRentable();*/
        System.exit(0);
    }

    @FXML
    void onMinimize() {
        var stage = (Stage)minimize_btn.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void onBasic() {
        showOnlyPanel(room_details_panel);
    }

    @FXML
    void onDashBoard() {
        showOnlyPanel(dash_board_panel);
    }

    @FXML
    void onBooking() {
        showOnlyPanel(room_panel);
    }

    private void showOnlyPanel(AnchorPane visiblePanel) {
        for (AnchorPane panel : panels) {
            panel.setVisible(panel == visiblePanel);
        }
    }

    @FXML
    private void addRoom() {
        try {
            var roomNumber = room_number_field.getText();
            var roomType = hotel_category.getValue();
            var roomStatus = hotel_status.getValue();

            if (roomNumber.isEmpty() || roomStatus == null || roomType == null) {
                showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Vui lòng điền đầy đủ thông tin phòng.");
                return;
            }

            String roomID = room_number_field.getText();
            if (findRoom(roomID) != null) {
                showAlert(Alert.AlertType.ERROR, "Phòng đã tồn tại", "Phòng với số này đã tồn tại.");
                return;
            }

            var newRoom = BookingAdapter.getRentable(roomType, roomStatus, price(roomType), roomNumber);
            Booking booking = new Booking(newRoom, null); // Tạo booking mới

            // Lưu booking vào danh sách
            bookingList.add(booking);

            // Lưu thông tin phòng vào cơ sở dữ liệu
            DatabaseRentable.saveRentable(newRoom); // Gọi hàm lưu vào DB

            updateRoomCounts();
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phòng mới đã được thêm.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void editRoom() {
        try {
            if (selectedBooking == null) {
                showAlert(Alert.AlertType.ERROR, "Không chọn phòng", "Vui lòng chọn phòng để chỉnh sửa.");
                return;
            }

            if (edit_btn.getText().equals("Edit")) {
                enableRoomFields(true);
                edit_btn.setText("Done");
            } else {
                if (validateRoomDetails()) {
                    // Cập nhật thông tin phòng
                    selectedBooking.getRentable().setNumber(room_number_field.getText());
                    selectedBooking.getRentable().setType(hotel_category.getValue());
                    selectedBooking.getRentable().setStatus(hotel_status.getValue());

                    // Cập nhật thông tin lên cơ sở dữ liệu
                    DatabaseRentable.updateRentable(selectedBooking.getRentable());

                    bookingListView.refresh();
                    updateRoomCounts();

                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Chi tiết phòng được cập nhật thành công.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Chi tiết không hợp lệ", "Vui lòng cung cấp chi tiết phòng hợp lệ.");
                }
                edit_btn.setText("Edit");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void removeRoom() {
        Booking chosen = bookingListView.getSelectionModel().getSelectedItem();
        Booking booking = findRoom(chosen.getRentable().getNumber());
        if (booking != null) {
            // Lưu số phòng để xóa từ cơ sở dữ liệu
            String roomNumber = booking.getRentable().getNumber();

            // Xóa phòng khỏi danh sách
            bookingList.remove(booking);

            // Xóa thông tin phòng từ cơ sở dữ liệu
            DatabaseRentable.removeRentable(roomNumber);

            updateRoomCounts();
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phòng đã bị xóa.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Thao tác không thành công.");
        }
    }


    @FXML
    private void checkIn() {
        try {
            Booking booking = user_book_list.getSelectionModel().getSelectedItem();

            if (booking != null) {
                System.out.println("A");
            } else {
                System.out.println("B");
            }

            if (booking == null || booking.getRentable().getStatus() != RentableStatus.Available) {
                showAlert(Alert.AlertType.ERROR, "Phòng không có sẵn", "Phòng này không có sẵn để check-in.");
                return;
            }

            String name = name_field.getText();
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Thiếu thông tin khách hàng", "Vui lòng nhập đủ thông tin khách hàng.");
                return;
            }

            int idNumber = Integer.parseInt(id_field.getText());
            // Kiểm tra xem khách hàng đã có phòng chưa
            if (customerExistsInDatabase(idNumber)) {
                showAlert(Alert.AlertType.ERROR, "Khách hàng đã có phòng", "Khách hàng này đã có phòng.");
                return;
            }

            long money = Long.parseLong(submitted_money.getText());
            IDProof idValue = id_box.getValue();
            int quantity = stringToNumber(renter_quantity.getValue()).intValue();
            int nights = stringToNumber(total_night_box.getValue()).intValue();
            Country country = citizen_box.getValue();
            boolean hasKids = kid_btn.isSelected();

            // Tính toán tổng chi phí dựa trên số đêm, số lượng, và điều kiện
            long totalPrice = calculateTotalCost(booking.getRentable().getType(), nights, quantity, hasKids);

            // Kiểm tra xem khách hàng có đủ tiền không
            if (money < totalPrice) {
                showAlert(Alert.AlertType.ERROR, "Không đủ tiền", "Khách hàng không đủ tiền để check-in.");
                return;
            }

            // Tạo đối tượng Customer mới
            Customer newCustomer = new Customer(name, idValue, idNumber, quantity, nights, country, money, hasKids);
            saveCustomerToDatabase(newCustomer); // Lưu thông tin vào cơ sở dữ liệu

            DatabaseRentable.updateRentableCustomer(booking.getRentable(), newCustomer.getIdNumber());

            // Cập nhật trạng thái phòng và danh sách đặt phòng
            booking.getRentable().setStatus(RentableStatus.Occupied);

            // Cập nhật trạng thái trong cơ sở dữ liệu
            DatabaseRentable.updateRentable(booking.getRentable());

            bookingList.remove(booking);
            bookingList.add(new Booking(booking.getRentable(), newCustomer));

            updateRoomCounts();
            bookingListView.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Khách hàng đã được check-in thành công.");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Thông tin không hợp lệ", "Vui lòng kiểm tra ID hoặc tiền đã nhập.");
        }
    }




    // Method to check if a customer exists in the database
    private boolean customerExistsInDatabase(int idNumber) {
        String query = "SELECT COUNT(*) FROM customers WHERE idNumber = ?";
        try (Connection connection = DatabaseCustomer.connect(); // Use your DatabaseCustomer class
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if count is greater than 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to save a customer to the database
    private void saveCustomerToDatabase(Customer customer) {
        String query = "INSERT INTO customers (name, idProof, idNumber, quantity, night, country, money, hasKids) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseCustomer.connect(); // Use your DatabaseCustomer class
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getIdProof().toString()); // Assuming you have a toString method for IDProof
            pstmt.setInt(3, customer.getIdNumber());
            pstmt.setInt(4, customer.getQuantity());
            pstmt.setInt(5, customer.getNight());
            pstmt.setString(6, customer.getCountry().toString()); // Assuming you have a toString method for Country
            pstmt.setLong(7, customer.getMoney());
            pstmt.setBoolean(8, customer.getKid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Có lỗi xảy ra khi lưu thông tin khách hàng vào cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }




    @FXML
    private void checkOut() {
        try {
            Booking booking = user_book_list.getSelectionModel().getSelectedItem();

            if (booking != null) {
                int idNumber = booking.getCustomer().getIdNumber();  // Lấy idNumber từ đối tượng Booking

                // Tải toàn bộ danh sách khách hàng từ cơ sở dữ liệu
                List<Customer> customers = DatabaseCustomer.loadCustomer();

                // Tìm khách hàng với idNumber phù hợp
                boolean customerFound = false;
                for (Customer customer : customers) {
                    if (customer.getIdNumber() == idNumber) {
                        customerFound = true;
                        break;
                    }
                }

                if (customerFound) {
                    // Cập nhật trạng thái phòng và danh sách đặt phòng
                    booking.getRentable().setStatus(RentableStatus.Available);

                    // Cập nhật trạng thái phòng trong cơ sở dữ liệu
                    DatabaseRentable.updateRentable(booking.getRentable());

                    // Xóa idNumber của phòng trong cơ sở dữ liệu (đặt lại thành null)
                    DatabaseRentable.updateRentableCustomer(booking.getRentable(), null);

                    // Xóa thông tin khách hàng khỏi cơ sở dữ liệu
                    DatabaseCustomer.deleteCustomer(idNumber);

                    bookingListView.refresh();
                    updateRoomCounts();
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phòng đã được trả và thông tin khách hàng đã được xóa.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Khách hàng không tồn tại trong hệ thống.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Phòng trống", "Phòng này hiện đang trống.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi trong quá trình trả phòng.");
        }
    }




    private void enableRoomFields(boolean enable) {
        room_number_field.setDisable(!enable);
        hotel_category.setDisable(!enable);
        hotel_status.setDisable(!enable);
    }

    private boolean validateRoomDetails() {
        return !room_number_field.getText().isEmpty() && hotel_category.getValue() != null && hotel_status.getValue() != null;
    }

    private void updateRoomCounts() {
        long availableRooms = bookingList.stream()
            .filter(booking -> booking != null && booking.getRentable().getStatus() == RentableStatus.Available)
            .count();
        total_rooms.setText(String.valueOf(bookingList.size()));
        valid_rooms.setText(String.valueOf(availableRooms));
        setupUserBookList();
    }

    private Booking findRoom(String roomID) {
        for (Booking booking : bookingList) {
            if (booking.getRentable().getNumber().equals(roomID)) {
                return booking;
            }
        }
        return null;
    }

    // Total cost based on the number of nights, 15% increment for each subsequent rental and 5% when u have a kid
    public long calculateTotalCost(RentableType type, int nights, int quantity, boolean hasKids) {
        double totalCost = price(type) * nights; // Giá phòng nhân với số đêm

        // Tăng giá cho mỗi lần thuê thêm
        for (int i = 1; i < quantity; i++) {
            totalCost *= 1.1; // Tăng 10% cho mỗi lần thuê
        }

        // Tăng giá nếu có trẻ em
        if (hasKids) {
            totalCost *= 1.05; // Tăng 5% khi có trẻ em
        }

        return Math.round(totalCost); // Làm tròn giá trị
    }
/*
    private List<Booking> loadRentable() {
        var rooms = data.load(DATA);
        if (rooms == null) {
            return new ArrayList<>();
        }
        return rooms;
    }

    private void saveRentable() {
        data.save(bookingList, DATA);
    }
*/
/*    private List<Customer> loadCustomer() {
        var customers = customer.load(USER);
        if (customers == null) {
            return new ArrayList<>();
        }
        return customers;
    }*/
}
