package com.ca.two;

import com.ca.two.graph.Graph;
import com.ca.two.graph.Pixel;
import com.ca.two.listviews.RoomListCell;
import com.ca.two.models.Room;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private LinkedList<Room> roomsList;
    private Graph<Room> rooms;
    private Graph<Pixel> pixels;
    private boolean shouldCalcRoute = false;

    @FXML
    private ChoiceBox<Room> avoidChoiceBox;
    @FXML
    private ChoiceBox<Room> destinationChoiceBox;
    @FXML
    private ListView<Room> listViewAvoid;
    @FXML
    private ListView<Room> listViewWaypoints;
    @FXML
    private ChoiceBox<Room> startChoiceBox;
    @FXML
    private Label lblStatus;
    @FXML
    private ChoiceBox<Room> wayPointChoiceBox;
    @FXML
    private ImageView routeOverlay;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
        setupListViewAndChoiceBoxes();
    }

    private void initData(){
        roomsList = DataAccess.readInCSV();
        rooms = DataAccess.createGraph(roomsList);
        System.out.println(rooms);

        Thread loadPixelsGraphThread;
        loadPixelsGraphThread = new Thread(() -> {
            pixels = DataAccess.readInMask();
            System.out.println(pixels);
        });

        loadPixelsGraphThread.start();
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

        //Set up the start and destination choice boxes.
        startChoiceBox.getItems().addAll(roomsList);
        destinationChoiceBox.getItems().addAll(roomsList);

        //When the choice box's values change make sure the other choice box has all the rooms except the selected one
        startChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                destinationChoiceBox.getItems().clear();
                destinationChoiceBox.getItems().addAll(roomsList);
                destinationChoiceBox.getItems().remove(newValue);
            }
        });
        destinationChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                startChoiceBox.getItems().clear();
                startChoiceBox.getItems().addAll(roomsList);
                startChoiceBox.getItems().remove(newValue);
            }
        });
    }


    @FXML
    void btnCalculateClicked(MouseEvent event) {

    }

    @FXML
    void btnClearClicked(MouseEvent event) {

    }
}