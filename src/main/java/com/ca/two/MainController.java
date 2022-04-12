package com.ca.two;

import com.ca.two.graph.Graph;
import com.ca.two.graph.Pixel;
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
;

public class MainController implements Initializable {
    private LinkedList<Room> roomsList;
    private Graph<Room> rooms;
    private Graph<Pixel> pixels;

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
        initData();
        setupListViewAndChoiceBoxes();
    }

    private void initData(){
        roomsList = DataAccess.readInCSV();
        rooms = DataAccess.createGraph(roomsList);
        System.out.println(rooms);

        Thread thread = new Thread(() -> {
            pixels = DataAccess.readInMask();
            System.out.println(pixels);
        });
        thread.start();
    }

    private void setupListViewAndChoiceBoxes() {
        //Set up the list views to use the RoomListCell class
        listViewAvoid.setCellFactory(param -> new RoomListCell(listViewAvoid));
        listViewWaypoints.setCellFactory(param -> new RoomListCell(listViewWaypoints));

        //Set up the choice boxes to load all the rooms but the ones in the list views when the list views' items change
        listViewWaypoints.getItems().addListener((ListChangeListener<? super Room>) (change) -> {
            wayPointChoiceBox.getItems().clear();
            wayPointChoiceBox.getItems().addAll(roomsList);
            wayPointChoiceBox.getItems().removeAll(listViewWaypoints.getItems());
        });
        listViewAvoid.getItems().addListener((ListChangeListener<? super Room>) (change) -> {
            avoidChoiceBox.getItems().clear();
            avoidChoiceBox.getItems().addAll(roomsList);
            avoidChoiceBox.getItems().removeAll(listViewAvoid.getItems());
        });

        //make sure the choice boxes are populated with the correct data
        wayPointChoiceBox.getItems().add(new Room());
        wayPointChoiceBox.getItems().clear();
        avoidChoiceBox.getItems().add(new Room());
        avoidChoiceBox.getItems().clear();

        //Add all the rooms to the choice boxes
        wayPointChoiceBox.getItems().addAll(roomsList);
        avoidChoiceBox.getItems().addAll(roomsList);

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