package com.espressif.esptouch.android.activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.espressif.esptouch.android.R;

public class KeyHistoryActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_keys_history);
    }
}
