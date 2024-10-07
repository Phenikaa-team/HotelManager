package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import group.phenikaa.hotelmanager.api.utility.interfaces.IUniqueKeyProvider;
import group.phenikaa.hotelmanager.api.utility.key.UniqueIndexer;

public abstract class AbstractRentable implements IUniqueKeyProvider {
    private RentableType type;
    private RentableStatus rentableStatus;
    private String number;
    private long price;
    private int key = UniqueIndexer.getInstance().generateKey();

    protected AbstractRentable(RentableType type, RentableStatus rentableStatus, long price, String number) {
        this.type = type;
        this.rentableStatus = rentableStatus;
        this.price = price;
        this.number = number;
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

    public String getNumber() {
        return number;
    }

    public void setType(RentableType type) {
        this.type = type;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setStatus(RentableStatus isAvailable) {
        this.rentableStatus = isAvailable;
    }

    @Override
    public int getUniqueKey() {
        return key;
    }
}
