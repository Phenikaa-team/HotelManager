package group.phenikaa.hotelmanager.api.info.impl.customer;

import group.phenikaa.hotelmanager.api.utility.enums.Country;
import group.phenikaa.hotelmanager.api.utility.enums.IDProof;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;

import static group.phenikaa.hotelmanager.api.utility.Utility.price;

// TODO: the amount of setter getter is crazy
public class Customer {
    protected String name;
    protected Enum<IDProof> idProof;
    protected int idNumber;
    protected int quantity;
    protected int night;
    protected Enum<Country> country;
    protected long money;
    protected boolean kid;

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

    public void setName(String name) {
        this.name = name;
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
            totalCost *= 1.15; // Increase by 15% for each rental
        }
        if (kid) {
            totalCost *= 1.05; // Increase by 5% when u have a kid
        }

        return Math.round(totalCost);
    }
}


