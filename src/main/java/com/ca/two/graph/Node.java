package com.ca.two.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

//A Node of a graph.
public class Node<T> {
    private final UUID id;
    private T value;
    private HashMap<Node<T>, Float> edges;

    //Constructor
    public Node(T value) {
        this.value = value;
        this.id = UUID.randomUUID();
        edges = new HashMap();
    }

    //Getters and setters
    public T getValue() {
        return value;
    }
    public void setValue(T value) {
        this.value = value;
    }

    //Add edge to node
    public void addEdge(Node<T> node, float weight) {
        //Make sure weight is between 0 and 1
        if (weight < 0 || weight > 1)
            throw new IllegalArgumentException("Weight must be between 0 and 1");

        if (!containsEdge(node)) {
            edges.put(node, weight);
            node.addEdge(this, weight);
        }
    }

    //Remove edge from node
    public void removeEdge(Node<T> node) {
        if (containsEdge(node)) {
            edges.remove(node);
            node.removeEdge(this);
        }
    }

    //Returns the weight of the edge between this node and the given node
    //Returns a number between 0 and 1
    //Returns -1 if there is no edge between the two nodes
    public float getEdgeWeight(Node<T> node) {
        if (containsEdge(node)) {
            return edges.get(node);
        }
        return -1;
    }

    //Sets the weight of the edge between this node and the given node
    //Returns true if the weight was changed
    //Returns false if there was no edge between the two nodes
    public boolean setEdgeWeight(Node<T> node, float weight) {
        if (containsEdge(node)) {
            if (edges.get(node) != weight) {
                edges.put(node, weight);
                node.setEdgeWeight(this, weight);
            }
            return true;
        }
        return false;
    }

    //Returns a list of Edges around this node
    public LinkedList<Edge<T>> getEdges() {
        LinkedList<Edge<T>> edges = new LinkedList<>();
        for (Node<T> node : this.edges.keySet() ) {
            edges.add(new Edge<>(this, node, this.edges.get(node)));
        }
        return edges;
    }

    //Contains edge to node
    public boolean containsEdge(Node<T> node) {
        return edges.containsKey(node);
    }

    //Equals
    public boolean equals(Node<T> node) {
        return this.id.equals(node.id);
    }

    //ToString
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Node: Data[").append(value).append("] Edges[" + edges.size() + "]");
        return sb.toString();
    }
}