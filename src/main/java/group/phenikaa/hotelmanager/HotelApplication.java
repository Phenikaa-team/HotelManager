package group.phenikaa.hotelmanager;

import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
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
        ComboBox<RentableStatus> availableEnumBox = new ComboBox<>();
        availableEnumBox.setId("availableEnumBox");
        TextField customerNameField = new TextField();
        customerNameField.setId("customerNameField");

        Button addRoomButton = new Button("Add Rentable");
        addRoomButton.setId("addRoomButton");
        Button deleteRoomButton = new Button("Delete Rentable");
        deleteRoomButton.setId("deleteRoomButton");
        Button bookRoomButton = new Button("Book Rentable");
        bookRoomButton.setId("bookRoomButton");
        Button checkOutButton = new Button("Check Out");
        checkOutButton.setId("checkOutButton");

        availableEnumBox.getItems().addAll(RentableStatus.values());

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(15);
        gridPane.setVgap(15);

        gridPane.add(new Label("Rentable Number:"), 1, 0);
        gridPane.add(roomNumberField, 2, 0);

        gridPane.add(new Label("Available:"), 1, 1);
        gridPane.add(availableEnumBox, 2, 1);

        gridPane.add(new Label("Renter Name:"), 1, 2);
        gridPane.add(customerNameField, 2, 2);

        HBox buttonBox = new HBox(10, addRoomButton, deleteRoomButton, bookRoomButton, checkOutButton);
        gridPane.add(buttonBox, 1, 3, 2, 1);

        gridPane.add(new Label("Rentable List"), 0, 0);
        gridPane.add(roomListView, 0, 1, 1, 3);

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
