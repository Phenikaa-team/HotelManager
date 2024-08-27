package group.phenikaa.hotelmanager.api.manager;

import java.io.Serializable;

public record Booking(Room room, Customer customer) implements Serializable {
}