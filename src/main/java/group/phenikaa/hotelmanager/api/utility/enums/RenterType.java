package group.phenikaa.hotelmanager.api.utility.enums;

import group.phenikaa.hotelmanager.api.info.impl.renter.Family;
import group.phenikaa.hotelmanager.api.info.impl.renter.Individual;
import group.phenikaa.hotelmanager.api.info.impl.renter.LegalEntities;
import group.phenikaa.hotelmanager.api.utility.interfaces.IDataClass;
import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;

public enum RenterType implements IDataClass {
    FAMILY(Family.class),
    INDIVIDUAL(Individual.class),
    LEGALENTITIES(LegalEntities.class);

    private final Class<? extends AbstractRenter> renterClass;

    RenterType(Class<? extends AbstractRenter> renterClass) {
        this.renterClass = renterClass;
    }

    public long getRenterId() {
        return getInstance().getUniqueID();
    }

    public AbstractRenter getInstance() {
        if (this == FAMILY) {
            return Family.getInstance();
        } else if (this == INDIVIDUAL) {
            return Individual.getInstance();
        } else if (this == LEGALENTITIES) {
            return LegalEntities.getInstance();
        }
        throw new UnsupportedOperationException("Unsupported renter type: " + this);
    }

    @Override
    public Class<?> getDataClass() {
        return renterClass;
    }

    @Override
    public String getDataString() {
        return renterClass.getName();
    }
}
