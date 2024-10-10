package group.phenikaa.hotelmanager.impl.data.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.info.impl.rentable.*;
import group.phenikaa.hotelmanager.api.info.impl.rentable.Double;
import group.phenikaa.hotelmanager.api.utility.enums.Country;
import group.phenikaa.hotelmanager.api.utility.enums.IDProof;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;

public class BookingAdapter extends TypeAdapter<Booking> {

    @Override
    public void write(JsonWriter out, Booking booking) {
        try {
            var rentable = booking.getRentable();
            var customer = booking.getCustomer();
            var syncKey = booking.hashCode();

            out.beginObject();

            // Rentable details
            out.name("RentableType").value(rentable.getType().name());
            out.name("RentableStatus").value(rentable.getStatus().name());
            out.name("RentableNumber").value(rentable.getNumber());
            out.name("RentablePrice").value(rentable.getPrice());

            // Customer details
            out.name("Customers").beginArray();
            if (customer != null) {
                out.beginObject();
                out.name("CustomerName").value(customer.getName());
                out.name("CustomerID").value(customer.getIdNumber());
                out.name("CustomerCountry").value(customer.getCountry().name());
                out.name("CustomerIDProof").value(customer.getIdProof().name());
                out.name("CustomerTenants").value(customer.getQuantity());
                out.name("CustomerKid").value(customer.getKid());
                out.name("CustomerNights").value(customer.getNight());
                out.name("SubmittedMoney").value(customer.getMoney());
                out.endObject();
            }
            out.endArray();
            // Synchronized key
            out.name("SynchronizedKey").value(syncKey);

            out.endObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Booking read(JsonReader in) {
        try {
            in.beginObject();

            // Rentable data
            RentableStatus status = RentableStatus.Available;
            RentableType rentableType = RentableType.Single;
            String rentId = "00x0";
            long price = 0L;
            int syncKey = 0;

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
                    case "RentableNumber" -> rentId = in.nextString();
                    case "RentablePrice" -> price = in.nextLong();

                    // Customer data
                    case "Customers" -> {
                        in.beginArray();
                        while (in.hasNext()) {
                            in.beginObject();
                            while (in.hasNext()) {
                                switch (in.nextName()) {
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
                        }
                        in.endArray();
                    }
                    // Synchronized key
                    case "SynchronizedKey" -> syncKey = in.nextInt();
                    default -> in.skipValue();
                }
            }
            in.endObject();

            // Create rentable
            AbstractRentable rentable = getRentable(rentableType, status, price, rentId);

            // Create customer if data is available
            Customer customer = null;
            if (name != null && idProof != null && country != null) {
                customer = new Customer(name, idProof, idNumber, tenants, nights, country, submittedMoney, kid);
            }
            return new Booking(rentable, customer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AbstractRentable getRentable(RentableType type, RentableStatus status, long price, String id) {
        return switch (type) {
            case Double -> new Double(status, price, id);
            case Suite -> new Suite(status, price, id);
            case Single -> new Single(status, price, id);
            case Deluxe -> new Deluxe(status, price, id);
            case Family -> new Family(status, price, id);
        };
    }
}
