package com.ca.two.models;

import javafx.geometry.Point2D;

import java.io.Serializable;

public class Pixel extends Point2D implements Serializable {

    public Pixel(double v, double v1) {
        super(v, v1);
    }

    public Pixel multiply(double v) {
        var point = super.multiply(v);
        return new Pixel(point.getX(), point.getY());
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
