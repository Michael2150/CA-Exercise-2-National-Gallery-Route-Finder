package com.ca.two.graph;

import com.ca.two.models.Room;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.*;

public class Algorithms {
    /**
     * Dijkstra's algorithm
     * @param graph graph
     * @param start start Node
     * @param destination end Node
     * @param <T> type of node
     * @return A list of nodes in order from the start node to the end node.
     */
    public static <T> LinkedList<T> Dijkstra(Graph<T> graph , T start, T destination) {
        //Check inputs
        var begin = graph.getNode(start);
        var end = graph.getNode(destination);
        if (begin == null || end == null)
            throw new IllegalArgumentException("start or end node is not in the graph");

        //Create variables
        HashMap<Node<T>, Float> weights = new HashMap<>();
        HashMap<Node<T>, Node<T>> previous = new HashMap<>();
        HashMap<Node<T>, Boolean> visited = new HashMap<>();

        //Create priority queue
        Comparator<Edge<T>> comparator = (e1, e2) -> (int) (e1.getWeight() - e2.getWeight());
        PriorityQueue<Edge<T>> queue = new PriorityQueue<>(comparator);

        //Set up the variables
        for (Node<T> node : graph.getNodes()) {
            weights.put(node, Float.MAX_VALUE);
            previous.put(node, null);
            visited.put(node, false);
        }
        weights.put(begin, 0f);

        //Do actual algorithm
        //Add the beginning node
        queue.add(new Edge<>(begin, begin, 1f));

        //While there are still nodes to visit
        while (!queue.isEmpty() && !visited.get(end)) {
            var closestEdge = queue.poll();
            var closestNode = closestEdge.getTo();

            //If the node is already visited, skip it
            if (visited.get(closestNode))
                continue;

            //Mark the node as visited
            visited.put(closestNode, true);

            for (var neighbor : closestNode.getEdges()) {
                var neighborNode = neighbor.getTo();
                if (!visited.get(neighborNode)) {
                    var newWeight = weights.get(closestNode) + neighbor.getWeight();
                    if (newWeight < weights.get(neighborNode)) {
                        weights.put(neighborNode, newWeight);
                        previous.put(neighborNode, closestNode);
                    }
                    queue.add(new Edge<>(closestNode, neighborNode, neighbor.getWeight()));
                }
            }
        }

        //Create the path
        LinkedList<T> path = new LinkedList<>();
        var current = end;
        while (previous.get(current) != null) {
            path.addFirst(current.getValue());
            current = previous.get(current);
        }
        path.addFirst(current.getValue());
        return path;
    }

    /**
     * Breadth first search
     * @param graph graph
     * @param start start Node
     * @param destination end Node
     * @param <T> type of node
     * @return A list of nodes in order from the start node to the end node.
     */
    public static <T> LinkedList<T> BFS(Graph<T> graph , T start, T destination) {
        //Check inputs
        var begin = graph.getNode(start);
        var end = graph.getNode(destination);
        if (begin == null || end == null)
            throw new IllegalArgumentException("start or end node is not in the graph");

        //Create variables
        HashMap<Node<T>, Boolean> visited = new HashMap<>();
        HashMap<Node<T>, Node<T>> previous = new HashMap<>();
        LinkedList<Node<T>> queue = new LinkedList<>();

        //Set up the variables
        for (Node<T> node : graph.getNodes()) {
            visited.put(node, false);
            previous.put(node, null);
        }
        visited.put(begin, true);
        queue.add(begin);

        //Do actual algorithm
        Node<T> current;
        while (!queue.isEmpty() && !visited.get(end)) {
            current = queue.removeFirst();
            for (var neighbor : current.getEdges()) {
                var neighborNode = neighbor.getTo();
                if (!visited.get(neighborNode)) {
                    visited.put(neighborNode, true);
                    previous.put(neighborNode, current);
                    queue.add(neighborNode);
                }
            }
        }

        //Create the path
        LinkedList<T> path = new LinkedList<>();
        current = end;
        while (previous.get(current) != null) {
            path.addFirst(current.getValue());
            current = previous.get(current);
        }
        path.addFirst(current.getValue());
        return path;
    }

    /**
     * Depth first search
     * @param graph graph
     * @param start start Node
     * @param destination end Node
     * @param <T> type of node
     * @return A List of all possible paths from the start node to the end node.
     */
    public static <T> LinkedList<LinkedList<T>> DFSAllPaths(Graph<T> graph , T start, T destination) {
        //Check inputs
        var begin = graph.getNode(start);
        var end = graph.getNode(destination);
        if (begin == null || end == null)
            throw new IllegalArgumentException("start or end node is not in the graph");

        //Create variables
        HashMap<Node<T>, Boolean> visited = new HashMap<>();
        LinkedList<LinkedList<T>> paths = new LinkedList<>();

        //Set up the variables
        for (Node<T> node : graph.getNodes()) {
            visited.put(node, false);
        }

        getAllPathsRecur(begin, end, visited, new LinkedList<>(), paths);

        return paths;
    }

