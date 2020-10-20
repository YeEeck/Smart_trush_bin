package com.trashapp.aitrashbin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        this.setTitle("设置");
    }
}
