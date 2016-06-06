package com.android.apanc.app.model;

/**
 * Created by apanc on 29-May-16.
 */
public class Team {
    private String id;
    private String points;
    private String color;

    public Team() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
