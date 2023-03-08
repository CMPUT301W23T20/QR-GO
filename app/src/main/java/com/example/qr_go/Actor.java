// unique android device:
// https://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id

package com.example.qr_go;


import android.provider.Settings;

public class Actor {
    private String username;
    private String deviceID;

    public Actor(String username, String deviceID) {
        this.username = username;
        this.deviceID = deviceID;
    }

    public String getUsername() {
        return username;
    }

    public String getDeviceID() {
        return deviceID;
    }
}
