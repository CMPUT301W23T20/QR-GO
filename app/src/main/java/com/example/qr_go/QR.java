package com.example.qr_go;

import java.util.ArrayList;

public class QR {
    private ArrayList<QRComment> commentsList;
    private final String qrHash;
    private final int score;
    private final String name;
    private final String avatar;

    private final Player discoverer;


    // private picture
    // private geolocation

    // will also need picture and geolocation as params
    public QR(String qrContents, Player discoverer) {
        // store only hash
        this.qrHash = hashQR(qrContents);
        this.score = generateScore(qrHash);
        this.name = generateName(qrHash);
        this.avatar = generateAvatar(qrHash);
        this.commentsList = new ArrayList<>();
        this.discoverer = discoverer;
    }

    private String hashQR(String qrContents) {
        // don't know if we should implement our own way hash but seems unnecessary
        return String.valueOf(qrContents.hashCode());// hashes a URL and converts the hash into a string for further use

    }

    private int generateScore(String qrHash) {
        // returning empty string to avoid error lines
        // actually implement this
        return 1;
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

    public void addComment(String comment, Actor commenter) {
        commentsList.add(new QRComment(comment, commenter));
    }

    public void deleteComment(int i) {
        commentsList.remove(i);
    }

    // getters and setters
    public int getScore() {
        return score;
    }
    public ArrayList<QRComment> getCommentsList() {
        return commentsList;
    }
}
