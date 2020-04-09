package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class MemoryFloatPostConfirmationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_float_post_confirmation);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();            // 遷移元から Intent を取得

        String nickName = intent.getStringExtra(MemoryFloatPostActivity.NICK_NAME_DATA);
        String age = intent.getStringExtra(MemoryFloatPostActivity.AGE_DATA);
        String job = intent.getStringExtra(MemoryFloatPostActivity.JOB_DATA);

        // Capture the layout's TextView and set the string as its text
        TextView nickNameData = findViewById(R.id.NickNameData);
        TextView ageData = findViewById(R.id.AgeData);
        TextView jobData = findViewById(R.id.JobData);

        nickNameData.setText(nickName);
        ageData.setText(age);
        jobData.setText(job);
    }


}