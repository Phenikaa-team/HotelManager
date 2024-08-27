package group.phenikaa.hotelmanager.api.manager;

import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

import java.util.List;

public class BookingManager {
    private final List<Booking> rooms;

    public BookingManager(List<Booking> rooms) {
        this.rooms = rooms;
    }

    public Booking findRoomByNumber(String rentalCode) {
        for (Booking room : rooms) {
            if (room.rentable().generateRentalCode().equals(rentalCode)) {
                return room;
            }
        }
        return null;
    }

    // Đặt phòng
    public void bookRoom(String roomNumber, AbstractRenter customer) {
        Booking room = findRoomByNumber(roomNumber);
        if (room != null && room.rentable().rentableStatus() == RentableStatus.Empty) {
            room.rentable().setStatus(RentableStatus.Empty);
            //room.abstractRentable().setCustomer(customer);
        }
    }

    // Trả phòng
    public boolean checkoutRoom(String roomNumber) {
        Booking room = findRoomByNumber(roomNumber);
        if (room != null && room.rentable().rentableStatus() == RentableStatus.Full) {
            room.rentable().setStatus(RentableStatus.Empty);
            //room.abstractRentable().setCustomer(null);
            return true;
        }
        return false;
    }
}
