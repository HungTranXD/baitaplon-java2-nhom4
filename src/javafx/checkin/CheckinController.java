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
import java.time.format.DateTimeParseException;
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
    private LocalDate checkinDate;
    private LocalTime checkinTime;
    private LocalDateTime checkinDatetime;
    private LocalDate checkoutDate;
    private LocalTime checkoutTime;
    private LocalDateTime checkoutDatetime;

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



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set init value for date time input:
        dpBookingDateStart.setValue(LocalDate.now());
        txtBookingHourStart.setText("14:00");
        dpBookingDateEnd.setValue(LocalDate.now().plusDays(1));
        txtBookingHourEnd.setText("12:00");

        //Encapsulate process to calculate payment into a separated method (to reuse)
        calculateRoomPayment();

        //Add listener for date time input
        dpBookingDateStart.valueProperty().addListener(observable -> calculateRoomPayment());
        txtBookingHourStart.textProperty().addListener(observable -> calculateRoomPayment());
        dpBookingDateEnd.valueProperty().addListener(observable -> calculateRoomPayment());
        txtBookingHourEnd.textProperty().addListener(observable -> calculateRoomPayment());

        //Initialize room table
        roomBookedNumberCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomBookedTypeCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        roomBookedMoneyCol.setCellValueFactory(new PropertyValueFactory<>("subPayment"));
        roomBookedMoneyCol.setCellFactory(tc -> new TableCell<Room, Double>() {
            @Override
            protected void updateItem(Double payment, boolean empty) {
                super.updateItem(payment, empty);
                if (empty) setText(null);
                else setText(String.format("%,.0f", payment));
            }
        });
        tbvRoomsBooked.setItems(roomsSelected);
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

    void calculateRoomPayment() {
        //Calculate subPayment for each room selected
        checkinDate = dpBookingDateStart.getValue();
        checkinTime = LocalTime.parse(txtBookingHourStart.getText());
        checkinDatetime = LocalDateTime.of(checkinDate, checkinTime);

        checkoutDate = dpBookingDateEnd.getValue();
        checkoutTime = LocalTime.parse(txtBookingHourEnd.getText());
        checkoutDatetime = LocalDateTime.of(checkoutDate, checkoutTime);
        try {
            if (checkoutDatetime.isBefore(checkinDatetime)) throw new Exception("Thời gian nhận phòng phải trước thời gian trả phòng");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.show();
            dpBookingDateStart.setValue(LocalDate.now());
            txtBookingHourStart.setText("14:00");
            dpBookingDateEnd.setValue(LocalDate.now().plusDays(1));
            txtBookingHourEnd.setText("12:00");
        }
        for(Room r: roomsSelected) {
            r.calculateSubPayment(checkinDatetime, checkoutDatetime);
        }

        //Calculate total roomPayment from roomsSelected
        roomPayment = 0.0;
        for(Room r: roomsSelected) {
            roomPayment += r.getSubPayment();
        }
        lbRoomPayment.setText(String.format("%,.0f", roomPayment));
        tbvRoomsBooked.refresh();
    }
    @FXML
    void createCheckin(ActionEvent event) {
        try {
            //Check if time input is correctly formatted (if parse fail it will automatically throw parse Exception)
            LocalTime.parse(txtBookingHourStart.getText());
            LocalTime.parse(txtBookingHourEnd.getText());


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
            if (ckr.create(ck)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Đặt phòng thành công");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    closeWindow(event);
                }
            }

        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Định dạng thời gian không đúng");
            alert.show();
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
