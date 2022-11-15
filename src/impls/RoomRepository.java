package impls;

import database.Connector;
import entities.Room;
import entities.RoomBooking;
import interfaces.IRepository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RoomRepository implements IRepository<Room> {
    @Override
    public ArrayList<Room> readAll() {
        ArrayList<Room> ls = new ArrayList<>();
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_room r LEFT JOIN nhom4_floor f ON r.floor_id = f.floor_id LEFT JOIN nhom4_room_type t ON r.type_id = t.type_id";
            ResultSet rs = connector.query(sql_txt);
            while (rs.next()) {
                ls.add(new Room(
                   rs.getInt("room_id"),
                   rs.getString("room_number"),
                   rs.getInt("floor_id"),
                   rs.getString("floor_name"),
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
            System.out.println("Error in readAll(): " + e.getMessage());
        }
        return ls;
    }

    @Override
    public Boolean create(Room r) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "INSERT INTO nhom4_room(room_number, floor_id, type_id) VALUE (?, ?, ?)";
            ArrayList parameters = new ArrayList<>();
            parameters.add(r.getNumber());
            parameters.add(r.getFloorId());
            parameters.add(r.getTypeId());
            return connector.execute(sql_txt, parameters);
        } catch (Exception e) {
            System.out.println("Error in create(): " + e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean update(Room r) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "UPDATE nhom4_room SET room_number = ?, floor_id = ?, type_id = ? WHERE room_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(r.getNumber());
            parameters.add(r.getFloorId());
            parameters.add(r.getTypeId());
            parameters.add(r.getId());
            return connector.execute(sql_txt, parameters);
        } catch (Exception e) {
            System.out.println("Error in update(): " + e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean delete(Room r) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "DELETE FROM nhom4_room WHERE room_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(r.getId());
            return connector.execute(sql_txt, parameters);
        } catch (Exception e) {
            System.out.println("Error in delete(): " + e.getMessage());
        }
        return false;
    }

    @Override
    public Room findById(int id) {
        try{
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_room r LEFT JOIN nhom4_floor f ON r.floor_id = f.floor_id LEFT JOIN nhom4_room_type t ON r.type_id = t.type_id WHERE r.room_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(id);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                Room r = new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getInt("floor_id"),
                    rs.getString("floor_name"),
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
                );
                return r;
            }
        } catch (Exception e) {
            System.out.println("Error in findById(): " + e.getMessage());
        }
        return null;
    }


    public ArrayList<Room> findByDate(LocalDateTime checkin, LocalDateTime checkout) {
        ArrayList<Room> ls = new ArrayList<>();
        try{
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_room r LEFT JOIN nhom4_floor f ON r.floor_id = f.floor_id LEFT JOIN nhom4_room_type t ON r.type_id = t.type_id WHERE r.room_id IN ( SELECT DISTINCT r.room_id FROM nhom4_room r LEFT JOIN nhom4_room_booking rb ON rb.room_id = r.room_id LEFT JOIN nhom4_booking b ON b.booking_id = rb.booking_id AND b.checkin_date < ? AND b.checkout_date > ? WHERE b.booking_id IS NULL );";
            ArrayList parameters = new ArrayList<>();
            parameters.add(checkout);
            parameters.add(checkin);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                ls.add(new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getInt("floor_id"),
                    rs.getString("floor_name"),
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
            System.out.println("Error in findByDate(): " + e);
        }
        return ls;
    }

    public ArrayList<RoomBooking> findByBookingId(int bookingId) {
        ArrayList<RoomBooking> ls = new ArrayList<>();
        try{
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_room_booking rb LEFT JOIN nhom4_room r ON rb.room_id = r.room_id LEFT JOIN nhom4_floor f ON r.floor_id = f.floor_id LEFT JOIN nhom4_room_type t ON r.type_id = t.type_id WHERE booking_id = ?;";
            ArrayList parameters = new ArrayList<>();
            parameters.add(bookingId);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                ls.add(new RoomBooking(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getInt("floor_id"),
                    rs.getString("floor_name"),
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
                    rs.getDouble("sub_payment")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in findByDate(): " + e);
        }
        return ls;
    }
}
