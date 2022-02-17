package com.espressif.esptouch.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.espressif.esptouch.android.ImageLinearGauge;
import com.espressif.esptouch.android.KdGaugeView;
import com.espressif.esptouch.android.LinearGauge;
import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.adapters.SwitchAdapter;
import com.espressif.esptouch.android.connect.Connection;
import com.espressif.esptouch.android.mqat.ConnectionManager;
import com.espressif.esptouch.android.pojo.CommonData;
import com.espressif.esptouch.android.pojo.Switch;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class SwitchActivity extends AppCompatActivity implements SwitchAdapter.OnItemClickListener, ConnectionManager.MessageReceived {
    private String type = "";
    private SwitchAdapter adapter1;
    private int board_pos = 0;
    private List<Switch> list;
    private int building_pos=0;
    int floor_pos =0;
    KdGaugeView speedoMeterView;
    ImageLinearGauge imageLinearGauge;
    TextView txtUnit,txtRs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        building_pos = bundle.getInt("building_pos");
        floor_pos = bundle.getInt("floor_pos");
        board_pos = bundle.getInt("board_pos");
        speedoMeterView = (KdGaugeView)findViewById(R.id.speedMeter);
        txtUnit=(TextView)findViewById(R.id.txt_unit);
        txtRs=(TextView)findViewById(R.id.txt_rs);
        imageLinearGauge = (ImageLinearGauge) findViewById(R.id.gauge);
        imageLinearGauge.setOrientation(LinearGauge.Orientation.HORIZONTAL);


        list = CommonData.getInstance().getRoot().all.get(building_pos).building.floor.get(floor_pos).board.get(board_pos).myswitch;
        RecyclerView recyclerView = findViewById(R.id.idGVcourses1);
        ConnectionManager.setCallback(this);
        adapter1 = new SwitchAdapter(list, type, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter1);
        addGraph();
        Connection.getInstance().getMsg().observe(this, s -> {
            setStatus(s);
        });

        txtUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mInent=new Intent(SwitchActivity.this,UnitHistoryActivity.class);
                startActivity(mInent);
            }
        });
    }

    private void setStatus(String s){
        //1 1 253 254 255 4 1 15 3 4 15 0 0 0 0 0 0 0 0 0 0
        String[] arr = s.split(" ");
        int boardPos = Integer.parseInt(arr[1]) - 1;
        txtUnit.setText("Total Units:- "+Integer.parseInt(arr[2]));
        txtRs.setText("Total Payment:- ");
        speedoMeterView.setSpeed(Integer.parseInt(arr[3]));
        imageLinearGauge.speedTo(Integer.parseInt(arr[4]));
        int totalSwitch = Integer.parseInt(arr[5]);
        String switchPos = Integer.toBinaryString(Integer.parseInt(arr[6]));
        switchPos=  String.format("%04d", Integer.parseInt(switchPos));
        char[] switchArr = switchPos.toCharArray();
        Log.d("Inzimam", switchPos+" : "+s);
        for (int i = 0; i < switchArr.length; i++) {
            String c = "" + switchArr[i];
            try {
                CommonData.getInstance().getRoot().all.get(building_pos).building.floor.get(floor_pos).board.get(board_pos).myswitch.get(i).setStatus(Integer.parseInt(c));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        list = CommonData.getInstance().getRoot().all.get(building_pos).building.floor.get(floor_pos).board.get(board_pos).myswitch;

        runOnUiThread(() -> adapter1.updateList(list));
    }

    @Override
    public void onItemClick(Switch item, int position) {
        if(building_pos == 0 || building_pos == 1 ){
            if (null != Connection.getInstance().getClientThread()) {
                String str= getCOmmand(position);
                Connection.getInstance().sendMessage(str);
                Toast.makeText(this, "TCP Clicked "+position+" "+str, Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "MQTT Clicked "+position+" "+getCOmmand(position), Toast.LENGTH_SHORT).show();
            ConnectionManager.publish(getCOmmand(position),"kunjan/receive",false);
        }
    }
    private String getCOmmand(int position){
        String switchValue = "";
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                switchValue = switchValue + (list.get(i).getStatus() == 0 ? 1 : 0);
            } else
                switchValue = switchValue + list.get(i).getStatus();
        }
        String value = binaryToDecimal(switchValue);
        String boardId = "0" + (board_pos + 1);
        String sendCommand = boardId + value;
        return sendCommand;
    }

    private void addGraph() {
        GraphView graphView = findViewById(R.id.idGraphView);

        // on below line we are adding data to our graph view.
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                // on below line we are adding
                // each point on our x and y axis.
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 9),
                new DataPoint(4, 6),
                new DataPoint(5, 3),
                new DataPoint(6, 6),
                new DataPoint(7, 1),
                new DataPoint(8, 2)
        });

        // after adding data to our line graph series.
        // on below line we are setting
        // title for our graph view.
        graphView.setTitle("My Graph View");


        // on below line we are setting
        // text color to our graph view.
        graphView.setTitleColor(R.color.colorAccent);

        // on below line we are setting
        // our title text size.
        graphView.setTitleTextSize(18);

        // on below line we are adding
        // data series to our graph view.
        graphView.addSeries(series);
    }

    private String binaryToDecimal(String binaryString) {
        return String.format("%03d", Integer.parseInt(binaryString, 2));
    }

    @Override
    public void msgReceived(String msg) {
        setStatus(msg);
    }
}
