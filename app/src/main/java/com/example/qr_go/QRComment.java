package com.example.qr_go;

import com.example.qr_go.Actor.Actor;

public class QRComment {
    private String comment;
    private final Actor commenter;

    public QRComment(String comment, Actor commenter) {
        this.comment = comment;
        this.commenter = commenter;
    }

    public String getComment() {
        return comment;
    }

    public void editComment(String comment) {
        this.comment = comment;
    }

    public Actor getCommenter() {
        return commenter;
    }
}
