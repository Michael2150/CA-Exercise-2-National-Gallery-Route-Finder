package com.ca.two;

import com.ca.two.graph.Graph;
import com.ca.two.models.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {
    private LinkedList<Room> list_of_rooms;
    private Graph<Room> rooms_graph;

    @AfterEach
    void tearDown() {
        list_of_rooms = null;
        rooms_graph = null;
    }

    @Test
    void readInCSV() {
        list_of_rooms = DataAccess.readInCSV();

        //Make sure the list has 67 rooms
        assertEquals(67, list_of_rooms.size());
    }

    @Test
    void createGraph() {
        list_of_rooms = DataAccess.readInCSV();
        rooms_graph = DataAccess.createGraph(list_of_rooms);

        //Make sure the graph has 67 nodes
        assertEquals(67, rooms_graph.getNodes().size());
    }

}