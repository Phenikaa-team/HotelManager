package group.phenikaa.hotelmanager.api.utility.interfaces;

import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

import java.util.ArrayList;

public interface IRentable {
    String name();
    /**
     * method that is used to generate 7 digit random
     * rental code for each rental
     * @return ArrayList<AbstractRenter>
     */
    ArrayList<AbstractRenter> generateRentalCode();

    RentableStatus rentableStatus();

    long rentablePrice();
    void setStatus(RentableStatus isAvailable);
}
