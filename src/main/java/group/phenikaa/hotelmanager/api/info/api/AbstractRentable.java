package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import group.phenikaa.hotelmanager.api.utility.key.UniqueIndexer;

import java.util.Objects;

public abstract class AbstractRentable {
    private RentableType type;
    private RentableStatus status;
    private String number;
    private long price;
    protected final long key = UniqueIndexer.getInstance().generateKey();

    protected AbstractRentable(RentableType type, RentableStatus status, long price, String number) {
        this.type = type;
        this.status = status;
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
        return status;
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
        this.status = isAvailable;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Long.hashCode(key ^ (key >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractRentable other = (AbstractRentable) obj;
        if (this.key != other.key) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        return Objects.equals(this.number, other.number);
    }
}
