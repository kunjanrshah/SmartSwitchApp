package com.espressif.esptouch.android.activities;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.adapters.BoardAdapter;
import com.espressif.esptouch.android.pojo.Board;
import com.espressif.esptouch.android.pojo.CommonData;
import com.espressif.esptouch.android.pojo.Floor;

public class BoardActivity extends AppCompatActivity implements BoardAdapter.OnItemClickListener {
    private String type = "";
    int building_pos = 0;
    int floor_pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        building_pos = bundle.getInt("building_pos");
        floor_pos = bundle.getInt("floor_pos");
        Floor floor = CommonData.getInstance().getRoot().all.get(building_pos).building.floor.get(floor_pos);
        RecyclerView recyclerView = findViewById(R.id.idGVcourses1);

        BoardAdapter adapter1 = new BoardAdapter(floor.board, type, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter1);
    }

    @Override
    public void onItemClick(Board item, int position) {
        Intent intent = new Intent(BoardActivity.this, SwitchActivity.class);
        intent.putExtra("building_pos", building_pos);
        intent.putExtra("floor_pos", floor_pos);
        intent.putExtra("board_pos", position);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}