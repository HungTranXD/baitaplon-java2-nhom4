package javafx.createBooking;

import entities.Booking;
import entities.Customer;
import entities.Room;
import entities.RoomBooking;
import enums.RepoType;
import factory.Factory;
import impls.BookingRepository;
import impls.CustomerRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.customerExistedConfirmation.CustomerExistedConfirmationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CreateBookingController implements Initializable {

    /* ----------------------------------------------------------- */
    /* ----------------------- Booking info ---------------------- */
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
    private TableView<RoomBooking> tbvRoomsBooked;

    @FXML
    private TableColumn<RoomBooking, String> roomBookedNumberCol;

    @FXML
    private TableColumn<RoomBooking, String> roomBookedTypeCol;

    @FXML
    private TableColumn<RoomBooking, Double> roomBookedMoneyCol;

    public static LocalDate checkinDate;
    public static LocalTime checkinTime;
    public static LocalDate checkoutDate;
    public static LocalTime checkoutTime;

    public static ObservableList<RoomBooking> roomsBooked = FXCollections.observableArrayList();

    /* ----------------------------------------------------------- */
    /* ---------------------- Customer info ---------------------- */
    /* ----------------------------------------------------------- */
    @FXML
    private TextField txtCustomerName;

    @FXML
    private ComboBox<String> cbCustomerGender;

    @FXML
    private TextField txtCustomerIdNumber;

    @FXML
    private TextField txtCustomerEmail;

    @FXML
    private TextField txtCustomerTelephone;

    @FXML
    private DatePicker dpCustomerBirthdate;

    @FXML
    private TextField txtCustomerNationality;

    @FXML
    private TextField txtCustomerAddress;

    /* ----------------------------------------------------------- */
    /* ---------------------- Payment info ----------------------- */
    /* ----------------------------------------------------------- */
    @FXML
    private Label lbTotalPayment;

    @FXML
    private ComboBox<String> cbPaymentMethod;

    @FXML
    private TextField txtPrepayment;

    @FXML
    private Label lbRemainPayment;

    private Double totalPayment;
    private Double prepayment;
    private Double remainPayment;


    static Stage window = new Stage();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prepayment = 0.0;

        dpBookingDateStart.setValue(checkinDate);
        txtBookingHourStart.setText(checkinTime.toString());
        dpBookingDateEnd.setValue(checkoutDate);
        txtBookingHourEnd.setText(checkoutTime.toString());

        roomBookedNumberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        roomBookedTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        roomBookedMoneyCol.setCellValueFactory(new PropertyValueFactory<>("subPayment"));
        tbvRoomsBooked.setItems(roomsBooked);

        //Insert customer gender to comboBox
        cbCustomerGender.getItems().addAll("Nam", "Nữ", "Khác");

        //Calculate total payment from "roomBooked"
        totalPayment = 0.0;
        for(RoomBooking rb: roomsBooked) {
            totalPayment += rb.getSubPayment();
        }
        lbTotalPayment.setText(totalPayment.toString());

        //Insert payment method to comboBox
        cbPaymentMethod.getItems().addAll("Tiền mặt", "Thẻ tín dụng", "Chuyển khoản ngân hàng", "Ví điện tử");

        //Enter the prepayment and calculate remainPayment
        txtPrepayment.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                prepayment = Double.parseDouble(newValue);
                remainPayment = totalPayment - prepayment;
                lbRemainPayment.setText(remainPayment.toString());
            } else {
                lbRemainPayment.setText(totalPayment.toString());
            }
        }));
    }

    //Method to display this window
    public static void display() throws Exception {
//        Stage window = new Stage();

        //Block event from home window
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Booking");

        Parent root = FXMLLoader.load(CreateBookingController.class.getResource("createBooking.fxml"));
        Scene sc = new Scene(root);
        window.setScene(sc);
        window.showAndWait(); //Display window and wait for it to be closed before returning
    }

    //Other method
    @FXML
    void createBooking(ActionEvent event) {
        try {
            LocalDateTime timeBooked = LocalDateTime.now();
            LocalDateTime checkinDateTime = LocalDateTime.of(checkinDate, checkinTime);
            LocalDateTime checkoutDateTime = LocalDateTime.of(checkoutDate, checkoutTime);

            //Create customer object:
            if (txtCustomerName.getText().isEmpty()) throw new Exception("Chưa nhập tên khách!");
            String customerName = txtCustomerName.getText();
            String customerGender = cbCustomerGender.getValue();
            if (txtCustomerIdNumber.getText().isEmpty()) throw new Exception("Chưa nhập số CMND/Hộ chiếu!");

            //Retrieve the customer with the same IdNumber in database (if exist)
            CustomerExistedConfirmationController.answer = false;
            CustomerRepository cusRepo = (CustomerRepository) Factory.createRepository(RepoType.CUSTOMER);
            if (cusRepo.findByIdNumber(txtCustomerIdNumber.getText()) != null) {
                CustomerExistedConfirmationController.existedCustomer = cusRepo.findByIdNumber(txtCustomerIdNumber.getText());
                CustomerExistedConfirmationController.display();
                if (CustomerExistedConfirmationController.answer) {
                    txtCustomerName.setText(CustomerExistedConfirmationController.existedCustomer.getCustomerName());
//                    cbCustomerGender.setValue(CustomerExistedConfirmationController.existedCustomer.getCustomerGender());
                    txtCustomerIdNumber.setText(CustomerExistedConfirmationController.existedCustomer.getCustomerIdNumber());
//                    txtCustomerEmail.setText(CustomerExistedConfirmationController.existedCustomer.getCustomerEmail());
                    txtCustomerTelephone.setText(CustomerExistedConfirmationController.existedCustomer.getCustomerTel());
//                    dpCustomerBirthdate.setValue(CustomerExistedConfirmationController.existedCustomer.getCustomerBirthdate());
//                    txtCustomerNationality.setText(CustomerExistedConfirmationController.existedCustomer.getCustomerNationality());
//                    txtCustomerAddress.setText(CustomerExistedConfirmationController.existedCustomer.getCustomerAddress());
                    //Disable all input
                    txtCustomerName.setDisable(true);
                    cbCustomerGender.setDisable(true);
                    txtCustomerIdNumber.setDisable(true);
                    txtCustomerEmail.setDisable(true);
                    txtCustomerTelephone.setDisable(true);
                    dpCustomerBirthdate.setDisable(true);
                    txtCustomerNationality.setDisable(true);
                    txtCustomerAddress.setDisable(true);
                }
            } else {
                System.out.println("No customer with the IdNumber found");
            }

            String customerIdNumber = txtCustomerIdNumber.getText();
            String customerEmail = txtCustomerEmail.getText().isEmpty() ? null : txtCustomerEmail.getText();
            if (txtCustomerTelephone.getText().isEmpty()) throw new Exception("Chưa nhập SĐT!");
            String customerTel = txtCustomerTelephone.getText().isEmpty() ? null : txtCustomerTelephone.getText();
            LocalDate customerBirthDate = dpCustomerBirthdate.getValue();
            String customerNationality = txtCustomerNationality.getText().isEmpty() ? null : txtCustomerNationality.getText();
            String customerAddress = txtCustomerAddress.getText().isEmpty() ? null : txtCustomerAddress.getText();

            Customer customer = new Customer(
                    CustomerExistedConfirmationController.answer ? CustomerExistedConfirmationController.existedCustomer.getCustomerId() : null,
                    customerName,
                    customerGender,
                    customerIdNumber,
                    customerEmail,
                    customerTel,
                    customerBirthDate,
                    customerNationality,
                    customerAddress
            );
            System.out.println("Customer id: " + customer.getCustomerId());

            if(cbPaymentMethod.getSelectionModel().isEmpty()) throw new Exception("Chưa chọn phương thức thanh toán!");
            String paymentMethod = cbPaymentMethod.getValue();
            //Variables for paymentTotal, paymentPrepaid, paymentRemain are already created
            if(prepayment > totalPayment || prepayment < 0) throw new Exception("Số tiền thanh toán trước phải trong khoảng từ 0 đến " + totalPayment);

            //Create ArrayList for rooms booked
            ArrayList<RoomBooking> ArrListRoomsBooked = new ArrayList<>(roomsBooked);

            //Create booking object:
            Booking b = new Booking(
                    null,
                    timeBooked,
                    checkinDateTime,
                    checkoutDateTime,
                    null,
                    null,
                    customer,
                    null,
                    paymentMethod,
                    totalPayment,
                    prepayment,
                    remainPayment,
                    ArrListRoomsBooked
            );

            //Call method in repository to add booking to database:
            BookingRepository br = (BookingRepository) Factory.createRepository(RepoType.BOOKING);
            if(br.create(b)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Đặt phòng thành công");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) window.close();
            };

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    void createBookingAndCheckin(ActionEvent event) {

    }

}
