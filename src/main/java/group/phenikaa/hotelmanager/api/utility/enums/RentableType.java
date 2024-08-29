package group.phenikaa.hotelmanager.api.utility.enums;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.info.impl.rentable.Apartment;
import group.phenikaa.hotelmanager.api.info.impl.rentable.Houses;
import group.phenikaa.hotelmanager.api.info.impl.rentable.Rooms;
import group.phenikaa.hotelmanager.api.utility.interfaces.IDataClass;

public enum RentableType implements IDataClass {
    House(Houses.class),
    Room(Rooms.class),
    Apartment(Apartment.class);

    private final Class<? extends AbstractRentable> rentable;

    RentableType(Class<? extends AbstractRentable> rentable) {
        this.rentable = rentable;
    }

    @Override
    public Class<?> getDataClass() {
        return rentable;
    }

    @Override
    public String getDataString() {
        return rentable.getName();
    }
}
