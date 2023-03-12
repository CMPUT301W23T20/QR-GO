package com.example.qr_go;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.QR.QR;

import org.junit.Test;

/**
 * Testing Player class
 */
public class PlayerTest {
    private Player player;

    public Player MockPlayer() {
        player = new Player("test user", "test device ID");
        return player;
    }

    public QR MockQR() {
        QR qr = new QR("test content");
        return qr;
    }

    /**
     * Test if adding QR to player works
     */
    @Test
    public void testAddQR() {
        player = MockPlayer();
        QR qr = MockQR();

        player.addQR(qr);

        assert(player.getQRList().size() == 1);
    }

    /**
     * Test if deleting QR to player works
     */
    @Test
    public void testDeleteQR() {
        player = MockPlayer();
        QR qr = MockQR();

        player.addQR(qr);
        player.deleteQR(0);

        assert(player.getQRList().size() == 0);
    }
}
