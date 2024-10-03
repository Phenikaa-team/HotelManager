package group.phenikaa.hotelmanager.impl.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import group.phenikaa.hotelmanager.api.info.impl.customer.User;
import group.phenikaa.hotelmanager.api.utility.interfaces.IDataStorage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginData implements IDataStorage<User> {
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter())
            .setPrettyPrinting().create();

    @Override
    public void save(List<User> list, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(list, writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> load(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File not found: " + fileName);
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type bookingListType = TypeToken.getParameterized(List.class, User.class).getType();
            return gson.fromJson(reader, bookingListType);
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
