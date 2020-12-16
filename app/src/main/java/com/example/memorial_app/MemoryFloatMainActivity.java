package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class MemoryFloatMainActivity extends AppCompatActivity {
    private MyDbPosts mDbPosts = null;
    MyAdapter4 rAdapter = null;
    RecyclerView recyclerView = null;
    static int id = 999;

    private Context context = null;

    private TestTask testTask;
    private static final String POSTS_URL = "http://andolabo.sakura.ne.jp/arproject/get_memory.php";


    static List<Integer> itemIds = new ArrayList<Integer>();
    static List<Integer> itemSpots_Ids = new ArrayList<Integer>();
    static List<String> itemUpdated_Ats = new ArrayList<String>();
    static List<String> itemNickNames = new ArrayList<String>();
    static List<String> itemPast_Addresses = new ArrayList<String>();
    static List<String> itemCurrent_Addresses = new ArrayList<String>();
    static List<Integer> itemAges = new ArrayList<Integer>();
    static List<String> itemJobs = new ArrayList<String>();
    static List<String> itemMemories = new ArrayList<String>();
    static List<String> itemTimes = new ArrayList<String>();
    static List<String> itemEmotions = new ArrayList<String>();
    static List<String> itemImages = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_float_main);

        //memoryfloatを表示
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 999);

        getMemoryFloat(id);

        recyclerView = findViewById(R.id.my_recycler_view4);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(rLayoutManager);

        rAdapter = new MyAdapter4(itemIds, itemNickNames, itemAges, itemJobs, itemMemories, itemTimes, itemEmotions, itemImages);
        recyclerView.setAdapter(rAdapter);
/*        rAdapter.setOnItemClickListener(new MyAdapter3.onItemClickListener(){
            @Override
            public void onClick(View view, int id){
                //Toast.makeText(SpotActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                //第２引数変更してそのページに遷移、作るか検討中
                Intent intent = new Intent(MemoryFloatMainActivity.this, MemoryFloatMainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });*/

    }

    public void getMemoryFloat(int id){
        final String json_input = "{\"spots_id\":\"" + id + "\"}";
        testTask = new TestTask(this);
        testTask.setListener(createListener());
        testTask.execute(POSTS_URL, json_input);
        Toast.makeText(context, "メモリーフロート取得開始", Toast.LENGTH_SHORT).show();
    }

    private TestTask.Listener createListener(){
        return new TestTask.Listener() {
            @Override
            public void onSuccess(String strPostURL, String json_input, String result) {
                if(strPostURL == POSTS_URL){
                    //json_string = result;
                    createPost(result);
                }
            }
        };
    }

    public void createPost(String result){

        itemIds.clear();
        itemSpots_Ids.clear();
        itemUpdated_Ats.clear();
        itemNickNames.clear();
        itemPast_Addresses.clear();
        itemCurrent_Addresses.clear();
        itemAges.clear();
        itemJobs.clear();
        itemMemories.clear();
        itemTimes.clear();
        itemEmotions.clear();
        itemImages.clear();

        //スポットID取得
        int spots_id = id;

        //データ取得処理
        JSONObject posts = null;
        try{
            JSONObject json = new JSONObject(result);
            posts = json.getJSONObject("Post_all").getJSONObject("posts");
        }
        catch(JSONException e) {
            e.printStackTrace();
            return;
        }

        // INSERT
        int posts_length = posts.length();
        for(int i = 0; i < posts_length; i++){
            try {
                JSONObject post = posts.getJSONObject(valueOf(i+1));
                itemIds.add(Integer.parseInt(post.getString("posts_id")));
                itemSpots_Ids.add(spots_id);
                itemUpdated_Ats.add(post.getString("posts_updated_at"));
                itemNickNames.add(post.getString("posts_nickname"));
                itemPast_Addresses.add(post.getString("posts_past_address"));
                itemCurrent_Addresses.add(post.getString("posts_current_address"));
                itemAges.add(Integer.parseInt(post.getString("posts_age")));
                itemJobs.add(post.getString("posts_job"));
                itemMemories.add(post.getString("posts_memory"));
                itemTimes.add(post.getString("posts_time"));
                itemEmotions.add(post.getString("posts_emotion"));
                itemImages.add(post.getString("posts_images_bin"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        refresh();
    }

    private void refresh() {
        rAdapter = new MyAdapter4(itemIds, itemNickNames, itemAges, itemJobs, itemMemories, itemTimes, itemEmotions, itemImages);
        recyclerView.setAdapter(rAdapter);
    }

    public void onButtonPost(View v) {
        // 画面指定
        Intent intent = new Intent(this,MemoryFloatPostActivity.class);
        intent.putExtra("id", id);
        // 画面を開く
        startActivity(intent);

    }
}
