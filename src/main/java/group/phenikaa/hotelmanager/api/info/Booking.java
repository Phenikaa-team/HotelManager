package group.phenikaa.hotelmanager.api.info;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;

import java.util.HashMap;

public class Booking {
    private final AbstractRentable rentable;
    private final Customer customer;
    private int synchronizedKey;

    public Booking(AbstractRentable rentable, Customer customer) {
        this.rentable = rentable;
        this.customer = customer;
        this.synchronizedKey = generateSynchronizedKey();
    }

    private int generateSynchronizedKey() {
        int customerKey = 0;
        if (customer != null) {
            customerKey = customer.getUniqueKey();
        }
        int rentableKey = rentable.getUniqueKey();
        return encryptKeys(customerKey, rentableKey);
    }

    private int encryptKeys(int key1, int key2) {
        return key1 + key2;
    }

    public boolean validateBooking(int syncKey) {
        return synchronizedKey == syncKey;
    }

    public AbstractRentable getRentable() {
        return rentable;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getSynchronizedKey() {
        return synchronizedKey;
    }

    public void setSynchronizedKey(int synchronizedKey) {
        this.synchronizedKey = synchronizedKey;
    }
}
