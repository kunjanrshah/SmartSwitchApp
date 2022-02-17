package com.espressif.esptouch.android.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.pojo.Floor;
import com.espressif.esptouch.android.pojo.Group;

import java.text.MessageFormat;
import java.util.List;

public class CreateGroupAdapter extends RecyclerView.Adapter<CreateGroupAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Group item, int position);
    }

    private String type = "";

    private final List<Group> items;
    private final OnItemClickListener listener;

    public CreateGroupAdapter(List<Group> items, String type, OnItemClickListener listener) {
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
        private String floor = "Floor";
        private String room = "Room";
        private String group = "Group";
        private TextView name;
        private ImageView courseIV;
        private ImageView selected;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.idTVCourse);
            courseIV = (ImageView) itemView.findViewById(R.id.idIVcourse);
            selected = (ImageView) itemView.findViewById(R.id.selected);
        }

        public void bind(final Group item, int position, String type, final OnItemClickListener listener) {

            courseIV.setImageResource(R.drawable.board);
            name.setText(item.name);


            itemView.setOnClickListener(v -> {
                item.selected = !item.selected;
                selected.setVisibility(item.selected ? View.VISIBLE : View.GONE);
                listener.onItemClick(item, position);
            });
        }
    }
}

