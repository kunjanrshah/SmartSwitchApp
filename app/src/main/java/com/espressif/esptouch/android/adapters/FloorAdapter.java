package com.espressif.esptouch.android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.pojo.Floor;
import com.espressif.esptouch.android.pojo.Group;

import java.text.MessageFormat;
import java.util.List;

public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Floor item, int position);

        void onItemClick(int position);
    }

    private String type = "";

    private final List<Floor> items;
    private final List<List<Group>> groupList;
    private final OnItemClickListener listener;

    public FloorAdapter(List<Floor> items, List<List<Group>> groupList, String type, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
        this.type = type;
        this.groupList = groupList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position, type, listener);
    }

    @Override
    public int getItemCount() {
        if (groupList != null)
            return items.size() + groupList.size();
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private String floor = "Floor";
        private String room = "Room";
        private String group = "Group";
        private TextView name;
        private ImageView courseIV;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.idTVCourse);
            courseIV = (ImageView) itemView.findViewById(R.id.idIVcourse);
        }

        public void bind(int position, String type, final OnItemClickListener listener) {
            if (position < items.size()) {
                Floor item = items.get(position);
                if (type.equals("home")) {
                    courseIV.setImageResource(R.drawable.room);
                    name.setText(MessageFormat.format("{0} {1}", room, position + 1));
                } else {
                    courseIV.setImageResource(R.drawable.floor);
                    name.setText(String.format("%s %d", floor, position + 1));
                }
                itemView.setOnClickListener(v -> listener.onItemClick(item, position));
            } else {

                courseIV.setImageResource(R.drawable.group);
                name.setText(String.format("%s %d", group, position - items.size() + 1));
                itemView.setOnClickListener(v -> listener.onItemClick(position - items.size()));
            }
        }
    }
}
