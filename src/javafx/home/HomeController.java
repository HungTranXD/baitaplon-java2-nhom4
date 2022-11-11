package javafx.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class HomeController {

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
        
}
