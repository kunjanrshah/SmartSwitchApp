package com.espressif.esptouch.android.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.adapters.BuildingAdapter;
import com.espressif.esptouch.android.connect.Connection;
import com.espressif.esptouch.android.main.EspMainActivity;
import com.espressif.esptouch.android.mqat.ConnectionManager;
import com.espressif.esptouch.android.mqat.NotificationService;
import com.espressif.esptouch.android.pojo.All;
import com.espressif.esptouch.android.pojo.CommonData;
import com.espressif.esptouch.android.pojo.Root;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class HomeActivity extends AppCompatActivity implements BuildingAdapter.OnItemClickListener {

    private ImageView imgView;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String data = getJson();
        Root root = new Gson().fromJson(data, Root.class);
        CommonData.getInstance().setRoot(root);
        RecyclerView recyclerView = findViewById(R.id.idGVcourses1);
        imgView = findViewById(R.id.imgView);
        Button start = findViewById(R.id.start);
        start.setOnClickListener(v -> {
            Connection.getInstance().sendMessage("-1");
            startService();
        });

        Button start1 = findViewById(R.id.start1);
        start1.setOnClickListener(v -> {
            Connection.getInstance().setNull();
            Connection.getInstance();
        });


        status = findViewById(R.id.status);
        initMqtt();
        TextView connectedCount = findViewById(R.id.connectedCount);

        Connection.getInstance().getMsg().observe(this, s -> {
            Log.d("Inzimam", s);
            runOnUiThread(() -> {
                String[] arr = s.split(" ");
                connectedCount.setText("Connected Devices : " + countBits(Integer.parseInt(arr[0])));
            });
        });
        BuildingAdapter adapter1 = new BuildingAdapter(root.all, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter1);
        imgView.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, EspMainActivity.class));
        });
    }

    private void initMqtt() {
        if (ConnectionManager.mqat != null) {
            if (ConnectionManager.mqat.isConnected()) {
                status.setText("CONNECTED");
                ConnectionManager.subscribe("kunjan/send");
            }
        }
        startService();
    }

    private void startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), NotificationService.class));
        } else {
            startService(new Intent(getApplicationContext(), NotificationService.class));
        }
    }

    private int countBits(int number) {
        if (number == 0) {
            return number;
        }
        int count = 0;
        while (number != 0) {
            number &= (number - 1);
            count++;
        }
        return count;
    }


    private String getJson() {
        InputStream is = getResources().openRawResource(R.raw.data);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writer.toString();
    }

    @Override
    public void onItemClick(All item, int position) {
        Intent intent = new Intent(HomeActivity.this, FloorActivity.class);
        intent.putExtra("pos", position);
        intent.putExtra("type", item.type);
        startActivity(intent);
    }
}
