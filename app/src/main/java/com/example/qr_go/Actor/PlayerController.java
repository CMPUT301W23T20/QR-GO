package com.example.qr_go.Actor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_go.QR.QR;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class PlayerController {
    private PlayerModel model;

    /**
     * PlayerController constructor
     * @param model
     * PlayerModel to be controlled by PlayerController
     */
    public PlayerController(PlayerModel model) {
        this.model = model;
    }

    /**
     * Adds QR to the end of the list
     * @param qr
     * QR code to be added to player's list
     */
    public void addQR(QR qr) {
        // add QR to end of list
        model.getQRList().add(qr);

        // update highest and lowest

        // replace highest score if the current QR score is larger
        if(qr.getScore() > model.getHighestScore()) {
            model.setHighestScore(qr.getScore());
        }

        // replace lowest score if the current QR score is smaller or if it is the only QR in list
        if(qr.getScore() < model.getLowestScore() || model.getQRList().size() == 1) {
            model.setLowestScore(qr.getScore());
        }

        // add to total
        model.updateTotalScore(1, qr.getScore());
    }

    /**
     * Deletes QR code at i from list
     * @param i
     * Index of QR code to be deleted
     */
    public void deleteQR(int i) {

        // save copy of deleted QR code
        QR deletedQR = model.getQRList().get(i);

        // delete QR at index i
        model.getQRList().remove(i);

        // update highest and lowest
        if(deletedQR.getScore() == model.getHighestScore()) {
            updateHighestLowest();
        }

        if(deletedQR.getScore() == model.getLowestScore()) {
            updateHighestLowest();
        }

        // subtract from total
        model.updateTotalScore(-1, deletedQR.getScore());
    }


    /**
     * Updates the highest and lowest scores based on the current state of the qrList
     */
    private void updateHighestLowest() {

        // reset highest and lowest scores
        model.setHighestScore(model.getQRList().get(0).getScore());
        model.setLowestScore(model.getQRList().get(0).getScore());
        // iterate through qrList
        for(int i = 0; i < model.getQRList().size(); i++) {
            QR qr = model.getQRList().get(i);

            // replace highest score if the current QR score is larger
            if(qr.getScore() > model.getHighestScore()) {
                model.setHighestScore(qr.getScore());
            }

            // replace lowest score if the current QR score is smaller
            if(qr.getScore() < model.getLowestScore()) {
                model.setLowestScore(qr.getScore());
            }
        }
    }

    /**
     * Updates firestone database with player's information. Document named after user device ID.
     */
    public void updateDB() {
        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection(PlayerModel.class.getSimpleName());

        // Create hashmap for data
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", model.getUsername());
        data.put("qrList", model.getQRList());
        data.put("rank", String.valueOf(model.getRank()));
        data.put("highestScore", String.valueOf(model.getHighestScore()));
        data.put("lowestScore", String.valueOf(model.getLowestScore()));
        data.put("totalScore", String.valueOf(model.getTotalScore()));

        // add data to database
        // document named after user deviceID
        collectionReference
                .document(model.getDeviceID())
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
        if(model.getRank() == rank || model.getHighestScore() == highestScore || model.getLowestScore() == lowestScore || model.getTotalScore() == totalScore) {
            return false;
        }

        return true;


    }
}
