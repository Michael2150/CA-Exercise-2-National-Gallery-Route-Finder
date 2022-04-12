package com.ca.two;

import com.ca.two.models.Room;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

public class DataAccess {
    public static LinkedList<Room> readInCSV() {
        LinkedList<Room> rooms = new LinkedList<>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            File file = new File("data/data.csv");
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String delimiter = ",";
            String curLine;
            String[] values;
            while ((curLine = br.readLine()) != null) {
                values = curLine.split(delimiter);

                Room room = new Room();
                room.setName(values[0]);
                room.setDescription(values[1]);
                room.setImage_url(values[2]);
                rooms.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null)
                    fr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (br != null)
                    br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rooms;
    }
}