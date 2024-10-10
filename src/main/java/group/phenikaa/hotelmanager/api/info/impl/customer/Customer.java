package group.phenikaa.hotelmanager.api.info.impl.customer;

import group.phenikaa.hotelmanager.api.utility.enums.Country;
import group.phenikaa.hotelmanager.api.utility.enums.IDProof;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;
import group.phenikaa.hotelmanager.api.utility.key.UniqueIndexer;

import static group.phenikaa.hotelmanager.api.utility.Utility.price;

// TODO: the amount of setter getter is crazy
// I want to use lombok or kotlin here :sob:

public class Customer {
    private String name;
    private Enum<IDProof> idProof;
    private int idNumber;
    private int quantity;
    private int night;
    private Enum<Country> country;
    private long money;
    private boolean kid;
    protected long reservationKey = UniqueIndexer.getInstance().generateKey();

    public Customer(String name, Enum<IDProof> idProof, int idNumber, int quantity, int night, Enum<Country> country, long money, boolean kid) {
        this.name = name;
        this.idProof = idProof;
        this.idNumber = idNumber;
        this.quantity = quantity;
        this.night = night;
        this.country = country;
        this.money = money;
        this.kid = kid;
    }

    public String getName() {
        return name;
    }

    public Enum<IDProof> getIdProof() {
        return idProof;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean getKid() {
        return kid;
    }

    public int getNight() {
        return night;
    }

    public Enum<Country> getCountry() {
        return country;
    }

    public long getMoney() {
        return money;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdProof(Enum<IDProof> idProof) {
        this.idProof = idProof;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setKid(boolean kid) {
        this.kid = kid;
    }

    public void setNight(int night) {
        this.night = night;
    }

    public void setCountry(Enum<Country> country) {
        this.country = country;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    // Total cost based on the number of nights, 15% increment for each subsequent rental and 5% when u have a kid
    public long calculateTotalCost(RentableType type) {
        double totalCost = price(type) * night;

        for (int i = 1; i < quantity; i++) {
            totalCost *= 1.1; // Increase by 10% for each rental
        }
        if (kid) {
            totalCost *= 1.05; // Increase by 5% when u have a kid
        }

        return Math.round(totalCost);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Long.hashCode(reservationKey ^ (reservationKey >>> 32));
        return hash;
    }
}


