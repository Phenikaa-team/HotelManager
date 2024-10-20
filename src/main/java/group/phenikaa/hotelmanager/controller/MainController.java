package group.phenikaa.hotelmanager.controller;

import group.phenikaa.hotelmanager.asm.database.CustomerDatabase;
import group.phenikaa.hotelmanager.asm.database.RentableDatabase;

import group.phenikaa.hotelmanager.HotelApplication;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.info.impl.customer.Session;
import group.phenikaa.hotelmanager.api.info.impl.customer.User;
import group.phenikaa.hotelmanager.api.utility.enums.*;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

import static group.phenikaa.hotelmanager.api.utility.Utility.*;

public class MainController implements Initializable {
    private Booking selectedBooking = null;

    // ListView for bookings
    @FXML
    private ListView<Booking> bookingListView;
    @FXML
    private ListView<Booking> user_book_list;
    @FXML
    private ListView<Booking> customer_list_view;

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
    @FXML
    private AnchorPane customer_details;

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

    //Room(Customer)
    @FXML
    private TextField search_bar;
    @FXML
    private Button search_btn;
    @FXML
    private Button checkout_btn;
    @FXML
    private Button remove_btn2;

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
            panels = Arrays.asList(room_details_panel, dash_board_panel, room_panel, customer_details);

            List<Booking> list = RentableDatabase.loadRentable();
            bookingList = FXCollections.observableArrayList(list);

            if (bookingListView != null) {
                setupListView();
            }

            setupCustomerListView();
            setupUserBookList();

            bookingListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateRoomDetails(newSelection);
                    edit_btn.setText("Edit");
                }
            });

            setupComboBoxItems();
            updateRoomCounts();
            setUpComboboxInfo();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupComboBoxItems() {
        citizen_box.setItems(FXCollections.observableArrayList(Country.values()));
        id_box.setItems(FXCollections.observableArrayList(IDProof.values()));
        renter_quantity.setItems(FXCollections.observableArrayList(convertIntegerListToString(rangeList(1, 12))));
        total_night_box.setItems(FXCollections.observableArrayList(convertIntegerListToString(rangeList(1, 62))));
        hotel_category.setItems(FXCollections.observableArrayList(RentableType.values()));
        hotel_status.setItems(FXCollections.observableArrayList(RentableStatus.values()));
        filter_box.setItems(FXCollections.observableArrayList(RentableStatus.values()));
        filter_box.setAccessibleText("Filter");
    }

    private void setUpComboboxInfo() {
        setComboBoxCellFactory(id_box, IDProof::name, IDProof::url);
        setComboBoxButtonCell(id_box, IDProof::name);

        setComboBoxCellFactory(hotel_category, RentableType::name, RentableType::url);
        setComboBoxButtonCell(hotel_category, RentableType::name);

        setComboBoxCellFactory(hotel_status, RentableStatus::name, RentableStatus::url);
        setComboBoxButtonCell(hotel_status, RentableStatus::name);
    }

    private <T> void setComboBoxCellFactory(ComboBox<T> comboBox, Function<T, String> nameExtractor, Function<T, String> urlExtractor) {
        comboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(nameExtractor.apply(item));
                    Tooltip tooltip = new Tooltip(urlExtractor.apply(item));
                    setTooltip(tooltip);
                }
            }
        });
    }

    private void populateRoomDetails(Booking booking) {
        selectedBooking = booking;
        room_number_field.setText(booking.getRentable().getNumber());
        hotel_category.setValue(booking.getRentable().getType());
        hotel_status.setValue(booking.getRentable().getStatus());
    }

    private <T> void setComboBoxButtonCell(ComboBox<T> comboBox, Function<T, String> nameExtractor) {
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(nameExtractor.apply(item));
                }
            }
        });
    }

    // TODO: Thêm BookingDataBase để nó lưu được thông tin của cả phòng lần người dùng đã thuê nó
    private void setupCustomerListView() {
        List<Booking> bookingsWithCustomers = RentableDatabase.loadRentableWithCustomers();

        ObservableList<Booking> observableBookings = FXCollections.observableArrayList(
                bookingsWithCustomers.stream()
                        .filter(booking -> booking.getCustomer() != null)
                        .toList()
        );

        customer_list_view.setItems(observableBookings);
        customer_list_view.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Booking booking, boolean empty) {
                super.updateItem(booking, empty);
                if (empty || booking == null || booking.getCustomer() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    var hbox = new HBox(20);

                    // Room Number
                    var roomNumberLabel = new Label(booking.getRentable().getNumber());
                    roomNumberLabel.setPrefWidth(80);

                    // Type
                    var typeLabel = new Label(booking.getRentable().getType().name());
                    typeLabel.setPrefWidth(180);

                    // Renter Name
                    Customer customer = booking.getCustomer();
                    String renterName = customer.getName();
                    var renterNameLabel = new Label(renterName);
                    renterNameLabel.setPrefWidth(200);

                    // Renter ID
                    String renterID = String.valueOf(customer.getIdNumber());
                    var renterIDLabel = new Label(renterID);
                    renterIDLabel.setPrefWidth(80);

                    hbox.getChildren().addAll(roomNumberLabel, typeLabel, renterNameLabel, renterIDLabel);
                    setGraphic(hbox);
                }
            }
        });
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
    private void searchCustomer() {
        String searchID = search_bar.getText().trim();
        if (!searchID.isEmpty()) {
            for (Booking booking : bookingList) {
                if (String.valueOf(booking.getCustomer().getIdNumber()).equals(searchID)) {
                    customer_list_view.getSelectionModel().select(booking);
                    return;
                }
            }
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "No customer found with the given ID.");
        }
    }

    @FXML
    void getEnterScene() throws IOException {
        HotelApplication.switchToLoginScene();
    }

    @FXML
    void onExit() {
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
    void onCustomerDetails() {
        showOnlyPanel(customer_details);
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
                showAlert(Alert.AlertType.ERROR, "Missing information", "Please fill in the room information.");
                return;
            }

            String roomID = room_number_field.getText();
            if (findRoom(roomID) != null) {
                showAlert(Alert.AlertType.ERROR, "The room already exists", "The room with this number already exists.");
                return;
            }

            var newRoom = getRentable(roomType, roomStatus, price(roomType), roomNumber);
            Booking booking = new Booking(newRoom, null);

            bookingList.add(booking);
            RentableDatabase.saveRentable(newRoom);

            updateRoomCounts();
            setupCustomerListView();
            setupUserBookList();
            setupComboBoxItems();
            updateRoomCounts();
            setUpComboboxInfo();
            showAlert(Alert.AlertType.INFORMATION, "Success", "A new room has been added.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editRoom() {
        try {
            if (selectedBooking == null) {
                showAlert(Alert.AlertType.ERROR, "Do not select a room", "Please select a room to edit.");
                return;
            }

            if (edit_btn.getText().equals("Edit")) {
                edit_btn.setText("Done");
            } else {
                if (validateRoomDetails()) {
                    selectedBooking.getRentable().setNumber(room_number_field.getText());
                    selectedBooking.getRentable().setType(hotel_category.getValue());
                    selectedBooking.getRentable().setStatus(hotel_status.getValue());

                    RentableDatabase.updateRentable(selectedBooking.getRentable());

                    bookingListView.refresh();
                    setupCustomerListView();
                    setupUserBookList();
                    setupComboBoxItems();
                    updateRoomCounts();
                    setUpComboboxInfo();
                    updateRoomCounts();

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Room details updated successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Invalid details", "Please provide valid room details.");
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
            String roomNumber = booking.getRentable().getNumber();
            bookingList.remove(booking);
            RentableDatabase.removeRentable(roomNumber);

            updateRoomCounts();
            setupCustomerListView();
            setupUserBookList();
            setupComboBoxItems();
            updateRoomCounts();
            setUpComboboxInfo();
            showAlert(Alert.AlertType.INFORMATION, "Success", "The room has been deleted.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Operation failed.");
        }
    }

    @FXML
    private void checkIn() {
        try {
            Booking booking = user_book_list.getSelectionModel().getSelectedItem();

            if (booking == null || booking.getRentable().getStatus() != RentableStatus.Available) {
                showAlert(Alert.AlertType.ERROR, "The room is not available", "This room is not available for check-in.");
                return;
            }

            String name = name_field.getText();
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Missing customer information", "Please enter enough customer information.");
                return;
            }

            int idNumber = Integer.parseInt(id_field.getText());
            if (customerExistsInDatabase(idNumber)) {
                showAlert(Alert.AlertType.ERROR, "The customer has a room", "This customer has a room.");
                return;
            }

            long money = Long.parseLong(submitted_money.getText());
            IDProof idValue = id_box.getValue();
            int quantity = stringToNumber(renter_quantity.getValue()).intValue();
            int nights = stringToNumber(total_night_box.getValue()).intValue();
            Country country = citizen_box.getValue();
            boolean hasKids = kid_btn.isSelected();

            long totalPrice = calculateTotalCost(booking.getRentable().getType(), nights, quantity, hasKids);

            if (money < totalPrice) {
                showAlert(Alert.AlertType.ERROR, "Not enough money", "Customers don't have enough money to check-in.");
                return;
            }

            Customer newCustomer = new Customer(name, idValue, idNumber, quantity, nights, country, money, hasKids);
            saveCustomerToDatabase(newCustomer);
            RentableDatabase.updateRentableCustomer(booking.getRentable(), newCustomer.getIdNumber());
            booking.getRentable().setStatus(RentableStatus.Occupied);
            RentableDatabase.updateRentable(booking.getRentable());

            bookingList.remove(booking);
            var newBooking = new Booking(booking.getRentable(), newCustomer); // Update booking info from null customer to new customer
            bookingList.add(newBooking);

            updateRoomCounts();
            bookingListView.refresh();
            setupCustomerListView();
            setupUserBookList();
            setupComboBoxItems();
            updateRoomCounts();
            setUpComboboxInfo();
            showAlert(Alert.AlertType.INFORMATION, "Success", "The customer has been successfully checked-in.");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid information", "Please check the ID or entered funds.");
        }
    }

    private boolean customerExistsInDatabase(int idNumber) {
        String query = "SELECT COUNT(*) FROM customers WHERE idNumber = ?";
        try (Connection connection = CustomerDatabase.connect() ;
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveCustomerToDatabase(Customer customer) {
        String query = "INSERT INTO customers (name, idProof, idNumber, quantity, night, country, money, hasKids) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = CustomerDatabase.connect() ;
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getIdProof().toString());
            pstmt.setInt(3, customer.getIdNumber());
            pstmt.setInt(4, customer.getQuantity());
            pstmt.setInt(5, customer.getNight());
            pstmt.setString(6, customer.getCountry().toString());
            pstmt.setLong(7, customer.getMoney());
            pstmt.setBoolean(8, customer.getKid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database error", "An error occurred while saving customer information to the database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void checkOut() {
        Booking selectedBooking = customer_list_view.getSelectionModel().getSelectedItem();

        if (selectedBooking != null) {
            // Lấy thông tin khách hàng
            Customer customer = selectedBooking.getCustomer();

            // Cập nhật trạng thái phòng
            selectedBooking.getRentable().setStatus(RentableStatus.Available);
            RentableDatabase.updateRentable(selectedBooking.getRentable());

            // Xóa booking khỏi danh sách nhưng giữ khách hàng
            bookingList.remove(selectedBooking);
            RentableDatabase.updateRentableCustomer(selectedBooking.getRentable(), null);

            // Không xóa khách hàng khỏi database, chỉ thực hiện cập nhật cần thiết khác

            // Cập nhật giao diện
            bookingListView.refresh();
            setupCustomerListView();
            setupUserBookList();
            setupComboBoxItems();
            updateRoomCounts();
            setUpComboboxInfo();
            updateRoomCounts();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Checkout completed successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "No selection", "Please select a customer to check out.");
        }
    }


    @FXML
    private void removeBooking() {
        Booking selectedBooking = customer_list_view.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            String roomNumber = selectedBooking.getRentable().getNumber();
            int customerId = selectedBooking.getCustomer().getIdNumber();

            // Xóa thông tin phòng từ cơ sở dữ liệu
            RentableDatabase.removeRentable(roomNumber);

            // Xóa thông tin khách hàng từ cơ sở dữ liệu
            CustomerDatabase.deleteCustomer(customerId);

            // Xóa booking khỏi danh sách
            bookingList.remove(selectedBooking);

            // Cập nhật giao diện
            bookingListView.refresh();
            setupCustomerListView();
            setupUserBookList();
            setupComboBoxItems();
            updateRoomCounts();
            setUpComboboxInfo();
            updateRoomCounts();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Booking and customer have been removed.");
        } else {
            showAlert(Alert.AlertType.ERROR, "No selection", "Please select a booking to remove.");
        }
    }


    private boolean validateRoomDetails() {
        return !room_number_field.getText().isEmpty() && hotel_category.getValue() != null && hotel_status.getValue() != null;
    }

    private void updateRoomCounts() {
        int totalRoomsCount = bookingList.size();
        int validRoomsCount = (int) bookingList.stream().filter(booking -> booking.getRentable().getStatus() == RentableStatus.Available).count();
        total_rooms.setText(String.valueOf(totalRoomsCount));
        valid_rooms.setText(String.valueOf(validRoomsCount));
    }

    private Booking findRoom(String roomID) {
        for (Booking booking : bookingList) {
            if (booking.getRentable().getNumber().equals(roomID)) {
                return booking;
            }
        }
        return null;
    }
}
