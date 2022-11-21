package impls;

import database.Connector;
import entities.CheckinOutService;
import entities.Service;
import interfaces.IRepository;

import java.sql.ResultSet;
import java.util.ArrayList;

public class CheckinOutServiceRepository implements IRepository<CheckinOutService> {
    @Override
    public ArrayList<CheckinOutService> readAll() {
        ArrayList<CheckinOutService> ls = new ArrayList<>();
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_checkin_out_service";
            ResultSet rs = connector.query(sql_txt);
            while (rs.next()) {
                ls.add(new CheckinOutService(
                        rs.getInt("checkin_out_service_id"),
                        rs.getInt("checkin_out_id"),
                        rs.getInt("service_id"),
                        rs.getInt("service_quantity"),
                        rs.getDouble("service_subtotal")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in readAll(): " + e);
            e.printStackTrace();
        }
        return ls;
    }

    @Override
    public Boolean create(CheckinOutService checkinOutService) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "INSERT INTO nhom4_checkin_out_service (checkin_out_id, service_id, service_quantity, service_subtotal) VALUES (?,?,?,?)";
            ArrayList parameters = new ArrayList<>();
            parameters.add(checkinOutService.getCheckinOutId());
            parameters.add(checkinOutService.getServiceId());
            parameters.add(checkinOutService.getServiceQuantity());
            parameters.add(checkinOutService.getServiceSubtotal());
            return connector.execute(sql_txt, parameters);
        } catch (Exception e) {
            System.out.println("Error in create(): " + e);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(CheckinOutService checkinOutService) {
        return null;
    }

    @Override
    public Boolean delete(CheckinOutService checkinOutService) {
        return null;
    }

    @Override
    public CheckinOutService findById(int id) {
        return null;
    }

    public ArrayList<CheckinOutService> findByCheckinOutId(int id) {
        ArrayList<CheckinOutService> ls = new ArrayList<>();
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_checkin_out_service WHERE checkin_out_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(id);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                ls.add(new CheckinOutService(
                        rs.getInt("checkin_out_service_id"),
                        rs.getInt("checkin_out_id"),
                        rs.getInt("service_id"),
                        rs.getInt("service_quantity"),
                        rs.getDouble("service_subtotal")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in readAll(): " + e);
            e.printStackTrace();
        }
        return ls;
    }
}
