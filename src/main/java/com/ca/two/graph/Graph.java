package com.ca.two.graph;

import java.io.Serializable;
import java.util.LinkedList;

//A Graph data structure of type T.
//The Graph stores nodes that are linked to each other with references.
//This is an edgeless graph.
public class Graph<T> implements Serializable {
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

    //Adds a node to the Graph. and connects it to the given nodes.
    public boolean addNode(T newData, T... neighbors) {
        if (contains(newData))
            return false;

        nodes.add(new Node<T>(newData));
        for (T n : neighbors) {
            addEdge(newData, n);
        }
        return true;
    }

    //Get weight between two nodes.
    public float getWeight(T node1, T node2) {
        if (!contains(node1) || !contains(node2))
            throw new IllegalArgumentException("One of the nodes does not exist.");

        Node<T> n1 = getNode(node1);
        assert n1 != null;

        Node<T> n2 = getNode(node2);
        assert n2 != null;

        return n1.getEdgeWeight(n2);
    }

    //Add an Edge to the Graph.
    //Returns true if the edge was added, false if it was not.
    public boolean addEdge(T from, T to) {
        return addEdge(from, to, 0.5f);
    }
    public boolean addEdge(T from, T to, float weight) {
        if (!contains(from) || !contains(to))
            throw new IllegalArgumentException("One of the nodes does not exist.");

        Node<T> fromNode = getNode(from);
        Node<T> toNode = getNode(to);

        fromNode.addEdge(toNode, weight);
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
        assert fromNode != null;

        Node<T> toNode = getNode(to);

        fromNode.removeEdge(toNode);
        return true;
    }

    //Returns a list of all the edges in the graph.
    public LinkedList<Edge<T>> getEdges() {
        var edges = new LinkedList<Edge<T>>();
        for (var node : nodes) {
            edges.addAll(node.getEdges());
        }

        //Remove duplicates
        var uniqueEdges = new LinkedList<Edge<T>>();
        for (var edge : edges) {
            if (!uniqueEdges.contains(edge))
                uniqueEdges.add(edge);
        }

        return uniqueEdges;
    }

    //Returns the count of edges in the graph.
    public int getEdgeCount() {
        return getEdges().size();
    }

    //Returns a list of the neighbors around a node.
    //Returns null if the node does not exist.
    public LinkedList<T> getNeighbors(T data) {
        if (!contains(data))
            return null;

        Node<T> node = getNode(data);
        assert node != null;

        var neighbors = new LinkedList<T>();
        for (var edge : node.getEdges()) {
            neighbors.add(edge.getTo().getValue());
        }
        return neighbors;
    }

    //Returns true if the Graph contains the Node, false if it does not.
    public boolean contains(T data) {
        for (Node<T> n : nodes) {
            if (n.getValue().equals(data))
                return true;
        }
        return false;
    }

    //Return all the node values in the graph.
    public LinkedList<T> getNodes() {
        var nodes = new LinkedList<T>();
        for (var node : this.nodes) {
            nodes.add(node.getValue());
        }
        return nodes;
    }

    //Returns true if there is a path between two nodes, false if not.
    public boolean containsEdge(T from, T to) {
        if (!contains(from) || !contains(to))
            throw new IllegalArgumentException("One of the nodes does not exist.");

        Node<T> fromNode = getNode(from);
        Node<T> toNode = getNode(to);

        assert fromNode != null;
        return fromNode.containsEdge(toNode);
    }

    //Returns the amount of nodes in the Graph.
    public int size() {
        return nodes.size();
    }

    //Returns the node with the given data.
    public Node<T> getNode(T data) {
        for (Node<T> node : nodes) {
            if (node.getValue().equals(data))
                return node;
        }
        return null;
    }

    //ToString
    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Graph: Node Count: ").append(size()).append(" Edge Count: ").append(getEdgeCount());
        return sb.toString();
    }
}