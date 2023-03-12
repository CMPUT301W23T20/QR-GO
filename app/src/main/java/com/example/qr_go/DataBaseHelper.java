package com.example.qr_go;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.QR.QR;
import com.example.qr_go.QR.QRComment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseHelper {

    public ArrayList<QR> convertQRListFromDB(List<Map<String, Object>> qrList) {
        ArrayList<QR> result = new ArrayList<>();

        for(int i = 0; i < qrList.size(); i++) {
            Map<String, Object> currentInQRList = qrList.get(i);
            String name = (String)currentInQRList.get("name");
            String avatar = (String)currentInQRList.get("avatar");
            Long scoreLong = (Long)currentInQRList.get("score");
            int score = scoreLong.intValue();

            QR currentQR = new QR(name , avatar, score,
                    (ArrayList<QRComment>)currentInQRList.get("commentsList"),
                    (ArrayList<Player>)currentInQRList.get("playerList"));
            result.add(currentQR);
        }

        return result;
    }

    public void updateDB(QR qr) {
        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection(QR.class.getSimpleName());

        // Create hashmap for data
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", qr.getName());
        data.put("score", String.valueOf(qr.getScore()));
        data.put("avatar", qr.getAvatar());
        data.put("commentsList", qr.getCommentsList());
        // add data to database
        // document named after user the hash
        collectionReference
                .document(qr.getQrHash())
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
     * Updates firestone database with player's information. Document named after user device ID.
     */
    public void updateDB(Player player) {
        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection(Player.class.getSimpleName());


        // Create hashmap for data
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", player.getUsername());
        data.put("qrList", player.getQRList());
        data.put("rank", String.valueOf(player.getRank()));
        data.put("highestScore", String.valueOf(player.getHighestScore()));
        data.put("lowestScore", String.valueOf(player.getLowestScore()));
        data.put("totalScore", String.valueOf(player.getTotalScore()));

        // add data to database
        // document named after user deviceID
        collectionReference
                .document(player.getDeviceID())
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
