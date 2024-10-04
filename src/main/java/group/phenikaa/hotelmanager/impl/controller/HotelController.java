package group.phenikaa.hotelmanager.impl.controller;

import group.phenikaa.hotelmanager.HotelApplication;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.info.impl.customer.Session;
import group.phenikaa.hotelmanager.api.info.impl.customer.User;
import group.phenikaa.hotelmanager.api.utility.enums.*;
import group.phenikaa.hotelmanager.impl.data.BookingAdapter;
import group.phenikaa.hotelmanager.impl.data.BookingData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static group.phenikaa.hotelmanager.HotelApplication.DATA;
import static group.phenikaa.hotelmanager.api.utility.Utility.*;

public class HotelController implements Initializable {
    private final BookingData data = new BookingData();
    private Booking selectedBooking = null;

    // ListView for bookings
    @FXML private ListView<Booking> bookingListView;
    // ObservableList for bookings
    private ObservableList<Booking> bookingList;

    // Buttons
    @FXML private Button dashboard_btn;
    @FXML private Button room_details_btn;
    @FXML private Button rooms_btn;

    // Panels
    private List<AnchorPane> panels;

    @FXML private AnchorPane room_details_panel;
    @FXML private AnchorPane dash_board_panel;
    @FXML private AnchorPane room_panel;

    //Dashboard
    @FXML private Text total_rooms;
    @FXML private Text valid_rooms;

    //Room(details)
    @FXML private ComboBox<RentableType> hotel_category;
    @FXML private ComboBox<RentableStatus> hotel_status;
    @FXML private TextField room_number_field;
    @FXML private Button add_btn;
    @FXML private Button edit_btn;

    //Room(booking)
    @FXML private TextField name_field;
    @FXML private TextField id_field;
    @FXML private TextField submitted_money;

    @FXML private ComboBox<Country> citizen_box;
    @FXML private ComboBox<IDProof> id_box;
    @FXML private ComboBox<Number> tenants_box;
    @FXML private ComboBox<Number> total_night_box;

    @FXML private CheckBox kid_btn;

    // Window control buttons
    @FXML private Button exit_btn;
    @FXML private Button minimize_btn;
    @FXML private Text user_name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            User user = Session.getCurrentUser();
            if (user != null) {
                user_name.setText(user.getUsername());
            }
            panels = Arrays.asList(room_details_panel, dash_board_panel, room_panel);

