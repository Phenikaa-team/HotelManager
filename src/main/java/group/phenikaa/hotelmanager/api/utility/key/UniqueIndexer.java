package group.phenikaa.hotelmanager.api.utility.key;

import group.phenikaa.hotelmanager.HotelApplication;

public class UniqueIndexer {
    private static final UniqueIndexer INSTANCE = new UniqueIndexer();
    private int key = 0;

    private UniqueIndexer() {
        HotelApplication.EVENT_BUS.register(this);
    }

    public static UniqueIndexer getInstance() {
        return INSTANCE;
    }

    public synchronized int generateKey() {
        return key++;
    }
}
