package com.example.memorial_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import static com.example.memorial_app.MyDbContract.SpotsTable;
import static com.example.memorial_app.MainActivity.spots_length;
import static com.example.memorial_app.MyDbContract.PostsTable;
import static java.lang.String.valueOf;


public class SpotActivity extends AppCompatActivity/* implements LocationListener*/ {
    private TestTask testTask;
    private static final String COUNT_URL = "http://andolabo.sakura.ne.jp/arproject/count_memory.php";

    private MyDbSpots mDbSpots = null;
    private MyDbPosts mDbPosts = null;

    private Context context = null;
    RecyclerView recyclerView = null;
    MyAdapter3 rAdapter = null;

    //絞り込み用フラグ default(絞り込みなし):0  1km以内:1  2km以内:2  5km以内:3 10km以内:4
    private int narrowing_flag = 0;
    //並び替え用フラグ default(登録id順):0  距離順:1  五十音順:2 メモリーフロート数順:3　 更新順:4
    private int sorting_flag = 0;

    //表示テキスト
    private String narrowing_text = "絞り込みなし";
    private String sorting_text = "登録順";

    //位置情報関連
    final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    //    private LocationManager locationManager;
    private GeomagneticField geomagneticField;
    private static double latitude = 0;
    private static double longitude = 0;

    private FusedLocationProviderClient fusedLocationClient;
    private Location location;

    static List<Integer> itemIds = new ArrayList<Integer>();
    static List<String> itemUpdated_at = new ArrayList<String>();
    static List<String> itemCreated_at = new ArrayList<String>();
    static List<String> itemNames = new ArrayList<String>();
    static List<String> itemRubys = new ArrayList<String>();
    static List<String> itemCaptions = new ArrayList<String>();
    static List<Float> itemLatitudes = new ArrayList<Float>();
    static List<Float> itemLongitudes = new ArrayList<Float>();
    static List<String> itemImages = new ArrayList<String>();
    static List<Double> itemDistances = new ArrayList<Double>();
    static List<Integer> itemMemoryFloats = new ArrayList<Integer>();

    static ArrayList<MyClass> items = new ArrayList<>();
    static ArrayList<MyClass> items_copy = new ArrayList<>();

/*    @Override
    public void onProviderDisabled(String provider){
    }
    @Override
    public void onProviderEnabled(String provider){
    }*/
/*    @Override
    public void onLocationChanged(Location location){
*//*        geomagneticField = new GeomagneticField((float) location.getLatitude(), (float) location.getLongitude(), (float) location.getAltitude(), new Date().getTime());
        if(longitude == NULL && latitude == NULL){
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            //LatLng newLocation = new LatLng(Latitude, Longitude);
        }
        else {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }*//*
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //onCreate上では不可（現在地取得がされないため）、onLocationChangedで実装
        //adapter用リスト更新（距離計算用）
        updateItemList();
        //ソート
        sortItem(narrowing_flag, sorting_flag);
        //再度リスト更新
        updateItemList();

        reload();
        //Toast.makeText(getApplicationContext(),"onlocationchanged lat="+latitude+" lon="+longitude+"", Toast.LENGTH_SHORT).show();
        //Log.d("SpotActivity","onlocationchanged lat="+latitude+" lon="+longitude+"");
    }*/
/*    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }*/

/*
    public void initLocationManager(){
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
           }
           else{
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
           }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,30,this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,30,this);
    }
*/


    public void onButton2(View v) {
        //絞り込み
        if (narrowing_flag == 0) {
            //narrowing_flag = 1 に遷移　1km以内
            narrowing_flag = 1;
            narrowing_text = "1km以内の見学スポット";
        } else if (narrowing_flag == 1) {
            //narrowing_flag = 2 に遷移　2km以内
            narrowing_flag = 2;
            narrowing_text = "2km以内の見学スポット";
        } else if (narrowing_flag == 2) {
            //narrowing_flag = 3 に遷移　5km以内
            narrowing_flag = 3;
            narrowing_text = "5km以内の見学スポット";
        } else if (narrowing_flag == 3) {
            //narrowing_flag = 4 に遷移　10km以内
            narrowing_flag = 4;
            narrowing_text = "10km以内の見学スポット";
        } else {
            //narrowing_flag = 0 に遷移　絞り込みなし
            narrowing_flag = 0;
            narrowing_text = "絞り込みなし";
        }
        getLastLocation();
        refresh();
    }

