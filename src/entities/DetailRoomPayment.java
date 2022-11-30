package entities;

public class DetailRoomPayment {
    private String type;
    private String time;
    private Double toMoney;

    public DetailRoomPayment() {
    }

    public DetailRoomPayment(String type, String time, Double toMoney) {
        this.type = type;
        this.time = time;
        this.toMoney = toMoney;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getToMoney() {
        return toMoney;
    }

    public void setToMoney(Double toMoney) {
        this.toMoney = toMoney;
    }
}
