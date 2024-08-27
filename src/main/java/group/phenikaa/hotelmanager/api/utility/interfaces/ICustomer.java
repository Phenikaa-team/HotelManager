package group.phenikaa.hotelmanager.api.utility.interfaces;

import group.phenikaa.hotelmanager.api.utility.enums.Gender;

public interface ICustomer {
    String name();
    int phoneNumber();
    Gender gender();
    int roomNum();
}
