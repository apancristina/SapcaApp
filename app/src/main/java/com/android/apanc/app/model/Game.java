package com.android.apanc.app.model;

import java.util.List;

/**
 * Created by apanc on 29-May-16.
 */
public class Game {
    private String id;
    private List<Team> teams;

    public Game() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
