package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MemoryFloatPostConfirmationActivity extends AppCompatActivity {

    private TestTask testTask;
    private static final String POSTS_URL = "http://andolabo.sakura.ne.jp/arproject/posts_insert.php";
    private Context context = null;
    String json_input = null;
    int spots_id = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_float_post_confirmation);

        context = getApplicationContext();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();            // 遷移元から Intent を取得
        spots_id = intent.getIntExtra(MemoryFloatPostActivity.SPOTS_ID, 999);
        String nickName = intent.getStringExtra(MemoryFloatPostActivity.NICKNAME_DATA);
        String age = intent.getStringExtra(MemoryFloatPostActivity.AGE_DATA);
        String job = intent.getStringExtra(MemoryFloatPostActivity.JOB_DATA);
        String pastAddress = intent.getStringExtra(MemoryFloatPostActivity.PAST_ADDRESS_DATA);
        String currentAddress = intent.getStringExtra(MemoryFloatPostActivity.CURRENT_ADDRESS_DATA);
        String memory = intent.getStringExtra(MemoryFloatPostActivity.MEMORY_DATA);
        String timeSeries = intent.getStringExtra(MemoryFloatPostActivity.TIME_SERIES_DATA);
        String emotion = intent.getStringExtra(MemoryFloatPostActivity.EMOTION_DATA);
        String encoded = intent.getStringExtra(MemoryFloatPostActivity.PICTURE_DATA);

        // Capture the layout's TextView and set the string as its text
        TextView nickNameData = findViewById(R.id.NickNameData);
        TextView ageData = findViewById(R.id.AgeData);
        TextView jobData = findViewById(R.id.JobData);
        TextView pastAddressData = findViewById(R.id.PastAddressData);
        TextView currentAddressData = findViewById(R.id.CurrentAddressData);
        TextView memoryData = findViewById(R.id.MemoryData);
        TextView timeSeriesData = findViewById(R.id.TimeSeriesData);
        TextView emotionData = findViewById(R.id.EmotionData);

        //画像表示
        ImageView post_pic = findViewById(R.id.PictureData);
        if(encoded != null){
            byte[] decodedByte = Base64.decode(encoded,Base64.DEFAULT);
            Bitmap decoded = BitmapFactory.decodeByteArray(decodedByte, 0,decodedByte.length);
            post_pic.setImageBitmap(decoded);
        }
        else{
            post_pic.setImageResource(R.drawable.no_image);
        }

        nickNameData.setText(nickName);
        ageData.setText(age);
        jobData.setText(job);
        pastAddressData.setText(pastAddress);
        currentAddressData.setText(currentAddress);
        memoryData.setText(memory);
        timeSeriesData.setText(timeSeries);
        emotionData.setText(emotion);

        //json作成
        json_input = "{\"posts_nickname\":\"" + nickName + "\"," +
                "\"posts_past_address\":\"" + pastAddress + "\"," +
                "\"posts_current_address\":\"" + currentAddress + "\"," +
                "\"posts_age\":\"" + age + "\"," +
                "\"posts_job\":\"" + job + "\"," +
                "\"posts_memory\":\"" + memory + "\"," +
                "\"posts_time\":\"" + timeSeries + "\"," +
                "\"posts_emotion\":\"" + emotion + "\"," +
                "\"posts_spots_id\":\"" + spots_id + "\"," +
                "\"posts_picture\":\"" + encoded + "\"" +
                "}";
    }

    public void postMemory(View v){
        testTask = new TestTask(this);
        testTask.setListener(createListener());
        testTask.execute(POSTS_URL, json_input);
    }

    @Override
    protected void onDestroy(){
        testTask.setListener(null);
        super.onDestroy();
    }

    private TestTask.Listener createListener(){
        return new TestTask.Listener() {
            @Override
            public void onSuccess(String strPostURL, String json_input, String result) {
                if(strPostURL == POSTS_URL){
                    //json_string = result;
                    //initDBSpots(result);
                    if(result != null){
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(context, "投稿しました", Toast.LENGTH_LONG).show();
                    }
                }
                moveActivity();
            }
        };
    }

    public void moveActivity(){
        Intent intent = new Intent(this,MemoryFloatMainActivity.class);
        intent.putExtra("id", spots_id);
        startActivity(intent);
    }
}