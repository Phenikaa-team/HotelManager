package group.phenikaa.hotelmanager.impl.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
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
import java.util.Locale;

public class BookingAdapter extends TypeAdapter<Booking> {
    @Override
    public void write(JsonWriter out, Booking room) throws IOException {
        out.beginObject();
        out.name("Status").value(room.rentable().rentableStatus().name());
        out.name("RentableType").value(room.rentable().rentableStatus().name());
        out.name("RentableID").value(room.renter().getRentalCode());
        out.name("RenterType").value(room.renter().name());
        out.endObject();
    }

    @Override
    public Booking read(JsonReader in) throws IOException {
        in.beginObject();
        var status = RentableStatus.Empty;
        var rentableType = RentableType.ROOMS;
        var rentType = RenterType.FAMILY;
        var price = 0;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "Status":
                    status = RentableStatus.valueOf(in.nextString().toUpperCase(Locale.ROOT));
                    break;
                case "RentableType":
                    rentableType = RentableType.valueOf(in.nextString().toUpperCase());
                    break;
                case "RentableID":
                    break;
                case "RenterType":
                    rentType = RenterType.valueOf(in.nextString().toUpperCase());
                    break;
            }
        }
        in.endObject();
        return new Booking(getRentable(rentableType, status, price), getRenter(rentType));
    }

    private AbstractRentable getRentable(RentableType type, RentableStatus status, long price) {
        return switch (type.getDataString()) {
            case "Rooms" -> new Rooms(status, price);
            case "Houses" -> new Houses(status, price);
            case "Apartment" -> new Apartment(status, price);
            default -> throw new IllegalArgumentException("Unknown rentable type: " + type);
        };
    }

    private AbstractRenter getRenter(RenterType type) {
        return switch (type.getDataString()) {
            case "Individual" -> new Individual();
            case "Family" -> new Family();
            case "LegalEntities" -> new LegalEntities();
            default -> throw new IllegalArgumentException("Unknown renter type: " + type);
        };
    }

}

