package group.phenikaa.hotelmanager.impl.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import group.phenikaa.hotelmanager.api.manager.Booking;
import group.phenikaa.hotelmanager.api.manager.Customer;
import group.phenikaa.hotelmanager.api.manager.Room;
import group.phenikaa.hotelmanager.api.utility.enums.Gender;
import group.phenikaa.hotelmanager.api.utility.enums.RoomStatus;

import java.io.IOException;
import java.util.Locale;

public class BookingAdapter extends TypeAdapter<Booking> {
    @Override
    public void write(JsonWriter out, Booking room) throws IOException {
        out.beginObject();
        out.name("available").value(room.room().roomStatus().name());
        out.name("roomNumber").value(room.room().roomNum());
        out.name("customerName").value(room.customer().name());
        out.name("phoneNumber").value(room.customer().phoneNumber());
        out.name("customerGender").value(room.customer().gender().name());
        out.name("sync-roomNumber").value(room.customer().roomNum());
        out.endObject();
    }

    @Override
    public Booking read(JsonReader in) throws IOException {
        in.beginObject();
        var isAvailable = RoomStatus.EMPTY;
        String roomNumber = null;
        var customerName = "None";
        var phoneNumber = 1234567890L;
        var customerGender = Gender.UNDETERMINED;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "available":
                    isAvailable = RoomStatus.valueOf(in.nextString().toUpperCase(Locale.ROOT));
                    break;
                case "roomNumber", "sync-roomNumber":
                    roomNumber = in.nextString();
                    break;
                case "customerName":
                    customerName = in.nextString();
                    break;
                case "phoneNumber":
                    phoneNumber = in.nextLong();
                    break;
                case "customerGender":
                    customerGender = Gender.valueOf(in.nextString().toUpperCase(Locale.ROOT));
                    break;
            }
        }
        in.endObject();
        return new Booking(new Room(isAvailable, roomNumber), new Customer(customerName, phoneNumber, customerGender, roomNumber));
    }
}

