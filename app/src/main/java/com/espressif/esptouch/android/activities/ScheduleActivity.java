package com.espressif.esptouch.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.adapters.CreateGroupAdapter;
import com.espressif.esptouch.android.pojo.CommonData;
import com.espressif.esptouch.android.pojo.Group;

import java.util.Calendar;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener, CreateGroupAdapter.OnItemClickListener {
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button scheduleGroup;
    boolean isOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Bundle bundle = getIntent().getExtras();
        int pos = bundle.getInt("pos");
        int groupPos = bundle.getInt("group_pos");
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);
        scheduleGroup = findViewById(R.id.scheduleGroup);
        scheduleGroup.setOnClickListener(this);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.idGVcourses1);
        List<Group> arrayList = CommonData.getInstance().getRoot().all.get(pos).building.groups.get(groupPos);
        CreateGroupAdapter adapter1 = new CreateGroupAdapter(arrayList, "", this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter1);
        Switch sw = findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isOn = isChecked;
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> txtTime.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == scheduleGroup) {
            if (txtTime.getText().toString().isEmpty() || txtDate.getText().toString().isEmpty()) {
                showAlert("Please select date/time to schedule your group", false);
            } else {
                String msg = String.format("You have scheduled this group to be %s at %s and %s.", isOn ? "on" : "off", txtDate.getText().toString(), txtTime.getText().toString());
                showAlert(msg, true);
            }
        }

    }

    @Override
    public void onItemClick(Group item, int position) {

    }

    private void showAlert(String msg, boolean finish) {
        AlertDialog alertDialog1 = new AlertDialog.Builder(this).create();
        alertDialog1.setTitle("Schedule Group");
        alertDialog1.setMessage(msg);

        // Setting Icon to Dialog
        //alertDialog1.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog1.setButton("OK", (dialog, which) -> {
            if (finish)
                finish();
        });
        alertDialog1.show();
    }
}