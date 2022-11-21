package entities;

import java.time.LocalDate;

public class Customer {
    private Integer customerId;
    private String customerName;
    private String customerIdNumber;
    private String customerTel;

    /* -------------------------------------------------------------- */
    /* ------------------------ CONSTRUCTORS ------------------------ */
    /* -------------------------------------------------------------- */
    public Customer() {
    }

    public Customer(Integer customerId, String customerName, String customerIdNumber, String customerTel) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerIdNumber = customerIdNumber;
        this.customerTel = customerTel;
    }

    /* -------------------------------------------------------------- */
    /* --------------------- GETTERS & SETTERS ---------------------- */
    /* -------------------------------------------------------------- */
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

    public String getCustomerIdNumber() {
        return customerIdNumber;
    }

    public void setCustomerIdNumber(String customerIdNumber) {
        this.customerIdNumber = customerIdNumber;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    @Override
    public String toString() {
        return getCustomerId() + " - " + getCustomerName() + " - " + getCustomerIdNumber() + " - " + getCustomerTel();
    }
}
