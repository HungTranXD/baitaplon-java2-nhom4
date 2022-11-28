package factory;

import enums.RepoType;
import impls.*;
import interfaces.IRepository;

public class Factory {
    public static IRepository createRepository(RepoType type) throws IllegalArgumentException {
        switch (type) {
            case ROOM: return new RoomRepository();
            case CUSTOMER: return new CustomerRepository();
            case CHECKINOUT: return new CheckinOutRepository();
            case SERVICE: return new ServiceRepository();
            case CHECKINOUTSERVICE: return new CheckinOutServiceRepository();
            case ROOMTYPE: return new RoomTypeRepository();
            default: throw new IllegalArgumentException("Class not found!");
        }
    }
}
