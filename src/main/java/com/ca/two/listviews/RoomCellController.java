package com.ca.two.listviews;

import com.ca.two.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class RoomCellController{
    private Room room;
    private ListView<Room> lv;

    @FXML
    private Label lblName;
    @FXML
    private Pane pnlColor;

    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
        lblName.setText(room.getName());
        pnlColor.setStyle("-fx-background-color: " + colorToHex(room.getColor()));
    }

    // Converts a color to a hexadecimal string
    private String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    public void setListView(ListView<Room> listView){
        lv = listView;
    }

    @FXML
    void btnCloseClicked(MouseEvent event) {
        lv.getItems().remove(room);
    }
}