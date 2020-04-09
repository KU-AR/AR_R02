package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;


public class RouteChoiceSpotActivity extends AppCompatActivity {


    public void onButton1(View v) {
        // 画面指定
        Intent intent = new Intent(this,RouteMapActivity.class);
        // 画面を開く
        startActivity(intent);
    }

    // *************************************************
    // *                RecyclerView                   *
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_choice_spot);
        // activity_route_choice_spot の id と 合ってるか確認すること

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

        // specify an adapter (see also next example)
        RecyclerView.Adapter rAdapter = new MyAdapter(itemImages, itemNames, itemCaptions);
        recyclerView.setAdapter(rAdapter);

    }

}
