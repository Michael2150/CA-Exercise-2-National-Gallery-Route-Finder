package com.ca.two.graph;

import java.util.LinkedList;

//A Graph data structure of type T.
//The Graph stores nodes that are linked to each other with references.
//This is an edgeless graph.
public class Graph<T> {
    private LinkedList<Node<T>> nodes;

    //Constructor
    public Graph() {
        nodes = new LinkedList<Node<T>>();
    }

    //Add a Node to the Graph.
    //Returns true if the node was added, false if it was not.
    public boolean addNode(T data) {
        if (contains(data))
            return false;

        nodes.add(new Node<T>(data));
        return true;
    }

    //Add an Edge to the Graph.
    //Returns true if the edge was added, false if it was not.
    public boolean addEdge(T from, T to) {
        if (!contains(from) || !contains(to))
            throw new IllegalArgumentException("One of the nodes does not exist.");

        Node<T> fromNode = getNode(from);
        Node<T> toNode = getNode(to);

        fromNode.addEdge(toNode);
        return true;
    }

    //Remove Node from the Graph.
    //Returns true if the node was removed, false if it was not.
    public boolean removeNode(T data) {
        if (!contains(data))
            return false;

        Node<T> node = getNode(data);
        for (Node<T> n : nodes) {
            n.removeEdge(node);
        }
        nodes.remove(node);
        return true;
    }

    //Remove Edge from the Graph.
    //Returns true if the edge was removed, false if it was not.
    public boolean removeEdge(T from, T to) {
        if (!contains(from) || !contains(to))
            throw new IllegalArgumentException("One of the nodes does not exist.");

        Node<T> fromNode = getNode(from);
        Node<T> toNode = getNode(to);

        fromNode.removeEdge(toNode);
        return true;
    }

    //Returns a list of the neighbors around a node.
    //Returns null if the node does not exist.
    public LinkedList<T> getNeighbors(T data) {
        if (!contains(data))
            return null;

        Node<T> node = getNode(data);
        var neighbors = new LinkedList<T>();
        for (Node<T> n : node.getEdges()) {
            neighbors.add(n.getData());
        }
        return neighbors;
    }

    //Returns true if the Graph contains the Node, false if it does not.
    public boolean contains(T data) {
        for (Node<T> n : nodes) {
            if (n.getData().equals(data))
                return true;
        }
        return false;
    }

    //Returns the node with the given data.
    private Node<T> getNode(T data) {
        for (Node<T> node : nodes) {
            if (node.getData().equals(data)) {
                return node;
            }
        }
        return null;
    }
}