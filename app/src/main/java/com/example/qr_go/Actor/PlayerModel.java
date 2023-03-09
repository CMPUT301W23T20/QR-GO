package com.example.qr_go.Actor;

import com.example.qr_go.QR.QR;
import com.google.android.material.color.utilities.Score;

import java.util.ArrayList;

public class PlayerModel extends Actor {
    private ArrayList<QR> qrList;
    private int rank;
    private int highestScore;
    private int lowestScore;
    private int totalScore;

    /**
     * Constructor for creating brand new player
     * @param username
     * Player's username
     * @param deviceID
     * Player's device ID
     */
    public PlayerModel(String username, String deviceID) {
        super(username, deviceID);
        qrList = new ArrayList<>();
    }

    /**
     * Constructor for creating existing player from database
     * @param username
     * Player's username
     * @param deviceID
     * Player's device ID
     * @param qrList
     * Player's list of QR codes scanned
     * @param rank
     * Player's rank
     * @param highestScore
     * Player's highest score
     * @param lowestScore
     * Player's lowest score
     * @param totalScore
     * Player's total score
     */
    public PlayerModel(String username, String deviceID, ArrayList<QR> qrList,
                       int rank, int highestScore, int lowestScore, int totalScore) {
        super(username, deviceID);
        this.qrList = qrList;
        this.rank = rank;
        this.highestScore = highestScore;
        this.lowestScore = lowestScore;
        this.totalScore = totalScore;
    }

    // getters and setters

    /**
     * Gets player's highest score
     * @return
     * Player's highest score
     */
    public int getHighestScore() {
        return highestScore;
    }

    /**
     * Sets players highest score
     * @param highestScore
     * New highest score
     */
    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    /**
     * Gets player's lowest score
     * @return
     * Player's lowest score
     */
    public int getLowestScore() {
        return lowestScore;
    }

    /**
     * Sets players highest score
     * @param lowestScore
     * New highest score
     */
    public void setLowestScore(int lowestScore) {
        this.lowestScore = lowestScore;
    }

    /**
     * Gets player's total score
     * @return
     * Player's total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Updates player's total score
     * @param operation
     * 1 to add, -1 to subtract
     * @param score
     * Score to be updated with
     */
    public void updateTotalScore(int operation, int score) {
        assert(operation == 1 || operation == -1);

        if(operation == 1) {
            this.totalScore += score;
        }

        else {
            this.totalScore -= score;
        }
    }

    /**
     * Gets player's rank
     * @return
     * Player's rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets player's rank
     * @param rank
     * Player's rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Gets player's list of QR codes scanned
     * @return
     * Player's list of QR codes scanned
     */
    public ArrayList<QR> getQRList() {
        return qrList;
    }

    /**
     * Gets total number of QRs scanned
     * @return
     * Total number of QRs scanned
     */
    public int getTotalQR() {
        return qrList.size();
    }
}