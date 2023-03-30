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
import com.example.qr_go.QR.QRComment;
import com.example.qr_go.R;

import java.util.ArrayList;


/**
 * Adapter for comments on QR code
 */
public class QRCommentAdapter extends RecyclerView.Adapter<QRCommentAdapter.CommentViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<QRComment> commentArrayList;

    public QRCommentAdapter(Context context, ArrayList<QRComment> commentArrayList,
                                RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.commentArrayList = commentArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_profile_qr_list, parent, false);

        return new QRCommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRCommentAdapter.CommentViewHolder holder, int position) {
        QRComment comment = commentArrayList.get(position);
        holder.SetDetails(comment);
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView commenterText, commentText;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commenterText = itemView.findViewById(R.id.commenter_text);
            commentText = itemView.findViewById(R.id.comment_text);

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
         * @param comment
         * Comment needed to be represented
         */
        void SetDetails(QRComment comment) {
            commenterText.setText("By: " + comment.getCommenter());
            commentText.setText(comment.getComment());
        }
    }
}
