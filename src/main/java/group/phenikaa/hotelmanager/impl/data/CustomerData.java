package group.phenikaa.hotelmanager.impl.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.utility.interfaces.IDataStorage;
import group.phenikaa.hotelmanager.impl.data.adapter.BookingAdapter;
import group.phenikaa.hotelmanager.impl.data.adapter.CustomerAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// TODO: Done this
public class CustomerData implements IDataStorage<Customer> {
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Customer.class, new CustomerAdapter())
            .setPrettyPrinting().create();

    @Override
    public void save(List<Customer> list, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(list, writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> load(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File not found: " + fileName);
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type customerData = TypeToken.getParameterized(List.class, Customer.class).getType();
            return gson.fromJson(reader, customerData);
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
