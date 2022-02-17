package com.espressif.esptouch.android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.pojo.All;

import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(All item, int position);
    }

    private final List<All> items;
    private final OnItemClickListener listener;

    public BuildingAdapter(List<All> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final String office = "Office";
        private final String home = "Home";
        private final TextView name;
        private ImageView courseIV;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.idTVCourse);
            courseIV = itemView.findViewById(R.id.idIVcourse);
        }

        public void bind(final All item, int position, final OnItemClickListener listener) {

            if (item.type.equals("home")) {
                name.setText(String.format("%s %d", home, position + 1));
                courseIV.setImageResource(R.drawable.home);
            } else {
                name.setText(String.format("%s %d", office, position + 1));
                courseIV.setImageResource(R.drawable.office);
            }
            itemView.setOnClickListener(v -> listener.onItemClick(item, position));
        }
    }
}