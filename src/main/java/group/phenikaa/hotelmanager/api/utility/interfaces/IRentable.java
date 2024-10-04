package group.phenikaa.hotelmanager.api.utility.interfaces;

import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;

public interface IRentable {
    RentableType getName();
    RentableStatus getStatus();
    String getID();
    void setStatus(RentableStatus isAvailable);
    long getPrice();
    void setPrice(long price);
    void setID(String id);
    void setName(RentableType name);
}
