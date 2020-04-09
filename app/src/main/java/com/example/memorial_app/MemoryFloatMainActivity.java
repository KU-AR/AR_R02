package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MemoryFloatMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_float_main);
    }

    public void onButtonPost(View v) {
        // 画面指定
        Intent intent = new Intent(this,MemoryFloatPostActivity.class);
        // 画面を開く
        startActivity(intent);
    }
}
