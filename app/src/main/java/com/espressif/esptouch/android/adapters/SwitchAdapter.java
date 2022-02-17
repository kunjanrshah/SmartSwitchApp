package com.espressif.esptouch.android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.pojo.Switch;

import java.util.List;

public class SwitchAdapter extends RecyclerView.Adapter<SwitchAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Switch item, int position);
    }

    private String type = "";

    private List<Switch> items;
    private OnItemClickListener listener;

    public SwitchAdapter(List<Switch> items, String type, OnItemClickListener listener) {
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

    public void updateList(List<Switch> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView courseIV;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.idTVCourse);
            courseIV = (ImageView) itemView.findViewById(R.id.idIVcourse);
        }

        public void bind(final Switch item, int position, String type, final OnItemClickListener listener) {
            name.setText(item.getName());
            if (item.getStatus() == 0)
                courseIV.setImageResource(R.drawable.light_off);
            else
                courseIV.setImageResource(R.drawable.light_on);
            itemView.setOnClickListener(v -> listener.onItemClick(item, position));
        }
    }
}