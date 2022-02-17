package com.espressif.esptouch.android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.pojo.Board;

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Board item, int position);
    }

    private String type = "";

    private List<Board> items;
    private OnItemClickListener listener;

    public BoardAdapter(List<Board> items, String type, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), position, type, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private String board = "Board";
        private TextView name;
        private ImageView courseIV;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.idTVCourse);
            courseIV = (ImageView) itemView.findViewById(R.id.idIVcourse);
        }

        public void bind(final Board item, int position, String type, final OnItemClickListener listener) {

            name.setText(board + (position + 1));
            courseIV.setImageResource(R.drawable.board);
            itemView.setOnClickListener(v -> listener.onItemClick(item, position));
        }
    }
}
