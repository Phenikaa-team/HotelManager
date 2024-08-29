package group.phenikaa.hotelmanager.impl;

import group.phenikaa.hotelmanager.HotelApplication;

public class UniqueIndexer {
    private static final UniqueIndexer INSTANCE = new UniqueIndexer();
    private int id = 0;

    private UniqueIndexer() {
        HotelApplication.EVENT_BUS.register(this);
    }

    public static UniqueIndexer getInstance() {
        return INSTANCE;
    }

    public synchronized int generateID() {
        return id++;
    }
}
