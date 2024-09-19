package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.interfaces.IRenter;

public abstract class AbstractRenter implements IRenter {
    protected String label, name;

    protected AbstractRenter(String label, String name) {
        this.label = label;
        this.name = name;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}

