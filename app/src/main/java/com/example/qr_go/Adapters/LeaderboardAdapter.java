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

        LeaderboardContent row= getItem(position);
        TextView playerName = view.findViewById(R.id.player_text);
        TextView score = view.findViewById(R.id.score_text);

        playerName.setText(row.getUserName());
        score.setText(""+ row.getScore());


        return view;



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
