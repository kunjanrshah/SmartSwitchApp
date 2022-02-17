package com.espressif.esptouch.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.espressif.esptouch.android.R;

import graphview.CurveGraphConfig;
import graphview.CurveGraphView;
import graphview.models.GraphData;
import graphview.models.PointMap;

public class UnitHistoryActivity extends Activity {

    CurveGraphView curveGraphView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_unit_history);

        curveGraphView = findViewById(R.id.cgv_day);

        curveGraphView.configure(
            new CurveGraphConfig.Builder(this)
                .setAxisColor(R.color.Blue)              // Set number of values to be displayed in X ax
                .setVerticalGuideline(4)                // Set number of background guidelines to be shown.
                .setHorizontalGuideline(2)
                .setGuidelineColor(R.color.Red)         // Set color of the visible guidelines.
                .setNoDataMsg(" No Data ")             // Message when no data is provided to the view.
                .setxAxisScaleTextColor(R.color.Black)          // Set X axis scale text color.
                .setyAxisScaleTextColor(R.color.Black)          // Set Y axis scale text color
                .setAnimationDuration(2000)                     // Set Animation Duration
                .build()
        );

        PointMap pointMap = new PointMap();
        pointMap.addPoint(1, 200);
        pointMap.addPoint(3, 400);
        pointMap.addPoint(4, 100);
        pointMap.addPoint(5, 600);

        final GraphData gd = GraphData.builder(this)
            .setPointMap(pointMap)
            .setGraphStroke(R.color.Black)
            .setGraphGradient(R.color.gradientStartColor2, R.color.gradientEndColor2)
            .animateLine(true)
            .setPointColor(R.color.Red)
            .setPointRadius(5)
            .build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                curveGraphView.setData(5, 600, gd);
            }
        }, 250);
    }
}
