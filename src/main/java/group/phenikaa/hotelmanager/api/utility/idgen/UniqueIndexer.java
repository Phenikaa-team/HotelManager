package group.phenikaa.hotelmanager.api.utility.idgen;

public class UniqueIndexer {
    private static final UniqueIndexer INSTANCE = new UniqueIndexer();
    private int id = 0;

    private UniqueIndexer() {
    }

    public static UniqueIndexer getInstance() {
        return INSTANCE;
    }

    public synchronized int generateID() {
        return id++;
    }
}
