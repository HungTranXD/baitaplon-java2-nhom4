package javafx.home;

import entities.Room;
import entities.RoomBooking;
import enums.RepoType;
import factory.Factory;
import impls.RoomRepository;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.createBooking.CreateBookingController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

import static javafx.createBooking.CreateBookingController.roomsBooked;

public class HomeController implements Initializable {

    @FXML
    private AnchorPane pnCreateReservation;

    @FXML
    private AnchorPane pnContact;

    @FXML
    private Button btMenuCreateReservation;

    @FXML
    private Button btMenuContact;

    @FXML
    private AnchorPane pnGeneral;

    @FXML
    private Button btMenuGeneral;

    @FXML
    private AnchorPane pnRoomPrice;

    @FXML
    private AnchorPane pnCustomerList;

    @FXML
    private Button btMenuRoomPrice;

    @FXML
    private Button btMenuRoomPlan;

    @FXML
    private AnchorPane pnRoomPlan;

    @FXML
    private Button btMenuCustomerList;

    //Display real time date at the bottom
    @FXML
    private Label lbLiveTime;
    @FXML
    private Label lbLiveDate;
    private volatile boolean stop = false;

    /* ------------------------------------------------------------------- */
    /* ------------------ 1) PANE - Create room booking ------------------ */
    /* ------------------------------------------------------------------- */
    @FXML
    private DatePicker dpBookingDateStart;

    @FXML
    private TextField txtBookingHourStart;

    @FXML
    private DatePicker dpBookingDateEnd;

    @FXML
    private TextField txtBookingHourEnd;

    @FXML
    private TableView<Room> tbvRoomsAvailable;

    @FXML
    private TableColumn<Room, CheckBox> roomAvailableSelectRoomCol;

    @FXML
    private TableColumn<Room, String> roomAvailableRoomNumberCol;

    @FXML
    private TableColumn<Room, String> roomAvailableRoomTypeCol;

    @FXML
    private TableColumn<Room, String> roomAvailableFloorCol;

    @FXML
    private TableColumn<Room, Double> roomAvailableDayPriceCol;

    private ObservableList<Room> availableRooms = FXCollections.observableArrayList();

    @FXML
    private ListView<RoomBooking> lvRoomsBooked;























    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayTimeNow();

        /* ------------------------------------------------------------------- */
        /* ------------------ 1) PANE - Create room booking ------------------ */
        /* ------------------------------------------------------------------- */
        dpBookingDateStart.setValue(LocalDate.now());
        txtBookingHourStart.setText("14:00");
        dpBookingDateEnd.setValue(LocalDate.now().plusDays(1));
        txtBookingHourEnd.setText("12:00");
        roomAvailableSelectRoomCol.setCellValueFactory(new PropertyValueFactory<>("cbSelectRoom"));
        roomAvailableRoomNumberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        roomAvailableRoomTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        roomAvailableFloorCol.setCellValueFactory(new PropertyValueFactory<>("floorName"));
        roomAvailableDayPriceCol.setCellValueFactory(new PropertyValueFactory<>("dayPrice"));

        lvRoomsBooked.setItems(roomsBooked);
    }
















    //Change StackPane when selecting side menu
    @FXML
    void changeMenuContent(ActionEvent event) {
        if (event.getSource() == btMenuGeneral) {
            pnGeneral.toFront();
        } else if (event.getSource() == btMenuRoomPlan) {
            pnRoomPlan.toFront();
        } else if (event.getSource() == btMenuCreateReservation) {
            pnCreateReservation.toFront();
        } else if (event.getSource() == btMenuCustomerList) {
            pnCustomerList.toFront();
        } else if (event.getSource() == btMenuRoomPrice) {
            pnRoomPrice.toFront();
        } else if (event.getSource() == btMenuContact) {
            pnContact.toFront();
        }
    }

    //Display live time at the bottom
    private void displayTimeNow() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat sdfDate = new SimpleDateFormat("E, dd MMM yyyy");
            while (!stop) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                final String timeNow = sdfTime.format(new Date());
                final String dateNow = sdfDate.format(new Date());
                Platform.runLater(() -> {
                    lbLiveTime.setText(timeNow);
                    lbLiveDate.setText(dateNow);
                });
            }
        });
        thread.start();
    }

    /* ------------------------------------------------------------------- */
    /* ------------------ 1) PANE - Create room booking ------------------ */
    /* ------------------------------------------------------------------- */
    public void searchRoomsAvailable(ActionEvent actionEvent) {
        clearRoomAvailable(actionEvent);
        lvRoomsBooked.getItems().clear();
        try {
            RoomRepository rr = (RoomRepository) Factory.createRepository(RepoType.ROOM);

            LocalDate checkinDate = dpBookingDateStart.getValue();
            LocalTime checkinTime = LocalTime.parse(txtBookingHourStart.getText());
            LocalDateTime checkin = LocalDateTime.of(checkinDate, checkinTime);
            System.out.println(checkin);

            LocalDate checkoutDate = dpBookingDateEnd.getValue();
            LocalTime checkoutTime = LocalTime.parse(txtBookingHourEnd.getText());
            LocalDateTime checkout = LocalDateTime.of(checkoutDate, checkoutTime);
            System.out.println(checkout);

            availableRooms.addAll(rr.findByDate(checkin, checkout));
            tbvRoomsAvailable.setItems(availableRooms);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    public void clearRoomAvailable(ActionEvent mouseEvent) {
        tbvRoomsAvailable.getItems().clear();

    }

    public void goToBookingWindow(ActionEvent actionEvent) throws Exception{
        CreateBookingController.checkinDate = dpBookingDateStart.getValue();
        CreateBookingController.checkinTime = LocalTime.parse(txtBookingHourStart.getText());
        CreateBookingController.checkoutDate = dpBookingDateEnd.getValue();
        CreateBookingController.checkoutTime = LocalTime.parse(txtBookingHourEnd.getText());

        //Calculate subPayment for each room booked
        for(RoomBooking rb: roomsBooked) {
            rb.setSubPayment(100.0);
        }

        CreateBookingController.display();
    }

    @FXML
    void setListView(ActionEvent event) {
    }
}
