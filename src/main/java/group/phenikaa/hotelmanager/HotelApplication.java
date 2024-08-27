package group.phenikaa.hotelmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HotelApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RoomManager.fxml"));
        primaryStage.setScene(new Scene(loader.load(), 320, 240));
        primaryStage.setTitle("Quản lý phòng khách sạn");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}