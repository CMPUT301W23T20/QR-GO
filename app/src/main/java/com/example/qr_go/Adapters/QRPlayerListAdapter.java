package com.example.qr_go.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.Interfaces.RecyclerViewInterface;
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.R;

import java.util.ArrayList;

/**
 * Adapter for list of players that have scanned a QR
 */
public class QRPlayerListAdapter extends RecyclerView.Adapter<QRPlayerListAdapter.PlayerViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<String> playerArrayList;

    public QRPlayerListAdapter(Context context, ArrayList<String> playerArrayList,
                            RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.playerArrayList = playerArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public QRPlayerListAdapter.PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_qr_player_list, parent, false);

        return new QRPlayerListAdapter.PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        String player = playerArrayList.get(position);
        holder.SetDetails(player);
    }

    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView playerText;
        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerText = itemView.findViewById(R.id.player_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null) {
                        int i = getAdapterPosition();

                        if(i != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(i);
                        }
                    }
                }
            });
        }

        /**
         * Sets details in view
         * @param player
         * Comment needed to be represented
         */
        void SetDetails(String player) {
            playerText.setText(player);
        }
    }
}
