package impls;

import database.Connector;
import entities.Customer;
import entities.Service;
import interfaces.IRepository;

import java.sql.ResultSet;
import java.util.ArrayList;

public class ServiceRepository implements IRepository<Service> {
    @Override
    public ArrayList<Service> readAll() {
        ArrayList<Service> ls = new ArrayList<>();
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_service";
            ResultSet rs = connector.query(sql_txt);
            while (rs.next()) {
                ls.add(new Service(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getString("service_unit"),
                        rs.getDouble("service_fee")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in readAll(): " + e);
            e.printStackTrace();
        }
        return ls;
    }

    @Override
    public Boolean create(Service service) {
        return null;
    }

    @Override
    public Boolean update(Service service) {
        return null;
    }

    @Override
    public Boolean delete(Service service) {
        return null;
    }

    @Override
    public Service findById(int id) {
        try{
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_service WHERE service_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(id);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                Service s = new Service(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getString("service_unit"),
                        rs.getDouble("service_fee")
                );
                return s;
            }
        } catch (Exception e) {
            System.out.println("Error in findById(): " + e);
            e.printStackTrace();
        }
        return null;
    }
}
