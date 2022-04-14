package com.ca.two;

import com.ca.two.graph.*;
import com.ca.two.listviews.RoomInfoListCell;
import com.ca.two.listviews.RoomListCell;
import com.ca.two.models.Room;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    private LinkedList<Room> roomsList;
    private Graph<Room> rooms;
    private Graph<Pixel> pixels;

    @FXML
    private ChoiceBox<Room> avoidChoiceBox;
    @FXML
    private ChoiceBox<Room> destinationChoiceBox;
    @FXML
    private ListView<Room> listViewAvoid;
    @FXML
    private ListView<Room> listViewWaypoints;
    @FXML
    private ListView<Room> routeListView;
    @FXML
    private ChoiceBox<Room> startChoiceBox;
    @FXML
    private Label lblStatus;
    @FXML
    private ChoiceBox<Room> wayPointChoiceBox;
    @FXML
    private CheckBox chkIncludeWaypoints;
    @FXML
    private CheckBox chkShortestPath;
    @FXML
    private CheckBox chkTimeFour;
    @FXML
    private CheckBox chkTimeOne;
    @FXML
    private CheckBox chkTimeThree;
    @FXML
    private CheckBox chkTimeTwo;
    @FXML
    private ImageView routeOverlay;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
        setupListViewAndChoiceBoxes();
    }

    private void initData(){
        setStatus("Loading Data...");

        roomsList = DataAccess.readInCSV();
        rooms = DataAccess.createGraph(roomsList);
        System.out.println(rooms);

        Thread loadPixelsGraphThread;
        loadPixelsGraphThread = new Thread(() -> {
            setPixels(DataAccess.readInMask());
            System.out.println(pixels);
        });

        loadPixelsGraphThread.start();
        setStatus("Ready");
    }

    private void setupListViewAndChoiceBoxes() {
        //Set up the list views to use the RoomListCell class
        listViewAvoid.setCellFactory(param -> new RoomListCell(listViewAvoid));
        listViewWaypoints.setCellFactory(param -> new RoomListCell(listViewWaypoints));
        routeListView.setCellFactory(param -> new RoomInfoListCell(routeListView));

        //Set up the choice boxes to load all the rooms but the ones in the list views when the list views' items change
        Runnable loadRooms = () -> {
            wayPointChoiceBox.getItems().clear();
            avoidChoiceBox.getItems().clear();

            wayPointChoiceBox.getItems().addAll(roomsList);
            avoidChoiceBox.getItems().addAll(roomsList);

            wayPointChoiceBox.getItems().removeAll(listViewWaypoints.getItems());
            wayPointChoiceBox.getItems().removeAll(listViewAvoid.getItems());
            avoidChoiceBox.getItems().removeAll(listViewWaypoints.getItems());
            avoidChoiceBox.getItems().removeAll(listViewAvoid.getItems());
        };

        listViewWaypoints.getItems().addListener((ListChangeListener<? super Room>) (change) -> loadRooms.run());
        listViewAvoid.getItems().addListener((ListChangeListener<? super Room>) (change) -> loadRooms.run());

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
    }

    @FXML
    void btnCalculateClicked(MouseEvent event) {
        //TEST
        startChoiceBox.getSelectionModel().select(getRoomWithID(66));
        destinationChoiceBox.getSelectionModel().select(getRoomWithID(33));
        listViewAvoid.getItems().addAll(getRoomWithID(10), getRoomWithID(11));
        listViewWaypoints.getItems().addAll(getRoomWithID(26), getRoomWithID(27), getRoomWithID(28));

        //Start a timer
        long startTime = System.currentTimeMillis();

        setStatus("Setting up weights...");

        Algorithms.setAllWeights(rooms, 0.5f);
        Algorithms.setupGraphWeights(rooms, 5f, 0.1f, listViewWaypoints, listViewAvoid);

        setStatus("Running Dijkstra's algorithm...");
        var startRoom = startChoiceBox.getSelectionModel().getSelectedItem();
        var destinationRoom = destinationChoiceBox.getSelectionModel().getSelectedItem();
        var results = Algorithms.Dijkstra(rooms, startRoom, destinationRoom);

        routeListView.getItems().clear();
        routeListView.getItems().addAll(results);

        //Set the status to the time taken
        setStatus("Ready ("+ (System.currentTimeMillis() - startTime) + "ms)");

        debug_graph(results);
    }

    private void debug_graph(LinkedList<Room> results) {
        //print the depth and weight and if the graph includes the waypoints and does not include the to avoid
        if (rooms.getNodes().containsAll(listViewWaypoints.getItems()) && !rooms.getNodes().containsAll(listViewAvoid.getItems())) {
            System.out.println("Diffusion depth: " + 5f + " Diffusion weight: " + 0.1f);
        }

        //Print the waypoints
        StringBuilder sb = new StringBuilder();
        sb.append("\nWaypoints: \n");
        for (Room waypoint : listViewWaypoints.getItems()) {
            sb.append(rooms.getNode(waypoint)).append(", \n");
        }
        //Print to avoid list
        sb.append("\nAvoid \n");
        for (Room avoid : listViewAvoid.getItems()) {
            sb.append(rooms.getNode(avoid)).append(", \n");
        }
        //Print the results
        sb.append("\nResults \n");
        for (Room room : results) {
            sb.append(rooms.getNode(room)).append(", \n");
        }
        System.out.println(sb.toString());
    }

    @FXML
    void btnClearClicked(MouseEvent event) {
        routeListView.getItems().clear();
    }

    private Room getRoomWithID(int id){
        for(Room r : roomsList){
            if(r.getId() == id){
                return r;
            }
        }
        return null;
    }

    private String status;
    private void setStatus(String status) {
        this.status = status;
        lblStatus.setText(((pixels == null) ? "(Loading Graph of Pixels...) " : "") + status);
    }
    private void setPixels(Graph<Pixel> pixels) {
        this.pixels = pixels;
        Platform.runLater(() -> setStatus(status));
    }
}