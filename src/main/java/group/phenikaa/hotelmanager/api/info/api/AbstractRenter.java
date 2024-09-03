package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.interfaces.IRenter;
import group.phenikaa.hotelmanager.api.utility.interfaces.IUniqueIDProvider;
import group.phenikaa.hotelmanager.impl.UniqueIndexer;

public abstract class AbstractRenter implements IRenter, IUniqueIDProvider {
    protected String label, name;
    private final int uniqueID = UniqueIndexer.getInstance().generateID();

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

    @Override
    public int getUniqueID() {
        return uniqueID;
    }
}

