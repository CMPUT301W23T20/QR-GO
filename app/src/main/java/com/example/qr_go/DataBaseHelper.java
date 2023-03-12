package com.example.qr_go;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.QR.QR;
import com.example.qr_go.QR.QRComment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataBaseHelper {

    public ArrayList<QR> convertQRListFromDB(List<Map<String, Object>> qrList) {
        ArrayList<QR> result = new ArrayList<>();

        for(int i = 0; i < qrList.size(); i++) {
            Map<String, Object> currentInQRList = qrList.get(i);
            String name = (String)currentInQRList.get("name");
            String avatar = (String)currentInQRList.get("avatar");
            Long scoreLong = (Long)currentInQRList.get("score");
            int score = scoreLong.intValue();

            QR currentQR = new QR(name , avatar, score,
                    (ArrayList<QRComment>)currentInQRList.get("commentsList"),
                    (ArrayList<Player>)currentInQRList.get("playerList"));
            result.add(currentQR);
        }

        return result;
    }
}
