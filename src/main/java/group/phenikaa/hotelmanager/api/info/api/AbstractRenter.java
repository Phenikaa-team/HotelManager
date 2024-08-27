package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.interfaces.IRenter;

public abstract class AbstractRenter implements IRenter {
    protected String name;
    protected long rentalCode;

    protected AbstractRenter(String name) {
        this.name = name;
        this.rentalCode = 0;
    }

    @Override
    public String name() {
        return name;
    }

    public void setRentalCode(long rentalCode) {
        this.rentalCode = rentalCode;
    }

    public long getRentalCode() {
        return rentalCode;
    }
}
