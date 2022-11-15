package javafx.createBooking;

import entities.Room;
import entities.RoomBooking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.LocalTime;
import java.util.ResourceBundle;

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



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpBookingDateStart.setValue(checkinDate);
        txtBookingHourStart.setText(checkinTime.toString());
        dpBookingDateEnd.setValue(checkoutDate);
        txtBookingHourEnd.setText(checkoutTime.toString());

        roomBookedNumberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        roomBookedTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        roomBookedMoneyCol.setCellValueFactory(new PropertyValueFactory<>("subPayment"));
        tbvRoomsBooked.setItems(roomsBooked);
    }


    //Method to display this window
    public static void display() throws Exception {
        Stage window = new Stage();

        //Block event from home window
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Booking");

        Parent root = FXMLLoader.load(CreateBookingController.class.getResource("createBooking.fxml"));
        Scene sc = new Scene(root);
        window.setScene(sc);
        window.showAndWait(); //Display window and wait for it to be closed before returning
    }
}
