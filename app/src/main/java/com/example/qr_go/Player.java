package com.example.qr_go;

import android.util.Log;

import androidx.annotation.NonNull;

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
    private FirebaseFirestore db;

    /**
     * Constructor for creating brand new player
     * @param username
     * @param deviceID
     */
    public Player(String username, String deviceID) {
        super(username, deviceID);
        qrList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
    }

    /**
     * Constructor for creating existing player from database
     * @param username
     * @param deviceID
     * @param qrList
     * @param rank
     * @param highestScore
     * @param lowestScore
     * @param totalScore
     */
    public Player(String username, String deviceID, ArrayList<QR> qrList,
                  int rank, int highestScore, int lowestScore, int totalScore) {
        super(username, deviceID);
        this.qrList = qrList;
        this.rank = rank;
        this.highestScore = highestScore;
        this.lowestScore = lowestScore;
        this.totalScore = totalScore;

        db = FirebaseFirestore.getInstance();
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

    // updates the highest and lowest scores based on the current state of the qrList
    // runtime: O(n)
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
    public int getHighestScore() {
        return highestScore;
    }

    public int getLowestScore() {
        return lowestScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public ArrayList<QR> getQRList() {
        return qrList;
    }
    public int getRank() {
        return rank;
    }

    /**
     * Updates firestone database with player's information. Document named after user device ID.
     */
    public void updateDB() {
        // get collection reference to player class
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
}