            List<Booking> list = loadRentable();
            bookingList = FXCollections.observableArrayList(list);
            if (bookingListView != null) {
                setupListView();
            }
            bookingListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateRoomDetails(newSelection);
                    edit_btn.setText("Edit");
                }
            });
            citizen_box.setItems(FXCollections.observableArrayList(Country.values()));
            id_box.setItems(FXCollections.observableArrayList(IDProof.values()));
            tenants_box.setItems(FXCollections.observableArrayList(rangeList(1, 12)));
            total_night_box.setItems(FXCollections.observableArrayList(rangeList(1, 62)));
            hotel_category.setItems(FXCollections.observableArrayList(RentableType.values()));
            hotel_status.setItems(FXCollections.observableArrayList(RentableStatus.values()));

            updateRoomCounts();
            edit_btn.setText("Edit");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateRoomDetails(Booking booking) {
        selectedBooking = booking;
        room_number_field.setText(booking.rentable().getID());
        hotel_category.setValue(booking.rentable().getName());
        hotel_status.setValue(booking.rentable().getStatus());
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
                    var roomNumberLabel = new Label(booking.rentable().getID());
                    roomNumberLabel.setPrefWidth(85);

                    // Type
                    var typeLabel = new Label(booking.rentable().getName().name());
                    typeLabel.setPrefWidth(80);

                    // Status
                    var statusLabel = new Label(booking.rentable().getStatus().toString());
                    statusLabel.setPrefWidth(230);

                    // Price
                    var priceLabel = new Label(booking.rentable().getPrice() + " VND");
                    priceLabel.setPrefWidth(100);

                    hbox.getChildren().addAll(roomNumberLabel, typeLabel, statusLabel, priceLabel);

                    setGraphic(hbox);
                }
            }
        });
    }

    @FXML
    void onExit() {
        saveRentable();
        System.exit(0);
    }

    @FXML
    void onMinimize() {
        var stage = (Stage) minimize_btn.getScene().getWindow();
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

            if (findBookingByRoom() != null) {
                showAlert(Alert.AlertType.ERROR, "Phòng đã tồn tại", "Phòng với số này đã tồn tại.");
                return;
            }

            var newRoom = BookingAdapter.getRentable(roomType, roomStatus, price(roomType), roomNumber);
            bookingList.add(new Booking(newRoom, null));
            updateRoomCounts();

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phòng mới đã được thêm.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editRoom() {
        try {
            if (selectedBooking == null) {
                showAlert(Alert.AlertType.ERROR, "No Room Selected", "Please select a room to edit.");
                return;
            }

            if (edit_btn.getText().equals("Edit")) {
                enableRoomFields(true);
                edit_btn.setText("Done");
            } else {
                if (validateRoomDetails()) {
                    selectedBooking.rentable().setID(room_number_field.getText());
                    selectedBooking.rentable().setName(hotel_category.getValue());
                    selectedBooking.rentable().setStatus(hotel_status.getValue());

                    bookingListView.refresh();
                    updateRoomCounts();

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Room details updated successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Invalid Details", "Please provide valid room details.");
                }
                //enableRoomFields(false);
                edit_btn.setText("Edit");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeRoom() {
        Booking booking = findBookingByRoom();
        if (booking != null) {
            bookingList.remove(booking);
            updateRoomCounts();
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phòng đã bị xóa.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Thao tác không thành công.");
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

    @FXML
    private void checkIn() {
        var roomNumber = room_number_field.getText();
        var booking = findBookingByRoom();
        var type = hotel_category.getValue();
        if (booking != null) {
            showAlert(Alert.AlertType.ERROR, "Phòng đang có người", "Phòng này đã được đặt.");
            return;
        }

        String name = name_field.getText();
        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin khách hàng", "Vui lòng nhập đủ thông tin khách hàng.");
            return;
        }

        try {
            int idNumber = Integer.parseInt(id_field.getText());
            long money = Long.parseLong(submitted_money.getText());

            Customer renter = new Customer(name, id_box.getValue(), idNumber, tenants_box.getValue().intValue(), total_night_box.getValue().intValue(), citizen_box.getValue(), money, kid_btn.isSelected());
            AbstractRentable rentable = BookingAdapter.getRentable(hotel_category.getValue(), hotel_status.getValue(), renter.calculateTotalCost(type), roomNumber);
            bookingList.add(new Booking(rentable, renter));
            updateRoomCounts();

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Khách hàng đã được đặt phòng.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Thông tin không hợp lệ", "Vui lòng kiểm tra ID hoặc tiền đã nhập.");
        }
    }

    @FXML
    void getEnterScene() throws IOException {
        HotelApplication.switchToLoginScene();
    }

    @FXML
    private void checkOut() {
        try {
            Booking booking = findBookingByRoom();
            if (booking != null) {
                booking.rentable().setStatus(RentableStatus.Available);
                bookingListView.refresh();
                updateRoomCounts();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phòng đã được trả.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Phòng trống", "Phòng này hiện đang trống.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateRoomCounts() {
        total_rooms.setText(String.valueOf(bookingList.size()));
        int availableRooms = (int) bookingList.stream()
                .filter(booking -> booking.rentable().getStatus() == RentableStatus.Available)
                .count();
        valid_rooms.setText(String.valueOf(availableRooms));
    }

    private Booking findBookingByRoom() {
        String roomID = room_number_field.getText();
        for (Booking booking : bookingList) {
            if (booking.rentable().getID().equals(roomID)) {
                return booking;
            }
        }
        return null;
    }

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
}
