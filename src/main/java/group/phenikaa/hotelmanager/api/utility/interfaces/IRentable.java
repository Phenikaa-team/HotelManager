package group.phenikaa.hotelmanager.api.utility.interfaces;

import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public interface IRentable {
    String name();

    RentableStatus rentableStatus();

    long rentablePrice();
    void setStatus(RentableStatus isAvailable);
}
