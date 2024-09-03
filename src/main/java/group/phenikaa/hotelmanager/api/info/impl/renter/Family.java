package group.phenikaa.hotelmanager.api.info.impl.renter;

import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;

public class Family extends AbstractRenter {
    private static Family INSTANCE;

    public Family(String name) {
        super("Family", name);
    }

    public static Family getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Family("Null");
        }
        return INSTANCE;
    }
}
