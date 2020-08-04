package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MemoryFloatPostActivity extends AppCompatActivity {

    public static final String SPOTS_ID = "spots_id";
    public static final String NICKNAME_DATA = "nickname";
    public static final String AGE_DATA = "age";
    public static final String JOB_DATA = "job";
    public static final String PAST_ADDRESS_DATA = "past_address";
    public static final String CURRENT_ADDRESS_DATA = "current_address";
    public static final String MEMORY_DATA = "job";
    public static final String TIME_SERIES_DATA = "time_series";
    public static final String EMOTION_DATA = "emotion";
    public static final String PICTURE_DATA = "picture";

    static int id = 999;
    private static final int REQUEST_GALLERY = 0;
    Bitmap img = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_float_post);

        //spots_id 取得
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 999);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, MemoryFloatPostConfirmationActivity.class);    // Intentオブジェクトを生成

        EditText nickNameData = (EditText) findViewById(R.id.NickName);
        EditText ageData = (EditText) findViewById(R.id.Age);
        EditText jobData = (EditText) findViewById(R.id.Job);
        EditText pastAddressData = (EditText) findViewById(R.id.PastAddress);
        EditText currentAddressData = (EditText) findViewById(R.id.CurrentAddress);
        EditText memoryData = (EditText) findViewById(R.id.Memory);
        EditText timeSeriesData = (EditText) findViewById(R.id.TimeSeries);
        EditText emotionData = (EditText) findViewById(R.id.Emotion);

        String nickName = nickNameData.getText().toString();    // エディットテキストのテキストを取得
        String age = ageData.getText().toString();
        String job = jobData.getText().toString();
        String pastAddress = pastAddressData.getText().toString();
        String currentAddress = currentAddressData.getText().toString();
        String memory = memoryData.getText().toString();
        String timeSeries = timeSeriesData.getText().toString();
        String emotion = emotionData.getText().toString();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        intent.putExtra(SPOTS_ID, id);
        intent.putExtra(NICKNAME_DATA, nickName);              // EditText の値をインテントに追加 putExtra(キー名,渡したい値)
        intent.putExtra(AGE_DATA, age);
        intent.putExtra(JOB_DATA, job);
        intent.putExtra(PAST_ADDRESS_DATA, pastAddress);
        intent.putExtra(CURRENT_ADDRESS_DATA, currentAddress);
        intent.putExtra(MEMORY_DATA, memory);
        intent.putExtra(TIME_SERIES_DATA, timeSeries);
        intent.putExtra(EMOTION_DATA, emotion);
        intent.putExtra(PICTURE_DATA, encoded);

        startActivity(intent);                                  //インテントで指定されたアクティビティ開始
    }

    public void selectPicture(View view){
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK){
            try{
                InputStream in = getContentResolver().openInputStream(data.getData());
                img = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}