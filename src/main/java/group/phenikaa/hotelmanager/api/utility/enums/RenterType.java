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
    LEGALENTITIES(LegalEntities::new);

    private final Supplier<? extends AbstractRenter> renterClass;

    RenterType(Supplier<? extends AbstractRenter> renterClass) {
        this.renterClass = renterClass;
    }

    public long getRenterId() {
        return renterClass.get().getUniqueID();
    }

    @Override
    public Class<?> getDataClass() {
        return renterClass.getClass();
    }

    @Override
    public String getDataString() {
        return renterClass.getClass().getName();
    }
}