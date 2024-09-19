package group.phenikaa.hotelmanager.impl.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.impl.rentable.*;
import group.phenikaa.hotelmanager.api.info.impl.rentable.Double;
import group.phenikaa.hotelmanager.api.info.impl.renter.Household;
import group.phenikaa.hotelmanager.api.info.impl.renter.Individual;
import group.phenikaa.hotelmanager.api.info.impl.renter.CorporateClient;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import group.phenikaa.hotelmanager.api.utility.enums.RenterType;

import java.io.IOException;

public class BookingAdapter extends TypeAdapter<Booking> {

    @Override
    public void write(JsonWriter out, Booking booking) throws IOException {
        var rentableName = booking.rentable().getClass().getSimpleName();
        var rentableType = RentableType.valueOf(rentableName.replaceAll("s$", ""));
        var rentableStatus = booking.rentable().getStatus().name();
        var renterType = RenterType.valueOf(booking.renter().getLabel().toUpperCase());
        var renterName = renterType.name();
       // var rentableID = booking.rentable().getUniqueID();

        out.beginObject();
        out.name("RentableType").value(rentableType.name());
        out.name("RentableStatus").value(rentableStatus);
        out.name("RenterType").value(renterType.name());
        out.name("RenterName").value(renterName);
        //out.name("RentableID").value(rentableID);
        out.endObject();
    }

    @Override
    public Booking read(JsonReader in) throws IOException {
        in.beginObject();
        var status = RentableStatus.Available;
        var rentableType = RentableType.Single;
        var rentType = RenterType.Household;
        var rentId = 0L;
        var price = 0;
        var name = "";
        AbstractRenter renter = null;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "RentableType":
                    rentableType = RentableType.valueOf(in.nextString());
                    System.out.println("RentableType: " + rentableType);
                    break;
                case "RentableStatus":
                    status = RentableStatus.valueOf(in.nextString());
                    break;
                case "RenterType":
                    rentType = RenterType.valueOf(in.nextString().toUpperCase());
                    renter = getRenter(rentType, name);
                    System.out.println("RenterType: " + rentType);
                    break;
                case "RenterName":
                    if (renter != null) {
                        name = in.nextString();
                        renter.setName(name);
                        System.out.println("RenterName: " + name);
                    }
                    break;
                case "RentableID":
                    if (in.peek() == JsonToken.NUMBER) {
                        rentId = in.nextLong();
                    } else {
                        rentId = Long.parseLong(in.nextString());
                    }
                    break;
            }
        }
        in.endObject();
        return new Booking(getRentable(rentableType, status, price, String.valueOf(rentId)), renter);
    }

    public static AbstractRentable getRentable(RentableType type, RentableStatus status, long price, String id) {
        return switch (type) {
            case Double -> new Double(status, price, id);
            case Suite -> new Suite(status, price, id);
            case Single -> new Single(status, price, id);
            case Deluxe -> new Deluxe(status, price, id);
            case Family -> new Family(status, price, id);
            case PresidentialSuite -> new PresidentialSuite(status, price, id);
            case null, default -> throw new IllegalArgumentException("Unknown rentable type: " + type);
        };
    }

    public static AbstractRenter getRenter(RenterType type, String name) {
        return switch (type) {
            case Individual -> new Individual(name);
            case Household -> new Household(name);
            case CorporateClient -> new CorporateClient(name);
            case null, default -> throw new IllegalArgumentException("Unknown renter type: " + type);
        };
    }
}

