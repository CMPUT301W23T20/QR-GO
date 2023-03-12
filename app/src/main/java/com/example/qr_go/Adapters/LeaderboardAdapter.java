package com.example.qr_go.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_go.Activities.Profile.PlayerProfileViewActivity;
import com.example.qr_go.Activities.Profile.ProfileActivity;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.Content.LeaderboardContent;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LeaderboardAdapter extends ArrayAdapter<LeaderboardContent>{

    Context mContext;
    LayoutInflater inflater;
    private List<LeaderboardContent> leaderboardContentList = null;
    private ArrayList<LeaderboardContent> arraylist;
//    public LeaderboardAdapter(Context context, ArrayList<LeaderboardContent>rows){
//        super(context, 0, rows);
//    }

    public LeaderboardAdapter(Context context, int simple_list_item_1, List<LeaderboardContent> leaderboardContentList) {
        super(context, 0, leaderboardContentList);
        mContext = context;
        this.leaderboardContentList = leaderboardContentList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<LeaderboardContent>();
        this.arraylist.addAll(leaderboardContentList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.content_leaderboard, parent, false);
        } else {
            view = convertView;
        }

        // get database information
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference collectionReference = db.collection(Player.class.getSimpleName());
//        Intent myIntent = new Intent(PlayerProfileViewActivity.this, ProfileQRListViewActivity.class);
//        myIntent.putExtra("android_id", android_id);
//        // put data into class
//        db.collection(Player.class.getSimpleName()).document(android_id).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        player = new Player((String)documentSnapshot.get("username"), (String)documentSnapshot.get("deviceID"), (ArrayList<QR>) documentSnapshot.get("qrList"),
//                                (int) Integer.parseInt((String)documentSnapshot.get("rank")), (int) Integer.parseInt((String)documentSnapshot.get("highestScore")),
//                                (int)Integer.parseInt((String)documentSnapshot.get("lowestScore")), (int)Integer.parseInt((String)documentSnapshot.get("totalScore")));
//
//                        // update UI
//                        usernameTextView.setText(player.getUsername());
//                        totalScoreTextView.setText("Total Score: " + player.getTotalScore());
//                        totalScannedTextView.setText("Total Scanned: " + player.getTotalQR());
//                    }
//
//
        return view;
//    }
    }

    /**
     *
     * @param charText: user's input into search bar
     * Filters out content on the leaderboard based on the user's input
     */
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        leaderboardContentList.clear();
        if (charText.length() == 0) {
            leaderboardContentList.addAll(arraylist);
        } else {
            for (LeaderboardContent wp : arraylist) {
                if (wp.getUserName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    leaderboardContentList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
