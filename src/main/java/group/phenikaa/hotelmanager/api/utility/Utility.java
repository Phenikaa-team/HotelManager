package group.phenikaa.hotelmanager.api.utility;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.impl.rentable.*;
import group.phenikaa.hotelmanager.api.info.impl.rentable.Double;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
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
                return java.lang.Double.parseDouble(str);
            } else {
                // Trường hợp số nguyên (Integer hoặc Long)
                return Long.parseLong(str);
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Input string is not a valid number: " + str);
        }
    }

    public static AbstractRentable getRentable(RentableType type, RentableStatus status, long price, String id) {
        return switch (type) {
            case Double -> new Double(status, price, id);
            case Suite -> new Suite(status, price, id);
            case Single -> new Single(status, price, id);
            case Deluxe -> new Deluxe(status, price, id);
            case Family -> new Family(status, price, id);
        };
    }

    public static List<String> convertIntegerListToString(Integer[] array) {
        List<String> resultList = new ArrayList<>();
        for (Integer num : array) {
            resultList.add(num.toString());
        }
        return resultList;
    }


    // Total cost based on the number of nights, 10% increment for each subsequent rental and 5% when u have a kid
    public static long calculateTotalCost(RentableType type, int nights, int quantity, boolean hasKids) {
        double totalCost = price(type) * nights; // Room rate multiplied by the number of nights

        // Price increase for each additional rental
        for (int i = 1; i < quantity; i++) {
            totalCost *= 1.10; // 10% increase per rental
        }

        // Price increase if there are children
        if (hasKids) {
            totalCost *= 1.05; // 5% increase in the presence of children
        }

        return Math.round(totalCost);
    }
}
