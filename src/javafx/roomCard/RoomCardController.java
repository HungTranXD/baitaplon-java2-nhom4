package javafx.roomCard;

import entities.Room;
import interfaces.MyListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

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

    @FXML
    void clickRoom(MouseEvent mouseEvent) {
        myListener.onClickListener(room);
    }

    private Room room;
    private MyListener myListener;

    public void setData(Room r, MyListener myListener) {
        this.room = r;
        this.myListener = myListener;

        if (r.getRoomStatus().equals("Trống")) {
            vbRoomInfo.setStyle("-fx-background-color: #43a047; -fx-background-radius: 10px 0 0 10px");
            vbRoomDetail.setStyle("-fx-background-color: #ebfceb; -fx-background-radius: 0 10px 10px 0");
            lbCustomerName.setStyle("-fx-text-fill: #43a047; -fx-border-color: #bcbcbc; -fx-border-width: 0 0 1px 0;");
            Image icon = new Image(getClass().getResourceAsStream("/icon/empty.png"));
            imgIconStatus.setImage(icon);
        } else if (r.getRoomStatus().equals("Đã đặt")) {
            vbRoomInfo.setStyle("-fx-background-color: #1e88e5; -fx-background-radius: 10px 0 0 10px");
            vbRoomDetail.setStyle("-fx-background-color: #e0ecfd; -fx-background-radius: 0 10px 10px 0");
            lbCustomerName.setStyle("-fx-text-fill: #1e88e5; -fx-border-color: #bcbcbc; -fx-border-width: 0 0 1px 0;");
            Image icon = new Image(getClass().getResourceAsStream("/icon/booked.png"));
            imgIconStatus.setImage(icon);
        } else if (r.getRoomStatus().equals("Chưa đến")) {
            vbRoomInfo.setStyle("-fx-background-color: #9c27b0; -fx-background-radius: 10px 0 0 10px");
            vbRoomDetail.setStyle("-fx-background-color: #f7e5fd;-fx-background-radius: 0 10px 10px 0");
            lbCustomerName.setStyle("-fx-text-fill: #9c27b0; -fx-border-color: #bcbcbc; -fx-border-width: 0 0 1px 0;");
            Image icon = new Image(getClass().getResourceAsStream("/icon/booked.png"));
            imgIconStatus.setImage(icon);
        } else if (r.getRoomStatus().equals("Có khách")) {
            vbRoomInfo.setStyle("-fx-background-color: #d9390d; -fx-background-radius: 10px 0 0 10px");
            vbRoomDetail.setStyle("-fx-background-color: #ffebe6; -fx-background-radius: 0 10px 10px 0");
            lbCustomerName.setStyle("-fx-text-fill: #d9390d; -fx-border-color: #bcbcbc; -fx-border-width: 0 0 1px 0;");
            Image icon = new Image(getClass().getResourceAsStream("/icon/occupied.png"));
            imgIconStatus.setImage(icon);
        } else if (r.getRoomStatus().equals("Chưa đi")) {
            vbRoomInfo.setStyle("-fx-background-color: #fc9540; -fx-background-radius: 10px 0 0 10px");
            vbRoomDetail.setStyle("-fx-background-color: #ffeee3; -fx-background-radius: 0 10px 10px 0");
            lbCustomerName.setStyle("-fx-text-fill: #fc9540; -fx-border-color: #bcbcbc; -fx-border-width: 0 0 1px 0;");
            Image icon = new Image(getClass().getResourceAsStream("/icon/outdated.png"));
            imgIconStatus.setImage(icon);
        } else {
            vbRoomInfo.setStyle("-fx-background-color: #696969; -fx-background-radius: 10px 0 0 10px");
            vbRoomDetail.setStyle("-fx-background-color: #d2d2d2; -fx-background-radius: 0 10px 10px 0");
            lbCustomerName.setStyle("-fx-text-fill: #696969; -fx-border-color: #bcbcbc; -fx-border-width: 0 0 1px 0;");
            Image icon = new Image(getClass().getResourceAsStream("/icon/error.png"));
        }

        lbRoomType.setText(r.getTypeName());
        lbRoomNumber.setText(r.getRoomNumber());

        if (r.getRoomStatus().equals("Trống")) {
            vbRoomDetail.getChildren().removeAll(lbCustomerName, lbCheckin, lbCheckout);
            lbRoomStatus.setText(r.getRoomStatus());
        } else {
            vbRoomDetail.getChildren().remove(lbRoomStatus);
            lbCustomerName.setText(r.getCustomerName());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            lbCheckin.setText("Đến: " + (r.getCheckinDatetime() == null ? "" : dtf.format(r.getCheckinDatetime())));
            lbCheckout.setText("Đi:    " + (r.getCheckinDatetime() == null ? "" : dtf.format(r.getCheckoutDatetime())));
        }

    }
}
