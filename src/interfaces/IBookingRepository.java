package interfaces;

import entities.Booking;

import java.util.ArrayList;

public interface IBookingRepository {
    ArrayList<Booking> readAll();
    Boolean create(Booking b);
    Boolean update(Booking b);
    Boolean delete(Booking b);
    Booking findById(int id);
}
