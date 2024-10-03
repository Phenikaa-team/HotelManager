package group.phenikaa.hotelmanager;

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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static group.phenikaa.hotelmanager.HotelApplication.DATA;
import static group.phenikaa.hotelmanager.api.utility.Utility.rangeList;
import static group.phenikaa.hotelmanager.api.utility.Utility.showAlert;

public class HotelController implements Initializable {
    private final BookingData data = new BookingData();

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
        citizen_box.setItems(FXCollections.observableArrayList(Country.values()));
        id_box.setItems(FXCollections.observableArrayList(IDProof.values()));
        tenants_box.setItems(FXCollections.observableArrayList(rangeList(1, 12)));
        total_night_box.setItems(FXCollections.observableArrayList(rangeList(1, 62)));
        hotel_category.setItems(FXCollections.observableArrayList(RentableType.values()));
        hotel_status.setItems(FXCollections.observableArrayList(RentableStatus.values()));

        total_rooms.setText(String.valueOf(bookingList.size()));
        int availableRooms = (int) bookingList.stream()
                .filter(booking -> booking.rentable().getStatus() == RentableStatus.Available)
                .count();
        valid_rooms.setText(String.valueOf(availableRooms));
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
                    int index = getIndex() + 1;
                    var hbox = new HBox(10);
                    var vbox = new VBox(5);
                    var indexLabel = new Label(index + ".");
                    indexLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                    var roomInfoLabel = new Label("Room ID: " + booking.rentable().getID());
                    var typeLabel = new Label("Type: " + booking.rentable().getName());
                    var statusLabel = new Label("Status: " + booking.rentable().getStatus());
                    var priceLabel = new Label("Price: " + booking.rentable().getPrice() + " VND");
                    vbox.getChildren().addAll(roomInfoLabel, typeLabel, statusLabel, priceLabel);
                    hbox.getChildren().addAll(indexLabel, vbox);
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

        long price = switch(roomType) {
            case Single -> 150000;
            case Double -> 200000;
            case Family -> 250000;
            case Suite -> 600000;
            case Deluxe -> 650000;
            case PresidentialSuite -> 700000;
        };

        var newRoom = BookingAdapter.getRentable(roomType, roomStatus, price, roomNumber);
        bookingList.add(new Booking(newRoom, null));

        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phòng mới đã được thêm.");
    }

    @FXML
    private void checkIn() {
        var roomNumber = room_number_field.getText();
        var booking = findBookingByRoom();

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
            AbstractRentable rentable = BookingAdapter.getRentable(hotel_category.getValue(), hotel_status.getValue(), renter.calculateTotalCost(), roomNumber);
            bookingList.add(new Booking(rentable, renter));

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
