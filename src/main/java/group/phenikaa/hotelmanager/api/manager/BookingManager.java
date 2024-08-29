package group.phenikaa.hotelmanager.api.manager;

import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

import java.util.List;

public class BookingManager {
    private final List<Booking> rooms;

    public BookingManager(List<Booking> rooms) {
        this.rooms = rooms;
    }

    public Booking findRoomByNumber(long rentalCode) {
        for (Booking room : rooms) {
            if (room.rentable().getUniqueID() == rentalCode) {
                return room;
            }
        }
        return null;
    }

    // Đặt phòng
    public void bookRoom(long roomNumber) {
        Booking room = findRoomByNumber(roomNumber);
        if (room != null && room.rentable().rentableStatus() == RentableStatus.Empty) {
            room.rentable().setStatus(RentableStatus.Empty);
            //room.abstractRentable().setCustomer(customer);
        }
    }

    // Trả phòng
    public boolean checkoutRoom(long roomNumber) {
        Booking room = findRoomByNumber(roomNumber);
        if (room != null && room.rentable().rentableStatus() == RentableStatus.Full) {
            room.rentable().setStatus(RentableStatus.Empty);
            //room.abstractRentable().setCustomer(null);
            return true;
        }
        return false;
    }
}
