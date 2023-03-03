package com.example.qr_go;

import java.util.ArrayList;

public class QR {
    private ArrayList<QRComment> commentsList;
    private ArrayList<Player> players;
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
        this.commentsList = new ArrayList<>();
        this.players = new ArrayList<>();
    }


    private String hashQR(String qrContents) {
        // returning empty string to avoid error lines
        // actually implement this
        return "";
    }

    /**
     * Generates score for QR
     * @param qrHash
     * Hash from QR contents
     * @return
     * QR score
     */
    private int generateScore(String qrHash) {
        // loop through hash
        int finalScore = 0;
        int streak;
        for(int i = 0; i < qrHash.length(); i++) {

        }
        return finalScore;
    }

    /**
     * Generates name for QR
     * @param qrHash
     * Hash from QR contents
     * @return
     * QR name
     */
    private String generateName(String qrHash) {
        // returning empty string to avoid error lines
        // actually implement this
        return "";
    }

    /**
     * Generates avatar for QR
     * @param qrHash
     * Hash from QR contents
     * @return
     * QR avatar represented by String
     */
    private String generateAvatar(String qrHash) {
        // returning empty string to avoid error lines
        // actually implement this
        return "";
    }

    /**
     * Adds comment to comment list
     * @param comment
     * Text of comment
     * @param commenter
     * Actor that commented
     */
    public void addComment(String comment, Actor commenter) {
        commentsList.add(new QRComment(comment, commenter));
    }

    /**
     * Deletes comment from comment list at index i
     * @param i
     * Index of comment
     */
    public void deleteComment(int i) {
        commentsList.remove(i);
    }

    /**
     * Adds player to player list if player is not already in list
     * @param player
     * Player to be added to list
     * @return
     * Return 1 if player addition is successful and -1 otherwise
     */
    public int addPlayer(Player player) {
        // don't add player to list and return -1
        if(players.contains(player)) {
            return -1;
        }

        // add player to list and return 1
        players.add(player);
        return 1;

    }

    // getters and setters
    public int getScore() {
        return score;
    }
    public ArrayList<QRComment> getCommentsList() {
        return commentsList;
    }
}
