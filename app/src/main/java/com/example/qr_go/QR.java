package com.example.qr_go;

import androidx.annotation.NonNull;

public class QR {
    private final String qrHash;
    private final int score;
    private final String name;
    private final String avatar;

    // private picture
    // private geolocation

    // will also need picture and geolocation as params
    public QR(String qrContents) {
        // store only hash
        this.qrHash = hashQR(qrContents);
        this.score = generateScore(qrHash);
        this.name = generateName(qrHash);
        this.avatar = generateAvatar(qrHash);
    }

    private String hashQR(String qrContents) {
        // returning empty string to avoid error lines
        // actually implement this
        return "";
    }

    private int generateScore(String qrHash) {
        // returning empty string to avoid error lines
        // actually implement this
        return 1;
    }

    private String generateName(String qrContents) {
        // returning empty string to avoid error lines
        // actually implement this
        return "";
    }

    private String generateAvatar(String qrContents) {
        // returning empty string to avoid error lines
        // actually implement this
        return "";
    }



    // getters and setters
    public int getScore() {
        return score;
    }


}
