package javafx.editCustomer;

import entities.Customer;
import enums.RepoType;
import factory.Factory;
import impls.CustomerRepository;
import javafx.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.home.HomeController;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCustomer implements Initializable {
    @FXML
    private TextField txtCustomerIdNumber;

    @FXML
    private TextField txtCustomerTelephone;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private Label lbIdNumberValidation;

    private Stage window = new Stage();
    public static Customer editingCustomer;
    private Boolean idNumberExisted = false;


    /* -------------------------------------------------------------------- */
    /* ---------------------------- INITIALIZE ---------------------------- */
    /* -------------------------------------------------------------------- */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtCustomerName.setText(editingCustomer.getCustomerName());
        txtCustomerIdNumber.setText(editingCustomer.getCustomerIdNumber());
        txtCustomerTelephone.setText(editingCustomer.getCustomerTel());

        //Check if idNumber existed
        txtCustomerIdNumber.textProperty().addListener( (observable) -> {
            CustomerRepository cusRepo = (CustomerRepository) Factory.createRepository(RepoType.CUSTOMER);
            if (cusRepo.findByIdNumber(txtCustomerIdNumber.getText()) != null) {
                idNumberExisted = true;
                lbIdNumberValidation.setVisible(true);
            } else {
                idNumberExisted = false;
                lbIdNumberValidation.setVisible(false);
            }
        });
    }


    /* -------------------------------------------------------------------- */
    /* -------------------------- OTHER METHODS --------------------------- */
    /* -------------------------------------------------------------------- */
    @FXML
    void saveAndGoBack(ActionEvent event) {
        if (!idNumberExisted) {
            try {
                if (txtCustomerName.getText().isEmpty()) throw new Exception("Chưa nhập tên khách!");
                String customerName = txtCustomerName.getText();
                if (txtCustomerIdNumber.getText().isEmpty()) throw new Exception("Chưa nhập số CMND/Hộ chiếu!");
                String customerIdNumber = txtCustomerIdNumber.getText();
                if (txtCustomerTelephone.getText().isEmpty()) throw new Exception("Chưa nhập SĐT!");
                String customerTel = txtCustomerTelephone.getText();

                Customer c = new Customer(
                        editingCustomer.getCustomerId(),
                        customerName,
                        customerIdNumber,
                        customerTel
                );

                CustomerRepository cusRepo = (CustomerRepository) Factory.createRepository(RepoType.CUSTOMER);
                if(cusRepo.update(c)) {
                    goBack(event);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Chỉnh sửa không thành công");
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        }

    }

    @FXML
    void goBack(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    //Method to display this window
    public void display() throws Exception{
        //Block event from home window
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Customer");

        Parent root = FXMLLoader.load(getClass().getResource("editCustomer.fxml"));
        Scene sc = new Scene(root);

        window.setScene(sc);
        window.showAndWait(); //Display window and wait for it to be closed before returning
    }

}
