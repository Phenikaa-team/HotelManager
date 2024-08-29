package group.phenikaa.hotelmanager;

import com.google.common.eventbus.EventBus;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import group.phenikaa.hotelmanager.api.utility.enums.RenterType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HotelApplication extends Application {
    public static EventBus EVENT_BUS = new EventBus();
    private Scene scene2;

    public void register(Control control, String name) {
        control.setId(name);
    }

    @Override
    public void start(Stage stage) {
        // Scene 1
        var label = new Label("Welcome to Hotel Manager application");
        var button = new Button("Go to control center.");
        button.setOnAction(event -> {
            stage.setScene(scene2);
        });

        var layout1 = new VBox(10);
        layout1.getChildren().addAll(label, button);
        layout1.setAlignment(Pos.CENTER);
        layout1.setPadding(new Insets(10));
        var scene1 = new Scene(layout1, 300, 200);

        // Scene 2
        ListView<Booking> roomListView = new ListView<>();
        register(roomListView, "roomListView");

        ComboBox<RentableType> rentableEnumBox = new ComboBox<>();
        register(rentableEnumBox, "rentableEnumBox");

        ComboBox<RentableStatus> statusEnumBox = new ComboBox<>();
        register(statusEnumBox, "statusEnumBox");

        ComboBox<RenterType> renterEnumBox = new ComboBox<>();
        register(renterEnumBox, "renterEnumBox");

        TextField customerNameField = new TextField();
        register(customerNameField, "customerNameField");

        var addRoomButton = new Button("Add Rentable");
        register(addRoomButton, "addRoomButton");
        var deleteRoomButton = new Button("Delete Rentable");
        register(deleteRoomButton, "deleteRoomButton");
        var bookRoomButton = new Button("Book Rentable");
        register(bookRoomButton, "bookRoomButton");
        var checkOutButton = new Button("Check Out");
        register(checkOutButton, "checkOutButton");

        rentableEnumBox.getItems().addAll(RentableType.values());
        statusEnumBox.getItems().addAll(RentableStatus.values());
        renterEnumBox.getItems().addAll(RenterType.values());

        var gridPane = new GridPane(15, 15);
        gridPane.setPadding(new Insets(20));

        gridPane.add(new Label("Rentable Type:"), 1, 0);
        gridPane.add(rentableEnumBox, 2, 0);

        gridPane.add(new Label("Rentable Status:"), 1, 1);
        gridPane.add(statusEnumBox, 2, 1);

        gridPane.add(new Label("Renter Type:"), 1, 2);
        gridPane.add(renterEnumBox, 2, 2);

        gridPane.add(new Label("Renter Name:"), 1, 3);
        gridPane.add(customerNameField, 2, 3);

        HBox buttonBox = new HBox(10, addRoomButton, deleteRoomButton, bookRoomButton, checkOutButton);
        gridPane.add(buttonBox, 1, 4, 2, 1);

        gridPane.add(new Label("Rentable List"), 0, 0);
        gridPane.add(roomListView, 0, 1, 1, 4);

        HotelController controller = new HotelController(gridPane);
        controller.initialize();

        scene2 = new Scene(gridPane, 800, 400);
        stage.setTitle("Hotel Manager");
        stage.setScene(scene1);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
