package com.example.qr_go.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_go.Interfaces.RecyclerViewInterface;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;

import java.util.ArrayList;

/**
 * Adapter for list of QRs player has scanned
 */
public class ProfileQRListAdapter extends RecyclerView.Adapter<ProfileQRListAdapter.QRViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<QR> qrArrayList;

    public ProfileQRListAdapter(Context context, ArrayList<QR> qrArrayList,
                                RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.qrArrayList = qrArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public QRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_profile_qr_list, parent, false);

        return new QRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRViewHolder holder, int position) {
        QR qr = qrArrayList.get(position);
        holder.SetDetails(qr);
    }

    @Override
    public int getItemCount() {
        return qrArrayList.size();
    }

    class QRViewHolder extends RecyclerView.ViewHolder {
        private TextView qrName, qrScore;
        public QRViewHolder(@NonNull View itemView) {
            super(itemView);
            qrName = itemView.findViewById(R.id.name_text);
            qrScore = itemView.findViewById(R.id.score_text);

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
         * @param qr
         * QR needed to be represented
         */
        void SetDetails(QR qr) {
            qrName.setText(qr.getName());
            qrScore.setText(String.valueOf(qr.getScore()));
        }
    }
}
