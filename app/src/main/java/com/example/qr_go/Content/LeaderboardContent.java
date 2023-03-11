package com.example.qr_go.Content;

import java.io.Serializable;

public class LeaderboardContent implements Serializable {
    private String username;
    private int score;

    public LeaderboardContent(String name, int score){
        this.username = name;
        this.score = score;
    }

    public String getUserName(){
        return username;
    }

    public int getScore() {
        return score;
    }

    // Setters
    public void setName(String newName) {
        this.username = newName;
    }
    public void setProvince(int newScore) {
        this.score= newScore;
    }
}