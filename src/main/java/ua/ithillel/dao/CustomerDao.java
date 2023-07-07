package ua.ithillel.dao;

import ua.ithillel.exception.CustomerCreateException;
import ua.ithillel.model.Customer;

// Customer Data Access Object
public interface CustomerDao {
    Customer findById(Integer id);
    Customer findByEmail(String email);
    Customer findByIdWithOrders(Integer id);
    void create(Customer customer) throws CustomerCreateException;
}