    public void onButton3(View v) {
        //並び替え
        if (sorting_flag == 0) {
            //sorting_flag = 1 に遷移　距離順
            sorting_flag = 1;
            sorting_text = "距離順（近い順）";
        } else if (sorting_flag == 1) {
            //sorting_flag = 2 に遷移　五十音順
            sorting_flag = 2;
            sorting_text = "五十音順";
        } else if (sorting_flag == 2) {
            //sorting_flag = 3 に遷移　メモリーフロート数順
            sorting_flag = 3;
            sorting_text = "メモリーフロート数順（多い順）";
        } else if (sorting_flag == 3) {
            //sorting_flag = 4 に遷移　更新順
            sorting_flag = 4;
            sorting_text = "更新順";
        } else {
            //sorting_flag = 0 に遷移　id順
            sorting_flag = 0;
            sorting_text = "登録順";
        }
        getLastLocation();
        refresh();
    }

    //各種変数がリセットされる場合は使用不可(flagがリセットされるため)
    private void refresh() {
        items_copy.clear();
        items_copy = (ArrayList<MyClass>) items.clone();
        updateItemList();
        //ソート
        sortItem(narrowing_flag, sorting_flag);
        //再度リスト更新
        updateItemList();

        //debug
        Log.d("debug", itemIds.toString());

        rAdapter = new MyAdapter3(itemIds, itemNames, itemCaptions, itemLatitudes, itemLongitudes, itemImages);
        recyclerView.setAdapter(rAdapter);
        rAdapter.setOnItemClickListener(new MyAdapter3.onItemClickListener() {
            @Override
            public void onClick(View view, int id) {
                //Toast.makeText(SpotActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SpotActivity.this, SpotDetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        context = getApplicationContext();

        //位置情報関連
//        initLocationManager();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        getLastLocation();

        //DBからデータ取得　並び替え絞り込み実装前
        createSpot(narrowing_flag, sorting_flag);

        //メモリーフロート数計算
        //countMemoryFloats();

/*
        updateItemList();
        //ソート
        sortItem(narrowing_flag, sorting_flag);
        //再度リスト更新
        updateItemList();
*/

        // activity_route_choice_spot の id と 合ってるか確認すること

        recyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(rLayoutManager);

/*        // specify an adapter (see also next example)
        rAdapter = new MyAdapter3(itemIds, itemNames, itemCaptions, itemLatitudes, itemLongitudes, itemImages);
        recyclerView.setAdapter(rAdapter);
        rAdapter.setOnItemClickListener(new MyAdapter3.onItemClickListener(){
            @Override
            public void onClick(View view, int id){
                //Toast.makeText(SpotActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SpotActivity.this, SpotDetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });*/

/*        getLastLocation();
        refresh();*/

    }

    public void onClose(View v) {
        finish();
    }

    public void createSpot(int narrowing_flag, int sorting_flag) {
/*        itemIds.clear();
        itemNames.clear();
        itemRubys.clear();
        itemCaptions.clear();
        itemLatitudes.clear();
        itemLongitudes.clear();
        itemImages.clear();*/

        items.clear();

        mDbSpots = new MyDbSpots(getApplicationContext());
        SQLiteDatabase reader = mDbSpots.getReadableDatabase();
        //SQLiteDatabase writer = mDbHelper.getWritableDatabase();

        // SELECT
        String[] projection = { // SELECT する列
                MyDbContract.SpotsTable.COL_ID,
                MyDbContract.SpotsTable.COL_UPDATED_AT,
                MyDbContract.SpotsTable.COL_CREATED_AT,
                MyDbContract.SpotsTable.COL_NAME,
                MyDbContract.SpotsTable.COL_RUBY,
                MyDbContract.SpotsTable.COL_DESCRIPTION,
                MyDbContract.SpotsTable.COL_LATITUDE,
                MyDbContract.SpotsTable.COL_LONGITUDE,
                MyDbContract.SpotsTable.COL_IMAGES_BIN
        };

        /*
        String selection = MyDbContract.MyTable.COL_ID + " = ?"; // WHERE 句
        String[] selectionArgs = { "1" };
        */

        String sortOrder = MyDbContract.SpotsTable.COL_ID + " ASC"; // ORDER 句
        Cursor cursor = reader.query(
                MyDbContract.SpotsTable.TABLE_NAME, // The table to query
                projection,         // The columns to return
                null,          // The columns for the WHERE clause
                null,      // The values for the WHERE clause
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
        while (cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_ID));
            String updated_at = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_UPDATED_AT));
            String created_at = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_CREATED_AT));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_NAME));
            String ruby = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_RUBY));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_DESCRIPTION));
            Float latitude = cursor.getFloat(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_LATITUDE));
            Float longitude = cursor.getFloat(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_LONGITUDE));
            String image_bin = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_IMAGES_BIN));
