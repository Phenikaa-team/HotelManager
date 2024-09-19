package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.interfaces.IRentable;

public abstract class AbstractRentable implements IRentable {
    protected String name;
    protected RentableStatus rentableStatus;
    protected long price;
    protected String id;

    protected AbstractRentable(String name, RentableStatus rentableStatus, long price, String id) {
        this.name = name;
        this.rentableStatus = rentableStatus;
        this.price = price;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getPrice() {
        return price;
    }

    @Override
    public RentableStatus getStatus() {
        return rentableStatus;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public void setStatus(RentableStatus isAvailable) {
        this.rentableStatus = isAvailable;
    }
}
