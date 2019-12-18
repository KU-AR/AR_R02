package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SpotDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);
    }

    public void onButtonMemoryFloatMain(View v) {
        // 画面指定
        Intent intent = new Intent(this,MemoryFloatMainActivity.class);
        // 画面を開く
        startActivity(intent);
    }
}
