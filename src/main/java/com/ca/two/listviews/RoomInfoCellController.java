package com.ca.two.listviews;

import com.ca.two.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RoomInfoCellController {
    private Room room;
    private ListView<Room> lv;

    @FXML
    private ImageView imgView;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblTitle;

    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
        lblTitle.setText(room.getName());
        lblDescription.setText(room.getDescription());
        imgView.setImage(new Image(room.getImage_url()));
    }

    public void setListView(ListView<Room> listView){
        lv = listView;
    }

}