    /**
     * Depth first search using the implementation from: <a href=https://www.geeksforgeeks.org/find-paths-given-source-destination/> Here </a>
     * @param source source Node
     * @param destination destination Node
     * @param isVisited HashMap of visited nodes
     * @param localPathList LinkedList of nodes in the current path working with
     */
    private static <T> void getAllPathsRecur(Node<T> source,
                                             Node<T> destination,
                                             HashMap<Node<T>, Boolean> isVisited,
                                             List<T> localPathList,
                                             List<LinkedList<T>> allPaths) {
        // if match found then no need to traverse more till depth
        if (source.equals(destination)){
            allPaths.add(new LinkedList<>(localPathList));
            return;
        }

        // Mark the current node
        isVisited.put(source, true);

        // Recur for all the vertices adjacent to current vertex
        for (Edge<T> neighborEdge : source.getEdges()) {
            var neighborNode = neighborEdge.getTo();
            if (!isVisited.get(neighborNode)) {
                // store current node
                localPathList.add(neighborNode.getValue());

                // recur for next node
                getAllPathsRecur(neighborNode, destination, isVisited, localPathList, allPaths);

                // remove current node
                localPathList.remove(neighborNode.getValue());
            }
        }

        // Mark the current node
        isVisited.put(source, false);
    }

    //Set all the connections in a graph to the same weight
    public static void setAllWeights(Graph<Room> graph) {
        for (var node : graph.getNodes()) {
            for (var edge : node.getEdges()) {
                var newWeight = (edge.getTo().getValue().getWeight() + edge.getFrom().getValue().getWeight())/2;
                node.setEdgeWeight(edge.getTo(), newWeight);
            }
        }
    }

    //Sets the weight of a connection between two nodes based on the list of waypoints and the list of points to avoid.
    public static void setupGraphWeights(Graph<Room> rooms, float diffusionDepth, double diffusionWeight, ListView<Room> listViewWaypoints, ListView<Room> listViewAvoid) {
        //Set up the weights for the graph
        LinkedList<Room> waypoints = new LinkedList<>();
        waypoints.addAll(listViewWaypoints.getItems());
        waypoints.addAll(listViewAvoid.getItems());

        for (Room waypoint : waypoints) {
            //Get all the nodes that are depth away from the waypoint
            LinkedList<Node<Room>> nodes = new LinkedList<>();
            HashMap<Edge<Room>, Integer> edgeDiffusion = new HashMap<>();
            HashMap<Edge<Room>, Integer> sign = new HashMap<>();

            nodes.add(rooms.getNode(waypoint));

            for (int i = 1; i <= diffusionDepth; i++) {
                LinkedList<Node<Room>> newNodes = new LinkedList<>();
                for (Node<Room> node : nodes) {
                    for (Edge<Room> edge : node.getEdges()) {
                        if (!nodes.contains(edge.getTo())) {
                            newNodes.add(edge.getTo());
                            edgeDiffusion.put(edge, i);
                            sign.put(edge, listViewWaypoints.getItems().contains(waypoint) ? -1 : +1);
                        }
                    }
                }
                nodes.addAll(newNodes);
            }

            //Apply weights to the edges
            for (Edge<Room> edge : edgeDiffusion.keySet()) {
                float diff = (float) (diffusionDepth * diffusionWeight - (edgeDiffusion.get(edge) * diffusionWeight));
                diff = sign.get(edge) * diff;
                edge.setWeight(edge.getWeight() + diff);
            }
        }

        //Get the lowest weight
        float lowestWeight = Float.MAX_VALUE;
        for (var edge : rooms.getEdges()) {
            if (edge.getWeight() < lowestWeight)
                lowestWeight = edge.getWeight();
        }
        if (lowestWeight < 0){
            //Add all the weights up with the absolute value of the lowest weight
            for (var edge : rooms.getEdges()) {
                edge.setWeight(edge.getWeight() + Math.abs(lowestWeight));
            }
        }
    }

    public static Image getRouteImage(LinkedList<Room> route, ImageView routeOverlay, Color color) {
        //Get the width and height of the image]
        int width = (int) routeOverlay.getFitWidth();
        int height = (int) routeOverlay.getFitHeight();

        //Create a new image
        WritableImage image = new WritableImage(width, height);

        //Get the pixelWriter
        PixelWriter pixelWriter = image.getPixelWriter();

        //Write a circle for each room in the route
        for (Room room : route) {
            //Get the coordinates of the room
            int x = (int) room.getPosition().getX();
            int y = (int) room.getPosition().getY();

            //Draw a circle
            for (int i = -5; i <= 5; i++) {
                for (int j = -5; j <= 5; j++) {
                    if (i * i + j * j <= 25)
                        pixelWriter.setColor(x + i, y + j, Color.RED);
                }
            }
        }

        return image;
    }
}
