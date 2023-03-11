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
import com.example.qr_go.R;

import java.util.ArrayList;

public class QRPlayerListAdapter extends ArrayAdapter<Player> {

    public QRPlayerListAdapter(Context context, ArrayList<Player> qrPlayerList) {
        super(context, 0, qrPlayerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.content_qr_player_list,
                    parent, false);
        } else {
            view = convertView;
        }
        Player player = getItem(position);

        TextView playerName = view.findViewById(R.id.player_text);
        TextView playerRank = view.findViewById(R.id.rank_text);

        playerName.setText(player.getUsername());
        playerRank.setText("Rank: " + player.getRank());

        return view;
    }
}
