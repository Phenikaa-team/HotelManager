package group.phenikaa.hotelmanager.api.info;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.utility.key.UniqueIndexer;

public class Booking {
    private final AbstractRentable rentable;
    private final Customer customer;
    private final long syncKey = UniqueIndexer.getInstance().generateKey();

    public Booking(AbstractRentable rentable, Customer customer) {
        this.rentable = rentable;
        this.customer = customer;
    }

    public AbstractRentable getRentable() {
        return rentable;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Long.hashCode(syncKey ^ (syncKey >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Booking other = (Booking) obj;
        return syncKey == other.syncKey;
    }
}
