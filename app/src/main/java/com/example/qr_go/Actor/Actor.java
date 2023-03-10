// unique android device:
// https://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id

package com.example.qr_go.Actor;


import android.provider.Settings;

/**
 * Entity that uses the application
 */
public class Actor {
    private String username;
    private String deviceID;

    private String contact;

    public Actor(String username, String deviceID) {
        this.username = username;
        this.deviceID = deviceID;
        this.contact = "";
    }

    /**
     * Gets actor's username
     * @return
     * Actor's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets actor's device ID
     * @return
     * Actor's device ID
     */
    public String getDeviceID() {
        return deviceID;
    }

    public String getContact() { return contact;}
    public void setContact(String contact) {this.contact = contact;}
}
