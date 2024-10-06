package group.phenikaa.hotelmanager.impl.data.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;

import java.io.IOException;

//TODO: Done this
public class CustomerAdapter extends TypeAdapter<Customer> {
    @Override
    public void write(JsonWriter out, Customer value) throws IOException {
        out.beginObject();


        out.endObject();
    }

    @Override
    public Customer read(JsonReader in) throws IOException {
        in.beginObject();


        in.endObject();
        return null;
    }
}
