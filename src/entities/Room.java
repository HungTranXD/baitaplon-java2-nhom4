package entities;

import javafx.createBooking.CreateBookingController;
import javafx.scene.control.CheckBox;

public class Room {
    private Integer id;
    private String number;
    private Integer floorId;
    private String floorName;
    private Integer typeId;
    private String type;
    private String typeDescription;
    private Double firstHourPrice;
    private Double nextHourPrice;
    private Double dayPrice;
    private Double earlyCheckinFee1;
    private Double earlyCheckinFee2;
    private Double lateCheckoutFee1;
    private Double lateCheckoutFee2;
    private Double lateCheckoutFee3;
    private Double monthPrice;
    private CheckBox cbSelectRoom;

    /* -------------------------------------------------------------- */
    /* ------------------------ CONSTRUCTORS ------------------------ */
    /* -------------------------------------------------------------- */
    public Room() {
    }

    public Room(Integer id, String number, Integer floorId, String floorName, Integer type_id, String type, String typeDescription, Double firstHourPrice, Double nextHourPrice, Double dayPrice, Double earlyCheckinFee1, Double earlyCheckinFee2, Double lateCheckoutFee1, Double lateCheckoutFee2, Double lateCheckoutFee3, Double monthPrice) {
        this.id = id;
        this.number = number;
        this.floorId = floorId;
        this.floorName = floorName;
        this.typeId = type_id;
        this.type = type;
        this.typeDescription = typeDescription;
        this.firstHourPrice = firstHourPrice;
        this.nextHourPrice = nextHourPrice;
        this.dayPrice = dayPrice;
        this.earlyCheckinFee1 = earlyCheckinFee1;
        this.earlyCheckinFee2 = earlyCheckinFee2;
        this.lateCheckoutFee1 = lateCheckoutFee1;
        this.lateCheckoutFee2 = lateCheckoutFee2;
        this.lateCheckoutFee3 = lateCheckoutFee3;
        this.monthPrice = monthPrice;
        this.cbSelectRoom = new CheckBox();
        cbSelectRoom.setOnAction(event -> {
            if(cbSelectRoom.isSelected()) {
                CreateBookingController.roomsBooked.add(this);
            } else {
                CreateBookingController.roomsBooked.remove(this);
            }
        });

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getFloorId() {
        return floorId;
    }

    public void setFloorId(Integer floorId) {
        this.floorId = floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Double getLateCheckoutFee3() {
        return lateCheckoutFee3;
    }

    public void setLateCheckoutFee3(Double lateCheckoutFee3) {
        this.lateCheckoutFee3 = lateCheckoutFee3;
    }

    public Double getMonthPrice() {
        return monthPrice;
    }

    public void setMonthPrice(Double monthPrice) {
        this.monthPrice = monthPrice;
    }

    public CheckBox getCbSelectRoom() {
        return cbSelectRoom;
    }

    public void setCbSelectRoom(CheckBox cbSelectRoom) {
        this.cbSelectRoom = cbSelectRoom;
    }


    @Override
    public String toString() {
        return id + "-" + number + "-" + floorName + "-" + type;
    }
}
