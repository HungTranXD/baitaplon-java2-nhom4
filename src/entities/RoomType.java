package entities;

public class RoomType {
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

    public RoomType() {
    }

    public RoomType(Integer typeId, String typeName, String typeDescription, Double firstHourPrice, Double nextHourPrice, Double dayPrice, Double earlyCheckinFee1, Double earlyCheckinFee2, Double lateCheckoutFee1, Double lateCheckoutFee2) {
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
}
