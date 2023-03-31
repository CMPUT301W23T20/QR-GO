package com.example.qr_go.QR;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_go.Actor.Actor;
import com.example.qr_go.Actor.Player;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * QR code that player scans
 */
public class QR implements Comparable<QR>, Parcelable {
    private ArrayList<QRComment> commentsList;
    private String qrHash;
    private final int score;
    private final String name;
    private final String avatar;
    private ArrayList<String> qrUsers; // array of people who have discovered this QR





    // private picture
    private String photo;
    // private geolocation
    private float latitude;
    private float longitude;

    /**
     * Constructor for creating new QR
     * @param qrContents
     */
    public QR(String qrContents) {
        // use hash to create contents
        this.qrHash = hashQR(qrContents);
        this.score = generateScore(qrHash);
        this.name = generateName(qrHash);
        this.avatar = generateAvatar(qrHash);
        this.commentsList = new ArrayList<>();
        this.qrUsers = new ArrayList<>();
    }

    /**
     * Constructor for creating QR from database
     * @param name
     * @param avatar
     * @param score
     * @param commentsList
     * @param qrUsers
     */
    public QR(String qr_hash, String name, String avatar, int score, ArrayList<QRComment> commentsList, ArrayList<String> qrUsers) {
        this.qrHash = qr_hash;
        this.name = name;
        this.avatar = avatar;
        this.score = score;
        this.commentsList = commentsList;
        this.qrUsers = qrUsers;
    }

    protected QR(Parcel in) {
        qrHash = in.readString();
        score = in.readInt();
        name = in.readString();
        avatar = in.readString();
        photo = in.readString();
    }

    public static final Creator<QR> CREATOR = new Creator<QR>() {
        @Override
        public QR createFromParcel(Parcel in) {
            return new QR(in);
        }

        @Override
        public QR[] newArray(int size) {
            return new QR[size];
        }
    };

    /**
     * hashes the URL using sha256
     * then combines the bits into one string
     * @param qrContents
     * @return hashed string
     */
    private String hashQR(String qrContents) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] result = md.digest(qrContents.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // handle the exception
            return null;
        }
    }

    /**
     * takes a hashed string and generates a score based on individual zeros which make up 1 point
     * and consecutive zeroes which make up 20^(number of zeros minus the first one)
     * @param qrHash
     * @return score
     */
    private int generateScore(String qrHash) {
        int score = 0;    // total score
        int zeroCount = 0;// counting consecutive zeros
        double points = 0;// variable that tracks the points that consecutive zeros create

        String hash = this.qrHash;
        for (int i = 0; i < hash.length(); i++) {

            if (hash.charAt(i) == '0') {

                zeroCount++;

                if (zeroCount >= 2) { // checks for consecutive zeros
                    points = Math.pow(20, zeroCount - 1);
                    if (i == hash.length() - 1) {
                        score += points;
                    }
                } else if (i + 1 < hash.length() && (hash.charAt(i + 1) != '0' || i == hash.length() - 1)) {
                    // the case where there is one zero
                    score++;

                }
            } else if (i != 0 && hash.charAt(i - 1) == '0') {
                zeroCount = 0;
                score += points;
            }

        }

        return score;
    }

    /**
     * generates a name from a dictionary
     * @param qrHash
     * @return
     */

    public static String generateName(String qrHash) {
        // Define a mapping from each hexadecimal character to a unique adjective
        Map<Character, String> adjectives = new HashMap<>();
        adjectives.put('0', "chubby");
        adjectives.put('1', "fast");
        adjectives.put('2', "stinky");
        adjectives.put('3', "furry");
        adjectives.put('4', "spiky");
        adjectives.put('5', "shiny");
        adjectives.put('6', "slimy");
        adjectives.put('7', "grumpy");
        adjectives.put('8', "greasy");
        adjectives.put('9', "silly");
        adjectives.put('a', "fancy");
        adjectives.put('b', "lumpy");
        adjectives.put('c', "stupid");
        adjectives.put('d', "tiny");
        adjectives.put('e', "sassy");
        adjectives.put('f', "dramatic");

        // Extract the first 6 characters of the input hexadecimal string
        String hexPrefix = qrHash.substring(0, 6);

        // Generate 5 adjectives based on the first 5 characters
        String[] adjectiveArray = new String[5];
        for (int i = 0; i < 5; i++) {
            char hexChar = hexPrefix.charAt(i);
            String adjective = adjectives.get(hexChar);
            adjectiveArray[i] = adjective;
        }

        // Generate a noun based on the last character
        char lastHexChar = hexPrefix.charAt(5);
        String noun;
        switch (lastHexChar) {
            case '0':
                noun = "dog";
                break;
            case '1':
                noun = "cat";
                break;
            case '2':
                noun = "tortoise";
                break;
            case '3':
                noun = "hamster";
                break;
            case '4':
                noun = "rabbit";
                break;
            case '5':
                noun = "parrot";
                break;
            case '6':
                noun = "goldfish";
                break;
            case '7':
                noun = "pegasus";
                break;
            case '8':
                noun = "snake";
                break;
            case '9':
                noun = "rat";
                break;
            case 'a':
                noun = "spider";
                break;
            case 'b':
                noun = "goose";
                break;
            case 'c':
                noun = "octopus";
                break;
            case 'd':
                noun = "lizard";
                break;
            case 'e':
                noun = "bee";
                break;
            case 'f':
                noun = "monkey";
                break;
            default:
                // This should never happen, but just in case...
                noun = "unknown";
        }

        // Concatenate the adjectives and noun into a single string
        String name = String.join("-", adjectiveArray) + "-" + noun;

        // Return the resulting name
        return name;
    }

    private String generateAvatar(String qrHash) {
        // returning empty string to avoid error lines
        // actually implement this
        Avatar qrAvatar = new Avatar();
        return qrAvatar.generateAvatar(qrHash);

    }

    public void addComment(String comment, String commenter) {
        getCommentsList().add(new QRComment(comment, commenter));
    }
    public void deleteComment(int i) { getCommentsList().remove(i);
    }

    public int getScore() {
        return score;
    }
    public String getAvatar(){ return avatar;}
    public String getName(){ return name;}
    public ArrayList<QRComment> getCommentsList() {
        return commentsList;
    }
    public String getQrHash() {
        return qrHash;
    }
    public ArrayList<String> getPlayerList() {
        return this.qrUsers;
    }
    public void addToPlayerList(String player) {
        if (!qrUsers.contains(player)) {
            this.qrUsers.add(player);
        }
    }

    @Override
    public int compareTo(QR qr) {
        if(this.score > qr.getScore()) {
            return 1;
        }

        else if(this.score == qr.getScore()) {
            return 0;
        }
        return -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(qrHash);
        dest.writeInt(score);
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(photo);
    }

    // getter and setter for photo
    // getter and setter for geolocation
    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
