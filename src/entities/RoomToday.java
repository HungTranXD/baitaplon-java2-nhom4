package entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RoomToday extends Room{
    private String status;
    private Integer bookingId;
    private LocalDateTime checkin;
    private LocalDateTime checkout;
    private Customer customer;

    public RoomToday() {
    }

    public RoomToday(Integer id, String number, Integer floorId, String floorName, Integer type_id, String type, String typeDescription, Double firstHourPrice, Double nextHourPrice, Double dayPrice, Double earlyCheckinFee1, Double earlyCheckinFee2, Double lateCheckoutFee1, Double lateCheckoutFee2, String status, Integer bookingId, LocalDateTime checkin, LocalDateTime checkout, Customer customer) {
        super(id, number, floorId, floorName, type_id, type, typeDescription, firstHourPrice, nextHourPrice, dayPrice, earlyCheckinFee1, earlyCheckinFee2, lateCheckoutFee1, lateCheckoutFee2);
        this.status = status;
        this.bookingId = bookingId;
        this.checkin = checkin;
        this.checkout = checkout;
        this.customer = customer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDateTime getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDateTime checkin) {
        this.checkin = checkin;
    }

    public LocalDateTime getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDateTime checkout) {
        this.checkout = checkout;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
