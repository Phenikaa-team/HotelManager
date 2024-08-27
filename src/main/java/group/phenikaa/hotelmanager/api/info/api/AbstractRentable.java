package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.interfaces.IRentable;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractRentable implements IRentable {
    protected String name;
    protected RentableStatus rentableStatus;
    protected long price;
    protected ArrayList<AbstractRenter> renterList;

    protected AbstractRentable(String name, RentableStatus rentableStatus, long price) {
        this.name = name;
        this.rentableStatus = rentableStatus;
        this.price = price;
        this.renterList = new ArrayList<>();
    }

    @Override
    public ArrayList<AbstractRenter> generateRentalCode() {
        ArrayList<AbstractRenter> customerList = renterList;

        // generating 7 digit random rental code
        Random randomCodeGenerator = new Random();

        for (AbstractRenter customer: customerList) {
            StringBuilder randomCode = new StringBuilder();
            for (int i = 0; i < 7; i++) {

                int randomDigit = randomCodeGenerator.nextInt(10);
                randomCode.append(randomDigit);
            }

            // at the end of the for loop we get 7 digit random code
            long rentalCode = Long.parseLong(randomCode.toString());

            customer.setRentalCode(rentalCode);
        }

        return customerList;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public long rentablePrice() {
        return price;
    }

    @Override
    public RentableStatus rentableStatus() {
        return rentableStatus;
    }

    @Override
    public void setStatus(RentableStatus isAvailable) {
        this.rentableStatus = isAvailable;
    }
}
