package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MemoryFloatMainActivity extends AppCompatActivity {
    MyAdapter4 rAdapter = null;
    RecyclerView recyclerView = null;
    static int id = 999;

    static List<Integer> itemId = new ArrayList<Integer>();
    static List<String> itemNickNames = new ArrayList<String>();
    static List<Integer> itemAge = new ArrayList<Integer>();
    static List<String> itemJob = new ArrayList<String>();
    static List<String> itemMemory = new ArrayList<String>();
    static List<String> itemTime = new ArrayList<String>();
    static List<String> itemEmotion = new ArrayList<String>();
    static List<String> itemImages = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_float_main);

        //memoryfloatを表示
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 999);

        recyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(rLayoutManager);

        rAdapter = new MyAdapter4(itemId, itemNickNames, itemAge, itemJob, itemMemory, itemTime, itemEmotion, itemImages);
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

    public void onButtonPost(View v) {
        // 画面指定
        Intent intent = new Intent(this,MemoryFloatPostActivity.class);
        // 画面を開く
        startActivity(intent);
    }
}
