package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class RouteMapActivity extends AppCompatActivity {

/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
    }*/

    // *************************************************
    // RecyclerView
    // *************************************************
    private static final String[] names = {
            "日和山", "蒲生干潟", "仙台市立荒浜小学校",
            "中野小学校校舎跡", "荒浜祈りの塔", "井土の一本松",
            "南蒲生浄化センター", "農業園芸センター", "浪分神社", "浪分桜"
    };

    // それぞれの画像ファイルをdarawableに入れます
    // ArrayListにコピーするためintからInteger型にしました
    private static final Integer[] photos = {
            R.drawable.img00, R.drawable.img01,
            R.drawable.img02, R.drawable.img03,
            R.drawable.img04, R.drawable.img05,
            R.drawable.img06, R.drawable.img07,
            R.drawable.img08, R.drawable.img09
    };

    private static final String[] captions = {
            "震災以前は標高6ｍの元祖日本一低い山だったが，現在では標高3mの山となっている",
            "仙台海浜鳥獣保護区の特別保護地区に指定されており，干潟でシジミなどが採れた",
            "海から約700mの所に位置しており，震災では校舎2階まで津波が押し寄せた震災遺構",
            "震災時に校舎2階まで津波が押し寄せた。2016年3月には142年の歴史に幕を下ろした",
            "3.11の犠牲者への慰霊と鎮魂のため，荒浜自治会と七郷連合町内会により建てられた",
            "海から1.5kmの距離にあり震災時は大きく津波をかぶったが，現在もそびえ立っている",
            "震災により主要な土木・建築物が破壊され，機械・電気設備が壊滅的な被害を受けた",
            "1989年に開園した農業園芸センターは，震災では津波冠水などの被害にあった",
            "創建当時は今の場所から500mほど南東に位置したとされている",
            "浪分神社にあやかり名づけられ，3.11の津波到達地周辺に目安木として植えられた桜"
    };

    MapFragment mf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
        // activity_route_map の id と 合ってるか確認すること

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(rLayoutManager);


        // 配列をArrayListにコピー
        List<String> itemNames = new ArrayList<String>(Arrays.asList(names));
        List<Integer> itemImages = new ArrayList<Integer>(Arrays.asList(photos));
        List<String> itemCaptions = new ArrayList<String>(Arrays.asList(captions));

        // 多分無駄処理だけど未解読（emailの名残）
        // 下のコードにしようとしたけど無理だった
        /*
        List<String> itemCaptions = new ArrayList<String>();
        for(int i=0; i<itemNames.size() ;i++ ){
            String str = String.format(Locale.ENGLISH, "%s", itemNames.get(i));
            itemCaptions.add(str);
        }*/


        // specify an adapter (see also next example)
        RecyclerView.Adapter rAdapter = new MyAdapter2(itemImages, itemNames, itemCaptions);
        recyclerView.setAdapter(rAdapter);

/*
        // *************************************************
        // *                Google Map                     *
        // *************************************************

        super.onCreate(savedInstanceState);

        mf = MapFragment.newInstance();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(android.R.id.content, mf);
        ft.commit();*/
    }


}
