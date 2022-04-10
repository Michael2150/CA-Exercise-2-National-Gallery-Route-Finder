package com.ca.two.graph;

import java.util.LinkedList;
import java.util.UUID;

//A Node of a graph.
public class Node<T> {
    private UUID id;
    private T data;
    private LinkedList<Node<T>> edges;

    //Constructor
    public Node(T data) {
        this.data = data;
        this.id = UUID.randomUUID();
        edges = new LinkedList<Node<T>>();
    }

    //Getters and setters
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    //Add edge to node
    public void addEdge(Node<T> node) {
        if (!containsEdge(node)) {
            edges.add(node);
            node.addEdge(this);
        }
    }

    //Remove edge from node
    public void removeEdge(Node<T> node) {
        if (containsEdge(node)) {
            edges.remove(node);
            node.removeEdge(this);
        }
    }

    //Returns the neighboring nodes
    public LinkedList<Node<T>> getEdges() {
        return edges;
    }

    //Contains edge to node
    public boolean containsEdge(Node<T> node) {
        return edges.contains(node);
    }

    //Equals
    public boolean equals(Node<T> node) {
        return this.id.equals(node.id);
    }
}