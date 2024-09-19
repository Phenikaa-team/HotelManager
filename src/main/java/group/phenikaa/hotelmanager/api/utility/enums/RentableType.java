package group.phenikaa.hotelmanager.api.utility.enums;

public enum RentableType {
    Single("Single Room, suitable for 1 person"),
    Double("Double Room, suitable for 2 persons"),
    Suite("Suite Room, luxury with separate living space"),
    Deluxe("Deluxe Room, high-end amenities"),
    Family("Family Room, suitable for families"),
    PresidentialSuite("Presidential Suite, the most luxurious room");

    private final String description;

    RentableType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name() + " - " + description;
    }
}

