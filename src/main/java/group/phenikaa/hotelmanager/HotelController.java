package group.phenikaa.hotelmanager;

import group.phenikaa.hotelmanager.api.manager.Booking;
import group.phenikaa.hotelmanager.api.manager.Customer;
import group.phenikaa.hotelmanager.api.manager.Room;
import group.phenikaa.hotelmanager.api.utility.enums.Gender;
import group.phenikaa.hotelmanager.api.utility.enums.RoomStatus;
import group.phenikaa.hotelmanager.impl.data.DataStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class HotelController {
    // Room
    @FXML private ListView<Booking> roomListView;
    @FXML private TextField roomNumberField;
    @FXML private ComboBox<RoomStatus> availableEnumBox;

    // Customer
    @FXML private TextField customerNameField;
    @FXML private TextField customerPhoneNumberField;
    @FXML private ComboBox<Gender> categoryEnumBox;

    private ObservableList<Booking> roomList;

    public void initialize() {
        roomList = FXCollections.observableArrayList(loadRooms());
        roomListView.setItems(roomList);

        categoryEnumBox.setItems(FXCollections.observableArrayList(Gender.values()));
        availableEnumBox.setItems(FXCollections.observableArrayList(RoomStatus.values()));

        customerPhoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                customerPhoneNumberField.setText(oldValue);
            }
        });

        // Chỉ khi phòng trống thì mới được nhập thông tin khách hàng.
        availableEnumBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            enableCustomerFields(newValue == RoomStatus.EMPTY);
        });
    }

    private void enableCustomerFields(boolean enable) {
        customerNameField.setDisable(!enable);
        customerPhoneNumberField.setDisable(!enable);
        categoryEnumBox.setDisable(!enable);
    }

    @FXML
    private void addRoom() {
        var roomNumber = roomNumberField.getText();
        var isAvailable = availableEnumBox.getSelectionModel().getSelectedItem();
        if (isAvailable != RoomStatus.EMPTY) {
            // Không cho phép thêm thông tin khách hàng nếu phòng không ở trạng thái EMPTY.
            var newRoom = new Booking(new Room(isAvailable, roomNumber), null);
            roomList.add(newRoom);
        } else {
            var customerName = customerNameField.getText();
            var phoneNumber = Integer.parseInt(customerPhoneNumberField.getText());
            var customerGender = categoryEnumBox.getSelectionModel().getSelectedItem();

            var newRoom = new Booking(new Room(isAvailable, roomNumber), new Customer(customerName, phoneNumber, customerGender, roomNumber));
            roomList.add(newRoom);
        }
        saveRooms();
    }

    @FXML
    private void deleteRoom() {
        var selectedRoom = roomListView.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            roomList.remove(selectedRoom);
            saveRooms();

            roomListView.refresh();
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
}