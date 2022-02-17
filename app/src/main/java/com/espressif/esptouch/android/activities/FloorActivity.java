package com.espressif.esptouch.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.adapters.FloorAdapter;
import com.espressif.esptouch.android.pojo.Building;
import com.espressif.esptouch.android.pojo.CommonData;
import com.espressif.esptouch.android.pojo.Floor;

public class FloorActivity extends AppCompatActivity implements FloorAdapter.OnItemClickListener {
    private String type = "";
    private FloorAdapter adapter1;
    RecyclerView recyclerView;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        pos = bundle.getInt("pos");
        Building building = CommonData.getInstance().getRoot().all.get(pos).building;
        recyclerView = findViewById(R.id.idGVcourses1);
        Button createGroup = findViewById(R.id.scheduleGroup);
        createGroup.setOnClickListener(v -> {
            Intent intent = new Intent(FloorActivity.this, CreateGroupActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("pos", pos);
            startActivity(intent);
        });

        adapter1 = new FloorAdapter(building.floor, building.groups, type, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Building building = CommonData.getInstance().getRoot().all.get(pos).building;
        adapter1 = new FloorAdapter(building.floor, building.groups, type, this);
        recyclerView.setAdapter(adapter1);
    }

    @Override
    public void onItemClick(Floor item, int position) {
        Intent intent = new Intent(FloorActivity.this, BoardActivity.class);
        intent.putExtra("building_pos", pos);
        intent.putExtra("floor_pos", position);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(FloorActivity.this, ScheduleActivity.class);
        intent.putExtra("pos", pos);
        intent.putExtra("group_pos", position);
        startActivity(intent);
    }
}