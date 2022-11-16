package impls;

import database.Connector;
import entities.Customer;
import interfaces.IRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
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
                        rs.getString("customer_gender"),
                        rs.getString("customer_idnumber"),
                        rs.getString("customer_email"),
                        rs.getString("customer_tel"),
                        LocalDate.parse(rs.getString("customer_birthdate")),
                        rs.getString("customer_nationality"),
                        rs.getString("customer_address")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in readAll(): " + e.getMessage());
        }
        return ls;
    }

    @Override
    public Boolean create(Customer c) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "INSERT INTO nhom4_customer(customer_name, customer_gender, customer_idnumber, customer_email, customer_tel, customer_birthdate, customer_nationality, customer_address) VALUES (?,?,?,?,?,?,?,?)";
            ArrayList parameters = new ArrayList<>();
            parameters.add(c.getCustomerName());
            parameters.add(c.getCustomerGender());
            parameters.add(c.getCustomerIdNumber());
            parameters.add(c.getCustomerEmail());
            parameters.add(c.getCustomerTel());
            parameters.add(c.getCustomerBirthdate());
            parameters.add(c.getCustomerNationality());
            parameters.add(c.getCustomerAddress());
            return connector.execute(sql_txt, parameters);
        } catch (SQLException e) {
            System.out.println("Error in create(): " + e.getErrorCode());
        } catch (Exception e) {
            System.out.println("Error in create(): " + e);
        }
        return false;
    }

    @Override
    public Boolean update(Customer c) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "UPDATE nhom4_customer SET customer_name = ?, customer_gender = ?, customer_idnumber = ?, customer_email = ?, customer_tel = ?, customer_birthdate = ?, customer_nationality = ?, customer_address = ? WHERE customer_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(c.getCustomerName());
            parameters.add(c.getCustomerGender());
            parameters.add(c.getCustomerIdNumber());
            parameters.add(c.getCustomerEmail());
            parameters.add(c.getCustomerTel());
            parameters.add(c.getCustomerBirthdate());
            parameters.add(c.getCustomerNationality());
            parameters.add(c.getCustomerAddress());
            parameters.add(c.getCustomerId());
            return connector.execute(sql_txt, parameters);
        } catch (Exception e) {
            System.out.println("Error in update(): " + e.getMessage());
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
            System.out.println("Error in delete(): " + e.getMessage());
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
                    rs.getString("customer_gender"),
                    rs.getString("customer_idnumber"),
                    rs.getString("customer_email"),
                    rs.getString("customer_tel"),
                    LocalDate.parse(rs.getString("customer_birthdate")),
                    rs.getString("customer_nationality"),
                    rs.getString("customer_address")
                );
                return c;
            }
        } catch (Exception e) {
            System.out.println("Error in findById(): " + e.getMessage());
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
//        ArrayList<Customer> ls = new ArrayList<>();
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
                        rs.getString("customer_gender"),
                        rs.getString("customer_idnumber"),
                        rs.getString("customer_email"),
                        rs.getString("customer_tel"),
                        rs.getString("customer_birthdate") == null ? null : LocalDate.parse(rs.getString("customer_birthdate")),
//                        null,
                        rs.getString("customer_nationality"),
                        rs.getString("customer_address")
                );
                return c;
            }
        } catch (Exception e) {
            System.out.println("Error in findByIdNumber(): " + e.getCause());
        }
        return null;
    }
}
