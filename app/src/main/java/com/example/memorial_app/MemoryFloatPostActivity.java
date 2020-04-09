package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MemoryFloatPostActivity extends AppCompatActivity {

    public static final String NICK_NAME_DATA = "com.example.myfirstapp.MESSAGE";
    public static final String AGE_DATA = "com.example.myfirstapp.MESSAGE";
    public static final String JOB_DATA = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_float_post);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, MemoryFloatPostConfirmationActivity.class);    // Intentオブジェクトを生成

        EditText nickNameData = (EditText) findViewById(R.id.NickName);
        EditText ageData = (EditText) findViewById(R.id.Age);
        EditText jobData = (EditText) findViewById(R.id.Job);

        String nickName = nickNameData.getText().toString();    // エディットテキストのテキストを取得
        String age = ageData.getText().toString();
        String job = jobData.getText().toString();

        intent.putExtra(NICK_NAME_DATA, nickName);              // EditText の値をインテントに追加 putExtra(キー名,渡したい値)
        intent.putExtra(AGE_DATA, age);
        intent.putExtra(JOB_DATA, job);

        startActivity(intent);                                  //インテントで指定されたアクティビティ開始
    }
}