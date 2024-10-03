package group.phenikaa.hotelmanager.api.utility;

import javafx.scene.control.Alert;

import java.util.stream.IntStream;

public class Utility {

    public static void showAlert(Alert.AlertType type, String title, String message) {
        var alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Integer[] rangeList(int start, int end) {
        return IntStream.rangeClosed(start, end).boxed().toArray(Integer[]::new);
    }

}
