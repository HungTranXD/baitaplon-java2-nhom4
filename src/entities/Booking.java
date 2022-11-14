package entities;

import java.time.LocalDateTime;
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
    private ArrayList<Room> roomsBooked;

    /* -------------------------------------------------------------- */
    /* ------------------------ CONSTRUCTORS ------------------------ */
    /* -------------------------------------------------------------- */
    public Booking() {
    }

    public Booking(Integer id, LocalDateTime timeBooked, LocalDateTime checkinDate, LocalDateTime checkoutDate, LocalDateTime actualCheckinDate, LocalDateTime actualCheckoutDate, Customer customerBooked, Integer paymentId, String paymentMethod, Double paymentTotal, Double paymentPrepaid, Double paymentRemain, ArrayList<Room> roomsBooked) {
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

    public ArrayList<Room> getRoomsBooked() {
        return roomsBooked;
    }

    public void setRoomsBooked(ArrayList<Room> roomsBooked) {
        this.roomsBooked = roomsBooked;
    }
}
