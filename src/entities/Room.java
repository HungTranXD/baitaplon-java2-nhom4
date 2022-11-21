package entities;


import javafx.checkin.CheckinController;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Room {
    private Integer roomId;
    private String roomNumber;
    private String floor;
    private Integer typeId;
    private String typeName;
    private String typeDescription;
    private Double firstHourPrice;
    private Double nextHourPrice;
    private Double dayPrice;
    private Double earlyCheckinFee1;
    private Double earlyCheckinFee2;
    private Double lateCheckoutFee1;
    private Double lateCheckoutFee2;
    
    private Double subPayment;// This field is for calculating payment based on checkin/out time

    private String roomStatus;
    private Integer checkinOutId;
    private LocalDateTime checkinDatetime;
    private LocalDateTime checkoutDatetime;
    private Integer customerId;
    private String customerName;

    private Button removeBtn;

//    //Add flag for selecting room
//    private Boolean selectFlag;


    /* -------------------------------------------------------------- */
    /* ------------------------ CONSTRUCTORS ------------------------ */
    /* -------------------------------------------------------------- */
    public Room() {
    }

    public Room(Integer roomId, String roomNumber, String floor, Integer typeId, String typeName, String typeDescription, Double firstHourPrice, Double nextHourPrice, Double dayPrice, Double earlyCheckinFee1, Double earlyCheckinFee2, Double lateCheckoutFee1, Double lateCheckoutFee2, Integer checkinOutId, LocalDateTime checkinDatetime, LocalDateTime estCheckoutDatetime, Integer customerId, String customerName) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.typeId = typeId;
        this.typeName = typeName;
        this.typeDescription = typeDescription;
        this.firstHourPrice = firstHourPrice;
        this.nextHourPrice = nextHourPrice;
        this.dayPrice = dayPrice;
        this.earlyCheckinFee1 = earlyCheckinFee1;
        this.earlyCheckinFee2 = earlyCheckinFee2;
        this.lateCheckoutFee1 = lateCheckoutFee1;
        this.lateCheckoutFee2 = lateCheckoutFee2;

        this.subPayment = 0.0;
        this.roomStatus = null;

        this.checkinOutId = checkinOutId;
        this.checkinDatetime = checkinDatetime;
        this.checkoutDatetime = estCheckoutDatetime;
        this.customerId = customerId;
        this.customerName = customerName;

        this.removeBtn = new Button();
        removeBtn.setOnAction(event -> {
            CheckinController.roomsSelected.remove(this);
        });
        Image cancelIcon = new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon/cancel.png")));
        ImageView view = new ImageView(cancelIcon);
        removeBtn.setGraphic(view);
        removeBtn.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-cursor: hand");
//        this.selectFlag = false;
    }

    /* -------------------------------------------------------------- */
    /* --------------------- GETTERS & SETTERS ---------------------- */
    /* -------------------------------------------------------------- */
    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public Double getFirstHourPrice() {
        return firstHourPrice;
    }

    public void setFirstHourPrice(Double firstHourPrice) {
        this.firstHourPrice = firstHourPrice;
    }

    public Double getNextHourPrice() {
        return nextHourPrice;
    }

    public void setNextHourPrice(Double nextHourPrice) {
        this.nextHourPrice = nextHourPrice;
    }

    public Double getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(Double dayPrice) {
        this.dayPrice = dayPrice;
    }

    public Double getEarlyCheckinFee1() {
        return earlyCheckinFee1;
    }

    public void setEarlyCheckinFee1(Double earlyCheckinFee1) {
        this.earlyCheckinFee1 = earlyCheckinFee1;
    }

    public Double getEarlyCheckinFee2() {
        return earlyCheckinFee2;
    }

    public void setEarlyCheckinFee2(Double earlyCheckinFee2) {
        this.earlyCheckinFee2 = earlyCheckinFee2;
    }

    public Double getLateCheckoutFee1() {
        return lateCheckoutFee1;
    }

    public void setLateCheckoutFee1(Double lateCheckoutFee1) {
        this.lateCheckoutFee1 = lateCheckoutFee1;
    }

    public Double getLateCheckoutFee2() {
        return lateCheckoutFee2;
    }

    public void setLateCheckoutFee2(Double lateCheckoutFee2) {
        this.lateCheckoutFee2 = lateCheckoutFee2;
    }

    public Double getSubPayment() {
        return subPayment;
    }

    public void setSubPayment(Double subPayment) {
        this.subPayment = subPayment;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Button getRemoveBtn() {
        return removeBtn;
    }

    public void setRemoveBtn(Button removeBtn) {
        this.removeBtn = removeBtn;
    }

//    public Boolean getSelectFlag() {
//        return selectFlag;
//    }
//
//    public void setSelectFlag(Boolean selectFlag) {
//        this.selectFlag = selectFlag;
//    }

    /* -------------------------------------------------------------- */
    /* ----------------------- OTHER METHODS ------------------------ */
    /* -------------------------------------------------------------- */
    public void calculateSubPayment(LocalDateTime checkin, LocalDateTime checkout) {
        long minutesBetween = ChronoUnit.MINUTES.between(checkin, checkout);
        System.out.println(minutesBetween);
        if(minutesBetween <= 360) {
            System.out.println("Hour Price");
            //Calculate according to hour price
            if(minutesBetween < 60) {
                this.subPayment = this.getFirstHourPrice();
            } else {
                this.subPayment = this.getFirstHourPrice() + Math.ceil((minutesBetween - 60.0) / 60.0) * this.getNextHourPrice();
            }
        } else {
            System.out.println("Day Price");
            //Calculate according to day price
            LocalDate ckinDate = checkin.toLocalDate();
            LocalTime ckinTime = checkin.toLocalTime();


            LocalDate ckoutDate = checkout.toLocalDate();
            LocalTime ckoutTime = checkout.toLocalTime();

            LocalTime _0h = LocalTime.of(0,0);
            LocalTime _5h = LocalTime.of(5,0);
            LocalTime _12h = LocalTime.of(12,0,1);
            LocalTime _14h = LocalTime.of(14,0);
            LocalTime _18h = LocalTime.of(18,0,1);

            if (ckinDate.equals(ckoutDate)) {
                System.out.println("TH 1");
                if ( ckinTime.isBefore(_5h)) {
                    System.out.println("TH 1 1");
                    if ( ckoutTime.isBefore(_12h)) {
                        System.out.println("TH 1 1 1");
                        this.subPayment = this.getDayPrice();
                    } else if (ckoutTime.isBefore(_18h)) {
                        System.out.println("TH 1 1 2");
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        this.subPayment = this.getDayPrice() + (hoursLate < 3 ? this.getLateCheckoutFee1() : this.getLateCheckoutFee2());
                    } else {
                        System.out.println("TH 1 1 3");
                        this.subPayment = 2.0 * this.getDayPrice();
                    }
                } else if (ckinTime.isBefore(_14h)) {
                    System.out.println("TH 1 2");
                    if ( ckoutTime.isBefore(_12h)) {
                        System.out.println("TH 1 2 1");
                        this.subPayment = this.getDayPrice();
                    } else if (ckoutTime.isBefore(_14h)) {
                        System.out.println("TH 1 2 2");
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        this.subPayment = this.getDayPrice() + (hoursLate < 3 ? this.getLateCheckoutFee1() : this.getLateCheckoutFee2());
                    } else {
                        System.out.println("TH 1 2 3");
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        this.subPayment = this.getDayPrice() + (hoursEarly < 5 ? this.getEarlyCheckinFee1() : this.getEarlyCheckinFee2());
                    }
                } else {
                    System.out.println("TH 1 3");
                    this.subPayment = this.getDayPrice();
                }
            } else if (ckoutDate.isAfter(ckinDate)) {
                System.out.println("TH 2");
                long dayBetween = ChronoUnit.DAYS.between(ckinDate, ckoutDate);
                if ( ckinTime.isBefore(_5h)) {
                    System.out.println("TH 2 1");
                    if ( ckoutTime.isBefore(_12h)) {
                        System.out.println("TH 2 1 1");
                        this.subPayment = (dayBetween + 1) * this.getDayPrice();
                    } else if (ckoutTime.isBefore(_18h)) {
                        System.out.println("TH 2 1 2");
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        this.subPayment = (dayBetween + 1) * this.getDayPrice() + (hoursLate < 3 ? this.getLateCheckoutFee1() : this.getLateCheckoutFee2());
                    } else {
                        System.out.println("TH 2 1 3");
                        this.subPayment = (dayBetween + 2) * this.getDayPrice();
                    }
                } else if (ckinTime.isBefore(_14h)) {
                    System.out.println("TH 2 2");
                    if ( ckoutTime.isBefore(_12h)) {
                        System.out.println("TH 2 2 1");
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        this.subPayment = dayBetween * this.getDayPrice() + (hoursEarly < 5 ? this.getEarlyCheckinFee1() : this.getEarlyCheckinFee2());
                    } else if (ckoutTime.isBefore(_18h)) {
                        System.out.println("TH 2 2 2");
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        this.subPayment = dayBetween * this.getDayPrice() + (hoursEarly < 5 ? this.getEarlyCheckinFee1() : this.getEarlyCheckinFee2()) + (hoursLate < 3 ? this.getLateCheckoutFee1() : this.getLateCheckoutFee2());
                    } else {
                        System.out.println("TH 2 2 3");
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        this.subPayment = (dayBetween + 1) * this.getDayPrice() + (hoursEarly < 5 ? this.getEarlyCheckinFee1() : this.getEarlyCheckinFee2());
                    }
                } else {
                    System.out.println("TH 2 3");
                    if (ckoutTime.isBefore(_12h)) {
                        System.out.println("TH 2 3 1");
                        this.subPayment = dayBetween * this.getDayPrice();
                    } else if (ckoutTime.isBefore(_18h)) {
                        System.out.println("TH 2 3 2");
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        this.subPayment = dayBetween * this.getDayPrice() + (hoursLate < 3 ? this.getLateCheckoutFee1() : this.getLateCheckoutFee2());
                    } else {
                        System.out.println("TH 2 3 3");
                        this.subPayment = (dayBetween + 1) * this.getDayPrice();
                    }
                }
            } else {
                System.out.println("TH SAI");
            }
        }
    }

    public void checkRoomStatus(){
        if (getCheckinOutId().equals(0)) {
            this.roomStatus = "Trống";
        } else if ( checkoutDatetime.isAfter(LocalDateTime.now()) || checkoutDatetime.equals(LocalDateTime.now()) ) {
            this.roomStatus = "Có khách";
        } else if ( checkoutDatetime.isBefore(LocalDateTime.now()) ) {
            this.roomStatus = "Chưa đi";
        } else {
            this.roomStatus = "Chưa xác định";
        }
    }

    @Override
    public String toString() {
        return getRoomId() + " - " + getRoomNumber() + " - " + getFloor() + " - " + getTypeName() + " - " + getRoomStatus() + " - " + getCheckinOutId() + " - " + getCheckinDatetime() + " - " + getCheckoutDatetime() + " - " + getCustomerId() + " - " + getCustomerName();
    }
}
