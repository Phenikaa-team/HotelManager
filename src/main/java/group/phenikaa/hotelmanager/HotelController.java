package group.phenikaa.hotelmanager;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.manager.BookingManager;
import group.phenikaa.hotelmanager.api.utility.enums.Gender;
import group.phenikaa.hotelmanager.api.utility.enums.RoomStatus;
import group.phenikaa.hotelmanager.impl.data.DataStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class HotelController {

    // AbstractRentable
    private final ListView<Booking> roomListView;
    private final TextField roomNumberField;
    private final ComboBox<RoomStatus> availableEnumBox;

    // AbstractRenter
    private final TextField customerNameField;
    private final TextField customerPhoneNumberField;
    private final ComboBox<Gender> categoryEnumBox;

    private ObservableList<Booking> roomList;
    private BookingManager bookingManager;

    public HotelController(GridPane gridPane) {
        roomListView = (ListView<Booking>) gridPane.lookup("#roomListView");
        roomNumberField = (TextField) gridPane.lookup("#roomNumberField");
        availableEnumBox = (ComboBox<RoomStatus>) gridPane.lookup("#availableEnumBox");
        customerNameField = (TextField) gridPane.lookup("#customerNameField");
        customerPhoneNumberField = (TextField) gridPane.lookup("#customerPhoneNumberField");
        categoryEnumBox = (ComboBox<Gender>) gridPane.lookup("#categoryEnumBox");

        var addRoomButton = (Button) gridPane.lookup("#addRoomButton");
        var deleteRoomButton = (Button) gridPane.lookup("#deleteRoomButton");
        var bookRoomButton = (Button) gridPane.lookup("#bookRoomButton");
        var checkOutButton = (Button) gridPane.lookup("#checkOutButton");

        addRoomButton.setOnAction(event -> addRoom());
        deleteRoomButton.setOnAction(event -> deleteRoom());
        bookRoomButton.setOnAction(event -> bookRoom());
        checkOutButton.setOnAction(event -> checkOut());

        roomListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateRoomDetails(newValue));

        initialize();
    }

    public void initialize() {
        List<Booking> rooms = loadRooms();
        bookingManager = new BookingManager(rooms);
        roomList = FXCollections.observableArrayList(rooms);
        roomListView.setItems(roomList);

        // Set items cho ComboBoxes
        categoryEnumBox.setItems(FXCollections.observableArrayList(Gender.values()));
        availableEnumBox.setItems(FXCollections.observableArrayList(RoomStatus.values()));

        customerPhoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                customerPhoneNumberField.setText(oldValue);
            }
        });

        availableEnumBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            enableCustomerFields(newValue == RoomStatus.Empty);
        });
    }

    private void enableCustomerFields(boolean enable) {
        customerNameField.setDisable(!enable);
        customerPhoneNumberField.setDisable(!enable);
        categoryEnumBox.setDisable(!enable);
    }

    private void addRoom() {
        var roomNumber = roomNumberField.getText();
        var isAvailable = availableEnumBox.getSelectionModel().getSelectedItem();

        if (roomNumber.isEmpty() || isAvailable == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }
        var newRoom = new Booking(new AbstractRentable(isAvailable, roomNumber), null);
        roomList.add(newRoom);
        saveRooms();
    }

    private void deleteRoom() {
        var selectedRoom = roomListView.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            roomList.remove(selectedRoom);
            saveRooms();
            roomListView.refresh();
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "No abstractRentable selected.");
        }
    }

    private void bookRoom() {
        var roomNumber = roomNumberField.getText();
        var customerName = customerNameField.getText();
        var phoneNumber = customerPhoneNumberField.getText();
        var gender = categoryEnumBox.getValue();

        if (roomNumber.isEmpty() || customerName.isEmpty() || phoneNumber.isEmpty() || gender == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        var room = bookingManager.findRoomByNumber(roomNumber);
        if (room == null || room.customer() != null) {
            showAlert(Alert.AlertType.ERROR, "Error", "AbstractRentable not found or already booked.");
            return;
        }

        try {
            long phone = Long.parseLong(phoneNumber);
            bookingManager.bookRoom(roomNumber, new AbstractRenter(customerName, phone, gender, roomNumber));
            showAlert(Alert.AlertType.INFORMATION, "Success", "AbstractRentable booked successfully.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid phone number.");
        }
    }

    private void checkOut() {
        var roomNumber = roomNumberField.getText();

        if (roomNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter abstractRentable number.");
            return;
        }

        var room = bookingManager.findRoomByNumber(roomNumber);
        if (room == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "AbstractRentable not found.");
            return;
        }

        if (room.customer() != null) {
            bookingManager.checkoutRoom(roomNumber);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Checked out successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No customer information for this abstractRentable.");
        }
    }

    private void updateRoomDetails(Booking booking) {
        if (booking != null) {
            roomNumberField.setText(booking.abstractRentable().roomNumber());
            availableEnumBox.setValue(booking.abstractRentable().roomStatus());
            if (booking.customer() != null) {
                customerNameField.setText(booking.customer().name());
                customerPhoneNumberField.setText(String.valueOf(booking.customer().phoneNumber()));
                categoryEnumBox.setValue(booking.customer().gender());
            } else {
                customerNameField.clear();
                customerPhoneNumberField.clear();
                categoryEnumBox.setValue(null);
            }
        }
    }

    private List<Booking> loadRooms() {
        var rooms = DataStorage.loadData("rooms.json");
        if (rooms == null) {
            return new ArrayList<>();
        }
        return rooms;
    }

    private void saveRooms() {
        DataStorage.saveData(new ArrayList<>(roomList), "rooms.json");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        var alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
