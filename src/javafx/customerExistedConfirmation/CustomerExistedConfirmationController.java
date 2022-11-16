package javafx.customerExistedConfirmation;

import entities.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.createBooking.CreateBookingController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.ResourceBundle;

public class CustomerExistedConfirmationController implements Initializable {
    @FXML
    private TableView<Customer> tbvExistedCustomer;

    @FXML
    private TableColumn<Customer, String> tbvColExistedCusName;

    @FXML
    private TableColumn<Customer, String> tbvColExistedCusIdNumber;

    @FXML
    private TableColumn<Customer, String> tbvColExistedCusTel;

    @FXML
    private TableColumn<Customer, String> tbvColExistedCusNationality;

    @FXML
    private Button btYesAndGoBack;

    @FXML
    private Button btNoAndGoBack;

    public static Customer existedCustomer;

    public static boolean answer;
    static Stage ConfirmWindow = new Stage();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tbvColExistedCusName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tbvColExistedCusIdNumber.setCellValueFactory(new PropertyValueFactory<>("customerIdNumber"));
        tbvColExistedCusTel.setCellValueFactory(new PropertyValueFactory<>("customerTel"));
        tbvColExistedCusNationality.setCellValueFactory((new PropertyValueFactory<>("customerNationality")));

        tbvExistedCustomer.setItems(FXCollections.observableArrayList(existedCustomer));
       btYesAndGoBack.setOnAction(event -> {
           answer = true;
           ConfirmWindow.close();
       });
       btNoAndGoBack.setOnAction(event -> {
           answer = false;
           ConfirmWindow.close();
       });

    }


        //Static method to display this window
    public static void display() throws Exception {

        //Block event from home window
//        ConfirmWindow.initModality(Modality.APPLICATION_MODAL);
        ConfirmWindow.setTitle("Confirmation");

        Parent root = FXMLLoader.load(CustomerExistedConfirmationController.class.getResource("customerExistedConfirmation.fxml"));
        Scene sc = new Scene(root);
        ConfirmWindow.setScene(sc);
        ConfirmWindow.showAndWait(); //Display window and wait for it to be closed before returning
    }

}
