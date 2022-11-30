package entities;

import javafx.editCustomer.EditCustomer;
import javafx.scene.control.Button;

public class Customer {
    private Integer customerId;
    private String customerName;
    private String customerIdNumber;
    private String customerTel;
    private Button btEdit;

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
        this.btEdit = new Button("Edit");
        btEdit.getStyleClass().add("button3");
        btEdit.setOnAction(event -> {
            EditCustomer.editingCustomer = this;
            EditCustomer editCustomer = new EditCustomer();
            try {
                editCustomer.display();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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

    public Button getBtEdit() {
        return btEdit;
    }

    public void setBtEdit(Button btEdit) {
        this.btEdit = btEdit;
    }

    @Override
    public String toString() {
        return getCustomerId() + " - " + getCustomerName() + " - " + getCustomerIdNumber() + " - " + getCustomerTel();
    }
}
