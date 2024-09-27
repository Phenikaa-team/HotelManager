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
        var rentableID = booking.rentable().getID();
        var rentablePrice = booking.rentable().getPrice();

        String renterTypeName = "NONE";
        String renterName = "N/A";

        if (booking.renter() != null) {
            var renterType = RenterType.valueOf(booking.renter().getLabel().toUpperCase());
            renterTypeName = renterType.name();
            renterName = booking.renter().getName();
        }

        out.beginObject();
        out.name("RentableType").value(rentableType.name());
        out.name("RentableStatus").value(rentableStatus);
        out.name("RentableID").value(rentableID);
        out.name("RentablePrice").value(rentablePrice);
        out.name("RenterType").value(renterTypeName);
        out.name("RenterName").value(renterName);
        out.endObject();
    }

    @Override
    public Booking read(JsonReader in) throws IOException {
        in.beginObject();
        var status = RentableStatus.Available;
        var rentableType = RentableType.Single;
        var rentType = RenterType.Household;
        var rentId = "00x0";
        var price = 0L;
        var name = "";
        AbstractRenter renter = null;

        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "RentableType":
                    rentableType = RentableType.valueOf(in.nextString());
                    break;
                case "RentableStatus":
                    status = RentableStatus.valueOf(in.nextString());
                    break;
                case "RenterType":
                    String renterTypeStr = in.nextString();
                    if (!"NONE".equals(renterTypeStr)) {
                        rentType = RenterType.valueOf(renterTypeStr.toUpperCase());
                        renter = getRenter(rentType, name);
                    }
                    break;
                case "RenterName":
                    if (renter != null && in.peek() != JsonToken.NULL) {
                        name = in.nextString();
                        renter.setName(name);
                    } else {
                        in.skipValue();
                    }
                    break;
                case "RentableID":
                    rentId = in.nextString();
                    break;
                case "RentablePrice":
                    price = in.nextLong();
                    break;
            }
        }
        in.endObject();

        return new Booking(getRentable(rentableType, status, price, rentId), renter);
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

