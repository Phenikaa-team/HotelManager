package group.phenikaa.hotelmanager.api.utility;

import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
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

    public static long price(RentableType type) {
        return switch(type) {
            case Single -> 150000;
            case Double -> 200000;
            case Family -> 250000;
            case Suite -> 600000;
            case Deluxe -> 650000;
        };
    }

    public static Number stringToNumber(String str) throws NumberFormatException {
        try {
            if (str.contains(".")) {
                // Trường hợp số thập phân (Double)
                return Double.parseDouble(str);
            } else {
                // Trường hợp số nguyên (Integer hoặc Long)
                return Long.parseLong(str);
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Input string is not a valid number: " + str);
        }
    }

    public static List<String> convertIntegerListToString(Integer[] array) {
        List<String> resultList = new ArrayList<>();
        for (Integer num : array) {
            resultList.add(num.toString());
        }
        return resultList;
    }
}
