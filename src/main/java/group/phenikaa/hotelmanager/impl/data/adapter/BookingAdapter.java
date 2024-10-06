package group.phenikaa.hotelmanager.impl.data.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.impl.rentable.*;
import group.phenikaa.hotelmanager.api.info.impl.rentable.Double;
import group.phenikaa.hotelmanager.api.utility.enums.Country;
import group.phenikaa.hotelmanager.api.utility.enums.IDProof;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;

import java.io.IOException;

public class BookingAdapter extends TypeAdapter<Booking> {

    @Override
    public void write(JsonWriter out, Booking booking) throws IOException {
        var rentable = booking.rentable();
        var rentableType = RentableType.valueOf(rentable.getClass().getSimpleName().replaceAll("s$", ""));
        var rentableStatus = rentable.getStatus().name();
        var rentableID = rentable.getId();
        var rentablePrice = rentable.getPrice();
        Customer customer = booking.customer();

        out.beginObject();
        out.name("RentableType").value(rentableType.name());
        out.name("RentableStatus").value(rentableStatus);
        out.name("RentableID").value(rentableID);
        out.name("RentablePrice").value(rentablePrice);

        if (customer != null) {
            out.name("CustomerName").value(customer.getName());
            out.name("CustomerID").value(customer.getIdNumber());
            out.name("CustomerCountry").value(customer.getCountry().name());
            out.name("CustomerIDProof").value(customer.getIdProof().name());
            out.name("CustomerTenants").value(customer.getQuantity());
            out.name("CustomerKid").value(customer.getKid());
            out.name("CustomerNights").value(customer.getNight());
            out.name("SubmittedMoney").value(customer.getMoney());
        }
        out.endObject();
    }

    @Override
    public Booking read(JsonReader in) throws IOException {
        in.beginObject();
        RentableStatus status = RentableStatus.Available;
        RentableType rentableType = RentableType.Single;
        String rentId = "00x0";
        long price = 0L;
        Customer renter = null;

        String name = null;
        int idNumber = 0, tenants = 0, nights = 0;
        boolean kid = false;
        Country country = null;
        IDProof idProof = null;
        long submittedMoney = 0;

        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "RentableType" -> rentableType = RentableType.valueOf(in.nextString());
                case "RentableStatus" -> status = RentableStatus.valueOf(in.nextString());
                case "RentableID" -> rentId = in.nextString();
                case "RentablePrice" -> price = in.nextLong();
                case "CustomerName" -> name = in.nextString();
                case "CustomerID" -> idNumber = in.nextInt();
                case "CustomerCountry" -> country = Country.valueOf(in.nextString());
                case "CustomerIDProof" -> idProof = IDProof.valueOf(in.nextString());
                case "CustomerTenants" -> tenants = in.nextInt();
                case "CustomerKid" -> kid = in.nextBoolean();
                case "CustomerNights" -> nights = in.nextInt();
                case "SubmittedMoney" -> submittedMoney = in.nextLong();
                default -> in.skipValue();
            }
        }
        in.endObject();

        AbstractRentable rentable = getRentable(rentableType, status, price, rentId);
        if (name != null) {
            renter = new Customer(name, idProof, idNumber, tenants, nights, country, submittedMoney, kid);
        }

        return new Booking(rentable, renter);
    }

    public static AbstractRentable getRentable(RentableType type, RentableStatus status, long price, String id) {
        return switch (type) {
            case Double -> new Double(status, price, id);
            case Suite -> new Suite(status, price, id);
            case Single -> new Single(status, price, id);
            case Deluxe -> new Deluxe(status, price, id);
            case Family -> new Family(status, price, id);
            default -> throw new IllegalArgumentException("Unknown rentable type: " + type);
        };
    }
}
