package com.ca.two.graph;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    private Graph<Integer> graph;

    @BeforeEach
    void setUp() {
        graph = new Graph();
    }

    @AfterEach
    void tearDown() {
        graph = null;
    }

    @Test
    void addNode() {
        //Add a node to the graph and check if it is added
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);

        assertTrue(graph.contains(1));
        assertTrue(graph.contains(2));
        assertTrue(graph.contains(3));

        //Check the size of the graph
        assertEquals(3, graph.size());
    }

    @Test
    void addEdge() {
        //Add the nodes to the graph
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);

        //Add the edges to the graph
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);

        //Check if the edges are added
        assertTrue(graph.containsEdge(1, 2));
        assertTrue(graph.containsEdge(1, 3));
        assertTrue(graph.containsEdge(2, 3));

        //Check the size of the graph
        assertEquals(3, graph.size());
    }

    @Test
    void removeNode() {
        //Add the nodes to the graph
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);

        //Remove the nodes from the graph
        graph.removeNode(1);
        graph.removeNode(2);
        graph.removeNode(3);

        //Check if the nodes are removed
        assertFalse(graph.contains(1));
        assertFalse(graph.contains(2));
        assertFalse(graph.contains(3));

        //Check the size of the graph
        assertEquals(0, graph.size());
    }

    @Test
    void removeEdge() {
        //Add the nodes to the graph
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);

        //Add the edges to the graph
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);

        //Remove the edges from the graph
        graph.removeEdge(1, 2);
        graph.removeEdge(1, 3);
        graph.removeEdge(2, 3);

        //Check if the edges are removed
        assertFalse(graph.containsEdge(1, 2));
        assertFalse(graph.containsEdge(1, 3));
        assertFalse(graph.containsEdge(2, 3));

        //Check the size of the graph
        assertEquals(3, graph.size());
    }

    @Test
    void getNeighbors() {
        //Add the nodes to the graph
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);

        //Add the edges to the graph
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);

        //Check if the neighbors are returned
        assertTrue(graph.getNeighbors(1).contains(2));
        assertTrue(graph.getNeighbors(1).contains(3));
        assertTrue(graph.getNeighbors(2).contains(3));

        //Check the size of the graph
        assertEquals(3, graph.size());
    }

    @Test
    void contains() {
        //Add the nodes to the graph
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);

        //Check if the nodes are contained
        assertTrue(graph.contains(1));
        assertTrue(graph.contains(2));
        assertTrue(graph.contains(3));

        //Check the size of the graph
        assertEquals(3, graph.size());
    }
}