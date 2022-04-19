package com.ca.two.listviews;

import com.ca.two.Application;
import com.ca.two.models.Room;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;

public class RoomListCell extends ListCell<Room> {
    private RoomCellController controller;
    private ListView<Room> parentListView;
    private Node thisNode;

    public RoomListCell(ListView<Room> parentListView) {
        this.parentListView = parentListView;
        prefWidthProperty().bind(parentListView.widthProperty().subtract(20));
    }

    @Override
    public void updateItem(Room item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            try {
                FXMLLoader fxmlLoader = Application.loadView("room_cell_ui.fxml");
                thisNode = fxmlLoader.load();
                thisNode.minWidth(parentListView.getWidth());
                controller = fxmlLoader.getController(); //New item's controller
                controller.setRoom(item);
                controller.setListView(parentListView);
                setGraphic(thisNode);
            } catch (IOException ex){
                ex.printStackTrace();
                setGraphic(null);
            }
        } else {
            setGraphic(null);
        }
    }

}

