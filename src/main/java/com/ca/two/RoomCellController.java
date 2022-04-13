package com.ca.two;

import com.ca.two.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

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
    }

    public void setListView(ListView<Room> listView){
        lv = listView;
    }

    @FXML
    void btnCloseClicked(MouseEvent event) {
        lv.getItems().remove(room);
    }
}