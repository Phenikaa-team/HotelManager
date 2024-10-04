package group.phenikaa.hotelmanager.impl.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.utility.interfaces.IDataStorage;
import group.phenikaa.hotelmanager.impl.data.adapter.BookingAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookingData implements IDataStorage<Booking> {
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Booking.class, new BookingAdapter())
            .setPrettyPrinting().create();

    @Override
    public void save(List<Booking> bookings, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(bookings, writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Booking> load(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File not found: " + fileName);
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type bookingListType = TypeToken.getParameterized(List.class, Booking.class).getType();
            return gson.fromJson(reader, bookingListType);
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

