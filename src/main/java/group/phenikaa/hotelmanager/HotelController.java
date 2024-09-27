package group.phenikaa.hotelmanager;

import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;
import group.phenikaa.hotelmanager.api.utility.enums.*;
import group.phenikaa.hotelmanager.impl.data.BookingAdapter;
import group.phenikaa.hotelmanager.impl.data.DataStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class HotelController implements Initializable {

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
    @FXML private Button find_btn;

    //Room(booking)
    @FXML private TextField first_name_field;
    @FXML private TextField id_field;
    @FXML private TextField last_name_field;
    @FXML private TextField submitted_money;

    @FXML private ComboBox<Country> citizen_box;
    @FXML private ComboBox<IDProof> id_box;
    @FXML private ComboBox<Number> tenants_box;
    @FXML private ComboBox<Number> total_night_box;

    // Window control buttons
    @FXML private Button exit_btn;
    @FXML private Button minimize_btn;

    public HotelController() {
        HotelApplication.EVENT_BUS.register(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        panels = Arrays.asList(room_details_panel, dash_board_panel, room_panel);

        List<Booking> list = loadRentable();
        bookingList = FXCollections.observableArrayList(list);
        if (bookingListView != null) {
            bookingListView.setItems(bookingList);

            bookingListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Booking booking, boolean empty) {
                    super.updateItem(booking, empty);
                    if (empty || booking == null) {
                        setText(null);
                    } else {
                        String roomInfo = "ID: " + booking.rentable().getID() + " | "
                                + "Type: " + booking.rentable().getName() + " | "
                                + "Status: " + booking.rentable().getStatus() + " | "
                                + "Price: " + booking.rentable().getPrice();
                        setText(roomInfo);
                    }
                }
            });
        }

        citizen_box.setItems(FXCollections.observableArrayList(Country.values()));
        id_box.setItems(FXCollections.observableArrayList(IDProof.values()));
        tenants_box.setItems(FXCollections.observableArrayList(rangeList(1, 8)));
        total_night_box.setItems(FXCollections.observableArrayList(rangeList(1, 62)));
        hotel_category.setItems(FXCollections.observableArrayList(RentableType.values()));
        hotel_status.setItems(FXCollections.observableArrayList(RentableStatus.values()));

        total_rooms.setText(String.valueOf(bookingList.size()));
        int i = 0;
        for (Booking booking : bookingList) {
            if (booking.rentable().getStatus() == RentableStatus.Available) {
                i++;
            }
        }
        valid_rooms.setText(String.valueOf(i));
    }

    private Integer[] rangeList(int start, int end) {
        return java.util.stream.IntStream.rangeClosed(start, end).boxed().toArray(Integer[]::new);
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
        String roomNumber = room_number_field.getText();
        RentableType roomType = hotel_category.getValue();
        RentableStatus roomStatus = hotel_status.getValue();

        if (roomNumber.isEmpty() || roomStatus == null || roomType == null) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Vui lòng điền đầy đủ thông tin phòng.");
            return;
        }

        if (findBookingByRoom() != null) {
            showAlert(Alert.AlertType.ERROR, "Phòng đã tồn tại", "Phòng với số này đã tồn tại.");
            return;
        }

        long price = switch(roomType) {
            case Single -> 150000;
            case Double -> 200000;
            case Family -> 250000;
            case Suite -> 600000;
            case Deluxe -> 650000;
            case PresidentialSuite -> 700000;
        };

        AbstractRentable newRoom = BookingAdapter.getRentable(roomType, roomStatus, price, roomNumber);
        bookingList.add(new Booking(newRoom, null));

        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phòng mới đã được thêm.");
    }


    @FXML
    private void checkIn() {
        String roomNumber = room_number_field.getText();
        Booking booking = findBookingByRoom();

        if (booking != null) {
            showAlert(Alert.AlertType.ERROR, "Phòng đang có người", "Phòng này đã được đặt.");
            return;
        }

        String firstName = first_name_field.getText();
        String lastName = last_name_field.getText();
        if (firstName.isEmpty() || lastName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin khách hàng", "Vui lòng nhập đủ thông tin khách hàng.");
            return;
        }

        AbstractRentable rentable = BookingAdapter.getRentable(hotel_category.getValue(), hotel_status.getValue(), 0, roomNumber);
        AbstractRenter renter = BookingAdapter.getRenter(RenterType.Individual, firstName + " " + lastName);
        bookingList.add(new Booking(rentable, renter));

        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Khách hàng đã được đặt phòng.");
    }

    @FXML
    private void checkOut() {
        Booking booking = findBookingByRoom();
        if (booking != null) {
            bookingList.remove(booking);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phòng đã được trả.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Phòng trống", "Phòng này hiện đang trống.");
        }
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
        var rooms = DataStorage.loadData("data.json");
        if (rooms == null) {
            return new ArrayList<>();
        }
        return rooms;
    }

    private void saveRentable() {
        DataStorage.saveData(bookingList, "data.json");
    }

    public static void showAlert(Alert.AlertType type, String title, String message) {
        var alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