/*            itemIds.add(id);
            itemNames.add(name);
            itemRubys.add(ruby);
            itemCaptions.add(description);
            itemLatitudes.add(latitude);
            itemLongitudes.add(longitude);
            itemImages.add(image_bin);*/

            //距離計算はonCreateにて,メモリーフロート数も別途計算
            items.add(new MyClass(id, updated_at, created_at, name, ruby, description, latitude, longitude, image_bin, (Float) null, (int) 0));
        }
        cursor.close();
    }

    //itemListの更新、距離の計算
    public void updateItemList() {
        //極半径、赤道半径(km)
        double pole_radius = 6356.752;
        double equatorial_radius = 6378.137;

        //緯度1度の距離
        double lat = pole_radius * 2 * Math.PI / 360;
        //経度1度の距離
        double lon_radius = Math.cos(latitude / 180 * Math.PI) * equatorial_radius;
        double lon = lon_radius * 2 * Math.PI / 360;

        //List作成
        itemIds.clear();
        itemUpdated_at.clear();
        itemCreated_at.clear();
        itemNames.clear();
        itemRubys.clear();
        itemCaptions.clear();
        itemLatitudes.clear();
        itemLongitudes.clear();
        itemImages.clear();
        itemDistances.clear();
/*        for(MyClass item: items_copy){
            itemIds.add(item.getItemIds());
            itemUpdated_at.add(item.getItemUpdated_at());
            itemCreated_at.add(item.getItemCreated_at());
            itemNames.add(item.getItemNames());
            itemRubys.add(item.getItemRubys());
            itemCaptions.add(item.getItemCaptions());
            itemLatitudes.add(item.getItemLatitudes());
            itemLongitudes.add(item.getItemLongitudes());
            itemImages.add(item.getItemImages());

            //距離計算
            if(latitude != 0 && longitude != 0){
*//*                double x1 = latitude*1000000;
                double y1 = longitude*1000000;
                double x2 = item.getItemLatitudes()*1000000;
                double y2 = item.getItemLongitudes()*1000000;*//*
                double x1 = latitude;
                double y1 = longitude;
                double x2 = item.getItemLatitudes();
                double y2 = item.getItemLongitudes();
                double lat_dis = Math.abs(x1-x2) * lat;
                double lon_dis = Math.abs(y1-y2) * lon;
                double distance = Math.sqrt(Math.pow(lat_dis, 2) + Math.pow(lon_dis, 2));
                itemDistances.add(distance);
                //itemへのsetでうまくいくか未確認
                item.setItemDistances((float) distance);
                Log.d("Distance Debug: ", String.valueOf(distance));
            }
            else{
                //現在地取得できていない
                ;
            }
        }*/
        for (int index = 0; index < items_copy.size(); index++) {
            itemIds.add(items_copy.get(index).getItemIds());
            itemUpdated_at.add(items_copy.get(index).getItemUpdated_at());
            itemCreated_at.add(items_copy.get(index).getItemCreated_at());
            itemNames.add(items_copy.get(index).getItemNames());
            itemRubys.add(items_copy.get(index).getItemRubys());
            itemCaptions.add(items_copy.get(index).getItemCaptions());
            itemLatitudes.add(items_copy.get(index).getItemLatitudes());
            itemLongitudes.add(items_copy.get(index).getItemLongitudes());
            itemImages.add(items_copy.get(index).getItemImages());

            //距離計算
            if (latitude != 0 && longitude != 0) {
/*                double x1 = latitude*1000000;
                double y1 = longitude*1000000;
                double x2 = items_copy.get(index).getItemLatitudes()*1000000;
                double y2 = items_copy.get(index).getItemLongitudes()*1000000;*/
                double x1 = latitude;
                double y1 = longitude;
                double x2 = items_copy.get(index).getItemLatitudes();
                double y2 = items_copy.get(index).getItemLongitudes();
                double lat_dis = Math.abs(x1 - x2) * lat;
                double lon_dis = Math.abs(y1 - y2) * lon;
                double distance = Math.sqrt(Math.pow(lat_dis, 2) + Math.pow(lon_dis, 2));
                itemDistances.add(distance);
                //itemへのsetでうまくいくか未確認
                items_copy.get(index).setItemDistances((float) distance);
                Log.d("Distance Debug: ", String.valueOf(distance));
            } else {
                //現在地取得できていない
                ;
            }
        }
    }

