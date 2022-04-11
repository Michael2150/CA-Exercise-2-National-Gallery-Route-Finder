package com.ca.two.graph;

import java.util.*;

public class Algorithms {
    /**
     * Dijkstra's algorithm
     * @param graph graph
     * @param start start Node
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
        HashMap<Node<T>, Float> distances = new HashMap<>();
        HashMap<Node<T>, Node<T>> previous = new HashMap<>();
        HashMap<Node<T>, Boolean> visited = new HashMap<>();

        //Create priority queue
        Comparator<Edge<T>> comparator = (e1, e2) -> (int) (e1.getWeight() - e2.getWeight());
        PriorityQueue<Edge<T>> queue = new PriorityQueue<>(comparator);

        //Set up the variables
        for (T nodeVal : graph.getNodes()) {
            Node<T> node = graph.getNode(nodeVal);
            distances.put(node, Float.MAX_VALUE);
            previous.put(node, null);
            visited.put(node, false);
        }
        distances.put(begin, 0f);

        //Do actual algorithm
        //Add the beginning node
        queue.add(new Edge<>(begin, begin, 1f));

        //While there are still nodes to visit
        while (!queue.isEmpty() || !visited.get(end)) {
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
                    var newDistance = distances.get(closestNode) + neighbor.getWeight();
                    if (newDistance < distances.get(neighborNode)) {
                        distances.put(neighborNode, newDistance);
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
}
