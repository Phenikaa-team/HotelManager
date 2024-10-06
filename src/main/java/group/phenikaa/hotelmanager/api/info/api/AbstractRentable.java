package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;

public abstract class AbstractRentable {
    private RentableType type;
    private RentableStatus rentableStatus;
    private String id;
    private long price;

    protected AbstractRentable(RentableType type, RentableStatus rentableStatus, long price, String id) {
        this.type = type;
        this.rentableStatus = rentableStatus;
        this.price = price;
        this.id = id;
    }

    public RentableType getType() {
        return type;
    }

    public long getPrice() {
        return price;
    }

    public RentableStatus getStatus() {
        return rentableStatus;
    }

    public String getId() {
        return id;
    }

    public void setType(RentableType type) {
        this.type = type;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(RentableStatus isAvailable) {
        this.rentableStatus = isAvailable;
    }
}
