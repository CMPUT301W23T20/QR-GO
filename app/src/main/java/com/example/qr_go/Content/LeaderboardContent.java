package com.example.qr_go.Content;

import com.google.common.collect.Sets;

import java.io.Serializable;

/**
 * Represents the content present in a part of the leaderboard such as username and score
 */
public class LeaderboardContent implements Serializable {
    private String username;
    private int score;

    public LeaderboardContent(String name, int score){
        this.username = name;
        this.score = score;
    }

    /**
     *
     * @return the String username of the player
     */
    public String getUserName(){
        return username;
    }

    /**
     *
     * @return the integer total score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     *
     * @param newName
     * Sets the username
     */
    public void setName(String newName) {
        this.username = newName;
    }

    /**
     *
     * @param newScore
     * Sets the score
     */
    public void setProvince(int newScore) {
        this.score= newScore;
    }
}