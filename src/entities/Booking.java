package entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Booking {
    private Integer id;
    private LocalDateTime timeBooked;
    private LocalDateTime checkinDate;
    private LocalDateTime checkoutDate;
    private LocalDateTime actualCheckinDate;
    private LocalDateTime actualCheckoutDate;
    private Customer customerBooked;
    private Integer paymentId;
    private String paymentMethod;
    private Double paymentTotal;
    private Double paymentPrepaid;
    private Double paymentRemain;
    private ArrayList<RoomBooking> roomsBooked;

    /* -------------------------------------------------------------- */
    /* ------------------------ CONSTRUCTORS ------------------------ */
    /* -------------------------------------------------------------- */
    public Booking() {
    }

    public Booking(Integer id, LocalDateTime timeBooked, LocalDateTime checkinDate, LocalDateTime checkoutDate, LocalDateTime actualCheckinDate, LocalDateTime actualCheckoutDate, Customer customerBooked, Integer paymentId, String paymentMethod, Double paymentTotal, Double paymentPrepaid, Double paymentRemain, ArrayList<RoomBooking> roomsBooked) {
        this.id = id;
        this.timeBooked = timeBooked;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.actualCheckinDate = actualCheckinDate;
        this.actualCheckoutDate = actualCheckoutDate;
        this.customerBooked = customerBooked;
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.paymentTotal = paymentTotal;
        this.paymentPrepaid = paymentPrepaid;
        this.paymentRemain = paymentRemain;
        this.roomsBooked = roomsBooked;
    }

    /* -------------------------------------------------------------- */
    /* --------------------- GETTERS & SETTERS ---------------------- */
    /* -------------------------------------------------------------- */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTimeBooked() {
        return timeBooked;
    }

    public void setTimeBooked(LocalDateTime timeBooked) {
        this.timeBooked = timeBooked;
    }

    public LocalDateTime getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDateTime checkinDate) {
        this.checkinDate = checkinDate;
    }

    public LocalDateTime getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDateTime checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public LocalDateTime getActualCheckinDate() {
        return actualCheckinDate;
    }

    public void setActualCheckinDate(LocalDateTime actualCheckinDate) {
        this.actualCheckinDate = actualCheckinDate;
    }

    public LocalDateTime getActualCheckoutDate() {
        return actualCheckoutDate;
    }

    public void setActualCheckoutDate(LocalDateTime actualCheckoutDate) {
        this.actualCheckoutDate = actualCheckoutDate;
    }

    public Customer getCustomerBooked() {
        return customerBooked;
    }

    public void setCustomerBooked(Customer customerBooked) {
        this.customerBooked = customerBooked;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getPaymentTotal() {
        return paymentTotal;
    }

    public void setPaymentTotal(Double paymentTotal) {
        this.paymentTotal = paymentTotal;
    }

    public Double getPaymentPrepaid() {
        return paymentPrepaid;
    }

    public void setPaymentPrepaid(Double paymentPrepaid) {
        this.paymentPrepaid = paymentPrepaid;
    }

    public Double getPaymentRemain() {
        return paymentRemain;
    }

    public void setPaymentRemain(Double paymentRemain) {
        this.paymentRemain = paymentRemain;
    }

    public ArrayList<RoomBooking> getRoomsBooked() {
        return roomsBooked;
    }

    public void setRoomsBooked(ArrayList<RoomBooking> roomsBooked) {
        this.roomsBooked = roomsBooked;
    }

    //Calculate subPayment for each room in ArrayList roomBooked
    public void calculateSubPayment() {
        for(RoomBooking rb: getRoomsBooked()) {
            Double firstHourPrice = rb.getFirstHourPrice();
            Double nextHourPrice = rb.getNextHourPrice();
            Double dayPrice = rb.getDayPrice();
            Double earlyCheckinFee1 = rb.getEarlyCheckinFee1();
            Double earlyCheckinFee2 = rb.getEarlyCheckinFee2();
            Double lateCheckoutFee1 = rb.getLateCheckoutFee1();
            Double lateCheckoutFee2 = rb.getLateCheckoutFee2();

            LocalDate ckinDate = getCheckinDate().toLocalDate();
            LocalTime ckinTime = getCheckinDate().toLocalTime();

            LocalDate ckoutDate = getCheckoutDate().toLocalDate();
            LocalTime ckoutTime = getCheckoutDate().toLocalTime();


            LocalTime _0h = LocalTime.of(0,0);
            LocalTime _5h = LocalTime.of(5,0);
            LocalTime _9h = LocalTime.of(9,0);
            LocalTime _12h = LocalTime.of(12,0);
            LocalTime _14h = LocalTime.of(14,0);
            LocalTime _18h = LocalTime.of(18,0);
            LocalTime _23h59 = LocalTime.of(23,59, 59);


            if (ckinDate.equals(ckoutDate)) {
                if (ckinTime.isAfter(_0h) && ckinTime.isBefore(_5h)) {
                    if (ckoutTime.isAfter(_0h) && ckoutTime.isBefore(_12h)) {
                        rb.setSubPayment(rb.getDayPrice());
                    } else if (ckoutTime.isBefore(_18h)) {
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        rb.setSubPayment(rb.getDayPrice() + (hoursLate < 3 ? rb.getLateCheckoutFee1() : rb.getLateCheckoutFee2()));
                    } else {
                        rb.setSubPayment(2.0 * rb.getDayPrice());
                    }
                } else if (ckinTime.isBefore(_14h)) {
                    if (ckoutTime.isAfter(_0h) && ckoutTime.isBefore(_12h)) {
                        rb.setSubPayment(rb.getDayPrice());
                    } else if (ckoutTime.isBefore(_14h)) {
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        rb.setSubPayment(rb.getDayPrice() + (hoursLate < 3 ? rb.getLateCheckoutFee1() : rb.getLateCheckoutFee2()));
                    } else {
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        rb.setSubPayment(rb.getDayPrice() + (hoursEarly < 5 ? rb.getEarlyCheckinFee1() : rb.getEarlyCheckinFee2()));
                    }
                } else {
                    rb.setSubPayment(rb.getDayPrice());
                }
            } else if (ckoutDate.isAfter(ckinDate)) {
                long dayBetween = ChronoUnit.DAYS.between(ckinDate, ckoutDate);
                if (ckinTime.isAfter(_0h) && ckinTime.isBefore(_5h)) {
                    if (ckoutTime.isAfter(_0h) && ckoutTime.isBefore(_12h)) {
                        rb.setSubPayment((dayBetween + 1) * rb.getDayPrice());
                    } else if (ckoutTime.isBefore(_18h)) {
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        rb.setSubPayment((dayBetween + 1) * rb.getDayPrice() + (hoursLate < 3 ? rb.getLateCheckoutFee1() : rb.getLateCheckoutFee2()));
                    } else {
                        rb.setSubPayment((dayBetween + 2) * rb.getDayPrice());
                    }
                } else if (ckinTime.isBefore(_14h)) {
                    if (ckoutTime.isAfter(_0h) && ckoutTime.isBefore(_12h)) {
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        rb.setSubPayment(dayBetween * rb.getDayPrice() + (hoursEarly < 5 ? rb.getEarlyCheckinFee1() : rb.getEarlyCheckinFee2()));
                    } else if (ckoutTime.isBefore(_18h)) {
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        rb.setSubPayment(dayBetween * rb.getDayPrice() + (hoursEarly < 5 ? rb.getEarlyCheckinFee1() : rb.getEarlyCheckinFee2()) + (hoursLate < 3 ? rb.getLateCheckoutFee1() : rb.getLateCheckoutFee2()));
                    } else {
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        rb.setSubPayment((dayBetween + 1) * rb.getDayPrice() + (hoursEarly < 5 ? rb.getEarlyCheckinFee1() : rb.getEarlyCheckinFee2()));
                    }
                }
            }
        }
    }
}
