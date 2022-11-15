package factory;

import enums.RepoType;
import impls.BookingRepository;
import impls.CustomerRepository;
import impls.RoomRepository;
import interfaces.IRepository;

public class Factory {
    public static IRepository createRepository(RepoType type) throws IllegalArgumentException {
        switch (type) {
            case ROOM: return new RoomRepository();
            case CUSTOMER: return new CustomerRepository();
            case BOOKING: return new BookingRepository();
            default: throw new IllegalArgumentException("Class not found!");
        }
    }
}
