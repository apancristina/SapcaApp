package com.example.android.apanc.app.Model;

/**
 * Created by apanc on 25-May-16.
 */
public class Round {
    private String text;
    private int roundPoints;
    private String options;
    private String teamId;
    private String teamColour;




    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRoundPoints() {
        return roundPoints;
    }

    public void setRoundPoints(int roundPoints) {
        this.roundPoints = roundPoints;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamColour() {
        return teamColour;
    }

    public void setTeamColour(String teamColour) {
        this.teamColour = teamColour;
    }
}
