package com.example.qr_go;

import java.util.ArrayList;

public class Player extends Actor {
    private ArrayList<QR> qrList;
    private int rank;
    private int highestScore;
    private int lowestScore;

    private int totalScore;

    public Player(String username, String deviceID) {
        super(username, deviceID);
        qrList = new ArrayList<>();
    }

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
}