package com.example.qr_go.QR;

import android.graphics.Bitmap;
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
public class QR implements Comparable<QR> {
    private ArrayList<QRComment> commentsList;
    private String qrHash;
    private final int score;
    private final String name;
    private final String avatar;
    private ArrayList<Player> qrUsers; // array of people who have discovered this QR





    // private picture
    private String photo;
    // private geolocation

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
    }

    /**
     * Constructor for creating QR from database
     * @param name
     * @param avatar
     * @param score
     * @param commentsList
     * @param qrUsers
     */
    public QR(String qr_hash, String name, String avatar, int score, ArrayList<QRComment> commentsList, ArrayList<Player> qrUsers) {
        this.qrHash = qr_hash;
        this.name = name;
        this.avatar = avatar;
        this.score = score;
        this.commentsList = commentsList;
        this.qrUsers = qrUsers;
    }

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
    private String generateName(String qrHash) {
        StringBuilder nameBuilder = new StringBuilder();

        // Define the naming dictionary
        HashMap<Character, String[]> dictionary = new HashMap<>();
        dictionary.put('0', new String[] { "Squiggly", "Wiggly" });
        dictionary.put('1', new String[] { "Inky", "Binky" });
        dictionary.put('2', new String[] { "Woogly", "Boogly" });
        dictionary.put('3', new String[] { "Booty", "Fruity" });
        dictionary.put('4', new String[] { "Scooby", "Dooby" });
        dictionary.put('5', new String[] { "Hard", "Soft" });
        dictionary.put('6', new String[] { "Brown", "Blue" });
        dictionary.put('7', new String[] { "Quirky", "Sassy" });
        dictionary.put('8', new String[] { "Mean", "Bean" });
        dictionary.put('9', new String[] { "Hairy", "Chubby" });
        dictionary.put('A', new String[] { "Smashing", "Bonkers" });
        dictionary.put('B', new String[] { "Nutty", "Funky" });
        dictionary.put('C', new String[] { "Boujee", "Gucci" });
        dictionary.put('D', new String[] { "Powerful", "Weak" });
        dictionary.put('E', new String[] { "Stupid", "Smart" });
        dictionary.put('F', new String[] { "Moody", "Greasy" });

        // Extract the first 6 hexadecimal characters from the hash
        String hexPrefix = qrHash.substring(0, 6);

        // Convert each hexadecimal digit to its corresponding word from the naming dictionary
        for (char c : hexPrefix.toCharArray()) {
            String[] words = dictionary.get(Character.toUpperCase(c));
            if (words != null) {
                nameBuilder.append(words[0]).append(" ");
            }
        }

        // Trim the name to 5 words if it is longer
        String name = nameBuilder.toString().trim();
        String[] words = name.split(" ");
        if (words.length > 5) {
            name = String.join(" ", Arrays.copyOfRange(words, 0, 5));
        }

        return name;
    }



    private String generateAvatar(String qrHash) {
        // returning empty string to avoid error lines
        // actually implement this
        Avatar qrAvatar = new Avatar();
        return qrAvatar.generateAvatar(qrHash);

    }

    public void addComment(String comment, Actor commenter) {
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
    public ArrayList<Player> getPlayerList() {
        return this.qrUsers;
    }
    public void addToPlayerList(Player player) throws Exception {
        if (qrUsers.contains(player)) {
            throw new Exception("Discoverer already exists in qrUsers list.");
        }

        this.qrUsers.add(player);
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

    // getter and setter for photo
    // getter and setter for geolocation
}
