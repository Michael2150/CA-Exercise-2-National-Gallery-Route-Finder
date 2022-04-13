package com.ca.two.models;

import com.ca.two.graph.Pixel;

import java.util.LinkedList;
import java.util.Objects;

public class Room {
    private int id;
    private String name;
    private String description;
    private LinkedList<Integer> connectedRooms;
    private String image_url;
    private Pixel position;

    public Room() {}

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkedList<Integer> getConnectedRooms() {
        return connectedRooms;
    }

    public void setConnectedRooms(LinkedList<Integer> connectedRooms) {
        this.connectedRooms = connectedRooms;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Pixel getPosition() {
        return position;
    }

    public void setPosition(Pixel position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return Objects.equals(getId(), room.getId());
    }
}
