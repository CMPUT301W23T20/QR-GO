package com.example.qr_go.QR;

import com.example.qr_go.Actor.Actor;

/**
 * Comment on QR codes
 */
public class QRComment {
    private String comment;
    private String commenter;

    public QRComment(String comment, String commenter) {
        this.comment = comment;
        this.commenter = commenter;
    }

    public String getComment() {
        return comment;
    }

    public void editComment(String comment) {
        this.comment = comment;
    }

    public String getCommenter() {
        return commenter;
    }
}
