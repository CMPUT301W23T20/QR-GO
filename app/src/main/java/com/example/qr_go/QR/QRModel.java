package com.example.qr_go.QR;

import android.graphics.Bitmap;

import com.example.qr_go.Actor.Actor;
import com.example.qr_go.Actor.PlayerModel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class QRModel {
    private ArrayList<QRComment> commentsList;
    private String qrHash;
    private final int score;
    private final String name;
    private final String avatar;
    private ArrayList<PlayerModel> qrUsers; // array of people who have discovered this QR



    // private picture
    private Bitmap photo;
    // private geolocation

    /**
     * Constructor for creating new QR
     * @param qrContents
     * @param discoverer
     */
    public QRModel(String qrContents, PlayerModel discoverer) throws Exception {
        // use hash to create contents
        this.qrHash = hashQR(qrContents);
        this.score = generateScore(qrHash);
        this.name = generateName(qrHash);
        this.avatar = generateAvatar(qrHash);
        this.commentsList = new ArrayList<>();
        if (qrUsers.contains(discoverer)) {
            throw new Exception("Discoverer already exists in qrUsers list.");
        }
        qrUsers.add(discoverer);
    }

    /**
     * Constructor for creating QR from database
     * @param name
     * @param avatar
     * @param score
     * @param commentsList
     */
    public QRModel(String name, String avatar, int score, ArrayList<QRComment> commentsList) {
        this.name = name;
        this.avatar = avatar;
        this.score = score;
        this.commentsList = commentsList;
    }

    /**
     * hashes the URL using sha256
     * then combines the bits into one string
     * @param qrContents
     * @return hashed string
     */
    public String hashQR(String qrContents) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] result = md.digest(qrContents.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // handle the exception
            return null;
        }
    }

    /**
     * takes a hashed string and generates a score based on individual zeros which make up 1 point
     * and consecutive zeroes which make up 20^(number of zeros minus the first one)
     * @param qrHash
     * @return score
     */
    private int generateScore(String qrHash) {
        int score = 0;    // total score
        int zeroCount = 0;// counting consecutive zeros
        double points = 0;// variable that tracks the points that consecutive zeros create

        String hash = this.qrHash;
        for (int i = 0; i < hash.length(); i++) {

            if (hash.charAt(i) == '0') {

                zeroCount++;

                if (zeroCount >= 2) { // checks for consecutive zeros
                    points = Math.pow(20, zeroCount - 1);
                    if (i == hash.length() - 1) {
                        score += points;
                    }
                } else if (i + 1 < hash.length() && (hash.charAt(i + 1) != '0' || i == hash.length() - 1)) {
                    // the case where there is one zero
                    score++;

                }
            } else if (i != 0 && hash.charAt(i - 1) == '0') {
                zeroCount = 0;
                score += points;
            }

        }

        return score;
    }

    private String generateName(String qrHash) {
        // returning empty string to avoid error lines
        // actually implement this
        return "";
    }

    private String generateAvatar(String qrHash) {
        // returning empty string to avoid error lines
        // actually implement this
        return "";
    }

    public int getScore() {
        return score;
    }
    public String getAvatar(){ return avatar;}

    public String getName(){ return name;}
    public ArrayList<QRComment> getCommentsList() {
        return commentsList;
    }
    public String getQrHash() {
        return qrHash;
    }

    // getter and setter for photo
    // getter and setter for geolocation
}