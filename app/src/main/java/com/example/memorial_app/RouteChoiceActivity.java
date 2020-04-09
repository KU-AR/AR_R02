package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class RouteChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_choice);
    }

    public void onButton1(View v) {
        // 画面指定
        Intent intent = new Intent(this,RouteChoiceTimeActivity.class);
        // 画面を開く
        startActivity(intent);
    }

    public void onButton2(View v) {
        // 画面指定
        Intent intent = new Intent(this,RouteChoiceSpotActivity.class);
        // 画面を開く
        startActivity(intent);
    }

    public void onClose(View v) {
        finish();
    }
}
