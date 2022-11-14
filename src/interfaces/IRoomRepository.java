package interfaces;

import entities.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface IRoomRepository {
    ArrayList<Room> readAll();
    Boolean create(Room r);
    Boolean update(Room r);
    Boolean delete(Room r);
    Room findById(int id);
    ArrayList<Room> findByDate(LocalDateTime checkin, LocalDateTime checkout);
    ArrayList<Room> findByBookingId(int bookingId);
}
