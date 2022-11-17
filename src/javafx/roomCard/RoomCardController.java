package javafx.roomCard;

import entities.RoomToday;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class RoomCardController {
    @FXML
    private VBox vbRoomDetail;

    @FXML
    private Label lbRoomNumber;

    @FXML
    private Label lbCustomerName;

    @FXML
    private VBox vbRoomInfo;

    @FXML
    private Label lbCheckin;

    @FXML
    private Label lbRoomType;

    @FXML
    private Label lbRoomStatus;

    @FXML
    private Label lbCheckout;

    @FXML
    private ImageView imgIconStatus;

    private RoomToday roomToday;

    public void setData(RoomToday roomToday) {
        this.roomToday = roomToday;

        if (roomToday.getStatus().equals("Trống")) {
            vbRoomDetail.setStyle("-fx-background-color: #43a047");
            vbRoomInfo.setStyle("-fx-background-color: #ebfceb");
            Image icon = new Image(getClass().getResourceAsStream("/icon/empty.png"));
            imgIconStatus.setImage(icon);
        } else if (roomToday.getStatus().equals("Đã đặt")) {
            vbRoomDetail.setStyle("-fx-background-color: #1e88e5");
            vbRoomInfo.setStyle("-fx-background-color: #e0ecfd");
            Image icon = new Image(getClass().getResourceAsStream("/icon/empty.png"));
            imgIconStatus.setImage(icon);
        } else if (roomToday.getStatus().equals("Chưa đến")) {
            vbRoomDetail.setStyle("-fx-background-color: #9c27b0");
            vbRoomInfo.setStyle("-fx-background-color: #f7e5fd");
            Image icon = new Image(getClass().getResourceAsStream("/icon/empty.png"));
            imgIconStatus.setImage(icon);
        } else if (roomToday.getStatus().equals("Có khách")) {
            vbRoomDetail.setStyle("-fx-background-color: #d9390d");
            vbRoomInfo.setStyle("-fx-background-color: #ffebe6");
            Image icon = new Image(getClass().getResourceAsStream("/icon/empty.png"));
            imgIconStatus.setImage(icon);
        } else if (roomToday.getStatus().equals("Chưa đi")) {
            vbRoomDetail.setStyle("-fx-background-color: #fc9540");
            vbRoomInfo.setStyle("-fx-background-color: #ffeee3");
            Image icon = new Image(getClass().getResourceAsStream("/icon/empty.png"));
            imgIconStatus.setImage(icon);
        }

        lbRoomType.setText(roomToday.getType());
        lbRoomNumber.setText(roomToday.getNumber());

        if (roomToday.getStatus().equals("Trống")) {
            vbRoomDetail.getChildren().removeAll(lbCustomerName, lbCheckin, lbCheckout);
            lbRoomStatus.setText(roomToday.getStatus());
        } else {
            vbRoomDetail.getChildren().remove(lbRoomStatus);
            lbCustomerName.setText(roomToday.getCustomer().getCustomerName());
            lbCheckin.setText("Đến: " + roomToday.getCheckin());
            lbCheckout.setText("Đi:  " + roomToday.getCheckout());
        }

    }
}
