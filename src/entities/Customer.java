package entities;

import java.time.LocalDate;

public class Customer {
    private Integer customerId;
    private String customerName;
    private String customerGender;
    private String customerIdNumber;
    private String customerEmail;
    private String customerTel;
    private LocalDate customerBirthdate;
    private String customerNationality;
    private String customerAddress;

    /* -------------------------------------------------------------- */
    /* ------------------------ CONSTRUCTORS ------------------------ */
    /* -------------------------------------------------------------- */
    public Customer() {
    }

    public Customer(Integer customerId, String customerName, String customerGender, String customerIdNumber, String customerEmail, String customerTel, LocalDate customerBirthdate, String customerNationality, String customerAddress) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerGender = customerGender;
        this.customerIdNumber = customerIdNumber;
        this.customerEmail = customerEmail;
        this.customerTel = customerTel;
        this.customerBirthdate = customerBirthdate;
        this.customerNationality = customerNationality;
        this.customerAddress = customerAddress;
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

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public String getCustomerIdNumber() {
        return customerIdNumber;
    }

    public void setCustomerIdNumber(String customerIdNumber) {
        this.customerIdNumber = customerIdNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public LocalDate getCustomerBirthdate() {
        return customerBirthdate;
    }

    public void setCustomerBirthdate(LocalDate customerBirthdate) {
        this.customerBirthdate = customerBirthdate;
    }

    public String getCustomerNationality() {
        return customerNationality;
    }

    public void setCustomerNationality(String customerNationality) {
        this.customerNationality = customerNationality;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}