/*    public void countMemoryFloats(){
        //Toast.makeText(context, "DBチェック開始", Toast.LENGTH_SHORT).show();

        List<Integer> MemoryFloats = new ArrayList<Integer>(items.size());

        //mDbHelper = new MyDbPosts(getApplicationContext());
        mDbPosts = new MyDbPosts(getApplicationContext());
        SQLiteDatabase reader = mDbPosts.getReadableDatabase();
        //SQLiteDatabase writer = mDbHelper.getWritableDatabase();


        // SELECT
        String[] projection = { // SELECT する列
                PostsTable.COL_SPOTS_ID,
                "count(*)"
        };

        String selection = PostsTable.COL_ID + " = ?"; // WHERE 句
        String[] selectionArgs = {  };
        String sortOrder = PostsTable.COL_ID + " ASC"; // ORDER 句
        Cursor cursor = reader.query(
                PostsTable.TABLE_NAME, // The table to query
                projection,         // The columns to return
                null,          // The columns for the WHERE clause
                null,      // The values for the WHERE clause
                PostsTable.COL_SPOTS_ID,               // group the rows
                null,               // don't filter by row groups
                sortOrder           // The sort order
        );
*//*        Cursor cursor = reader.query(
                MyTable.TABLE_NAME, // The table to query
                projection,         // The columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // don't group the rows
                null,               // don't filter by row groups
                sortOrder           // The sort order
        );*//*
        while(cursor.moveToNext()) {
            int spots_id = cursor.getInt(cursor.getColumnIndexOrThrow(PostsTable.COL_SPOTS_ID));
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("count(*)"));
            //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            //System.out.println("id: " + String.valueOf(id) + ", name: " + name);
            Log.d("memoryfloat", "spots_id: " + String.valueOf(spots_id) + ", count: " + count);
            items.get(spots_id - 1).setItemMemoryFloats(count);
        }

        cursor.close();

        itemMemoryFloats.clear();
        for(int index = 0; index < items.size(); index++){
            itemMemoryFloats.add(items.get(index).getItemMemoryFloats());
        }

        //Toast.makeText(context, "DBチェック終了", Toast.LENGTH_SHORT).show();
    }*/

    public void sortItem(int narrowing_flag, int sorting_flag) {
        //itemを絞り込み、並び替え
        //latitude1°は110.9463km     仙台駅　　（緯度：38.26°、緯度： 140.88°）　経度 1°の距離： 87.4082Km

        ArrayList<MyClass> temp = new ArrayList<>();

        //並び替え用フラグ default(登録id順):0  距離順:1  五十音順:2 メモリーフロート数順:3　 更新順:4
        switch (sorting_flag) {
            case 0:
                Collections.sort(items_copy, new IdComp());
                break;
            case 1:
                Collections.sort(items_copy, new DistanceComp());
                break;
            case 2:
                Collections.sort(items_copy, new RubyComp());
                break;
            case 3:
                //メモリーフロート数取得
                getCount();
                Collections.sort(items_copy, new MemoryFloatComp());
                break;
            case 4:
                Collections.sort(items_copy, new UpdateComp());
        }

        //絞り込み用フラグ default(絞り込みなし):0  1km以内:1  2km以内:2  5km以内:3 10km以内:4
        switch (narrowing_flag) {
            case 0:
                for (MyClass item : items_copy) {
                    Log.d("debug", "distance:" + item.getItemDistances().toString());
                    temp.add(item);
                }
                break;
            case 1:
                for (MyClass item : items_copy) {
                    Log.d("debug", "distance:" + item.getItemDistances().toString());
                    if (item.getItemDistances() < 1) {
                        temp.add(item);
                    }
                }
                break;
            case 2:
                for (MyClass item : items_copy) {
                    Log.d("debug", "distance:" + item.getItemDistances().toString());
                    if (item.getItemDistances() < 2) {
                        temp.add(item);
                    }
                }
                break;
            case 3:
                for (MyClass item : items_copy) {
                    Log.d("debug", "distance:" + item.getItemDistances().toString());
                    if (item.getItemDistances() < 5) {
                        temp.add(item);
                    }
                }
                break;
            case 4:
                for (MyClass item : items_copy) {
                    Log.d("debug", "distance:" + item.getItemDistances().toString());
                    if (item.getItemDistances() < 10) {
                        temp.add(item);
                    }
                }
        }
        items_copy.clear();
        items_copy = (ArrayList<MyClass>) temp.clone();
        Toast.makeText(context, "絞り込み：" + narrowing_text + "\n" + "並び替え：" + sorting_text, Toast.LENGTH_LONG).show();
    }

    public void getCount() {
        final String json_input = "{\"test\":\"abc\"}";
        testTask = new TestTask(this);
        testTask.setListener(createListener());
        testTask.execute(COUNT_URL, json_input);
        Toast.makeText(context, "メモリー数取得開始", Toast.LENGTH_SHORT).show();

    }

    private TestTask.Listener createListener() {
        return new TestTask.Listener() {
            @Override
            public void onSuccess(String strPostURL, String json_input, String result) {
                if (strPostURL == COUNT_URL) {
                    //json_string = result;
                    setCount(result);
                }
            }
        };
    }

    private void setCount(String result) {
        JSONObject counts = null;
        try {
            JSONObject json = new JSONObject(result);
            counts = json.getJSONObject("memory").getJSONObject("counts");
        } catch (JSONException e) {
            return;
        }
        int counts_length;
        counts_length = counts.length();
        itemMemoryFloats.clear();
        int j = 0;
        for (int i = 0; i < spots_length; i++) {
            try {
                JSONObject count = counts.getJSONObject(valueOf(j + 1));
                if (i == Integer.parseInt(count.getString("posts_spots_id")) - 1) {
                    items.get(i).setItemMemoryFloats(Integer.parseInt(count.getString("count")));
                    itemMemoryFloats.add(Integer.parseInt(count.getString("count")));
                    if (j < counts_length - 1) {
                        j++;
                    }
                } else {
                    items.get(i).setItemMemoryFloats(0);
                    itemMemoryFloats.add(0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getLastLocation() {
        //11/18 追記
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(
                        this,
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    location = task.getResult();
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    refresh();
                                } else {
                                    Log.d("debug", "計測不可");
                                }
                            }
                        }
                );
    }

}
