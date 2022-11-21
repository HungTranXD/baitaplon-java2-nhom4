package javafx.checkin;

import entities.CheckinOut;
import entities.Customer;
import entities.Room;
import entities.Service;
import enums.RepoType;
import factory.Factory;
import impls.CheckinOutRepository;
import impls.CustomerRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.customerExistedConfirmation.CustomerExistedConfirmationController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CheckinController implements Initializable {

    /* ----------------------------------------------------------- */
    /* ----------------------- Checkin info ---------------------- */
    /* ----------------------------------------------------------- */
    @FXML
    private DatePicker dpBookingDateStart;

    @FXML
    private TextField txtBookingHourStart;

    @FXML
    private DatePicker dpBookingDateEnd;

    @FXML
    private TextField txtBookingHourEnd;

    @FXML
    private TableView<Room> tbvRoomsBooked;

    @FXML
    private TableColumn<Room, String> roomBookedNumberCol;

    @FXML
    private TableColumn<Room, String> roomBookedTypeCol;

    @FXML
    private TableColumn<Room, Double> roomBookedMoneyCol;

    //Variable for date time
    static public LocalDate checkinDate;
    static public LocalTime checkinTime;
    static public LocalDate checkoutDate;
    static public LocalTime checkoutTime;

    static public ObservableList<Room> roomsSelected = FXCollections.observableArrayList();

    /* ----------------------------------------------------------- */
    /* ---------------------- Customer info ---------------------- */
    /* ----------------------------------------------------------- */
    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtCustomerIdNumber;

    @FXML
    private TextField txtCustomerTelephone;

    /* ----------------------------------------------------------- */
    /* ---------------------- Payment info ----------------------- */
    /* ----------------------------------------------------------- */
    @FXML
    private Label lbRoomPayment;

    //Variable for payment
    private Double roomPayment;

    public Stage window = new Stage();

    @FXML
    private Button btCloseWindow;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dpBookingDateStart.setValue(checkinDate);
        txtBookingHourStart.setText(checkinTime.toString());
        dpBookingDateEnd.setValue(checkoutDate);
        txtBookingHourEnd.setText(checkoutTime.toString());


        //Initialize room table
        roomBookedNumberCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomBookedTypeCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        roomBookedMoneyCol.setCellValueFactory(new PropertyValueFactory<>("subPayment"));
        tbvRoomsBooked.setItems(roomsSelected);

        //Calculate total roomPayment from roomsSelected
        roomPayment = 0.0;
        for(Room r: roomsSelected) {
            roomPayment += r.getSubPayment();
        }
        lbRoomPayment.setText(roomPayment.toString());
    }


    //Method to display this window
    public void display() throws Exception {
        //Block event from home window
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Checkin");

        Parent root = FXMLLoader.load(getClass().getResource("checkin.fxml"));
        Scene sc = new Scene(root);

        window.setScene(sc);
        window.showAndWait(); //Display window and wait for it to be closed before returning
    }

    //Other methods
    @FXML
    void createCheckin(ActionEvent event) {
        try {
            LocalDateTime checkinDatetime = LocalDateTime.of(checkinDate, checkinTime);
            LocalDateTime checkoutDatetime = LocalDateTime.of(checkoutDate, checkoutTime);

            //Create customer object:
            if (txtCustomerName.getText().isEmpty()) throw new Exception("Chưa nhập tên khách!");
            String customerName = txtCustomerName.getText();
            if (txtCustomerIdNumber.getText().isEmpty()) throw new Exception("Chưa nhập số CMND/Hộ chiếu!");
            String customerIdNumber = txtCustomerIdNumber.getText();
            if (txtCustomerTelephone.getText().isEmpty()) throw new Exception("Chưa nhập SĐT!");
            String customerTel = txtCustomerTelephone.getText();

            //Retrieve the customer with the same IdNumber in database (if exist)
            CustomerExistedConfirmationController.answer = false;
            CustomerRepository cusRepo = (CustomerRepository) Factory.createRepository(RepoType.CUSTOMER);
            if (cusRepo.findByIdNumber(txtCustomerIdNumber.getText()) != null) {
                CustomerExistedConfirmationController.existedCustomer = cusRepo.findByIdNumber(txtCustomerIdNumber.getText());
                CustomerExistedConfirmationController cusExistedController = new CustomerExistedConfirmationController();
                cusExistedController.display();

                if (CustomerExistedConfirmationController.answer) {
                    txtCustomerName.setText(CustomerExistedConfirmationController.existedCustomer.getCustomerName());
                    txtCustomerIdNumber.setText(CustomerExistedConfirmationController.existedCustomer.getCustomerIdNumber());
                    txtCustomerTelephone.setText(CustomerExistedConfirmationController.existedCustomer.getCustomerTel());

                    //Disable all input
                    txtCustomerName.setDisable(true);
                    txtCustomerIdNumber.setDisable(true);
                    txtCustomerTelephone.setDisable(true);

                }
            } else {
                System.out.println("No customer with the IdNumber found");
            }

            Customer customer = new Customer(
                    CustomerExistedConfirmationController.answer ? CustomerExistedConfirmationController.existedCustomer.getCustomerId() : null,
                    customerName,
                    customerIdNumber,
                    customerTel
            );
            System.out.println("Customer id: " + customer.getCustomerId());

            //Create Arraylist for rooms in this checkin
            ArrayList<Room> checkinOutRooms = new ArrayList<>(roomsSelected);

            //Create checkinOut object
            CheckinOut ck = new CheckinOut(
                    null,
                    checkinDatetime,
                    checkoutDatetime,
                    roomPayment,
                    null,
                    null,
                    null,
                    customer,
                    checkinOutRooms,
                    null
            );

            //Call method in repository to add new checkinOut to database
            CheckinOutRepository ckr = (CheckinOutRepository) Factory.createRepository(RepoType.CHECKINOUT);
            if(ckr.create(ck)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Đặt phòng thành công");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    closeWindow(event);
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
