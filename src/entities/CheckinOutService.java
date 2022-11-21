package entities;

public class CheckinOutService {
    private Integer checkinOutServiceId;
    private Integer checkinOutId;
    private Integer serviceId;
    private Integer serviceQuantity;
    private Double serviceSubtotal;

    public CheckinOutService() {
    }

    public CheckinOutService(Integer checkinOutServiceId, Integer checkinOutId, Integer serviceId, Integer serviceQuantity, Double serviceSubtotal) {
        this.checkinOutServiceId = checkinOutServiceId;
        this.checkinOutId = checkinOutId;
        this.serviceId = serviceId;
        this.serviceQuantity = serviceQuantity;
        this.serviceSubtotal = serviceSubtotal;
    }

    public Integer getCheckinOutServiceId() {
        return checkinOutServiceId;
    }

    public void setCheckinOutServiceId(Integer checkinOutServiceId) {
        this.checkinOutServiceId = checkinOutServiceId;
    }

    public Integer getCheckinOutId() {
        return checkinOutId;
    }

    public void setCheckinOutId(Integer checkinOutId) {
        this.checkinOutId = checkinOutId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getServiceQuantity() {
        return serviceQuantity;
    }

    public void setServiceQuantity(Integer serviceQuantity) {
        this.serviceQuantity = serviceQuantity;
    }

    public Double getServiceSubtotal() {
        return serviceSubtotal;
    }

    public void setServiceSubtotal(Double serviceSubtotal) {
        this.serviceSubtotal = serviceSubtotal;
    }
}
