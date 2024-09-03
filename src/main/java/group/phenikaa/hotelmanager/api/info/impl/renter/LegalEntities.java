package group.phenikaa.hotelmanager.api.info.impl.renter;

import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;

public class LegalEntities extends AbstractRenter {
    private static LegalEntities INSTANCE;

    public LegalEntities(String name) {
        super("LegalEntities",  name);
    }

    public static LegalEntities getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LegalEntities("Null");
        }
        return INSTANCE;
    }
}
