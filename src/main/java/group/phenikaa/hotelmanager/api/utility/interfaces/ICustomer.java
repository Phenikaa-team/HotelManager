package group.phenikaa.hotelmanager.api.utility.interfaces;

import group.phenikaa.hotelmanager.api.utility.enums.Gender;

public interface ICustomer {
    String name();
    long phoneNumber();
    Gender gender();
    String roomNum();
}
