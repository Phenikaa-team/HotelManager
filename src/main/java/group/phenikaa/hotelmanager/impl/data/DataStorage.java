package group.phenikaa.hotelmanager.impl.data;

import group.phenikaa.hotelmanager.api.manager.Booking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
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
        try (Reader reader = new FileReader(filename)) {
            Type roomListType = new TypeToken<List<Booking>>(){}.getType();
            return gson.fromJson(reader, roomListType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

