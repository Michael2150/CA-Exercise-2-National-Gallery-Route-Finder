package com.ca.two;

import com.ca.two.graph.*;
import com.ca.two.listviews.RoomInfoListCell;
import com.ca.two.listviews.RoomListCell;
import com.ca.two.models.Pixel;
import com.ca.two.models.Room;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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
    private RadioButton chkIncludeWaypoints;
    @FXML
    private RadioButton chkShortestPath;
    @FXML
    private ToggleGroup pathTypeGroup;
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

            //For each room in rooms set the position to the closest pixel using the DataAccess.getClosestValue() method
            for(Room room : roomsList){
                room.setPosition(DataAccess.getClosestValue(pixels, room.getPosition()));
            }

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
    void btnClearClicked(MouseEvent event) {
        routeListView.getItems().clear();
        routeOverlay.setImage(null);
    }

    @FXML
    void btnBFSClicked(MouseEvent event) {
        testing();

        //Start the timer
        long startTime = System.currentTimeMillis();

        //Set the status label
        setStatus("Running Breadth First Search Algorithm...");

        //Get the start and destination rooms
        Room start = startChoiceBox.getSelectionModel().getSelectedItem();
        Room destination = destinationChoiceBox.getSelectionModel().getSelectedItem();

        var results = Algorithms.BFS(rooms, start, destination);

        //Set the status to loading the route image
        setStatus("Loading route image...");

        //Generate the route image
        loadImageView(results);

        //Set the status to the time taken
        setStatus("Ready ("+ (System.currentTimeMillis() - startTime) + "ms)" + (pixelsLoaded() ? "" : " (Path could not be plotted, pixels still loading...) "));

        loadRouteResults(results);

        debug_graph(results);
    }

    @FXML
    void btnDFSClicked(MouseEvent event) {
        testing();

        //Start the timer
        long startTime = System.currentTimeMillis();

        //Set the status label
        setStatus("Running Depth First Search Algorithm...");

        //Get the start and destination rooms
        Room start = startChoiceBox.getSelectionModel().getSelectedItem();
        Room destination = destinationChoiceBox.getSelectionModel().getSelectedItem();

        var results = Algorithms.DFSAllPaths(rooms, start, destination);

        //Get All the paths from the results
        setStatus("Generating all paths...");

        if (pixelsLoaded()) {
            var paths = new LinkedList<LinkedList<Pixel>>();
            for (var result : results) {
                //print the index of the result out of the results
                System.out.println("Result: " + results.indexOf(result)  + " of " + results.size());

                //Get the path
                var path = Algorithms.getBFSPixelsBetweenRooms(pixels, result);

                //Add the path to the list of paths
                paths.add(path);
            }

            //Set the status to loading the route image
            setStatus("Loading route image...");

            //Generate the route image

        }

        //Set the status to the time taken
        setStatus("Ready ("+ (System.currentTimeMillis() - startTime) + "ms)" + (pixelsLoaded() ? "" : " (Path could not be plotted, pixels still loading...) "));

        //Load the results
        //loadRouteResults(results);
    }

    @FXML
    void btnDijkstraClicked(MouseEvent event) {
        testing();

        //Clear the route list view
        btnClearClicked(null);

        //Start a timer
        long startTime = System.currentTimeMillis();

        setStatus("Setting up weights...");

        Algorithms.setAllWeights(rooms);

        if (!shouldGetShortestPath()) //If we're not getting the shortest path, we need to set the weights of the waypoints
            Algorithms.setupGraphWeights(rooms, 5f, 0.1f, listViewWaypoints, listViewAvoid);

        setStatus("Running Dijkstra's algorithm...");

        var startRoom = startChoiceBox.getSelectionModel().getSelectedItem();
        var destinationRoom = destinationChoiceBox.getSelectionModel().getSelectedItem();
        LinkedList<Room> results = new LinkedList<>();

        if (shouldIncludeWaypoints()){
            LinkedList<Node<Room>> waypointList = new LinkedList<>();
            var startNode = rooms.getNode(startRoom);
            var destinationNode = rooms.getNode(destinationRoom);

            waypointList.add(startNode); //Add the start room to the list
            listViewWaypoints.getItems().forEach(room -> waypointList.add(rooms.getNode(room))); //Add all the waypoints to the list
            waypointList.add(destinationNode); //Add the destination room to the list

            //Sort the waypoints away from the start room yet closer to the destination room
            waypointList.sort(Comparator.comparingInt(node -> {
                var startToWaypoint = startNode.getDistance(node);
                var destinationToWaypoint = destinationNode.getDistance(node);
                return startToWaypoint - destinationToWaypoint;
            }));

            //Get the path between each waypoint in the waypoint list
            results.add(startRoom);
            for (int i = 0; i < waypointList.size() - 1; i++) {
                var currentNode = waypointList.get(i).getValue();
                var nextNode = waypointList.get(i + 1).getValue();
                var d = Algorithms.Dijkstra(rooms, currentNode, nextNode);
                d.remove(); //Remove the start room from the path
                results.addAll(d);
            }

        } else {
            results = Algorithms.Dijkstra(rooms, startRoom, destinationRoom);
        }

        setStatus("Plotting path...");
        loadImageView(results);

        //Set the status to the time taken
        setStatus("Ready ("+ (System.currentTimeMillis() - startTime) + "ms)" + (pixelsLoaded() ? "" : " (Path could not be plotted, pixels still loading...) "));

        loadRouteResults(results);

        debug_graph(results);
    }

    private void loadImageView(LinkedList<Room> results) {
        var pxls = Algorithms.getBFSPixelsBetweenRooms(pixels , results);
        var img = Algorithms.getPathImageForPixels((int) routeOverlay.getFitWidth(), (int) routeOverlay.getFitHeight(), pxls);
        routeOverlay.setImage(img);
    }

    private boolean shouldIncludeWaypoints() {
        return pathTypeGroup.getSelectedToggle() == chkIncludeWaypoints;
    }
    private boolean shouldGetShortestPath() {
        return pathTypeGroup.getSelectedToggle() == chkShortestPath;
    }
    private boolean pixelsLoaded() {
        return pixels != null && pixels.size() > 0;
    }

    private void loadRouteResults(LinkedList<Room> results) {
        routeListView.getItems().clear();
        routeListView.getItems().addAll(results);
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
        lblStatus.setText(((!pixelsLoaded()) ? "(Loading Graph of Pixels...) " : "") + status);
    }
    private void setPixels(Graph<Pixel> pixels) {
        this.pixels = pixels;
        Platform.runLater(() -> setStatus(status));
    }

    private void testing(){
//        listViewAvoid.getItems().clear();
//        listViewWaypoints.getItems().clear();
//        startChoiceBox.getSelectionModel().select(getRoomWithID(42));
//        destinationChoiceBox.getSelectionModel().select(getRoomWithID(21));
//        listViewAvoid.getItems().addAll(getRoomWithID(10), getRoomWithID(11));
//        listViewWaypoints.getItems().addAll(getRoomWithID(26), getRoomWithID(27), getRoomWithID(28));
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
}