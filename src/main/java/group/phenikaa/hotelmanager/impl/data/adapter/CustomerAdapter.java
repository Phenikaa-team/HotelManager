package group.phenikaa.hotelmanager.impl.data.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.utility.enums.Country;
import group.phenikaa.hotelmanager.api.utility.enums.IDProof;

//TODO: Done this
public class CustomerAdapter extends TypeAdapter<Customer> {
    @Override
    public void write(JsonWriter out, Customer value) {
        try {
            out.beginObject();

            if (value != null) {
                out.name("CustomerName").value(value.getName());
                out.name("CustomerID").value(value.getIdNumber());
                out.name("CustomerCountry").value(value.getCountry().name());
                out.name("CustomerIDProof").value(value.getIdProof().name());
                out.name("CustomerTenants").value(value.getQuantity());
                out.name("CustomerKid").value(value.getKid());
                out.name("CustomerNights").value(value.getNight());
                out.name("SubmittedMoney").value(value.getMoney());
                out.name("CustomerKey").value(value.hashCode());
            }
            out.endObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customer read(JsonReader in) {
        try {
            in.beginObject();

            String name = null;
            int idNumber = 0, tenants = 0, nights = 0;
            boolean kid = false;
            Country country = null;
            IDProof idProof = null;
            long submittedMoney = 0;

            while (in.hasNext()) {
                String fieldName = in.nextName();
                switch (fieldName) {
                    case "CustomerName" -> name = in.nextString();
                    case "CustomerID" -> idNumber = in.nextInt();
                    case "CustomerCountry" -> country = Country.valueOf(in.nextString());
                    case "CustomerIDProof" -> idProof = IDProof.valueOf(in.nextString());
                    case "CustomerTenants" -> tenants = in.nextInt();
                    case "CustomerKid" -> kid = in.nextBoolean();
                    case "CustomerNights" -> nights = in.nextInt();
                    case "SubmittedMoney" -> submittedMoney = in.nextLong();
                    case "CustomerKey" -> in.nextLong();
                    default -> in.skipValue();
                }
            }
            in.endObject();

            if (name != null && idProof != null && country != null) {
                return new Customer(name, idProof, idNumber, tenants, nights, country, submittedMoney, kid);
            } else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
