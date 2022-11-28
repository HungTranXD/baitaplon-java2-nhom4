package impls;

import database.Connector;
import entities.RoomType;
import interfaces.IRepository;

import java.sql.ResultSet;
import java.util.ArrayList;

public class RoomTypeRepository implements IRepository<RoomType> {
    @Override
    public ArrayList<RoomType> readAll() {
        ArrayList<RoomType> ls = new ArrayList<>();
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_room_type";
            ResultSet rs = connector.query(sql_txt);
            while (rs.next()) {
                ls.add(new RoomType(
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("type_description"),
                        rs.getDouble("first_hour_price"),
                        rs.getDouble("next_hour_price"),
                        rs.getDouble("day_price"),
                        rs.getDouble("early_checkin_fee_1"),
                        rs.getDouble("early_checkin_fee_2"),
                        rs.getDouble("late_checkout_fee_1"),
                        rs.getDouble("late_checkout_fee_2")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in readAll(): " + e);
            e.printStackTrace();
        }
        return ls;
    }

    @Override
    public Boolean create(RoomType roomType) {
        return null;
    }

    @Override
    public Boolean update(RoomType roomType) {
        return null;
    }

    @Override
    public Boolean delete(RoomType roomType) {
        return null;
    }

    @Override
    public RoomType findById(int id) {
        return null;
    }
}
