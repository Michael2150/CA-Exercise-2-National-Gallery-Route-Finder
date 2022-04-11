package com.ca.two;

import com.ca.two.listviews.RoomListCell;
import com.ca.two.models.Room;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private LinkedList<Room> rooms;

    @FXML
    private ChoiceBox<Room> avoidChoiceBox;
    @FXML
    private ListView<Room> listViewAvoid;
    @FXML
    private ListView<Room> listViewWaypoints;
    @FXML
    private ChoiceBox<Room> wayPointChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rooms = DataAccess.readInCSV();
        rooms.remove(0); //Remove the header row

        setupListViewAndChoiceBoxes();
    }

    private void setupListViewAndChoiceBoxes() {
        //Set up the list views to use the RoomListCell class
        listViewAvoid.setCellFactory(param -> new RoomListCell(listViewAvoid));
        listViewWaypoints.setCellFactory(param -> new RoomListCell(listViewWaypoints));

        //Set up the choice boxes to load all the rooms but the ones in the list views when the list views' items change
        listViewWaypoints.getItems().addListener((ListChangeListener<? super Room>) (change) -> {
            wayPointChoiceBox.getItems().clear();
            wayPointChoiceBox.getItems().addAll(rooms);
            wayPointChoiceBox.getItems().removeAll(listViewWaypoints.getItems());
        });
        listViewAvoid.getItems().addListener((ListChangeListener<? super Room>) (change) -> {
            avoidChoiceBox.getItems().clear();
            avoidChoiceBox.getItems().addAll(rooms);
            avoidChoiceBox.getItems().removeAll(listViewAvoid.getItems());
        });

        //make sure the choice boxes are populated with the correct data
        wayPointChoiceBox.getItems().add(new Room());
        wayPointChoiceBox.getItems().clear();
        avoidChoiceBox.getItems().add(new Room());
        avoidChoiceBox.getItems().clear();

        //Add all the rooms to the choice boxes
        wayPointChoiceBox.getItems().addAll(rooms);
        avoidChoiceBox.getItems().addAll(rooms);

        //When the choice box's values change, add the selected value to the list view
        wayPointChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                listViewWaypoints.getItems().add(newValue);
        });
        avoidChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                listViewAvoid.getItems().add(newValue);
        });
    }
}