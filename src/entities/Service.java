package entities;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Service {
    private Integer serviceId;
    private String serviceName;
    private String serviceUnit;
    private Double serviceFee;
    private TextField txtQuantity;
    private Double subPayment;


    public Service() {
    }

    public Service(Integer serviceId, String serviceName, String serviceUnit, Double serviceFee) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceUnit = serviceUnit;
        this.serviceFee = serviceFee;
        this.txtQuantity = new TextField("0");
        txtQuantity.setMaxWidth(50);
        txtQuantity.setAlignment(Pos.CENTER);
        this.subPayment = 0.0;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceUnit() {
        return serviceUnit;
    }

    public void setServiceUnit(String serviceUnit) {
        this.serviceUnit = serviceUnit;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public TextField getTxtQuantity() {
        return txtQuantity;
    }

    public void setTxtQuantity(TextField txtQuantity) {
        this.txtQuantity = txtQuantity;
    }

    public Double getSubPayment() {
        return subPayment;
    }

    public void setSubPayment(Double subPayment) {
        this.subPayment = subPayment;
    }

    //Other methods:
    public void calculateSubPayment() {
        this.setSubPayment( Integer.parseInt(getTxtQuantity().getText()) * getServiceFee());
    }
}


