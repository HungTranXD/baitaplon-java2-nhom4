package entities;

public class RoomBooking extends Room{
    //Add toMoney field to calculate money based on room type and time when booking
    private Double subPayment;

    public RoomBooking(Integer id, String number, Integer floorId, String floorName, Integer type_id, String type, String typeDescription, Double firstHourPrice, Double nextHourPrice, Double dayPrice, Double earlyCheckinFee1, Double earlyCheckinFee2, Double lateCheckoutFee1, Double lateCheckoutFee2, Double toMoney) {
        super(id, number, floorId, floorName, type_id, type, typeDescription, firstHourPrice, nextHourPrice, dayPrice, earlyCheckinFee1, earlyCheckinFee2, lateCheckoutFee1, lateCheckoutFee2);
        this.subPayment = toMoney;
    }

    public Double getSubPayment() {
        return subPayment;
    }

    public void setSubPayment(Double subPayment) {
        this.subPayment = subPayment;
    }
}
