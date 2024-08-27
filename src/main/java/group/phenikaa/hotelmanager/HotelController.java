package group.phenikaa.hotelmanager;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.manager.BookingManager;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
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
    private final ComboBox<RentableStatus> availableEnumBox;

    // AbstractRenter
    private final TextField customerNameField;

    private ObservableList<Booking> roomList;
    private BookingManager bookingManager;

    public HotelController(GridPane gridPane) {
        roomListView = (ListView<Booking>) gridPane.lookup("#roomListView");
        roomNumberField = (TextField) gridPane.lookup("#roomNumberField");
        availableEnumBox = (ComboBox<RentableStatus>) gridPane.lookup("#availableEnumBox");
        customerNameField = (TextField) gridPane.lookup("#customerNameField");

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
        availableEnumBox.setItems(FXCollections.observableArrayList(RentableStatus.values()));

        availableEnumBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            enableCustomerFields(newValue == RentableStatus.Empty);
        });
    }

    private void enableCustomerFields(boolean enable) {
        customerNameField.setDisable(!enable);
    }

    private void addRoom() {
        var roomNumber = roomNumberField.getText();
        var isAvailable = availableEnumBox.getSelectionModel().getSelectedItem();

        if (roomNumber.isEmpty() || isAvailable == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }
        //var newRoom = new Booking(new AbstractRentable(isAvailable), null);
        //roomList.add(newRoom);
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


        if (roomNumber.isEmpty() || customerName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        var room = bookingManager.findRoomByNumber(roomNumber);
        if (room == null || room.renter() != null) {
            showAlert(Alert.AlertType.ERROR, "Error", "AbstractRentable not found or already booked.");
            return;
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

        if (room.renter() != null) {
            bookingManager.checkoutRoom(roomNumber);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Checked out successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No customer information for this abstractRentable.");
        }
    }

    private void updateRoomDetails(Booking booking) {
        if (booking != null) {
            roomNumberField.setText(booking.rentable().generateRentalCode().toString());
            availableEnumBox.setValue(booking.rentable().rentableStatus());
            if (booking.renter() != null) {
                customerNameField.setText(booking.renter().name());
            } else {
                customerNameField.clear();
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
