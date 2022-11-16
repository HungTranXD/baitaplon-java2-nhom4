package impls;

import database.Connector;
import entities.Booking;
import entities.Customer;
import entities.Room;
import entities.RoomBooking;
import enums.RepoType;
import factory.Factory;
import interfaces.IRepository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BookingRepository implements IRepository<Booking> {
    @Override
    public ArrayList<Booking> readAll() {
        ArrayList<Booking> ls = new ArrayList<>();
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_booking b LEFT JOIN nhom4_customer c ON b.customer_id = c.customer_id LEFT JOIN nhom4_payment p ON b.booking_id = p.booking_id;";
            ResultSet rs = connector.query(sql_txt);
            while (rs.next()) {
                Integer id = rs.getInt("booking_id");
                LocalDateTime timeBooked = LocalDateTime.parse(rs.getString("time_booked").replaceAll("\\s+", "T"));
                LocalDateTime checkinDate = LocalDateTime.parse(rs.getString("checkin_date").replaceAll("\\s+", "T"));
                LocalDateTime checkoutDate = LocalDateTime.parse(rs.getString("checkout_date").replaceAll("\\s+", "T"));
                LocalDateTime actualCheckinDate = LocalDateTime.parse(rs.getString("actual_checkin_date").replaceAll("\\s+", "T"));
                LocalDateTime actualCheckoutDate = LocalDateTime.parse(rs.getString("actual_checkout_date").replaceAll("\\s+", "T"));

                //Get the customer
                Integer customerId = rs.getInt("customer_id");
                String customerName = rs.getString("customer_name");
                String customerGender = rs.getString("customer_gender");
                String customerIdNumber = rs.getString("customer_idnumber");
                String customerEmail = rs.getString("customer_email");
                String customerTel = rs.getString("customer_tel");
                LocalDate customerBirthdate = LocalDate.parse(rs.getString("customer_birthdate"));
                String customerNationality = rs.getString("customer_nationality");
                String customerAddress = rs.getString("customer_address");
                Customer customerBooked = new Customer(customerId, customerName, customerGender, customerIdNumber, customerEmail, customerTel, customerBirthdate, customerNationality, customerAddress);

                Integer paymentId = rs.getInt("payment_id");
                String paymentMethod = rs.getString("payment_method");
                Double paymentTotal = rs.getDouble("payment_total");
                Double paymentPrepaid = rs.getDouble("payment_prepaid");
                Double paymentRemain = rs.getDouble("payment_remain");

                //Get all rooms booked in this Booking
                RoomRepository rr = (RoomRepository) Factory.createRepository(RepoType.ROOM);
                ArrayList<RoomBooking> roomsBooked = rr.findByBookingId(id);

                ls.add(new Booking(id, timeBooked, checkinDate, checkoutDate, actualCheckinDate, actualCheckoutDate, customerBooked, paymentId, paymentMethod, paymentTotal, paymentPrepaid, paymentRemain, roomsBooked));
            }
        } catch (Exception e) {
            System.out.println("Error in readAll():" + e);
        }
        return ls;
    }

    @Override
    public Boolean create(Booking b) {
        //We have 2 scenarios:
        // 1) customerId == null -> create new customer into customer table first then create new booking in booking table and new payment and room_booking
        // 2) customerId != null -> which means we successfully retrieved customer with inputted IdNumber in CreateBookingController.java
        try {
            // 1) customerId == null
            if (b.getCustomerBooked().getCustomerId() == null) {
                //create (new) customer in database
                CustomerRepository cr = (CustomerRepository) Factory.createRepository(RepoType.CUSTOMER);
                Boolean createCustomerFlag = cr.create(b.getCustomerBooked());

                //If create customer successful
                if(createCustomerFlag) {
                    System.out.println("Create customer in database successful");
                    //Retrieve the new customer created in database (with new id)
                    Integer retrievedCustomerId = cr.findNewestId();
                    System.out.println(retrievedCustomerId);

                    //Insert new booking into booking table in database
                    Connector connector = Connector.getInstance();
                    String sql_txt_1 = "INSERT INTO nhom4_booking (time_booked, checkin_date, checkout_date, actual_checkin_date, actual_checkout_date, customer_id) VALUES (?,?,?,?,?,?)";
                    ArrayList parameters_1 = new ArrayList<>();
                    parameters_1.add(LocalDateTime.now());
                    parameters_1.add(b.getCheckinDate());
                    parameters_1.add(b.getCheckoutDate());
                    parameters_1.add(b.getActualCheckinDate());
                    parameters_1.add(b.getActualCheckoutDate());
                    parameters_1.add(retrievedCustomerId);
                    Boolean createBookingFlag = connector.execute(sql_txt_1, parameters_1);

                    //If create booking successful
                    if(createBookingFlag) {
                        System.out.println("Create booking in database successful");
                        //Retrieve the newly created booking (by selecting the highest booking_id)
                        int retrievedBookingId = 0;
                        String sql_txt_2 = "SELECT * FROM nhom4_booking ORDER BY booking_id DESC LIMIT 1;";
                        ResultSet rs = connector.query(sql_txt_2);
                        while (rs.next()) {
                            retrievedBookingId = rs.getInt("booking_id");
                        }

                        //Insert new payment into payment table in database
                        String sql_txt_3 = "INSERT INTO nhom4_payment (payment_method, payment_total, payment_prepaid, payment_remain, booking_id) VALUES (?,?,?,?,?)";
                        ArrayList parameters_3 = new ArrayList<>();
                        parameters_3.add(b.getPaymentMethod());
                        parameters_3.add(b.getPaymentTotal());
                        parameters_3.add(b.getPaymentPrepaid());
                        parameters_3.add(b.getPaymentRemain());
                        parameters_3.add(retrievedBookingId);
                        Boolean createPaymentFlag = connector.execute(sql_txt_3, parameters_3);

                        if(createPaymentFlag) {
                            System.out.println("Create payment in database successful");
                            //Insert new room_booking into room_booking table in database
                            for (RoomBooking rb: b.getRoomsBooked()) {
                                String sql_txt_4 = "INSERT INTO nhom4_room_booking (booking_id, room_id, sub_payment) VALUES (?,?,?)";
                                ArrayList parameters_4 = new ArrayList<>();
                                parameters_4.add(retrievedBookingId);
                                parameters_4.add(rb.getId());
                                parameters_4.add(rb.getSubPayment());
                                Boolean createRoomBookingFlag = connector.execute(sql_txt_4,parameters_4);
                                if (!createRoomBookingFlag) return false;
                            }
                            return true;
                        } else {
                            System.out.println("Create payment in database successful");
                            return false;
                        }
                    } else {
                        System.out.println("Create booking in database fail");
                        return false;
                    }
                } else {
                    System.out.println("Create customer in database fail");
                    return false;
                }
            // 2) customerId != null
            } else {
                //Insert new booking into booking table in database
                Connector connector = Connector.getInstance();
                String sql_txt_1 = "INSERT INTO nhom4_booking (time_booked, checkin_date, checkout_date, actual_checkin_date, actual_checkout_date, customer_id) VALUES (?,?,?,?,?,?)";
                ArrayList parameters_1 = new ArrayList<>();
                parameters_1.add(LocalDateTime.now());
                parameters_1.add(b.getCheckinDate());
                parameters_1.add(b.getCheckoutDate());
                parameters_1.add(b.getActualCheckinDate());
                parameters_1.add(b.getActualCheckoutDate());
                parameters_1.add(b.getCustomerBooked().getCustomerId());
                Boolean createBookingFlag = connector.execute(sql_txt_1, parameters_1);

                //If create booking successful
                if(createBookingFlag) {
                    System.out.println("Create booking in database successful");
                    //Retrieve the newly created booking (by selecting the highest booking_id)
                    int retrievedBookingId = 0;
                    String sql_txt_2 = "SELECT * FROM nhom4_booking ORDER BY booking_id DESC LIMIT 1;";
                    ResultSet rs = connector.query(sql_txt_2);
                    while (rs.next()) {
                        retrievedBookingId = rs.getInt("booking_id");
                    }

                    //Insert new payment into payment table in database
                    String sql_txt_3 = "INSERT INTO nhom4_payment (payment_method, payment_total, payment_prepaid, payment_remain, booking_id) VALUES (?,?,?,?,?)";
                    ArrayList parameters_3 = new ArrayList<>();
                    parameters_3.add(b.getPaymentMethod());
                    parameters_3.add(b.getPaymentTotal());
                    parameters_3.add(b.getPaymentPrepaid());
                    parameters_3.add(b.getPaymentRemain());
                    parameters_3.add(retrievedBookingId);
                    Boolean createPaymentFlag = connector.execute(sql_txt_3, parameters_3);

                    if(createPaymentFlag) {
                        System.out.println("Create payment in database successful");
                        //Insert new room_booking into room_booking table in database
                        for (RoomBooking rb: b.getRoomsBooked()) {
                            String sql_txt_4 = "INSERT INTO nhom4_room_booking (booking_id, room_id, sub_payment) VALUES (?,?,?)";
                            ArrayList parameters_4 = new ArrayList<>();
                            parameters_4.add(retrievedBookingId);
                            parameters_4.add(rb.getId());
                            parameters_4.add(rb.getSubPayment());
                            Boolean createRoomBookingFlag = connector.execute(sql_txt_4,parameters_4);
                            if (!createRoomBookingFlag) return false;
                        }
                        return true;
                    } else {
                        System.out.println("Create payment in database successful");
                        return false;
                    }
                } else {
                    System.out.println("Create booking in database fail");
                    return false;
                }
            }

        } catch (Exception e) {
            System.out.println("Error in create(): " + e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean update(Booking b) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "UPDATE nhom4_booking SET time_booked = ?, checkin_date = ?, checkout_date = ?, actual_checkin_date= ?, actual_checkout_date= ?,customer_id= ? WHERE booking_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(b.getTimeBooked());
            parameters.add(b.getCheckinDate());
            parameters.add(b.getCheckoutDate());
            parameters.add(b.getActualCheckinDate());
            parameters.add(b.getActualCheckoutDate());
            parameters.add(b.getCustomerBooked().getCustomerId());
            parameters.add(b.getId());
            return connector.execute(sql_txt, parameters);
        } catch (Exception e) {
            System.out.println("Error in update(): " + e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean delete(Booking b) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt_1 = "DELETE FROM nhom4_payment WHERE booking_id = ?";
            ArrayList parameters = new ArrayList<>();
            parameters.add(b.getId());
            if(connector.execute(sql_txt_1,parameters)) {
                String sql_txt_2 = "DELETE FROM nhom4_room_booking WHERE booking_id = ?";
                if (connector.execute(sql_txt_2, parameters)) {
                    String sql_txt_3 = "DELETE FROM nhom4_booking WHERE booking_id = ?";
                    return connector.execute(sql_txt_3,parameters);
                } else return false;
            } else return false;
        } catch (Exception e) {
            System.out.println("Error in delete(): " + e.getMessage());
        }
        return false;
    }

    @Override
    public Booking findById(int bookingId) {
        try {
            Connector connector = Connector.getInstance();
            String sql_txt = "SELECT * FROM nhom4_booking b LEFT JOIN nhom4_customer c ON b.customer_id = c.customer_id LEFT JOIN nhom4_payment p ON b.booking_id = p.booking_id WHERE b.booking_id = ?;";
            ArrayList parameters = new ArrayList<>();
            parameters.add(bookingId);
            ResultSet rs = connector.query(sql_txt, parameters);
            while (rs.next()) {
                Integer id = rs.getInt("booking_id");
                LocalDateTime timeBooked = LocalDateTime.parse(rs.getString("time_booked").replaceAll("\\s+", "T"));
                LocalDateTime checkinDate = LocalDateTime.parse(rs.getString("checkin_date").replaceAll("\\s+", "T"));
                LocalDateTime checkoutDate = LocalDateTime.parse(rs.getString("checkout_date").replaceAll("\\s+", "T"));
                LocalDateTime actualCheckinDate = LocalDateTime.parse(rs.getString("actual_checkin_date").replaceAll("\\s+", "T"));
                LocalDateTime actualCheckoutDate = LocalDateTime.parse(rs.getString("actual_checkout_date").replaceAll("\\s+", "T"));

                //Get the customer
                Integer customerId = rs.getInt("customer_id");
                String customerName = rs.getString("customer_name");
                String customerGender = rs.getString("customer_gender");
                String customerIdNumber = rs.getString("customer_idnumber");
                String customerEmail = rs.getString("customer_email");
                String customerTel = rs.getString("customer_tel");
                LocalDate customerBirthdate = LocalDate.parse(rs.getString("customer_birthdate"));
                String customerNationality = rs.getString("customer_nationality");
                String customerAddress = rs.getString("customer_address");
                Customer customerBooked = new Customer(customerId, customerName, customerGender, customerIdNumber, customerEmail, customerTel, customerBirthdate, customerNationality, customerAddress);

                Integer paymentId = rs.getInt("payment_id");
                String paymentMethod = rs.getString("payment_method");
                Double paymentTotal = rs.getDouble("payment_total");
                Double paymentPrepaid = rs.getDouble("payment_prepaid");
                Double paymentRemain = rs.getDouble("payment_remain");

                //Get all rooms booked in this Booking
                RoomRepository rr = (RoomRepository) Factory.createRepository(RepoType.ROOM);
                ArrayList<RoomBooking> roomsBooked = rr.findByBookingId(id);

                Booking b = new Booking(id, timeBooked, checkinDate, checkoutDate, actualCheckinDate, actualCheckoutDate, customerBooked, paymentId, paymentMethod, paymentTotal, paymentPrepaid, paymentRemain, roomsBooked);
                return b;
            }
        } catch (Exception e) {
            System.out.println("Error in findById():" + e);
        }
        return null;
    }
}
