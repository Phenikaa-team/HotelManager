package group.phenikaa.hotelmanager.api.manager;

import group.phenikaa.hotelmanager.api.utility.enums.Gender;
import group.phenikaa.hotelmanager.api.utility.interfaces.ICustomer;

public class Customer implements ICustomer {
    private String name;
    private int phoneNum;
    private Gender gender;
    private int roomNum;

    public Customer(String name, int phoneNumber, Gender gender, int roomNumber) {
        this.name = name;
        this.phoneNum = phoneNumber;
        this.gender = gender;
        this.roomNum = roomNumber;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int phoneNumber() {
        return phoneNum;
    }

    @Override
    public Gender gender() {
        return gender;
    }

    @Override
    public int roomNum() {
        return roomNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }
}
