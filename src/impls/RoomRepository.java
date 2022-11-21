package impls;

import database.Connector;
import entities.Room;
import interfaces.IRepository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RoomRepository implements IRepository<Room> {
    @Override
    public ArrayList<Room> readAll() {
        ArrayList<Room> ls = new ArrayList<>();
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_room r LEFT JOIN nhom4_room_type t ON r.type_id = t.type_id LEFT JOIN nhom4_checkin_out ck ON r.checkin_out_id = ck.checkin_out_id LEFT JOIN nhom4_customer c ON ck.customer_id = c.customer_id;";
            ResultSet rs = connector.query(sql_txt);
            while (rs.next()) {
                ls.add(new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("floor"),
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("type_description"),
                        rs.getDouble("first_hour_price"),
                        rs.getDouble("next_hour_price"),
                        rs.getDouble("day_price"),
                        rs.getDouble("early_checkin_fee_1"),
                        rs.getDouble("early_checkin_fee_2"),
                        rs.getDouble("late_checkout_fee_1"),
                        rs.getDouble("late_checkout_fee_2"),
                        rs.getInt("checkin_out_id"),
                        rs.getString("checkin_datetime") == null ? null : LocalDateTime.parse(rs.getString("checkin_datetime").replaceAll("\\s+", "T")),
                        rs.getString("checkout_datetime") == null ? null : LocalDateTime.parse(rs.getString("checkout_datetime").replaceAll("\\s+", "T")),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in readAll(): " + e);
            e.printStackTrace();
        }
        return ls;
    }

    @Override
    public Boolean create(Room r) {
        return false;
    }

    @Override
    public Boolean update(Room r) {
        return false;
    }

    @Override
    public Boolean delete(Room r) {
        return false;
    }

    @Override
    public Room findById(int id) {
        try{
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_room r LEFT JOIN nhom4_room_type t ON r.type_id = t.type_id LEFT JOIN nhom4_checkin_out ck ON r.checkin_out_id = ck.checkin_out_id LEFT JOIN nhom4_customer c ON ck.customer_id = c.customer_id WHERE r.room_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(id);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                Room r = new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("floor"),
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("type_description"),
                        rs.getDouble("first_hour_price"),
                        rs.getDouble("next_hour_price"),
                        rs.getDouble("day_price"),
                        rs.getDouble("early_checkin_fee_1"),
                        rs.getDouble("early_checkin_fee_2"),
                        rs.getDouble("late_checkout_fee_1"),
                        rs.getDouble("late_checkout_fee_2"),
                        rs.getInt("checkin_out_id"),
                        rs.getString("checkin_datetime") == null ? null : LocalDateTime.parse(rs.getString("checkin_datetime").replaceAll("\\s+", "T")),
                        rs.getString("checkout_datetime") == null ? null : LocalDateTime.parse(rs.getString("checkout_datetime").replaceAll("\\s+", "T")),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name")
                );
                return r;
            }
        } catch (Exception e) {
            System.out.println("Error in findById(): " + e);
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Room> findByCheckinOutId(int id) {
        ArrayList<Room> ls = new ArrayList<>();
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_room r LEFT JOIN nhom4_room_type t ON r.type_id = t.type_id LEFT JOIN nhom4_checkin_out ck ON r.checkin_out_id = ck.checkin_out_id LEFT JOIN nhom4_customer c ON ck.customer_id = c.customer_id WHERE ck.checkin_out_id = ?;";
            ArrayList parameters = new ArrayList<>();
            parameters.add(id);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                ls.add(new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("floor"),
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("type_description"),
                        rs.getDouble("first_hour_price"),
                        rs.getDouble("next_hour_price"),
                        rs.getDouble("day_price"),
                        rs.getDouble("early_checkin_fee_1"),
                        rs.getDouble("early_checkin_fee_2"),
                        rs.getDouble("late_checkout_fee_1"),
                        rs.getDouble("late_checkout_fee_2"),
                        rs.getInt("checkin_out_id"),
                        rs.getString("checkin_datetime") == null ? null : LocalDateTime.parse(rs.getString("checkin_datetime").replaceAll("\\s+", "T")),
                        rs.getString("checkout_datetime") == null ? null : LocalDateTime.parse(rs.getString("checkout_datetime").replaceAll("\\s+", "T")),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in readAll(): " + e);
            e.printStackTrace();
        }
        return ls;
    }

    public Boolean updateCheckinOutId(int roomId, int checkinOutId) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "UPDATE nhom4_room SET checkin_out_id = ? WHERE room_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(checkinOutId);
            parameters.add(roomId);
            return connector.execute(sql_txt, parameters);
        } catch (Exception e) {
            System.out.println("Error in updateCheckinOutId(): " + e);
            e.printStackTrace();
        }
        return false;
    }

    public Boolean removeCheckinOutId(int roomId) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "UPDATE nhom4_room SET checkin_out_id = NULL WHERE room_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(roomId);
            return connector.execute(sql_txt, parameters);
        } catch (Exception e) {
            System.out.println("Error in removeCheckinOutId(): " + e);
            e.printStackTrace();
        }
        return false;
    }
}
