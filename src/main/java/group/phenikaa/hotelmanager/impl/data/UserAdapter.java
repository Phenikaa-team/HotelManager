package group.phenikaa.hotelmanager.impl.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import group.phenikaa.hotelmanager.api.info.impl.customer.User;

import java.io.IOException;

public class UserAdapter extends TypeAdapter<User> {

    @Override
    public void write(JsonWriter out, User user) throws IOException {
        out.beginObject();

        out.name("username").value(user.getUsername());
        out.name("password").value(user.getPassword());

        out.endObject();
    }

    @Override
    public User read(JsonReader in) throws IOException {
        User user = new User();

        in.beginObject();

        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "username":
                    user.setUsername(in.nextString());
                    break;
                case "password":
                    user.setPassword(in.nextString());
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();

        return user;
    }
}
