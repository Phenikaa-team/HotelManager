package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.interfaces.IRenter;
import group.phenikaa.hotelmanager.api.utility.interfaces.IUniqueIDProvider;
import group.phenikaa.hotelmanager.impl.UniqueIndexer;

public abstract class AbstractRenter implements IRenter, IUniqueIDProvider {
    protected String label, name;
    private final int uniqueID = UniqueIndexer.getInstance().generateID();

    protected AbstractRenter(String label) {
        this.label = label;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getUniqueID() {
        return uniqueID;
    }
}

