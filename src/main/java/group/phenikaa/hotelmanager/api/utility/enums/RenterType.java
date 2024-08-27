package group.phenikaa.hotelmanager.api.utility.enums;

import group.phenikaa.hotelmanager.api.info.impl.renter.Family;
import group.phenikaa.hotelmanager.api.info.impl.renter.Individual;
import group.phenikaa.hotelmanager.api.info.impl.renter.LegalEntities;
import group.phenikaa.hotelmanager.api.utility.interfaces.IDataClass;
import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;

import java.util.function.Supplier;

public enum RenterType implements IDataClass {
    FAMILY(Family::new),
    INDIVIDUAL(Individual::new),
    LEGAL_ENTITIES(LegalEntities::new);

    private final Supplier<? extends AbstractRenter> renter;

    RenterType(Supplier<? extends AbstractRenter> renter) {
        this.renter = renter;
    }

    @Override
    public Class<?> getDataClass() {
        return renter.get().getClass();
    }

    @Override
    public String getDataString() {
        return renter.get().name();
    }
}
