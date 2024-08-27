package group.phenikaa.hotelmanager.impl.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import group.phenikaa.hotelmanager.api.info.Booking;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Booking.class, new BookingAdapter())
            .setPrettyPrinting()
            .create();

    public static void saveData(List<Booking> rooms, String filename) {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(rooms, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Booking> loadData(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File not found: " + filename);
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type roomListType = new TypeToken<List<Booking>>(){}.getType();
            return gson.fromJson(reader, roomListType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
