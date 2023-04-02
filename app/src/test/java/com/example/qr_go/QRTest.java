package com.example.qr_go;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.qr_go.QR.Avatar;
import com.example.qr_go.QR.QR;

import org.junit.Test;

public class QRTest {
    String qrContents = "mohamed";
    QR qr = new QR(qrContents);

    @Test
    public void testGetScore(){
        int score = qr.getScore();
        assertTrue(score >= 0);  // score should be non-negative
        // print the score for debugging purposes
        System.err.println("Score: " + score);

    }
    @Test
    public void testGetQRHash(){
        String hash = qr.getQrHash();
        System.err.println("hash: " + hash);

    }

    @Test
    public void testGetName() {
        // Create a new QR object with a sample URL


        // Call getName() method and check if it returns a valid string
        System.err.println("Name: " + qr.getName());

    }
    @Test
    public void testGetAvatar() {
        Avatar avatarGenerator = new Avatar();
        String avatar = avatarGenerator.generateAvatar(qr.getQrHash());

        // Print the generated avatar to the console
        System.out.println("Generated avatar:\n" + avatar);
    }

}