package group.phenikaa.hotelmanager.api.info;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;

public record Booking(AbstractRentable abstractRentable, AbstractRenter customer) {
}