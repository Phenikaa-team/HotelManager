package group.phenikaa.hotelmanager.api.manager;

import group.phenikaa.hotelmanager.api.info.Booking;
import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;
import group.phenikaa.hotelmanager.api.utility.enums.RoomStatus;

import java.util.List;

public class BookingManager {
    private final List<Booking> rooms;

    public BookingManager(List<Booking> rooms) {
        this.rooms = rooms;
    }

    public Booking findRoomByNumber(String roomNumber) {
        for (Booking room : rooms) {
            if (room.abstractRentable().roomNumber().equals(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    // Đặt phòng
    public void bookRoom(String roomNumber, AbstractRenter customer) {
        Booking room = findRoomByNumber(roomNumber);
        if (room != null && room.abstractRentable().isAvailable == RoomStatus.Empty) {
            room.abstractRentable().setAvailable(RoomStatus.Empty);
            room.abstractRentable().setCustomer(customer);
        }
    }

    // Trả phòng
    public boolean checkoutRoom(String roomNumber) {
        Booking room = findRoomByNumber(roomNumber);
        if (room != null && room.abstractRentable().isAvailable == RoomStatus.Full) {
            room.abstractRentable().setAvailable(RoomStatus.Empty);
            room.abstractRentable().setCustomer(null);
            return true;
        }
        return false;
    }
}
