package entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class CheckinOut {
    private Integer checkinOutId;
    private LocalDateTime checkinDatetime;
    private LocalDateTime checkoutDatetime;
    private Double roomPayment;
    private Double servicePayment;
    private Double totalPayment;
    private String paymentMethod;

    private Customer customer;
    private ArrayList<Room> checkinOutRooms =new ArrayList<>();
    private ArrayList<CheckinOutService> checkinOutServices = new ArrayList<>();

    /* -------------------------------------------------------------- */
    /* ------------------------ CONSTRUCTORS ------------------------ */
    /* -------------------------------------------------------------- */
    public CheckinOut() {
    }

    public CheckinOut(Integer checkinOutId, LocalDateTime checkinDatetime, LocalDateTime checkoutDatetime, Double roomPayment, Double servicePayment, Double totalPayment, String paymentMethod, Customer customer, ArrayList<Room> checkinOutRooms, ArrayList<CheckinOutService> checkinOutServices) {
        this.checkinOutId = checkinOutId;
        this.checkinDatetime = checkinDatetime;
        this.checkoutDatetime = checkoutDatetime;
        this.roomPayment = roomPayment;
        this.servicePayment = servicePayment;
        this.totalPayment = totalPayment;
        this.paymentMethod = paymentMethod;
        this.customer = customer;
        this.checkinOutRooms = checkinOutRooms;
        this.checkinOutServices = checkinOutServices;
    }

    /* -------------------------------------------------------------- */
    /* --------------------- GETTERS & SETTERS ---------------------- */
    /* -------------------------------------------------------------- */
    public Integer getCheckinOutId() {
        return checkinOutId;
    }

    public void setCheckinOutId(Integer checkinOutId) {
        this.checkinOutId = checkinOutId;
    }

    public LocalDateTime getCheckinDatetime() {
        return checkinDatetime;
    }

    public void setCheckinDatetime(LocalDateTime checkinDatetime) {
        this.checkinDatetime = checkinDatetime;
    }

    public LocalDateTime getCheckoutDatetime() {
        return checkoutDatetime;
    }

    public void setCheckoutDatetime(LocalDateTime checkoutDatetime) {
        this.checkoutDatetime = checkoutDatetime;
    }

    public Double getRoomPayment() {
        return roomPayment;
    }

    public void setRoomPayment(Double roomPayment) {
        this.roomPayment = roomPayment;
    }

    public Double getServicePayment() {
        return servicePayment;
    }

    public void setServicePayment(Double servicePayment) {
        this.servicePayment = servicePayment;
    }

    public Double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<Room> getCheckinOutRooms() {
        return checkinOutRooms;
    }

    public void setCheckinOutRooms(ArrayList<Room> checkinOutRooms) {
        this.checkinOutRooms = checkinOutRooms;
    }

    public ArrayList<CheckinOutService> getCheckinOutServices() {
        return checkinOutServices;
    }

    public void setCheckinOutServices(ArrayList<CheckinOutService> checkinOutServices) {
        this.checkinOutServices = checkinOutServices;
    }

    @Override
    public String toString() {
        return checkinOutId + " - " + checkinDatetime + " - " + checkoutDatetime + " - " + roomPayment + " - " + servicePayment + " - " + totalPayment + " - " + paymentMethod + customer + " - " + checkinOutRooms + " - " + checkinOutServices;
    }
}
