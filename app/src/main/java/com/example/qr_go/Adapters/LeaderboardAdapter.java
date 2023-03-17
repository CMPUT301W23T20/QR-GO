package com.example.qr_go.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.Content.LeaderboardContent;
import com.example.qr_go.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Represents an array adapter for the leaderboard
 */
public class LeaderboardAdapter extends ArrayAdapter<Player>{

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<Player> playerList = null;
    private ArrayList<Player> searchList;
//    public LeaderboardAdapter(Context context, ArrayList<LeaderboardContent>rows){
//        super(context, 0, rows);
//    }

    public LeaderboardAdapter(Context context, int simple_list_item_1, ArrayList<Player> leaderboardContentList) {
        super(context, 0, leaderboardContentList);
        mContext = context;
        this.playerList = leaderboardContentList;
        inflater = LayoutInflater.from(mContext);
        // added this
        this.searchList = new ArrayList<Player>();
        this.searchList.addAll(playerList);
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

        TextView playerName = view.findViewById(R.id.player_text);
        TextView score = view.findViewById(R.id.score_text);

        playerName.setText(playerList.get(position).getUsername());
        score.setText("Total Score: " + playerList.get(position).getTotalScore());


        return view;
    }

    /**
     *
     * @param charText: user's input into search bar
     * Filters out content on the leaderboard based on the user's input
     */
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        playerList.clear();
        if (charText.length() == 0) {
            playerList.addAll(searchList);
        } else {
            for (Player wp : searchList) {
                if (wp.getUsername().toLowerCase(Locale.getDefault()).contains(charText)) {
                    playerList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
