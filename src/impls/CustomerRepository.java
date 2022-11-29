package impls;

import database.Connector;
import entities.Customer;
import interfaces.IRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomerRepository implements IRepository<Customer> {
    @Override
    public ArrayList<Customer> readAll() {
        ArrayList<Customer> ls = new ArrayList<>();
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_customer";
            ResultSet rs = connector.query(sql_txt);
            while (rs.next()) {
                ls.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("customer_idnumber"),
                        rs.getString("customer_tel")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in readAll(): " + e);
            e.printStackTrace();
        }
        return ls;
    }

    @Override
    public Boolean create(Customer c) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "INSERT INTO nhom4_customer(customer_name,  customer_idnumber, customer_tel) VALUES (?,?,?)";
            ArrayList parameters = new ArrayList<>();
            parameters.add(c.getCustomerName());
            parameters.add(c.getCustomerIdNumber());
            parameters.add(c.getCustomerTel());
            return connector.execute(sql_txt, parameters);
        } catch (Exception e) {
            System.out.println("Error in create(): " + e);
        }
        return false;
    }

    @Override
    public Boolean update(Customer c) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "UPDATE nhom4_customer SET customer_name = ?,  customer_idnumber = ?, customer_tel = ? WHERE customer_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(c.getCustomerName());
            parameters.add(c.getCustomerIdNumber());
            parameters.add(c.getCustomerTel());
            parameters.add(c.getCustomerId());
            return connector.execute(sql_txt, parameters);
        } catch (Exception e) {
            System.out.println("Error in update(): " + e);
        }
        return false;
    }

    @Override
    public Boolean delete(Customer c) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "DELETE FROM nhom4_customer WHERE customer_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(c.getCustomerId());
            return connector.execute(sql_txt, parameters);
        } catch (Exception e) {
            System.out.println("Error in delete(): " + e);
        }
        return false;
    }

    @Override
    public Customer findById(int id) {
        try{
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_customer WHERE customer_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(id);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                Customer c = new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("customer_name"),
                    rs.getString("customer_idnumber"),
                    rs.getString("customer_tel")
                );
                return c;
            }
        } catch (Exception e) {
            System.out.println("Error in findById(): " + e);
        }
        return null;
    }

    public Integer findNewestId() {
        try{
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT customer_id FROM nhom4_customer ORDER BY customer_id DESC LIMIT 1";
            ResultSet rs = connector.query(sql_txt);
            while (rs.next()) {
                return rs.getInt("customer_id");
            }
        } catch (Exception e) {
            System.out.println("Error in findNewestId(): " + e);
            e.printStackTrace();
        }
        return null;
    }

    public Customer findByIdNumber(String idnumber) {
        try{
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_customer WHERE customer_idnumber LIKE ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(idnumber);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("customer_idnumber"),
                        rs.getString("customer_tel")
                );
                return c;
            }
        } catch (Exception e) {
            System.out.println("Error in findByIdNumber(): " + e);
        }
        return null;
    }

    //Search customers by multiple criteria (name, idNumber, tel, checkinDate, checkoutDate)
    public ArrayList<Customer> findCustomer(String cusName, String cusIdNumber, String cusTel, LocalDateTime checkin, LocalDateTime checkout) {
        ArrayList<Customer> ls = new ArrayList<>();
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_customer WHERE customer_id IN (SELECT c.customer_id FROM nhom4_customer c LEFT JOIN nhom4_checkin_out ck ON c.customer_id = ck.customer_id LEFT JOIN nhom4_room r ON ck.checkin_out_id = r.checkin_out_id WHERE c.customer_name LIKE ? AND c.customer_idnumber LIKE ? AND c.customer_tel LIKE ? AND ( (ck.checkin_datetime >= ? AND ck.checkin_datetime <= ?) OR (ck.checkout_datetime >= ? AND ck.checkout_datetime <= ?) OR (ck.checkout_datetime > ? AND ck.checkin_datetime < ?) OR r.checkin_out_id IS NOT NULL ) )";
            ArrayList parameters = new ArrayList<>();
            parameters.add('%' + cusName + '%');
            parameters.add('%' + cusIdNumber + '%');
            parameters.add('%' + cusTel + '%');
            if (checkin == null) checkin =  LocalDateTime.of(1900, 1, 1, 0, 0);
            parameters.add(checkin);
            if (checkout == null) checkout =  LocalDateTime.of(2100, 1, 1, 0, 0);
            parameters.add(checkout);

            parameters.add(checkin);
            parameters.add(checkout);

            parameters.add(checkin);
            parameters.add(checkout);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                ls.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("customer_idnumber"),
                        rs.getString("customer_tel")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in readAll(): " + e);
            e.printStackTrace();
        }
        return ls;
    }
}
