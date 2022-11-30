package javafx.home;

import entities.*;
import enums.RepoType;
import enums.RoomStatus;
import factory.Factory;
import impls.CustomerRepository;
import impls.RoomRepository;
import impls.RoomTypeRepository;
import interfaces.MyListener;
import javafx.Main;
import javafx.application.Platform;
import javafx.checkin.CheckinController;
import javafx.checkout.CheckoutController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.editCustomer.EditCustomer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.roomCard.RoomCardController;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
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
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class HomeController implements Initializable {
    @FXML
    ChoiceBox<String> cbFacility;

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
    private RoomStatus roomStatus;

    // -- End of Filter buttons --

    @FXML
    private ScrollPane scrollRooms;

    @FXML
    private GridPane gridRoomsFloor1;
    @FXML
    private GridPane gridRoomsFloor2;
    @FXML
    private GridPane gridRoomsFloor3;

    private ArrayList<Room> roomsPlanToday = new ArrayList<>();

    private MyListener myListener;

    /* ------------------------------------------------------------------- */
    /* -------------------- 2) PANE - Customer list ---------------------- */
    /* ------------------------------------------------------------------- */
    @FXML
    private TextField txtCusName;
    @FXML
    private TextField txtCusTel;
    @FXML
    private TextField txtCusIdNumber;
    @FXML
    private DatePicker dpCusCheckinDate;
    @FXML
    private DatePicker dpCusCheckoutDate;
    @FXML
    private TableView<Customer> tbvCustomer;
    @FXML
    private TableColumn<Customer, String> tbvColCusName;
    @FXML
    private TableColumn<Customer, String> tbvColCusTel;
    @FXML
    private TableColumn<Customer, String> tbvColCusIdNumber;
    @FXML
    private TableColumn<Customer, Button> tbvColCusEdit;

    //Variables for customer list pane
    private LocalDateTime checkin;
    private LocalDateTime checkout;

    /* ------------------------------------------------------------------- */
    /* -------------------- 3) PANE - Rooms & Price ---------------------- */
    /* ------------------------------------------------------------------- */
    @FXML
    private TableView<RoomType> tbvPrice;

    @FXML
    private TableColumn<RoomType, String> tbvPrice_TypeCol;

    @FXML
    private TableColumn<RoomType, Double> tbvPrice_FirstHourCol;

    @FXML
    private TableColumn<RoomType, Double> tbvPrice_NextHourCol;

    @FXML
    private TableColumn<RoomType, Double> tbvPrice_DayCol;

    @FXML
    private TableColumn<RoomType, Double> tbvPrice_Early1Col;

    @FXML
    private TableColumn<RoomType, Double> tbvPrice_Early2Col;

    @FXML
    private TableColumn<RoomType, Double> tbvPrice_Late1Col;

    @FXML
    private TableColumn<RoomType, Double> tbvPrice_Late2Col;









    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbFacility.getItems().add("Cơ sở 1 - Cầu Giấy");
        cbFacility.setValue("Cơ sở 1 - Cầu Giấy");
        displayTimeNow();
        Main.rootStage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                stop = true;
            }
        });
        /* ------------------------------------------------------------------- */
        /* ------------------- 0) SIDE MENU - Set default -------------------- */
        /* ------------------------------------------------------------------- */
        btMenuRoomPlan.getStyleClass().add("button1-focused");


        /* ------------------------------------------------------------------- */
        /* ---------------------- 1) PANE - Rooms Plan ----------------------- */
        /* ------------------------------------------------------------------- */
        //First, set roomType and roomStatus = all
        roomType = "Tất cả";
        roomStatus = RoomStatus.ALL;
        btAllType.getStyleClass().add("button2-focused");
        btAllStatus.getStyleClass().add("button-blue-focused");


        //Encapsulate all method to initialize roomsPlan into a single method and run with type and status = all
        //initRoomsPlan(roomType, roomStatus);
        scrollRooms.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * 0.001;
            scrollRooms.setVvalue(scrollRooms.getVvalue() - deltaY);
        });
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initRoomsPlan(roomType, roomStatus);
                }
            });
        };
        ScheduledFuture<?> scheduledFuture = ses.scheduleAtFixedRate(task,0,1, TimeUnit.MINUTES);


        /* ------------------------------------------------------------------- */
        /* -------------------- 2) PANE - Customer list ---------------------- */
        /* ------------------------------------------------------------------- */
        dpCusCheckinDate.setValue(LocalDate.now());
        dpCusCheckoutDate.setValue(LocalDate.now());

        //Initialize customer table
        tbvColCusName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tbvColCusTel.setCellValueFactory(new PropertyValueFactory<>("customerTel"));
        tbvColCusIdNumber.setCellValueFactory(new PropertyValueFactory<>("customerIdNumber"));
        tbvColCusEdit.setCellValueFactory(new PropertyValueFactory<>("btEdit"));

        findCustomers(null);




        /* ------------------------------------------------------------------- */
        /* -------------------- 3) PANE - Rooms & Price ---------------------- */
        /* ------------------------------------------------------------------- */
        //Get room types and their price from database
        RoomTypeRepository rtr = (RoomTypeRepository) Factory.createRepository(RepoType.ROOMTYPE);
        ObservableList<RoomType> roomTypesAndPrice = FXCollections.observableArrayList(rtr.readAll());

        tbvPrice_TypeCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        tbvPrice_FirstHourCol.setCellValueFactory(new PropertyValueFactory<>("firstHourPrice"));
        tbvPrice_NextHourCol.setCellValueFactory(new PropertyValueFactory<>("nextHourPrice"));
        tbvPrice_DayCol.setCellValueFactory(new PropertyValueFactory<>("dayPrice"));
        tbvPrice_Early1Col.setCellValueFactory(new PropertyValueFactory<>("earlyCheckinFee1"));
        tbvPrice_Early2Col.setCellValueFactory(new PropertyValueFactory<>("earlyCheckinFee2"));
        tbvPrice_Late1Col.setCellValueFactory(new PropertyValueFactory<>("lateCheckoutFee1"));
        tbvPrice_Late2Col.setCellValueFactory(new PropertyValueFactory<>("lateCheckoutFee2"));

        tbvPrice.setItems(roomTypesAndPrice);

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
        initRoomsPlan(roomType, roomStatus);
    }

    @FXML
    void filterByStatus(ActionEvent event) {
        if (event.getSource() == btAllStatus) {
            roomStatus = RoomStatus.ALL;
            btAllStatus.getStyleClass().add("button-blue-focused");
            //Remove focused from other buttons
            btStatusEmpty.getStyleClass().remove("button-green-focused");
            btStatusOccupied.getStyleClass().remove("button-red-focused");
            btStatusOverdue.getStyleClass().remove("button-orange-focused");
        } else if (event.getSource() == btStatusEmpty) {
            roomStatus = RoomStatus.EMPTY;
            btStatusEmpty.getStyleClass().add("button-green-focused");
            //Remove focused from other buttons
            btAllStatus.getStyleClass().remove("button-blue-focused");
            btStatusOccupied.getStyleClass().remove("button-red-focused");
            btStatusOverdue.getStyleClass().remove("button-orange-focused");
        } else if (event.getSource() == btStatusOccupied) {
            roomStatus = RoomStatus.OCCUPIED;
            btStatusOccupied.getStyleClass().add("button-red-focused");
            //Remove focused from other buttons
            btAllStatus.getStyleClass().remove("button-blue-focused");
            btStatusEmpty.getStyleClass().remove("button-green-focused");
            btStatusOverdue.getStyleClass().remove("button-orange-focused");
        } else if (event.getSource() == btStatusOverdue) {
            roomStatus = RoomStatus.OVERDUE;
            btStatusOverdue.getStyleClass().add("button-orange-focused");
            //Remove focused from other buttons
            btAllStatus.getStyleClass().remove("button-blue-focused");
            btStatusEmpty.getStyleClass().remove("button-green-focused");
            btStatusOccupied.getStyleClass().remove("button-red-focused");
        }
        initRoomsPlan(roomType, roomStatus);
    }
    // -- End of Filter rooms --


    public void initRoomsPlan(String roomType, RoomStatus roomStatus) {
        roomsPlanToday.clear();
        gridRoomsFloor1.getChildren().clear();
        gridRoomsFloor2.getChildren().clear();
        gridRoomsFloor3.getChildren().clear();
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
            if (r.getRoomStatus() == RoomStatus.EMPTY) emptyQuantity ++;
            else if (r.getRoomStatus() == RoomStatus.OCCUPIED) occupiedQuantity++;
            else if (r.getRoomStatus() == RoomStatus.OVERDUE) overdueQuantity++;
            totalQuantity++;
        }
        //Display number of room for each status
        btAllStatus.setText("Tất cả (" + totalQuantity + ")");
        btStatusEmpty.setText("Trống (" + emptyQuantity + ")");
        btStatusOccupied.setText("Có khách (" + occupiedQuantity + ")");
        btStatusOverdue.setText("Chưa đi (" + overdueQuantity + ")");

        if (roomStatus != RoomStatus.ALL) {
            roomsPlanToday.removeIf(r -> r.getRoomStatus() != roomStatus);
        }
        myListener = new MyListener() {
            @Override
            public void onClickListener(Room r) {
                //1) IF the room is EMPTY we add this listener:
                if(r.getRoomStatus() == RoomStatus.EMPTY) {
                    //Ask if user want to go to checkin window
                    Alert confirmation1 = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmation1.setTitle("Confirmation");
                    confirmation1.setHeaderText("Nhận phòng này?");

                    Optional<ButtonType> result = confirmation1.showAndWait();
                    if(result.get() == ButtonType.OK){ //if user chose Ok then proceed:
                        try { //handle display() method exception
                            CheckinController.roomsSelected.add(r);
                            CheckinController ckInController = new CheckinController();
                            ckInController.window.setOnHidden(event1 -> {
                                //Refresh parent
                                initRoomsPlan(roomType, roomStatus);
                                //Clear roomSelected array and subsequently clear tableView tbvRoomsSelected
                                CheckinController.roomsSelected.clear();
                            });
                            ckInController.display();
                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error!");
                            alert.setHeaderText(e.getMessage());
                            alert.show();
                            e.printStackTrace();
                        }
                    }

                //2) IF the room is OCCUPIED we add this listener:
                } else if (r.getRoomStatus() == RoomStatus.OCCUPIED || r.getRoomStatus() == RoomStatus.OVERDUE) {
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


        try {
            //Load roomCard fxml to grid - FLOOR 1:
            int column1 = 0;
            int row1 = 1;
            List<Room> roomsPlanFloor1 = roomsPlanToday.stream().filter(room -> room.getFloor().equals("Tầng 1")).collect(Collectors.toList());
            for (int i = 0; i < roomsPlanFloor1.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../roomCard/roomCard.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                RoomCardController roomCardController = fxmlLoader.getController();
                roomCardController.setData(roomsPlanFloor1.get(i), myListener);

                if (column1 == 4) {
                    column1 = 0;
                    row1++;
                }
                gridRoomsFloor1.add(anchorPane, column1++, row1);
                GridPane.setMargin(anchorPane, new Insets(7, 10, 7, 10));
            }
            //Load roomCard fxml to grid - FLOOR 2:
            int column2 = 0;
            int row2 = 1;
            List<Room> roomsPlanFloor2 = roomsPlanToday.stream().filter(room -> room.getFloor().equals("Tầng 2")).collect(Collectors.toList());
            for (int i = 0; i < roomsPlanFloor2.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../roomCard/roomCard.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                RoomCardController roomCardController = fxmlLoader.getController();
                roomCardController.setData(roomsPlanFloor2.get(i), myListener);

                if (column2 == 4) {
                    column2 = 0;
                    row2++;
                }
                gridRoomsFloor2.add(anchorPane, column2++, row2);
                GridPane.setMargin(anchorPane, new Insets(7, 10, 7, 10));
            }
            //Load roomCard fxml to grid - FLOOR 2:
            int column3 = 0;
            int row3 = 1;
            List<Room> roomsPlanFloor3 = roomsPlanToday.stream().filter(room -> room.getFloor().equals("Tầng 3")).collect(Collectors.toList());
            for (int i = 0; i < roomsPlanFloor3.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../roomCard/roomCard.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                RoomCardController roomCardController = fxmlLoader.getController();
                roomCardController.setData(roomsPlanFloor3.get(i), myListener);

                if (column3 == 4) {
                    column3 = 0;
                    row3++;
                }
                gridRoomsFloor3.add(anchorPane, column3++, row3);
                GridPane.setMargin(anchorPane, new Insets(7, 10, 7, 10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* ------------------------------------------------------------------- */
    /* -------------------- 2) PANE - Customer list ---------------------- */
    /* ------------------------------------------------------------------- */
    @FXML
    void findCustomers(ActionEvent event) {
        String customerName = txtCusName.getText();
        String customerIdNumber = txtCusIdNumber.getText();
        String customerTel = txtCusTel.getText();
        LocalDateTime checkinDatetime = dpCusCheckinDate.getValue() == null ? null : LocalDateTime.of(dpCusCheckinDate.getValue(), LocalTime.of(0,0,0));
        LocalDateTime checkoutDatetime = dpCusCheckoutDate.getValue() == null ? null : LocalDateTime.of(dpCusCheckoutDate.getValue(), LocalTime.of(23,59,59));
        System.out.println(customerName + "-" + customerIdNumber + "-" + customerTel + "-" + checkinDatetime + "-" + checkoutDatetime);
        CustomerRepository cr = (CustomerRepository) Factory.createRepository(RepoType.CUSTOMER);
        ObservableList<Customer> customers = FXCollections.observableArrayList(cr.findCustomer(customerName, customerIdNumber, customerTel, checkinDatetime, checkoutDatetime));
        tbvCustomer.setItems(customers);
    }

}
