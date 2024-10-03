package group.phenikaa.hotelmanager.api.info;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;

public record Booking(AbstractRentable rentable, Customer customer) {
}