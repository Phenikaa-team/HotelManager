package group.phenikaa.hotelmanager.api.utility.interfaces;

import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public interface IRentable {
    String getName();

    RentableStatus getStatus();

    long getPrice();
    void setStatus(RentableStatus isAvailable);
}
