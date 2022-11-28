package javafx.checkout;

import entities.CheckinOut;
import entities.CheckinOutService;
import entities.Room;
import entities.Service;
import enums.RepoType;
import factory.Factory;
import impls.CheckinOutRepository;
import impls.ServiceRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CheckoutController implements Initializable {
    public Stage window = new Stage();
    public static Integer checkinOutId; //this variable is for data transfer from the parent stage (window)
    private CheckinOut ck; //this variable will be used to receive data from database with the checkinOutId above
    /* ----------------------------------------------------------- */
    /* ---------------------- Checkout info ---------------------- */
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
    private Button btTime1;

    @FXML
    private Button btTime2;

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

    static public ObservableList<Room> roomsCheckout = FXCollections.observableArrayList();


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
    /* ---------------------- Services info ---------------------- */
    /* ----------------------------------------------------------- */
    @FXML
    private TableView<Service> tbvService;

    @FXML
    private TableColumn<Service, String> tbvColServiceName;

    @FXML
    private TableColumn<Service, String> tbvColServiceUnit;

    @FXML
    private TableColumn<Service, TextField> tbvColServiceQty;

    @FXML
    private TableColumn<Service, Double> tbvColServiceSubtotal;

    private ObservableList<Service> allServices;

    /* ----------------------------------------------------------- */
    /* ---------------------- Payment info ----------------------- */
    /* ----------------------------------------------------------- */
    @FXML
    private Label lbRoomPayment;

    @FXML
    private Label lbServicePayment;

    @FXML
    private Label lbTotalPayment;

    @FXML
    private ComboBox<String> cbPaymentMethod;

    //Variables for payment
    private Double roomPayment;
    private Double servicePayment;
    private Double totalPayment;

    /* ----------------------------------------------------------- */
    /* -------------------------- Button ------------------------- */
    /* ----------------------------------------------------------- */

    @FXML
    private Button btCheckout;



    /* ------------------------------ INITIALIZE ---------------------------------- */
    /* ---------------------------------------------------------------------------- */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //First, get the checkinOut object with specified checkinOutId from database
        CheckinOutRepository ckRepository = (CheckinOutRepository) Factory.createRepository(RepoType.CHECKINOUT);
        ck = ckRepository.findById(checkinOutId);
        System.out.println(ck.toString());

        //Initialize checkin/out date time
        checkinDate = ck.getCheckinDatetime().toLocalDate();
        checkinTime = ck.getCheckinDatetime().toLocalTime();
        //--- checkout datetime will be the checkout datetime
        checkoutDate = LocalDate.now();
        checkoutTime = LocalTime.now();

        //--- set datetime input:
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        dpBookingDateStart.setValue(checkinDate);
        txtBookingHourStart.setText(timeFormatter.format(checkinTime));
        dpBookingDateEnd.setValue(checkoutDate);
        txtBookingHourEnd.setText(timeFormatter.format(checkoutTime));

        //--- set style for 2 buttons to choose time checkout
        btTime1.setStyle("-fx-background-color: #1e88e5;-fx-background-radius:10px;-fx-text-fill: #FFFFFF;");
        btTime2.setStyle("-fx-background-color: transparent;");

        //Initialize rooms table
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
        //--- calculate subPayment for each room in checkinOut object checkinOutRooms array:
        for(Room r: ck.getCheckinOutRooms()) {
            r.calculateSubPayment(LocalDateTime.of(checkinDate, checkinTime), LocalDateTime.of(checkoutDate, checkoutTime));
        }
        tbvRoomsBooked.setItems(FXCollections.observableArrayList(ck.getCheckinOutRooms()));

        //Calculate roomPayment from rooms in table
        roomPayment = 0.0;
        for(Room r: ck.getCheckinOutRooms()) {
            roomPayment += r.getSubPayment();
        }
        lbRoomPayment.setText(String.format("%,.0f", roomPayment));

        //Display customer info
        txtCustomerName.setText(ck.getCustomer().getCustomerName());
        txtCustomerIdNumber.setText(ck.getCustomer().getCustomerIdNumber());
        txtCustomerTelephone.setText(ck.getCustomer().getCustomerTel());

        //Initialize service table
        tbvColServiceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        tbvColServiceUnit.setCellValueFactory(new PropertyValueFactory<>("serviceUnit"));
        tbvColServiceQty.setCellValueFactory(new PropertyValueFactory<>("txtQuantity"));
        tbvColServiceSubtotal.setCellValueFactory(new PropertyValueFactory<>("subPayment"));
        tbvColServiceSubtotal.setCellFactory(tc -> new TableCell<Service, Double>() {
            @Override
            protected void updateItem(Double payment, boolean empty) {
                super.updateItem(payment, empty);
                if (empty) setText(null);
                else setText(String.format("%,.0f", payment));
            }
        });
        //-- Get all service from database and set them to tbvService
        ServiceRepository sr = (ServiceRepository) Factory.createRepository(RepoType.SERVICE);
        allServices = FXCollections.observableArrayList(sr.readAll());
        tbvService.setItems(allServices);
        //-- Add listener for each textField txtQuantity to calculate subPayment
        servicePayment = 0.0;
        lbServicePayment.setText(String.format("%,.0f", servicePayment));
        totalPayment = roomPayment + servicePayment;
        lbTotalPayment.setText(String.format("%,.0f", totalPayment));
        for (Service s: allServices) {
            s.getTxtQuantity().textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if(!oldValue.isEmpty() && !newValue.isEmpty()) {
                        s.calculateSubPayment();
                        Integer oldValueInt = Integer.parseInt(oldValue);
                        Integer newValueInt = Integer.parseInt(newValue);
                        if (newValueInt < 0) throw new Exception("Số lượng phải >= 0");

                        servicePayment += ((newValueInt - oldValueInt) * s.getServiceFee());
                        tbvService.refresh();
                        lbServicePayment.setText(String.format("%,.0f", servicePayment));

                        totalPayment = roomPayment + servicePayment;
                        lbTotalPayment.setText(String.format("%,.0f", totalPayment));
                    } else if (newValue.isEmpty()){ //oldValue not empty, newValue empty
                        s.setSubPayment(0.0);
                        servicePayment -= Integer.parseInt(oldValue) * s.getServiceFee();
                        tbvService.refresh();
                        lbServicePayment.setText(String.format("%,.0f", servicePayment));

                        totalPayment = roomPayment + servicePayment;
                        lbTotalPayment.setText(String.format("%,.0f", totalPayment));
                    } else { //oldValue empty, newValue not empty
                        s.calculateSubPayment();
                        servicePayment += s.getSubPayment();
                        tbvService.refresh();
                        lbServicePayment.setText(String.format("%,.0f", servicePayment));

                        totalPayment = roomPayment + servicePayment;
                        lbTotalPayment.setText(String.format("%,.0f", totalPayment));
                    }
                } catch (Exception e) {
                    s.getTxtQuantity().setText("0");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText(e.getMessage());
                    alert.show();
                    e.printStackTrace();
                }

            });
        }

        //Add payment methods combobox
        cbPaymentMethod.getItems().addAll("Tiền mặt", "Thẻ tín dụng", "Chuyển khoản ngân hàng", "Ví điện tử");

    }
    /* ---------------------------------------------------------------------------- */
    /* ---------------------------------------------------------------------------- */


    //Method to display this window
    public void display() throws Exception{
        //Block event from home window
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Checkout");

        Parent root = FXMLLoader.load(getClass().getResource("checkout.fxml"));
        Scene sc = new Scene(root);

        window.setScene(sc);
        window.showAndWait(); //Display window and wait for it to be closed before returning
    }

    //Other methods
    @FXML
    void createCheckout(ActionEvent event) {
        try {
            //checkinTime will be the same as "ck" object above
            LocalDateTime checkoutDatetime = LocalDateTime.of(checkoutDate, checkoutTime);

            if (cbPaymentMethod.getSelectionModel().isEmpty()) throw new Exception("Chưa chọn phương thức thanh toán");
            String paymentMethod = cbPaymentMethod.getValue();

            //Customer object will be the same as "ck" object above

            //Arraylist<Room> checkinOutRooms will be the same as "ck" object above

            //Create arraylist<checkinOutService> for service with quantity > 0 (subPayment > 0)
            ArrayList<CheckinOutService> checkinOutServices = new ArrayList<>();
            for(Service s: allServices) {
                if (s.getSubPayment() > 0) {
                    checkinOutServices.add(new CheckinOutService(
                            null,
                            ck.getCheckinOutId(),
                            s.getServiceId(),
                            Integer.parseInt(s.getTxtQuantity().getText()),
                            s.getSubPayment()
                    ));
                }
            }

            // --> Create checkinOut object
            CheckinOut updatedCk = new CheckinOut(
                    ck.getCheckinOutId(),
                    ck.getCheckinDatetime(),
                    checkoutDatetime,
                    roomPayment,
                    servicePayment,
                    totalPayment,
                    paymentMethod,
                    ck.getCustomer(),
                    ck.getCheckinOutRooms(),
                    checkinOutServices
            );

            // --> Call method in repository to update checkin_out in database
            CheckinOutRepository ckr = (CheckinOutRepository) Factory.createRepository(RepoType.CHECKINOUT);
            if (ckr.update(updatedCk)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Thanh toán và trả phòng thành công");

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

    @FXML
    void changeCheckoutDatetime(ActionEvent event) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        if (event.getSource() == btTime1) {
            btTime1.setStyle("-fx-background-color: #1e88e5;-fx-background-radius:10px;-fx-text-fill: #FFFFFF;");
            btTime2.setStyle("-fx-background-color: transparent;");
            dpBookingDateEnd.setValue(LocalDate.now());
            txtBookingHourEnd.setText(timeFormatter.format(LocalTime.now()));
        } else if (event.getSource() == btTime2) {
            btTime2.setStyle("-fx-background-color: #1e88e5;-fx-background-radius:10px;-fx-text-fill: #FFFFFF;");
            btTime1.setStyle("-fx-background-color: transparent;");
            dpBookingDateEnd.setValue(ck.getCheckoutDatetime().toLocalDate());
            txtBookingHourEnd.setText(timeFormatter.format(ck.getCheckoutDatetime().toLocalTime()));
        }
        checkinDate = dpBookingDateStart.getValue();
        checkinTime = LocalTime.parse(txtBookingHourStart.getText());
        checkoutDate = dpBookingDateEnd.getValue();
        checkoutTime = LocalTime.parse(txtBookingHourEnd.getText());
        //Recalculate subPayment for each room in checkinOut object checkinOutRooms array:
        for(Room r: ck.getCheckinOutRooms()) {
            r.calculateSubPayment(LocalDateTime.of(checkinDate, checkinTime), LocalDateTime.of(checkoutDate, checkoutTime));
        }
        tbvRoomsBooked.refresh();

        //Recalculate roomPayment from rooms in table
        roomPayment = 0.0;
        for(Room r: ck.getCheckinOutRooms()) {
            roomPayment += r.getSubPayment();
        }
        lbRoomPayment.setText(String.format("%,.0f",roomPayment));

        //Recalculate total payment
        totalPayment = roomPayment + servicePayment;
        lbTotalPayment.setText(String.format("%,.0f",totalPayment));
    }

    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }



}
