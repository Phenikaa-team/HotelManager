package group.phenikaa.hotelmanager.api.utility.interfaces;

import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public interface IRentable {
    String getName();
    RentableStatus getStatus();
    void setStatus(RentableStatus isAvailable);
    long getPrice();
    void setPrice(long price);
    String getID();
    void setID(String id);
}
