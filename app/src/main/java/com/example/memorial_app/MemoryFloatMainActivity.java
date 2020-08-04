package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MemoryFloatMainActivity extends AppCompatActivity {
    private MyDbPosts mDbPosts = null;
    MyAdapter4 rAdapter = null;
    RecyclerView recyclerView = null;
    static int id = 999;

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

        createPost(id);

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

    public void createPost(int spots_id){

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

        mDbPosts = new MyDbPosts(getApplicationContext());
        SQLiteDatabase reader = mDbPosts.getReadableDatabase();
        //SQLiteDatabase writer = mDbHelper.getWritableDatabase();

        // SELECT
        String[] projection = { // SELECT する列
                MyDbContract.PostsTable.COL_ID,
                MyDbContract.PostsTable.COL_SPOTS_ID,
                MyDbContract.PostsTable.COL_UPDATED_AT,
                MyDbContract.PostsTable.COL_NICKNAME,
                MyDbContract.PostsTable.COL_PAST_ADDRESS,
                MyDbContract.PostsTable.COL_CURRENT_ADDRESS,
                MyDbContract.PostsTable.COL_AGE,
                MyDbContract.PostsTable.COL_JOB,
                MyDbContract.PostsTable.COL_MEMORY,
                MyDbContract.PostsTable.COL_TIME,
                MyDbContract.PostsTable.COL_EMOTION,
                MyDbContract.PostsTable.COL_IMAGES_BIN
        };


        String selection = MyDbContract.PostsTable.COL_SPOTS_ID + " = ?"; // WHERE 句
        String[] selectionArgs = { String.valueOf(spots_id) };


        String sortOrder = MyDbContract.PostsTable.COL_ID + " ASC"; // ORDER 句
        Cursor cursor = reader.query(
                MyDbContract.PostsTable.TABLE_NAME, // The table to query
                projection,         // The columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // don't group the rows
                null,               // don't filter by row groups
                sortOrder           // The sort order
        );
/*        Cursor cursor = reader.query(
                MyTable.TABLE_NAME, // The table to query
                projection,         // The columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // don't group the rows
                null,               // don't filter by row groups
                sortOrder           // The sort order
        );*/

        while(cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_ID));
            Integer s_id = cursor.getInt(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_SPOTS_ID));
            String updated_at = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_UPDATED_AT));
            String nickname = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_NICKNAME));
            String past_address = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_PAST_ADDRESS));
            String current_address = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_CURRENT_ADDRESS));
            Integer age = cursor.getInt(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_AGE));
            String job = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_JOB));
            String memory = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_MEMORY));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_TIME));
            String emotion = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_EMOTION));
            String image_bin = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.PostsTable.COL_IMAGES_BIN));

            itemIds.add(id);
            itemSpots_Ids.add(s_id);
            itemUpdated_Ats.add(updated_at);
            itemNickNames.add(nickname);
            itemPast_Addresses.add(past_address);
            itemCurrent_Addresses.add(current_address);
            itemAges.add(age);
            itemJobs.add(job);
            itemMemories.add(memory);
            itemTimes.add(time);
            itemEmotions.add(emotion);
            itemImages.add(image_bin);

            //距離計算はonCreateにて
            //items.add(new MyClass(id, updated_at, created_at, name, ruby, description, latitude, longitude, image_bin, (Float)null));
        }
        cursor.close();
    }

    public void onButtonPost(View v) {
        // 画面指定
        Intent intent = new Intent(this,MemoryFloatPostActivity.class);
        intent.putExtra("id", id);
        // 画面を開く
        startActivity(intent);

    }
}
