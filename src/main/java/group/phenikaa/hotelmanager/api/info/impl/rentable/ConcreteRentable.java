package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;

import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public class ConcreteRentable extends AbstractRentable {

    public ConcreteRentable(RentableType type, RentableStatus status, long price, String number) {
        super(type, status, price, number);
    }

    // Optional: You can add additional methods or override methods from AbstractRentable if needed
    @Override
    public String toString() {
        return "ConcreteRentable{" +
                "type=" + getType() +
                ", status=" + getStatus() +
                ", price=" + getPrice() +
                ", number='" + getNumber() + '\'' +
                '}';
    }
}
