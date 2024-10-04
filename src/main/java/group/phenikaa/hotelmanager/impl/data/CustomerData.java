package group.phenikaa.hotelmanager.impl.data;

import group.phenikaa.hotelmanager.api.info.impl.customer.Customer;
import group.phenikaa.hotelmanager.api.utility.interfaces.IDataStorage;

import java.util.List;

// TODO: Done this
public class CustomerData implements IDataStorage<Customer> {
    @Override
    public void save(List<Customer> list, String fileName) {

    }

    @Override
    public List<Customer> load(String fileName) {
        return List.of();
    }
}
