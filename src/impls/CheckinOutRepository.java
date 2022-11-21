package impls;

import database.Connector;
import entities.CheckinOut;
import entities.CheckinOutService;
import entities.Customer;
import entities.Room;
import enums.RepoType;
import factory.Factory;
import interfaces.IRepository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CheckinOutRepository implements IRepository<CheckinOut> {
    @Override
    public ArrayList<CheckinOut> readAll() {
        ArrayList<CheckinOut> ls = new ArrayList<>();
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_checkin_out ck LEFT JOIN nhom4_customer c ON ck.customer_id = c.customer_id;";
            ResultSet rs = connector.query(sql_txt);
            while (rs.next()) {
                Integer checkinOutId = rs.getInt("checkin_out_id");
                LocalDateTime checkinDatetime = rs.getString("checkin_datetime") == null ? null : LocalDateTime.parse(rs.getString("checkin_datetime").replaceAll("\\s+", "T"));
                LocalDateTime checkoutDatetime = rs.getString("checkout_datetime") == null ? null : LocalDateTime.parse(rs.getString("checkout_datetime").replaceAll("\\s+", "T"));
                Double roomPayment = rs.getDouble("room_payment");
                Double servicePayment = rs.getDouble("service_payment");
                Double totalPayment = rs.getDouble("total_payment");
                String paymentMethod = rs.getString("payment_method");

                //Get the customer of this checkinOut
                Integer customerId = rs.getInt("customer_id");
                String customerName = rs.getString("customer_name");
                String customerIdNumber = rs.getString("customer_idnumber");
                String customerTel = rs.getString("customer_tel");
                Customer c = new Customer(customerId, customerName, customerIdNumber, customerTel);


                //Get all rooms in this checkinOut
                RoomRepository rr = (RoomRepository) Factory.createRepository(RepoType.ROOM);
                ArrayList<Room> checkinOutRooms = rr.findByCheckinOutId(checkinOutId);

                //Get all checkinOutServices in this checkinOut
                CheckinOutServiceRepository cksr = (CheckinOutServiceRepository) Factory.createRepository(RepoType.CHECKINOUTSERVICE);
                ArrayList<CheckinOutService> checkinOutServices = cksr.findByCheckinOutId(checkinOutId);


                ls.add(new CheckinOut(
                        checkinOutId,
                        checkinDatetime,
                        checkoutDatetime,
                        roomPayment,
                        servicePayment,
                        totalPayment,
                        paymentMethod,
                        c,
                        checkinOutRooms,
                        checkinOutServices
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in readAll():" + e);
            e.printStackTrace();
        }
        return ls;
    }

    @Override
    public Boolean create(CheckinOut ck) {
        //We have 2 scenarios:
        // 1) customerId == null -> create new customer into customer table first then create new checkinOut and new checkinOutService
        // 2) customerId != null -> which means we successfully retrieved customer with inputted IdNumber
        try {
            // 1) customerId == null
            if (ck.getCustomer().getCustomerId() == null) {
                //create (new) customer in database
                CustomerRepository cr = (CustomerRepository) Factory.createRepository(RepoType.CUSTOMER);
                //If create customer successful
                if( cr.create(ck.getCustomer()) ) {
                    System.out.println("Create customer in database successful");
                    //Retrieve the new customer created in database (with new id)
                    Integer retrievedCustomerId = cr.findNewestId();
                    System.out.println(retrievedCustomerId);

                    //Insert new checkinOut into checkin_out table in database
                    Connector connector = Connector.getInstance();
                    String sql_txt_1 = "INSERT INTO nhom4_checkin_out(customer_id, checkin_datetime, checkout_datetime, room_payment, service_payment, total_payment, payment_method) VALUES (?,?,?,?,?,?,?)";
                    ArrayList parameters_1 = new ArrayList<>();
                    parameters_1.add(retrievedCustomerId);
                    parameters_1.add(ck.getCheckinDatetime());
                    parameters_1.add(ck.getCheckoutDatetime());
                    parameters_1.add(ck.getRoomPayment());
                    parameters_1.add(ck.getServicePayment());
                    parameters_1.add(ck.getTotalPayment());
                    parameters_1.add(ck.getPaymentMethod());

                    //If create checkinOut successful
                    if( connector.execute(sql_txt_1, parameters_1) ) {
                        System.out.println("Create checkinOut in database successful");
                        //Retrieve the newly created checkinOut (by selecting the highest booking_id)
                        int retrievedcheckinOutId = 0;
                        String sql_txt_2 = "SELECT * FROM nhom4_checkin_out ORDER BY checkin_out_id DESC LIMIT 1;";
                        ResultSet rs = connector.query(sql_txt_2);
                        while (rs.next()) {
                            retrievedcheckinOutId = rs.getInt("checkin_out_id");
                        }

                        //Update room table with new checkinOutId
                        RoomRepository rr = (RoomRepository) Factory.createRepository(RepoType.ROOM);
                        for(Room r: ck.getCheckinOutRooms()) {
                            if(!rr.updateCheckinOutId(r.getRoomId(), retrievedcheckinOutId)) return false;
                        }
//
//                        //Insert new checkinOutService into checkin_out_service table in database
//                        for(CheckinOutService cks: ck.getCheckinOutServices()) {
//                            CheckinOutServiceRepository checkinOutServiceRepository = (CheckinOutServiceRepository) Factory.createRepository(RepoType.CHECKINOUTSERVICE);
//                            if( checkinOutServiceRepository.create(cks) ) return false;
//                        }
                        return true;

                    } else {
                        System.out.println("Create checkinOut in database fail");
                        return false;
                    }
                } else {
                    System.out.println("Create customer in database fail");
                    return false;
                }
            // 2) customerId != null
            } else {
                //Insert new checkinOut into checkin_out table in database
                Connector connector = Connector.getInstance();
                String sql_txt_1 = "INSERT INTO nhom4_checkin_out(customer_id, checkin_datetime, checkout_datetime, room_payment, service_payment, total_payment, payment_method) VALUES (?,?,?,?,?,?,?)";
                ArrayList parameters_1 = new ArrayList<>();
                parameters_1.add(ck.getCustomer().getCustomerId());
                parameters_1.add(ck.getCheckinDatetime());
                parameters_1.add(ck.getCheckoutDatetime());
                parameters_1.add(ck.getRoomPayment());
                parameters_1.add(ck.getServicePayment());
                parameters_1.add(ck.getTotalPayment());
                parameters_1.add(ck.getPaymentMethod());

                //If create checkinOut successful
                if( connector.execute(sql_txt_1, parameters_1) ) {
                    System.out.println("Create checkinOut in database successful");
                    //Retrieve the newly created checkinOut (by selecting the highest booking_id),
                    int retrievedcheckinOutId = 0;
                    String sql_txt_2 = "SELECT * FROM nhom4_checkin_out ORDER BY checkin_out_id DESC LIMIT 1;";
                    ResultSet rs = connector.query(sql_txt_2);
                    while (rs.next()) {
                        retrievedcheckinOutId = rs.getInt("checkin_out_id");
                    }

                    //Update room table with new checkinOutId
                    RoomRepository rr = (RoomRepository) Factory.createRepository(RepoType.ROOM);
                    for(Room r: ck.getCheckinOutRooms()) {
                        if(!rr.updateCheckinOutId(r.getRoomId(), retrievedcheckinOutId)) return false;
                    }
//
//                    //Insert new checkinOutService into checkin_out_service table in database
//                    for(CheckinOutService cks: ck.getCheckinOutServices()) {
//                        CheckinOutServiceRepository checkinOutServiceRepository = (CheckinOutServiceRepository) Factory.createRepository(RepoType.CHECKINOUTSERVICE);
//                        if( checkinOutServiceRepository.create(cks) ) return false;
//                    }
                    return true; //If all loop create successfully then return true

                } else {
                    System.out.println("Create checkinOut in database fail");
                    return false;
                }
            }

        } catch (Exception e) {
            System.out.println("Error in create(): " + e);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(CheckinOut ck) {
        try {
            // - 1) Get the checkinOutServices array in ck and insert each checkinOutService as new line in checkin_out_service in database
            for (CheckinOutService ckService: ck.getCheckinOutServices()) {
                CheckinOutServiceRepository ckServiceRepository = (CheckinOutServiceRepository) Factory.createRepository(RepoType.CHECKINOUTSERVICE);
                if( !ckServiceRepository.create(ckService) ) return false;
            }
            System.out.println("Create checkinOutService in database successful");

            // - 2) Update checkin_out table in database with new checkout_datetime, room_payment, service_payment, payment_method
            Connector connector = Connector.getInstance();
            String sql_txt = "UPDATE nhom4_checkin_out SET customer_id=?,checkin_datetime=?,checkout_datetime=?,room_payment=?,service_payment=?,total_payment=?,payment_method=? WHERE checkin_out_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(ck.getCustomer().getCustomerId());
            parameters.add(ck.getCheckinDatetime());
            parameters.add(ck.getCheckoutDatetime());
            parameters.add(ck.getRoomPayment());
            parameters.add(ck.getServicePayment());
            parameters.add(ck.getTotalPayment());
            parameters.add(ck.getPaymentMethod());
            parameters.add(ck.getCheckinOutId());
            if (connector.execute(sql_txt, parameters)) {
                System.out.println("Update checkinOut successful");

                // - 3) Update checkinOutId in room table to null (remove checkinOutId so the room status can be changed to EMPTY
                for(Room r: ck.getCheckinOutRooms()) {
                    RoomRepository rr = (RoomRepository) Factory.createRepository(RepoType.ROOM);
                    if ( !rr.removeCheckinOutId(r.getRoomId()) ) return false;
                }
                System.out.println("Remove checkinOutId in rooms successful");
                return true;
            } else {
                System.out.println("Update checkinOut fail");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error in update(): " + e);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(CheckinOut ck) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt_1 = "DELETE FROM nhom4_checkin_out_service WHERE checkin_out_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(ck.getCheckinOutId());
            if(connector.execute(sql_txt_1,parameters)) {
                String sql_txt_2 = "DELETE FROM nhom4_checkin_out WHERE checkin_out_id = ?";
                return connector.execute(sql_txt_2, parameters);
            } else return false;
        } catch (Exception e) {
            System.out.println("Error in delete(): " + e);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public CheckinOut findById(int id) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_checkin_out ck LEFT JOIN nhom4_customer c ON ck.customer_id = c.customer_id WHERE ck.checkin_out_id = ?;";
            ArrayList parameters = new ArrayList<>();
            parameters.add(id);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                Integer checkinOutId = rs.getInt("checkin_out_id");
                LocalDateTime checkinDatetime = rs.getString("checkin_datetime") == null ? null : LocalDateTime.parse(rs.getString("checkin_datetime").replaceAll("\\s+", "T"));
                LocalDateTime checkoutDatetime = rs.getString("checkout_datetime") == null ? null : LocalDateTime.parse(rs.getString("checkout_datetime").replaceAll("\\s+", "T"));
                Double roomPayment = rs.getDouble("room_payment");
                Double servicePayment = rs.getDouble("service_payment");
                Double totalPayment = rs.getDouble("total_payment");
                String paymentMethod = rs.getString("payment_method");

                //Get the customer of this checkinOut
                Integer customerId = rs.getInt("customer_id");
                String customerName = rs.getString("customer_name");
                String customerIdNumber = rs.getString("customer_idnumber");
                String customerTel = rs.getString("customer_tel");
                Customer c = new Customer(customerId, customerName, customerIdNumber, customerTel);


                //Get all rooms in this checkinOut
                RoomRepository rr = (RoomRepository) Factory.createRepository(RepoType.ROOM);
                ArrayList<Room> checkinOutRooms = rr.findByCheckinOutId(checkinOutId);

                //Get all checkinOutServices in this checkinOut
                CheckinOutServiceRepository cksr = (CheckinOutServiceRepository) Factory.createRepository(RepoType.CHECKINOUTSERVICE);
                ArrayList<CheckinOutService> checkinOutServices = cksr.findByCheckinOutId(checkinOutId);


                CheckinOut ck = new CheckinOut(
                        checkinOutId,
                        checkinDatetime,
                        checkoutDatetime,
                        roomPayment,
                        servicePayment,
                        totalPayment,
                        paymentMethod,
                        c,
                        checkinOutRooms,
                        checkinOutServices
                );
                return ck;
            }
        } catch (Exception e) {
            System.out.println("Error in readAll():" + e);
            e.printStackTrace();
        }
        return null;
    }

}
