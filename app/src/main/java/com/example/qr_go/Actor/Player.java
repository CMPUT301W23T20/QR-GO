package com.example.qr_go.Actor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_go.QR;
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
        qrList.add(qr);

        // update highest and lowest

        // replace highest score if the current QR score is larger
        if(qr.getScore() > highestScore) {
            highestScore = qr.getScore();
        }

        // replace lowest score if the current QR score is smaller or if it is the only QR in list
        if(qr.getScore() < lowestScore || qrList.size() == 1) {
            lowestScore = qr.getScore();
        }

        // update total
        totalScore += qr.getScore();
    }

    /**
     * Deletes QR code at i from list
     * @param i
     * Index of QR code to be deleted
     */
    public void deleteQR(int i) {

        // save copy of deleted QR code
        QR deletedQR = qrList.get(i);

        // delete QR at index i
        qrList.remove(i);

        // update highest and lowest
        if(deletedQR.getScore() == highestScore) {
            updateHighestLowest();
        }

        if(deletedQR.getScore() == lowestScore) {
            updateHighestLowest();
        }

        // update total
        totalScore -= deletedQR.getScore();
    }


    /**
     * Updates the highest and lowest scores based on the current state of the qrList
     */
    private void updateHighestLowest() {

        // reset highest and lowest scores
        highestScore = qrList.get(0).getScore();
        lowestScore = qrList.get(0).getScore();
        // iterate through qrList
        for(int i = 0; i < qrList.size(); i++) {
            QR qr = qrList.get(i);

            // replace highest score if the current QR score is larger
            if(qr.getScore() > highestScore) {
                highestScore = qr.getScore();
            }

            // replace lowest score if the current QR score is smaller
            if(qr.getScore() < lowestScore) {
                lowestScore = qr.getScore();
            }
        }
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
     * Gets player's lowest score
     * @return
     * Player's lowest score
     */
    public int getLowestScore() {
        return lowestScore;
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
     * Gets player's rank
     * @return
     * Player's rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Gets total number of QRs scanned
     * @return
     * Total number of QRs scanned
     */
    public int getTotalQR() {
        return qrList.size();
    }

    /**
     * Updates firestone database with player's information. Document named after user device ID.
     */
    public void updateDB() {
        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection(Player.class.getSimpleName());

        // Create hashmap for data
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", this.getUsername());
        data.put("qrList", this.getQRList());
        data.put("rank", this.getRank());
        data.put("highestScore", this.getHighestScore());
        data.put("lowestScore", this.getLowestScore());
        data.put("totalScore", this.getTotalScore());

        // add data to database
        // document named after user deviceID
        collectionReference
                .document(this.getDeviceID())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("updateDB()", "Data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("updateDB()", "Data not added: " + e);
                    }
                });
    }

    /**
     * Checks if data has changed
     * @param rank
     * Rank to check against current instance
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
        if(this.rank == rank || this.highestScore == highestScore || this.lowestScore == lowestScore || this.totalScore == totalScore) {
            return false;
        }

        return true;
    }
}