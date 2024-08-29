package group.phenikaa.hotelmanager.impl.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.impl.rentable.Apartment;
import group.phenikaa.hotelmanager.api.info.impl.rentable.Houses;
import group.phenikaa.hotelmanager.api.info.impl.rentable.Rooms;
import group.phenikaa.hotelmanager.api.info.impl.renter.Family;
import group.phenikaa.hotelmanager.api.info.impl.renter.Individual;
import group.phenikaa.hotelmanager.api.info.impl.renter.LegalEntities;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import group.phenikaa.hotelmanager.api.utility.enums.RenterType;

import java.io.IOException;

public class BookingAdapter extends TypeAdapter<Booking> {
    @Override
    public void write(JsonWriter out, Booking booking) throws IOException {
        var rentableName = booking.rentable().getClass().getSimpleName();
        var rentableType = RentableType.valueOf(rentableName.replaceAll("s$", ""));
        var renterType = RenterType.valueOf(booking.renter().label().toUpperCase()).name();
        out.beginObject();
        out.name("RentableType").value(rentableType.name());
        out.name("Status").value(booking.rentable().rentableStatus().name());
        out.name("RenterType").value(renterType);
        out.name("RenterName").value(booking.renter().name());
        out.name("RentableID").value(booking.renter().getUniqueID());
        out.endObject();
    }

    @Override
    public Booking read(JsonReader in) throws IOException {
        in.beginObject();
        var status = RentableStatus.Empty;
        var rentableType = RentableType.Room;
        var rentType = RenterType.FAMILY;
        var rentId = 0L;
        var price = 0;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "RentableType":
                    rentableType = RentableType.valueOf(in.nextName());
                    break;
                case "Status":
                    status = RentableStatus.valueOf(in.nextString());
                    break;
                case "RenterType":
                    rentType = RenterType.valueOf(in.nextString().toUpperCase());
                    break;
                case "RenterName":
                    getRenter(rentType).setName(in.nextString());
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
        return new Booking(getRentable(rentableType, status, price), getRenter(rentType), rentId);

    }

    // Mapping RentableType to AbstractRentable - I can't find anything can replace this ( ˘︹˘ )
    public static AbstractRentable getRentable(RentableType type, RentableStatus status, long price) {
        return switch (type) {
            case Room -> new Rooms(status, price);
            case House -> new Houses(status, price);
            case Apartment -> new Apartment(status, price);
            case null, default -> throw new IllegalArgumentException("Unknown rentable type: " + type);
        };
    }

    // Mapping RenterType to AbstractRenter - I can't find anything can replace this ( ˘︹˘ )
    public static AbstractRenter getRenter(RenterType type) {
        return switch (type) {
            case INDIVIDUAL -> new Individual();
            case FAMILY -> new Family();
            case LEGALENTITIES -> new LegalEntities();
            case null, default -> throw new IllegalArgumentException("Unknown renter type: " + type);
        };
    }
}

