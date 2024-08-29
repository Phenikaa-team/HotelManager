package group.phenikaa.hotelmanager;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;
import group.phenikaa.hotelmanager.api.manager.BookingManager;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import group.phenikaa.hotelmanager.api.utility.enums.RenterType;
import group.phenikaa.hotelmanager.impl.data.DataStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

import static group.phenikaa.hotelmanager.impl.data.BookingAdapter.getRentable;
import static group.phenikaa.hotelmanager.impl.data.BookingAdapter.getRenter;

public class HotelController {

    // Rentable
    private final ComboBox<RentableType> rentableEnumBox;
    private final ListView<Booking> bookingListView;
    private final ComboBox<RentableStatus> statusEnumBox;

    // Renter
    private final ComboBox<RenterType> renterEnumBox;
    private final TextField customerNameField;
    private ObservableList<Booking> bookingList;
    private BookingManager bookingManager;

    public HotelController(GridPane gridPane) {
        rentableEnumBox = (ComboBox<RentableType>) gridPane.lookup("#rentableEnumBox");
        bookingListView = (ListView<Booking>) gridPane.lookup("#roomListView");
        statusEnumBox = (ComboBox<RentableStatus>) gridPane.lookup("#statusEnumBox");
        renterEnumBox = (ComboBox<RenterType>) gridPane.lookup("#renterEnumBox");
        customerNameField = (TextField) gridPane.lookup("#customerNameField");

        var addRoomButton = (Button) gridPane.lookup("#addRoomButton");
        var deleteRoomButton = (Button) gridPane.lookup("#deleteRoomButton");
        var bookRoomButton = (Button) gridPane.lookup("#bookRoomButton");
        var checkOutButton = (Button) gridPane.lookup("#checkOutButton");

        addRoomButton.setOnAction(event -> runWithExceptionHandling(this::addRentable));
        deleteRoomButton.setOnAction(event -> runWithExceptionHandling(this::deleteRoom));
        bookRoomButton.setOnAction(event -> runWithExceptionHandling(this::bookRentable));
        checkOutButton.setOnAction(event -> runWithExceptionHandling(this::checkOut));

        bookingListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateRoomDetails(newValue));
    }

    public void initialize() {
        List<Booking> list = loadRentable();
        bookingManager = new BookingManager(list);
        bookingList = FXCollections.observableArrayList(list);
        bookingListView.setItems(bookingList);

        // Set items cho ComboBoxes
        statusEnumBox.setItems(FXCollections.observableArrayList(RentableStatus.values()));

        statusEnumBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            enableCustomerFields(newValue == RentableStatus.Empty);
        });
    }

    private void runWithExceptionHandling(RunnableWithException runnable) {
        try {
            runnable.run();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void enableCustomerFields(boolean enable) {
        customerNameField.setDisable(!enable);
    }

    private void addRentable() {
        var renterId = renterEnumBox.getSelectionModel().getSelectedItem().getRenterId();
        var rentableType = rentableEnumBox.getSelectionModel().getSelectedItem();

        if (rentableType == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        var rentable = getRentable(rentableEnumBox.getSelectionModel().getSelectedItem(), statusEnumBox.getSelectionModel().getSelectedItem(), 100);
        var renter = getRenter(renterEnumBox.getSelectionModel().getSelectedItem());
        var newRoom = new Booking(rentable, renter, renterId);
        bookingList.add(newRoom);
        saveRentable();
    }

    private void deleteRoom() {
        var selectedRentable = bookingListView.getSelectionModel().getSelectedItem();
        if (selectedRentable != null) {
            bookingList.remove(selectedRentable);
            saveRentable();
            bookingListView.refresh();
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Rentable selected.");
        }
    }

    private void bookRentable() {
        var renterId = renterEnumBox.getSelectionModel().getSelectedItem().getRenterId();
        var customerName = customerNameField.getText();

        if (customerName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        var room = bookingManager.findRoomByNumber(renterId);
        if (room == null || room.renter() != null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Rentable not found or already booked.");
        }
    }

    private void checkOut() {
        var renterId = renterEnumBox.getSelectionModel().getSelectedItem().getRenterId();

        var room = bookingManager.findRoomByNumber(renterId);
        if (room == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Rentable not found.");
            return;
        }

        if (room.renter() != null) {
            bookingManager.checkoutRoom(renterId);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Checked out successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No customer information for this Rentable.");
        }
    }

    private void updateRoomDetails(Booking booking) {
        if (booking != null) {
            statusEnumBox.setValue(booking.rentable().rentableStatus());
            if (booking.renter() != null) {
                customerNameField.setText(booking.renter().label());
            } else {
                customerNameField.clear();
            }
        }
    }

    private List<Booking> loadRentable() {
        var rooms = DataStorage.loadData("data.json");
        if (rooms == null) {
            return new ArrayList<>();
        }
        return rooms;
    }

    private void saveRentable() {
        DataStorage.saveData(new ArrayList<>(bookingList), "data.json");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        var alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FunctionalInterface
    private interface RunnableWithException {
        void run() throws InstantiationException, IllegalAccessException;
    }
}
