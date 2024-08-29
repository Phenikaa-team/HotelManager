package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.interfaces.IRentable;
import group.phenikaa.hotelmanager.api.utility.interfaces.IUniqueIDProvider;
import group.phenikaa.hotelmanager.impl.UniqueIndexer;

public abstract class AbstractRentable implements IRentable, IUniqueIDProvider {
    protected String name;
    protected RentableStatus rentableStatus;
    protected long price;
    private final int uniqueID = UniqueIndexer.getInstance().generateID();

    protected AbstractRentable(String name, RentableStatus rentableStatus, long price) {
        this.name = name;
        this.rentableStatus = rentableStatus;
        this.price = price;
    }

    @Override
    public int getUniqueID() {
        return uniqueID;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public long rentablePrice() {
        return price;
    }

    @Override
    public RentableStatus rentableStatus() {
        return rentableStatus;
    }

    @Override
    public void setStatus(RentableStatus isAvailable) {
        this.rentableStatus = isAvailable;
    }
}
