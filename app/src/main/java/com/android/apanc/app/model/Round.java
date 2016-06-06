package com.android.apanc.app.model;

/**
 * Created by apanc on 25-May-16.
 */
public class Round {
    private String text;
    private String points;
    private String options;
    private Team team;

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Team getTeam() {
        return team;
    }
}
