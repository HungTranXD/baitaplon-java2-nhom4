package javafx.home;

import entities.*;
import enums.RepoType;
import factory.Factory;
import impls.RoomRepository;
import interfaces.MyListener;
import javafx.application.Platform;
import javafx.checkin.CheckinController;
import javafx.checkout.CheckoutController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.roomCard.RoomCardController;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class HomeController implements Initializable {

    @FXML
    private AnchorPane pnContact;

    @FXML
    private Button btMenuContact;

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
    /* ---------------------- 1) PANE - Rooms Plan ----------------------- */
    /* ------------------------------------------------------------------- */
    // -- Filter buttons --
    @FXML
    private Button btAllType;
    @FXML
    private Button btType1;
    @FXML
    private Button btType2;
    @FXML
    private Button btType3;
    @FXML
    private Button btType4;

    @FXML
    private Button btAllStatus;
    @FXML
    private Button btStatusEmpty;
    @FXML
    private Button btStatusOccupied;
    @FXML
    private Button btStatusOverdue;

    //Variable for type:
    private String roomType;
    //Variable for status:
    private String roomStatus;

    // -- End of Filter buttons --

    @FXML
    private ScrollPane scrollRooms;

    @FXML
    private GridPane gridRooms;

    @FXML
    private TableView<Room> tbvRoomsSelected;

    @FXML
    private TableColumn<Room, String> tbvColRoomSelectedName;

    @FXML
    private TableColumn<Room, String> tbvColRoomSelectedFloor;

    @FXML
    private TableColumn<Room, String> tbvColRoomSelectedType;

    @FXML
    private TableColumn<Room, Double> tbvColRoomSelected1stHour;

    @FXML
    private TableColumn<Room, Double> tbvColRoomSelectedNextHour;

    @FXML
    private TableColumn<Room, Double> tbvColRoomSelectedDayPrice;

    @FXML
    private TableColumn<Room, Button> tbvColRoomSelectedRemove;

    @FXML
    private DatePicker dpCheckinDate;

    @FXML
    private TextField txtCheckinTime;

    @FXML
    private DatePicker dpCheckoutDate;

    @FXML
    private TextField txtCheckoutTime;

    private ArrayList<Room> roomsPlanToday = new ArrayList<>();

    private MyListener myListener;





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayTimeNow();
        /* ------------------------------------------------------------------- */
        /* ------------------- 0) SIDE MENU - Set default -------------------- */
        /* ------------------------------------------------------------------- */
        btMenuRoomPlan.getStyleClass().add("button1-focused");


        /* ------------------------------------------------------------------- */
        /* ---------------------- 1) PANE - Rooms Plan ----------------------- */
        /* ------------------------------------------------------------------- */
        //First, set roomType and roomStatus = all
        roomType = "Tất cả";
        roomStatus = "Tất cả";
        btAllType.getStyleClass().add("button2-focused");
        btAllStatus.getStyleClass().add("button-blue-focused");


        //Encapsulate all method to initialize roomsPlan into a single method and run with type and status = all
        //initRoomsPlan(roomType, roomStatus);
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
//                    roomsPlanToday.clear();
//                    gridRooms.getChildren().clear();
                    initRoomsPlan(roomType, roomStatus);
                }
            });
        };
        ScheduledFuture<?> scheduledFuture = ses.scheduleAtFixedRate(task,0,10000, TimeUnit.SECONDS);


        //Initialize Selected rooms table
        tbvRoomsSelected.setItems(CheckinController.roomsSelected);

        //Set value for checkin/out input
        dpCheckinDate.setValue(LocalDate.now());
        txtCheckinTime.setText("14:00");
        dpCheckoutDate.setValue(LocalDate.now().plusDays(1));
        txtCheckoutTime.setText("12:00");

        //Initialize selectedRoom table
        tbvColRoomSelectedName.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        tbvColRoomSelectedFloor.setCellValueFactory(new PropertyValueFactory<>("floor"));
        tbvColRoomSelectedType.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        tbvColRoomSelected1stHour.setCellValueFactory(new PropertyValueFactory<>("firstHourPrice"));
        tbvColRoomSelectedNextHour.setCellValueFactory(new PropertyValueFactory<>("nextHourPrice"));
        tbvColRoomSelectedDayPrice.setCellValueFactory(new PropertyValueFactory<>("dayPrice"));
        tbvColRoomSelectedRemove.setCellValueFactory(new PropertyValueFactory<>("removeBtn"));

    }





    //Change StackPane when selecting side menu
    @FXML
    void changeMenuContent(ActionEvent event) {
        if (event.getSource() == btMenuRoomPlan) {
            pnRoomPlan.toFront();
            btMenuRoomPlan.getStyleClass().add("button1-focused");
            //Remove focus from other buttons
            btMenuCustomerList.getStyleClass().remove("button1-focused");
            btMenuRoomPrice.getStyleClass().remove("button1-focused");
            btMenuContact.getStyleClass().remove("button1-focused");
        } else if (event.getSource() == btMenuCustomerList) {
            pnCustomerList.toFront();
            btMenuCustomerList.getStyleClass().add("button1-focused");
            //Remove focus from other buttons
            btMenuRoomPlan.getStyleClass().remove("button1-focused");
            btMenuRoomPrice.getStyleClass().remove("button1-focused");
            btMenuContact.getStyleClass().remove("button1-focused");
        } else if (event.getSource() == btMenuRoomPrice) {
            pnRoomPrice.toFront();
            btMenuRoomPrice.getStyleClass().add("button1-focused");
            //Remove focus from other buttons
            btMenuRoomPlan.getStyleClass().remove("button1-focused");
            btMenuCustomerList.getStyleClass().remove("button1-focused");
            btMenuContact.getStyleClass().remove("button1-focused");
        } else if (event.getSource() == btMenuContact) {
            pnContact.toFront();
            btMenuContact.getStyleClass().add("button1-focused");
            //Remove focus from other buttons
            btMenuRoomPlan.getStyleClass().remove("button1-focused");
            btMenuCustomerList.getStyleClass().remove("button1-focused");
            btMenuRoomPrice.getStyleClass().remove("button1-focused");
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
    /* ---------------------- 1) PANE - Rooms Plan ----------------------- */
    /* ------------------------------------------------------------------- */
    // -- Filter rooms --
    @FXML
    void filterByType(ActionEvent event) {
        if (event.getSource() == btAllType) {
            roomType = "Tất cả";
            btAllType.getStyleClass().add("button2-focused");
            //Remove focused from other buttons
            btType1.getStyleClass().remove("button2-focused");
            btType2.getStyleClass().remove("button2-focused");
            btType3.getStyleClass().remove("button2-focused");
            btType4.getStyleClass().remove("button2-focused");
        } else if (event.getSource() == btType1) {
            roomType = "P.đơn";
            btType1.getStyleClass().add("button2-focused");
            //Remove focused from other buttons
            btAllType.getStyleClass().remove("button2-focused");
            btType2.getStyleClass().remove("button2-focused");
            btType3.getStyleClass().remove("button2-focused");
            btType4.getStyleClass().remove("button2-focused");
        } else if (event.getSource() == btType2) {
            roomType = "P.đôi";
            btType2.getStyleClass().add("button2-focused");
            //Remove focused from other buttons
            btAllType.getStyleClass().remove("button2-focused");
            btType1.getStyleClass().remove("button2-focused");
            btType3.getStyleClass().remove("button2-focused");
            btType4.getStyleClass().remove("button2-focused");
        } else if (event.getSource() == btType3) {
            roomType = "P.2Gi";
            btType3.getStyleClass().add("button2-focused");
            //Remove focused from other buttons
            btAllType.getStyleClass().remove("button2-focused");
            btType1.getStyleClass().remove("button2-focused");
            btType2.getStyleClass().remove("button2-focused");
            btType4.getStyleClass().remove("button2-focused");
        } else if (event.getSource() == btType4) {
            roomType = "P.VIP";
            btType4.getStyleClass().add("button2-focused");
            //Remove focused from other buttons
            btAllType.getStyleClass().remove("button2-focused");
            btType1.getStyleClass().remove("button2-focused");
            btType2.getStyleClass().remove("button2-focused");
            btType3.getStyleClass().remove("button2-focused");
        }
//        roomsPlanToday.clear();
//        gridRooms.getChildren().clear();
        initRoomsPlan(roomType, roomStatus);
    }

    @FXML
    void filterByStatus(ActionEvent event) {
        if (event.getSource() == btAllStatus) {
            roomStatus = "Tất cả";
            btAllStatus.getStyleClass().add("button-blue-focused");
            //Remove focused from other buttons
            btStatusEmpty.getStyleClass().remove("button-green-focused");
            btStatusOccupied.getStyleClass().remove("button-red-focused");
            btStatusOverdue.getStyleClass().remove("button-orange-focused");
        } else if (event.getSource() == btStatusEmpty) {
            roomStatus = "Trống";
            btStatusEmpty.getStyleClass().add("button-green-focused");
            //Remove focused from other buttons
            btAllStatus.getStyleClass().remove("button-blue-focused");
            btStatusOccupied.getStyleClass().remove("button-red-focused");
            btStatusOverdue.getStyleClass().remove("button-orange-focused");
        } else if (event.getSource() == btStatusOccupied) {
            roomStatus = "Có khách";
            btStatusOccupied.getStyleClass().add("button-red-focused");
            //Remove focused from other buttons
            btAllStatus.getStyleClass().remove("button-blue-focused");
            btStatusEmpty.getStyleClass().remove("button-green-focused");
            btStatusOverdue.getStyleClass().remove("button-orange-focused");
        } else if (event.getSource() == btStatusOverdue) {
            roomStatus = "Chưa đi";
            btStatusOverdue.getStyleClass().add("button-orange-focused");
            //Remove focused from other buttons
            btAllStatus.getStyleClass().remove("button-blue-focused");
            btStatusEmpty.getStyleClass().remove("button-green-focused");
            btStatusOccupied.getStyleClass().remove("button-red-focused");
        }
//        roomsPlanToday.clear();
//        gridRooms.getChildren().clear();
        initRoomsPlan(roomType, roomStatus);
    }
    // -- End of Filter rooms --


    public void initRoomsPlan(String roomType, String roomStatus) {
        roomsPlanToday.clear();
        gridRooms.getChildren().clear();
        //Get all room from database
        RoomRepository rr = (RoomRepository) Factory.createRepository(RepoType.ROOM);

        if (roomType.equals("Tất cả")) {
            roomsPlanToday = rr.readAll();
        } else {
            for(Room r: rr.readAll()) {
                if (r.getTypeName().equals(roomType)) roomsPlanToday.add(r);
            }
        }

        //Run loop to determine the status of each room
        Integer totalQuantity = 0;
        Integer emptyQuantity = 0;
        Integer occupiedQuantity = 0;
        Integer overdueQuantity = 0;
        for (Room r: roomsPlanToday) {
            r.checkRoomStatus();
            if (r.getRoomStatus().equals("Trống")) emptyQuantity ++;
            else if (r.getRoomStatus().equals("Có khách")) occupiedQuantity++;
            else if (r.getRoomStatus().equals("Chưa đi")) overdueQuantity++;
            totalQuantity++;
        }
        //Display number of room for each status
        btAllStatus.setText("Tất cả (" + totalQuantity + ")");
        btStatusEmpty.setText("Trống (" + emptyQuantity + ")");
        btStatusOccupied.setText("Có khách (" + occupiedQuantity + ")");
        btStatusOverdue.setText("Chưa đi (" + overdueQuantity + ")");

        if (!roomStatus.equals("Tất cả")) {
            roomsPlanToday.removeIf(r -> !r.getRoomStatus().equals(roomStatus));
        }
        myListener = new MyListener() {
            @Override
            public void onClickListener(Room r) {
                //1) IF the room is EMPTY we add this listener:
                if(r.getRoomStatus().equals("Trống")) {
                    //First check if arrayList have the same roomId
                    boolean checkRoomIdFlag = false;
                    for (Room selectedRoom: CheckinController.roomsSelected) {
                        if (selectedRoom.getRoomId().equals(r.getRoomId())) {
                            checkRoomIdFlag = true;
                            break;
                        }
                    }
                    if(checkRoomIdFlag) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Phòng " + r.getRoomNumber() + " đã được chọn");
                        alert.show();
                    } else {
                        CheckinController.roomsSelected.add(r);
                    }

                //2) IF the room is OCCUPIED we add this listener:
                } else if (r.getRoomStatus().equals("Có khách") || r.getRoomStatus().equals("Chưa đi")) {
                    //Ask if user want to go to checkout window
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmation.setTitle("Confirmation");
                    confirmation.setHeaderText("Thanh toán và trả phòng?");

                    Optional<ButtonType> result = confirmation.showAndWait();
                    if(result.get() == ButtonType.OK){ //if user chose Ok then proceed:
                        try { //handle display() method exception
                            CheckoutController.checkinOutId = r.getCheckinOutId();
                            CheckoutController ckOutController = new CheckoutController();
                            ckOutController.window.setOnHidden(event -> initRoomsPlan(roomType, roomStatus));
                            ckOutController.display();
                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error!");
                            alert.setHeaderText(e.getMessage());
                            alert.show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        //Specify number of rows and columns in grid
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < roomsPlanToday.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../roomCard/roomCard.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                RoomCardController roomCardController = fxmlLoader.getController();
                roomCardController.setData(roomsPlanToday.get(i), myListener);

                if (column == 4) {
                    column = 0;
                    row++;
                }
                gridRooms.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(7, 10, 7, 10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToCheckinWindow(ActionEvent event) {
        try {
            CheckinController.checkinDate = dpCheckinDate.getValue();
            CheckinController.checkinTime = LocalTime.parse(txtCheckinTime.getText());
            CheckinController.checkoutDate = dpCheckoutDate.getValue();
            CheckinController.checkoutTime = LocalTime.parse(txtCheckoutTime.getText());

            //Check if is any rooms have been selected
            if (CheckinController.roomsSelected.isEmpty()) {
                throw new Exception("Chưa chọn phòng nào!");
            }

            //Calculate subPayment for each room selected
            LocalDate checkinDate = dpCheckinDate.getValue();
            LocalTime checkinTime = LocalTime.parse(txtCheckinTime.getText());
            LocalDateTime checkinDatetime = LocalDateTime.of(checkinDate, checkinTime);

            LocalDate checkoutDate = dpCheckoutDate.getValue();
            LocalTime checkoutTime = LocalTime.parse(txtCheckoutTime.getText());
            LocalDateTime checkoutDatetime = LocalDateTime.of(checkoutDate, checkoutTime);

            if(checkoutDatetime.isBefore(checkinDatetime)) throw new Exception("Thời gian checkout phải sau checkin!");

            for(Room r: CheckinController.roomsSelected) {
                r.calculateSubPayment(checkinDatetime, checkoutDatetime);
            }

            CheckinController checkinController = new CheckinController();
            checkinController.window.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    //Refresh parent
                    initRoomsPlan(roomType, roomStatus);
                    //Clear roomSelected array and subsequently clear tableView tbvRoomsSelected
                    CheckinController.roomsSelected.clear();
                }
            });
            checkinController.display();


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.show();
            e.printStackTrace();
        }

    }


}
