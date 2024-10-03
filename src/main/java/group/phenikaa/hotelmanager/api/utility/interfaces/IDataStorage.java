package group.phenikaa.hotelmanager.api.utility.interfaces;

import java.util.List;

public interface IDataStorage<T> {
    void save(List<T> list, String fileName);
    List<T> load(String fileName);
}
