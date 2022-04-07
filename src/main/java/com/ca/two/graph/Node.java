package com.ca.two.graph;

import java.util.LinkedList;

//A Node of a graph.
public class Node<T> {
    private T data;
    private LinkedList<Node<T>> edges;

    //Constructor
    public Node(T data) {
        this.data = data;
        edges = new LinkedList<Node<T>>();
    }

    //Adds an edge to the node.
    public void addEdge(Node<T> node) {
        edges.add(node);
    }

    //Returns the data of the node.
    public T getData() {
        return data;
    }

    //Removes this node from all of its edges.
    public void removeItself() {
        for (Node<T> node : edges) {
            node.removeEdge(this);
        }
    }

    //Removes the given node from this node's edges.
    public void removeEdge(Node<T> node) {
        edges.remove(node);
    }

    //Returns a string representation of the node.
    public String toString() {
        return "Node: [" + data.toString() + "]";
    }
}