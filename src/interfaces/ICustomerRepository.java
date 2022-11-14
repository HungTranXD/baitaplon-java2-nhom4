package interfaces;

import entities.Customer;

import java.util.ArrayList;

public interface ICustomerRepository {
    ArrayList<Customer> readAll();
    Boolean create(Customer c);
    Boolean update(Customer c);
    Boolean delete(Customer c);
    Customer findById(int id);
    Customer findByIdNumber(String idnumber);
}
