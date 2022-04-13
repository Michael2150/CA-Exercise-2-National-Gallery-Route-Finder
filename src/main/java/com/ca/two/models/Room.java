package com.ca.two.models;

import com.ca.two.graph.Pixel;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Objects;

public class Room {
    private int id;
    private String name;
    private String description;
    private LinkedList<Integer> connectedRooms;
    private String image_url;
    private Pixel position;
    private String timePeriod;

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

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Color getColor() {
        switch (getTimePeriod().trim()) {
            case "1200-1500":
                return Color.rgb(84, 133, 189);
            case "1500-1600":
                return Color.rgb(172, 62, 89);
            case "1600-1700":
                return Color.rgb(234, 141, 0);
            case "1700-1930":
                return Color.rgb(102, 131, 91);
            default:
                return Color.rgb(0, 0, 0);
        }
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
