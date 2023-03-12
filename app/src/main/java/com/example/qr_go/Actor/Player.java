package com.example.qr_go.Actor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_go.QR.QR;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Actor {
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
    public Player(String username, String deviceID) {
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
    public Player(String username, String deviceID, ArrayList<QR> qrList,
                  int rank, int highestScore, int lowestScore, int totalScore) {
        super(username, deviceID);
        this.qrList = qrList;
        this.rank = rank;
        this.highestScore = highestScore;
        this.lowestScore = lowestScore;
        this.totalScore = totalScore;
    }

    /**
     * Adds QR to the end of the list
     * @param qr
     * QR code to be added to player's list
     */
    public void addQR(QR qr) {
        // add QR to end of list
        getQRList().add(qr);

        // update highest and lowest

        // replace highest score if the current QR score is larger
        if(qr.getScore() > getHighestScore()) {
            setHighestScore(qr.getScore());
        }

        // replace lowest score if the current QR score is smaller or if it is the only QR in list
        if(qr.getScore() < getLowestScore() || getQRList().size() == 1) {
            setLowestScore(qr.getScore());
        }

        // add to total
        updateTotalScore(1, qr.getScore());
    }

    /**
     * Deletes QR code at i from list
     * @param i
     * Index of QR code to be deleted
     */
    public void deleteQR(int i) {

        // save copy of deleted QR code
        QR deletedQR = getQRList().get(i);

        // delete QR at index i
        getQRList().remove(i);

        // update highest and lowest
        if(deletedQR.getScore() == getHighestScore()) {
            updateHighestLowest();
        }

        if(deletedQR.getScore() == getLowestScore()) {
            updateHighestLowest();
        }

        // subtract from total
        updateTotalScore(-1, deletedQR.getScore());
    }


    /**
     * Updates the highest and lowest scores based on the current state of the qrList
     */
    private void updateHighestLowest() {

        // reset highest and lowest scores
        setHighestScore(getQRList().get(0).getScore());
        setLowestScore(getQRList().get(0).getScore());
        // iterate through qrList
        for(int i = 0; i < getQRList().size(); i++) {
            QR qr = getQRList().get(i);

            // replace highest score if the current QR score is larger
            if(qr.getScore() > getHighestScore()) {
                setHighestScore(qr.getScore());
            }

            // replace lowest score if the current QR score is smaller
            if(qr.getScore() < getLowestScore()) {
                setLowestScore(qr.getScore());
            }
        }
    }

    /**
     * Checks if data has changed
     * @param highestScore
     * Highest score to check against current instance
     * @param lowestScore
     * Lowest score to check against current instance
     * @param totalScore
     * Total score to check against current instance
     * @return
     * True if data has changed False otherwise
     */
    public boolean dataChanged(int rank, int highestScore, int lowestScore, int totalScore) {
        if(getRank() == rank || getHighestScore() == highestScore || getLowestScore() == lowestScore || getTotalScore() == totalScore) {
            return false;
        }

        return true;


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