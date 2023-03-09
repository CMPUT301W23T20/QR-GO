package com.example.qr_go.QR;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_go.Actor.Actor;
import com.example.qr_go.Actor.PlayerModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class QRController {
    private QRModel model;

    public QRController(QRModel model) {this.model = model;}

    public void addComment(String comment, Actor commenter) {
        model.getCommentsList().add(new QRComment(comment, commenter));
    }
    public void deleteComment(int i) {
        model.getCommentsList().remove(i);
    }

    public void updateDB() {
        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection(PlayerModel.class.getSimpleName());

        // Create hashmap for data
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", model.getName());
        data.put("score", String.valueOf(model.getScore()));
        data.put("avatar", model.getAvatar());
        data.put("commentsList", model.getCommentsList());
        // add data to database
        // document named after user the hash
        collectionReference
                .document(model.getQrHash())
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
