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

    //Adds a node to the graph.
    public void addNode(T data) {
        nodes.add(new Node<T>(data));
    }

    //Adds an edge between two nodes.
    public void addEdge(T from, T to) {
        Node<T> fromNode = getNode(from);
        Node<T> toNode = getNode(to);
        assert fromNode != null;
        fromNode.addEdge(toNode);
    }

    //Remove an edge between two nodes.
    public void removeEdge(T from, T to) {
        Node<T> fromNode = getNode(from);
        Node<T> toNode = getNode(to);
        assert fromNode != null;
        fromNode.removeEdge(toNode);
    }

    //Removes a node from the graph.
    public void removeNode(T data) {
        Node<T> node = getNode(data);
        assert node != null;
        nodes.remove(node);
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