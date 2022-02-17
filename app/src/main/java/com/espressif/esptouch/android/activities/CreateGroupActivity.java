package com.espressif.esptouch.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.adapters.CreateGroupAdapter;
import com.espressif.esptouch.android.pojo.Board;
import com.espressif.esptouch.android.pojo.Building;
import com.espressif.esptouch.android.pojo.CommonData;
import com.espressif.esptouch.android.pojo.Floor;
import com.espressif.esptouch.android.pojo.Group;

import java.util.ArrayList;

public class CreateGroupActivity extends AppCompatActivity implements CreateGroupAdapter.OnItemClickListener {
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        int pos = bundle.getInt("pos");
        Building building = CommonData.getInstance().getRoot().all.get(pos).building;
        ArrayList<Group> arrayList = new ArrayList<>();
        for (int i = 0; i < building.floor.size(); i++) {
            Floor floor = building.floor.get(i);
            for (int j = 0; j < floor.board.size(); j++) {
                Board board = floor.board.get(j);
                for (int k = 0; k < board.myswitch.size(); k++) {
                    arrayList.add(new Group("Room" + (i + 1) + "-" + "Board" + (j + 1) + "-" + "Switch" + (k + 1), i, j, k));
                }
            }
        }

        RecyclerView recyclerView = findViewById(R.id.idGVcourses1);
        Button createGroup = findViewById(R.id.scheduleGroup);
        ArrayList<Group> selected = new ArrayList<>();
        createGroup.setOnClickListener(v -> {
            for (Group group : arrayList) {
                if (group.selected) {
                    selected.add(group);
                }
            }
            if (CommonData.getInstance().getRoot().all.get(pos).building.groups == null)
                CommonData.getInstance().getRoot().all.get(pos).building.groups = new ArrayList<>();
            CommonData.getInstance().getRoot().all.get(pos).building.groups.add(selected);
            Toast.makeText(this, "Congrats!!! Your group has been created successfully", Toast.LENGTH_LONG).show();
            finish();
        });

        CreateGroupAdapter adapter1 = new CreateGroupAdapter(arrayList, type, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter1);
    }

    @Override
    public void onItemClick(Group item, int position) {

    }
}