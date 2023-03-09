package com.example.qr_go.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_go.QR.QRModel;
import com.example.qr_go.R;

import java.util.ArrayList;

public class ProfileQRListAdapter extends ArrayAdapter<QRModel> {
    public ProfileQRListAdapter(Context context, ArrayList<QRModel> qrList) {
        super(context, 0, qrList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.content_profile_qr_list,
                    parent, false);
        } else {
            view = convertView;
        }

        QRModel qr = getItem(position);

        TextView qrName = view.findViewById(R.id.name_text);
        TextView qrScore = view.findViewById(R.id.score_text);

        qrName.setText(qr.getName());
        qrScore.setText(String.valueOf(qr.getScore()));

        return view;
    }
}
