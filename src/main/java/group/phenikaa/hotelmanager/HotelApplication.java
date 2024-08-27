package group.phenikaa.hotelmanager;

import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.utility.enums.Gender;
import group.phenikaa.hotelmanager.api.utility.enums.RoomStatus;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HotelApplication extends Application {

    @Override
    public void start(Stage stage) {
        ListView<Booking> roomListView = new ListView<>();
        roomListView.setId("roomListView");
        TextField roomNumberField = new TextField();
        roomNumberField.setId("roomNumberField");
        ComboBox<RoomStatus> availableEnumBox = new ComboBox<>();
        availableEnumBox.setId("availableEnumBox");
        TextField customerNameField = new TextField();
        customerNameField.setId("customerNameField");
        TextField customerPhoneNumberField = new TextField();
        customerPhoneNumberField.setId("customerPhoneNumberField");
        ComboBox<Gender> categoryEnumBox = new ComboBox<>();
        categoryEnumBox.setId("categoryEnumBox");

        Button addRoomButton = new Button("Add AbstractRentable");
        addRoomButton.setId("addRoomButton");
        Button deleteRoomButton = new Button("Delete AbstractRentable");
        deleteRoomButton.setId("deleteRoomButton");
        Button bookRoomButton = new Button("Book AbstractRentable");
        bookRoomButton.setId("bookRoomButton");
        Button checkOutButton = new Button("Check Out");
        checkOutButton.setId("checkOutButton");

        availableEnumBox.getItems().addAll(RoomStatus.values());
        categoryEnumBox.getItems().addAll(Gender.values());

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(15);
        gridPane.setVgap(15);

        gridPane.add(new Label("AbstractRentable Number:"), 1, 0);
        gridPane.add(roomNumberField, 2, 0);

        gridPane.add(new Label("Available:"), 1, 1);
        gridPane.add(availableEnumBox, 2, 1);

        gridPane.add(new Label("AbstractRenter Name:"), 1, 2);
        gridPane.add(customerNameField, 2, 2);

        gridPane.add(new Label("Phone Number:"), 1, 3);
        gridPane.add(customerPhoneNumberField, 2, 3);

        gridPane.add(new Label("Gender:"), 1, 4);
        gridPane.add(categoryEnumBox, 2, 4);

        HBox buttonBox = new HBox(10, addRoomButton, deleteRoomButton, bookRoomButton, checkOutButton);
        gridPane.add(buttonBox, 1, 5, 2, 1);

        gridPane.add(new Label("AbstractRentable List"), 0, 0);
        gridPane.add(roomListView, 0, 1, 1, 5);

        HotelController controller = new HotelController(gridPane);
        controller.initialize();

        Scene scene = new Scene(gridPane, 800, 400);
        stage.setTitle("Hotel Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
