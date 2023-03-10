package com.example.qr_go;

import com.example.qr_go.QR.QRModel;

import java.io.Serializable;

public class ProfileQRListContent implements Serializable {
    private QRModel qr;

    public ProfileQRListContent(QRModel qr) {
        this.qr = qr;
    }


}
