package group.phenikaa.hotelmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class HotelApplication extends Application {
    private static Stage primaryStage;
    private static double xOffset = 0;
    private static double yOffset = 0;
    public static String DATA = "data.json";
    public static String USER = "user.json";

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(HotelApplication.class.getResourceAsStream("/assets/textures/logo.png"))));

        var scene1 = loadScene("start-view.fxml", 400, 285);
        dragScene(scene1);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Hotel Manager");
        stage.setScene(scene1);
        stage.show();
    }

    public static void switchToMenuScene() throws IOException {
        var scene = loadScene("menu-view.fxml", 890, 510);
        dragScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
    }

    public static void switchToLoginScene() throws IOException {
        var scene = loadScene("start-view.fxml", 400, 285);
        dragScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
    }

    private static Scene loadScene(String fxmlPath, int width, int height) throws IOException {
        var fxmlLoader = new FXMLLoader(HotelApplication.class.getResource(fxmlPath));
        var scene = new Scene(fxmlLoader.load(), width, height);
        scene.setFill(Color.TRANSPARENT);
        return scene;
    }

    private static void dragScene(Scene scene) {
        scene.setOnMousePressed(event -> {
            xOffset = event.getScreenX() - primaryStage.getX();
            yOffset = event.getScreenY() - primaryStage.getY();
        });

        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
