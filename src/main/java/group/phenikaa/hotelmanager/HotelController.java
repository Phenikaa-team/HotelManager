package group.phenikaa.hotelmanager;

import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import group.phenikaa.hotelmanager.impl.data.DataStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class HotelController implements Initializable {

    // ListView for bookings
    @FXML private ListView<Booking> bookingListView;

    // Buttons
    @FXML private Button info_btn;
    @FXML private Button dashboard_btn;
    @FXML private Button room_details_btn;
    @FXML private Button rooms_btn;

    // Panels
    private List<AnchorPane> panels;

    @FXML private AnchorPane basic_info_panel;
    @FXML private AnchorPane dash_board_panel;
    @FXML private AnchorPane room_panel;

    //Basic
    @FXML private TextField hotel_name_field;
    @FXML private ComboBox<RentableType> hotel_category;
    @FXML private ComboBox<RentableStatus> hotel_status;
    @FXML private TextField room_number_field;
    @FXML private Button add_btn;
    @FXML private Button find_btn;

    // Window control buttons
    @FXML private Button exit_btn;
    @FXML private Button minimize_btn;

    // ObservableList for bookings
    private ObservableList<Booking> bookingList;

    public HotelController() {
        HotelApplication.EVENT_BUS.register(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        panels = Arrays.asList(basic_info_panel, dash_board_panel, room_panel);

        List<Booking> list = loadRentable();
        bookingList = FXCollections.observableArrayList(list);
        if (bookingListView != null) {
            bookingListView.setItems(bookingList);
        }

        hotel_category.setItems(FXCollections.observableArrayList(RentableType.values()));
        hotel_status.setItems(FXCollections.observableArrayList(RentableStatus.values()));
    }

    @FXML
    void onExit() {
        saveRentable();
        System.exit(0);
    }

    @FXML
    void onMinimize() {
        Stage stage = (Stage) minimize_btn.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void onBasic() {
        showOnlyPanel(basic_info_panel);
    }

    @FXML
    void onDashBoard() {
        showOnlyPanel(dash_board_panel);
    }

    @FXML
    void onRoom() {
        showOnlyPanel(room_panel);
    }

    private void showOnlyPanel(AnchorPane visiblePanel) {
        for (AnchorPane panel : panels) {
            panel.setVisible(panel == visiblePanel);
        }
    }

    @FXML
    private void checkIn() {
        Booking booking = findBookingByRoom();
        if (booking != null) {
            showAlert(Alert.AlertType.ERROR, "Room Occupied", "This room is already occupied.");
        } else {
            bookingList.add(new Booking(null, null));
            showAlert(Alert.AlertType.INFORMATION, "Check-in Successful", "Guest has been checked in.");
        }
    }

    @FXML
    private void checkOut() {
        Booking booking = findBookingByRoom();
        if (booking != null) {
            bookingList.remove(booking);
            showAlert(Alert.AlertType.INFORMATION, "Check-out Successful", "Guest has been checked out.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Room Vacant", "This room is already vacant.");
        }
    }

    private Booking findBookingByRoom() {
        for (Booking booking : bookingList) {
            if (booking.rentable().getID().equals(room_number_field.getText())) {
                return booking;
            }
        }
        return null;
    }

    private List<Booking> loadRentable() {
        var rooms = DataStorage.loadData("data.json");
        if (rooms == null) {
            return new ArrayList<>();
        }
        return rooms;
    }

    private void saveRentable() {
        DataStorage.saveData(bookingList, "data.json");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        var alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
