package com.ca.two.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    @Test
    void dijkstra() {
        //Create a graph to test with
        Graph<String> graph = new Graph();
        graph.addNode("S");
        graph.addNode("A", "S");
        graph.addNode("B", "S", "A");
        graph.addNode("C", "S");
        graph.addNode("D", "A", "B");
        graph.addNode("H", "B");
        graph.addNode("F", "H", "D");
        graph.addNode("G", "H");
        graph.addNode("L", "C");
        graph.addNode("I", "L");
        graph.addNode("J", "L", "I");
        graph.addNode("K", "I", "J");
        graph.addNode("E", "K", "G");

        //There should be 18 edges in the graph
        graph.getNode("S").setEdgeWeight(graph.getNode("A"), 7f/8f);
        graph.getNode("S").setEdgeWeight(graph.getNode("B"), 2f/8f);
        graph.getNode("A").setEdgeWeight(graph.getNode("B"), 3f/8f);
        graph.getNode("S").setEdgeWeight(graph.getNode("C"), 3f/8f);
        graph.getNode("A").setEdgeWeight(graph.getNode("D"), 4f/8f);
        graph.getNode("B").setEdgeWeight(graph.getNode("H"), 1f/8f);
        graph.getNode("B").setEdgeWeight(graph.getNode("D"), 4f/8f);
        graph.getNode("D").setEdgeWeight(graph.getNode("F"), 5f/8f);
        graph.getNode("H").setEdgeWeight(graph.getNode("G"), 2f/8f);
        graph.getNode("H").setEdgeWeight(graph.getNode("F"), 3f/8f);
        graph.getNode("C").setEdgeWeight(graph.getNode("L"), 2f/8f);
        graph.getNode("L").setEdgeWeight(graph.getNode("I"), 4f/8f);
        graph.getNode("L").setEdgeWeight(graph.getNode("J"), 4f/8f);
        graph.getNode("I").setEdgeWeight(graph.getNode("J"), 6f/8f);
        graph.getNode("I").setEdgeWeight(graph.getNode("K"), 4f/8f);
        graph.getNode("J").setEdgeWeight(graph.getNode("K"), 4f/8f);
        graph.getNode("K").setEdgeWeight(graph.getNode("E"), 5f/8f);
        graph.getNode("G").setEdgeWeight(graph.getNode("E"), 2f/8f);

        //Test the Dijkstra algorithm
        var result = Algorithms.Dijkstra(graph, "S", "E");
        var expected = new String[]{"S", "B", "H", "G", "E"};
        assertArrayEquals(expected, result.toArray());
    }
}