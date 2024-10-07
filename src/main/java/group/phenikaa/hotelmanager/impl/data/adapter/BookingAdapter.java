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
            var syncKey = booking.getSynchronizedKey();

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
                out.name("CustomerIDProof").value(customer.getIdProof().name());
                out.name("CustomerIDNumber").value(customer.getIdNumber());
                out.name("CustomerCountry").value(customer.getCountry().name());
                out.name("CustomerQuantity").value(customer.getQuantity());
                out.name("CustomerNights").value(customer.getNight());
                out.name("CustomerSubmittedMoney").value(customer.getMoney());
                out.name("CustomerHasKids").value(customer.getKid());
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
            String customerName = null;
            IDProof idProof = null;
            int idNumber = 0;
            Country country = null;
            int quantity = 1;
            int nights = 1;
            long submittedMoney = 0L;
            boolean hasKids = false;
            int syncKey = 0;

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
                                    case "CustomerName" -> customerName = in.nextString();
                                    case "CustomerIDProof" -> idProof = IDProof.valueOf(in.nextString());
                                    case "CustomerIDNumber" -> idNumber = in.nextInt();
                                    case "CustomerCountry" -> country = Country.valueOf(in.nextString());
                                    case "CustomerQuantity" -> quantity = in.nextInt();
                                    case "CustomerNights" -> nights = in.nextInt();
                                    case "CustomerSubmittedMoney" -> submittedMoney = in.nextLong();
                                    case "CustomerHasKids" -> hasKids = in.nextBoolean();
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
            if (customerName != null) {
                customer = new Customer(customerName, idProof, idNumber, quantity, nights, country, submittedMoney, hasKids);
            }

            // Create booking
            Booking booking = new Booking(rentable, customer);

            // Check for synchronized key
            if (syncKey != 0 && syncKey != booking.getSynchronizedKey()) {
                throw new IllegalArgumentException("Synchronized key mismatch.");
            }

            return booking;
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
            default -> throw new IllegalArgumentException("Unknown rentable type: " + type);
        };
    }
}
