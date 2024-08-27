package group.phenikaa.hotelmanager.api.manager;

import java.io.Serializable;

public class Booking implements Serializable {
    private final Room room;
    private final Customer customer;

    public Booking(Room room, Customer customer) {
        this.room = room;
        this.customer = customer;
    }

    public Room getRoom() {
        return room;
    }

    public Customer getCustomer() {
        return customer;
    }
}