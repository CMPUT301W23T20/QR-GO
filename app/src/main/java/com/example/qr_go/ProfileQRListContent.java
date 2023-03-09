package com.example.qr_go;

import com.example.qr_go.Fragments.ProfileQRListFragment;
import com.example.qr_go.QR.QR;

import java.io.Serializable;

public class ProfileQRListContent implements Serializable {
    private QR qr;

    public ProfileQRListContent(QR qr) {
        this.qr = qr;
    }


}
