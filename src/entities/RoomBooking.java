package entities;

import javafx.createBooking.CreateBookingController;
import javafx.scene.control.CheckBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class RoomBooking extends Room{
    //Add toMoney field to calculate money based on room type and time when booking
    private Double subPayment;
    //Add check box to select room for booking
    private CheckBox cbSelectRoom;

    public RoomBooking(Integer id, String number, Integer floorId, String floorName, Integer type_id, String type, String typeDescription, Double firstHourPrice, Double nextHourPrice, Double dayPrice, Double earlyCheckinFee1, Double earlyCheckinFee2, Double lateCheckoutFee1, Double lateCheckoutFee2, Double toMoney) {
        super(id, number, floorId, floorName, type_id, type, typeDescription, firstHourPrice, nextHourPrice, dayPrice, earlyCheckinFee1, earlyCheckinFee2, lateCheckoutFee1, lateCheckoutFee2);
        this.subPayment = toMoney;
        this.cbSelectRoom = new CheckBox();

        cbSelectRoom.setOnAction(event -> {
            if (cbSelectRoom.isSelected()) CreateBookingController.roomsBooked.add(this);
            else CreateBookingController.roomsBooked.remove(this);
        });
    }

    public Double getSubPayment() {
        return subPayment;
    }

    public void setSubPayment(Double subPayment) {
        this.subPayment = subPayment;
    }

    public CheckBox getCbSelectRoom() {
        return cbSelectRoom;
    }

    public void setCbSelectRoom(CheckBox cbSelectRoom) {
        this.cbSelectRoom = cbSelectRoom;
    }

    public void calculateSubPayment(LocalDateTime checkin, LocalDateTime checkout) throws Exception{
        long minutesBetween = ChronoUnit.MINUTES.between(checkin, checkout);
        System.out.println(minutesBetween);
        if(minutesBetween <= 360) {
            System.out.println("Hour Price");
            //Calculate according to hour price
            if(minutesBetween < 60) {
                this.setSubPayment(this.getFirstHourPrice());
            } else {
                this.setSubPayment(this.getFirstHourPrice() + Math.ceil((minutesBetween - 60.0) / 60.0) * this.getNextHourPrice());
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
                        this.setSubPayment(this.getDayPrice());
                    } else if (ckoutTime.isBefore(_18h)) {
                        System.out.println("TH 1 1 2");
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        this.setSubPayment(this.getDayPrice() + (hoursLate < 3 ? this.getLateCheckoutFee1() : this.getLateCheckoutFee2()));
                    } else {
                        System.out.println("TH 1 1 3");
                        this.setSubPayment(2.0 * this.getDayPrice());
                    }
                } else if (ckinTime.isBefore(_14h)) {
                    System.out.println("TH 1 2");
                    if ( ckoutTime.isBefore(_12h)) {
                        System.out.println("TH 1 2 1");
                        this.setSubPayment(this.getDayPrice());
                    } else if (ckoutTime.isBefore(_14h)) {
                        System.out.println("TH 1 2 2");
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        this.setSubPayment(this.getDayPrice() + (hoursLate < 3 ? this.getLateCheckoutFee1() : this.getLateCheckoutFee2()));
                    } else {
                        System.out.println("TH 1 2 3");
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        this.setSubPayment(this.getDayPrice() + (hoursEarly < 5 ? this.getEarlyCheckinFee1() : this.getEarlyCheckinFee2()));
                    }
                } else {
                    System.out.println("TH 1 3");
                    this.setSubPayment(this.getDayPrice());
                }
            } else if (ckoutDate.isAfter(ckinDate)) {
                System.out.println("TH 2");
                long dayBetween = ChronoUnit.DAYS.between(ckinDate, ckoutDate);
                if ( ckinTime.isBefore(_5h)) {
                    System.out.println("TH 2 1");
                    if ( ckoutTime.isBefore(_12h)) {
                        System.out.println("TH 2 1 1");
                        this.setSubPayment((dayBetween + 1) * this.getDayPrice());
                    } else if (ckoutTime.isBefore(_18h)) {
                        System.out.println("TH 2 1 2");
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        this.setSubPayment((dayBetween + 1) * this.getDayPrice() + (hoursLate < 3 ? this.getLateCheckoutFee1() : this.getLateCheckoutFee2()));
                    } else {
                        System.out.println("TH 2 1 3");
                        this.setSubPayment((dayBetween + 2) * this.getDayPrice());
                    }
                } else if (ckinTime.isBefore(_14h)) {
                    System.out.println("TH 2 2");
                    if ( ckoutTime.isBefore(_12h)) {
                        System.out.println("TH 2 2 1");
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        this.setSubPayment(dayBetween * this.getDayPrice() + (hoursEarly < 5 ? this.getEarlyCheckinFee1() : this.getEarlyCheckinFee2()));
                    } else if (ckoutTime.isBefore(_18h)) {
                        System.out.println("TH 2 2 2");
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        this.setSubPayment(dayBetween * this.getDayPrice() + (hoursEarly < 5 ? this.getEarlyCheckinFee1() : this.getEarlyCheckinFee2()) + (hoursLate < 3 ? this.getLateCheckoutFee1() : this.getLateCheckoutFee2()));
                    } else {
                        System.out.println("TH 2 2 3");
                        long hoursEarly = ChronoUnit.HOURS.between(ckinTime, _14h);
                        this.setSubPayment((dayBetween + 1) * this.getDayPrice() + (hoursEarly < 5 ? this.getEarlyCheckinFee1() : this.getEarlyCheckinFee2()));
                    }
                } else {
                    System.out.println("TH 2 3");
                    if (ckoutTime.isBefore(_12h)) {
                        System.out.println("TH 2 3 1");
                        this.setSubPayment(dayBetween * this.getDayPrice());
                    } else if (ckoutTime.isBefore(_18h)) {
                        System.out.println("TH 2 3 2");
                        long hoursLate = ChronoUnit.HOURS.between(_12h, ckoutTime);
                        this.setSubPayment(dayBetween * this.getDayPrice() + (hoursLate < 3 ? this.getLateCheckoutFee1() : this.getLateCheckoutFee2()));
                    } else {
                        System.out.println("TH 2 3 3");
                        this.setSubPayment((dayBetween + 1) * this.getDayPrice());
                    }
                }
            } else {
                System.out.println("TH SAI");
                throw new Exception("Fail to calculate subPayment!");
            }
        }
    }
}
