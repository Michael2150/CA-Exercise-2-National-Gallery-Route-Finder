package com.ca.two;

import com.ca.two.graph.Graph;
import com.ca.two.graph.Pixel;
import com.ca.two.models.Room;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class DataAccess {
    public static LinkedList<Room> readInCSV() {
        LinkedList<Room> rooms = new LinkedList<>();

        //If there is a header row in the CSV, skip it
        boolean shouldSkipHeader = true;

        // CSV file to read in
        File file = new File("data/data.txt");

        // tab delimited file
        String delimiter = "\t";

        //Array Indices for CSV
        final int ROOM_ID = 0;
        final int ROOM_NAME = 1;
        final int ROOM_DESCRIPTION = 2;
        final int ROOM_CONNECTIONS = 3;
        final int ROOM_IMAGE_URL = 4;
        final int ROOM_X = 5;
        final int ROOM_Y = 6;

        try (FileReader fr = new FileReader(file)) { //Get the FileReader
            try (BufferedReader br = new BufferedReader(fr)) { //Get the BufferedReader
                String line;
                while ((line = br.readLine()) != null) { //Read the file line by line
                    if (shouldSkipHeader) {
                        shouldSkipHeader = false;
                        continue;
                    }

                    String[] values = line.split(delimiter); //Split the line into an array of values

                    if (values.length != 8)
                        continue;

                    //Remove " from the values
                    for (int i = 0; i < values.length; i++) {
                        values[i] = values[i].replaceAll("\"", "");
                    }

                    //Create a new Room object
                    Room room = new Room();
                    room.setId(Integer.parseInt(values[ROOM_ID]));
                    room.setName(values[ROOM_NAME]);
                    room.setDescription(values[ROOM_DESCRIPTION]);
                    room.setImage_url(values[ROOM_IMAGE_URL]);

                    //Get the x and y coordinates and set them
                    int x = Integer.parseInt(values[ROOM_X]);
                    int y = Integer.parseInt(values[ROOM_Y]);
                    room.setPosition(new Pixel(x, y));

                    //Create a list of connections
                    String[] connections = values[ROOM_CONNECTIONS].split(","); //Split the connections into an array of values
                    LinkedList<Integer> connectionList = new LinkedList<>(); //Create a new LinkedList to store the connections
                    List.of(connections).forEach(connection -> connectionList.add(Integer.parseInt(connection))); //Cast each string to an integer and add to the list
                    room.setConnectedRooms(connectionList); //Set the connections for the room

                    //Add the room to the list
                    rooms.add(room);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rooms;
    }


    //Read in the mask image and create a graph of the green pixels in the image
    public static Graph<Pixel> readInMask() {
        Graph<Pixel> graph = new Graph<>();

        //Get the image
        File file = new File("data/floorplan_mask.png");
        Image image = new Image(file.toURI().toString());

        //Get the width and height of the image
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        //Get the pixelReader
        PixelReader pixelReader = image.getPixelReader();

        //Get the color of the first pixel
        Color color = pixelReader.getColor(0, 0);

        //Loop through the image and add the edges
        for (int y = 0; y < height; y ++) {
            for (int x = 0; x < width; x ++) {
                //Get the color of the pixel
                Color center = pixelReader.getColor(x, y);

                //If the center is the same as the color of the first pixel, add it as a node
                if (center.equals(color)) {
                    var newCurNode = new Pixel(x, y).multiply(2);
                    graph.addNode(newCurNode);

                    //Check the surrounding pixels and add edges if they are the same color
                    Color top = (y - 1 >= 0) ? pixelReader.getColor(x, y - 1) : Color.TRANSPARENT;
                    Color left = (x - 1 >= 0) ? pixelReader.getColor(x - 1, y) : Color.TRANSPARENT;

                    if (top.equals(color)) {
                        var newNode = new Pixel(x, y - 1).multiply(2);
                        graph.addNode(newNode);
                        graph.addEdge(newCurNode, newNode);
                    }
                    if (left.equals(color)) {
                        var newNode = new Pixel(x - 1, y).multiply(2);
                        graph.addNode(newNode);
                        graph.addEdge(newCurNode, newNode);
                    }
                }
            }
        }

        return graph;
    }

    //Get the closest value on the graph to the given value
    public static Pixel getClosestValue(Graph<Pixel> graph, Pixel value) {
        var closestNode = graph.getNode(value);
        Pixel closestPixel = null;

        if (closestNode == null) {
            var closestDist = Double.POSITIVE_INFINITY;
            for (var pixel : graph.getNodes()) {
                //Find the distance between the pixel and the value
                var distance = pixel.distance(value);

                //If the distance is less than the closest distance, set the closest distance and pixel
                if (distance < closestDist) {
                    closestDist = distance;
                    closestPixel = pixel;
                }
            }
        } else {
            closestPixel = closestNode.getValue();
        }

        return closestPixel;
    }

    //Create a Graph from LinkedList of Rooms
    public static Graph<Room> createGraph(LinkedList<Room> rooms) {
        Graph<Room> graph = new Graph<>();

        for (Room room : rooms) {
            graph.addNode(room);
        }

        for (Room room : rooms) {
            for (Integer connection : room.getConnectedRooms()) {
                for (Room connectedRoom : rooms) {
                    if (connectedRoom.getId() == connection)
                        graph.addEdge(room, connectedRoom);
                }
            }
        }

        return graph;
    }
}