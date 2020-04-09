package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.GeomagneticField;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



import static java.sql.Types.NULL;



public class SpotActivity extends AppCompatActivity implements LocationListener{
    private MyDbHelper mDbHelper = null;

    //絞り込み用フラグ default(絞り込みなし):0  1km以内:1  2km以内:2  5km以内:3 10km以内:4
    private int narrowing_flag = 0;
    //並び替え用フラグ default(登録id順):0  距離順:1  五十音順:2 メモリーフロート数順:3　 更新順:4
    private int sorting_flag = 0;

    //位置情報関連
    final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    private LocationManager locationManager;
    private GeomagneticField geomagneticField;
    private static double latitude = 0;
    private static double longitude = 0;

    static List<Integer> itemIds = new ArrayList<Integer>();
    static List<String> itemNames = new ArrayList<String>();
    static List<String> itemRubys = new ArrayList<String>();
    static List<String> itemCaptions = new ArrayList<String>();
    static List<Float> itemLatitudes = new ArrayList<Float>();
    static List<Float> itemLongitudes = new ArrayList<Float>();
    static List<String> itemImages = new ArrayList<String>();
    static List<Double> itemDistances = new ArrayList<Double>();

    static ArrayList<MyClass> items = new ArrayList<>();

    @Override
    public void onProviderDisabled(String provider){
    }
    @Override
    public void onProviderEnabled(String provider){
    }
    @Override
    public void onLocationChanged(Location location){
/*        geomagneticField = new GeomagneticField((float) location.getLatitude(), (float) location.getLongitude(), (float) location.getAltitude(), new Date().getTime());
        if(longitude == NULL && latitude == NULL){
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            //LatLng newLocation = new LatLng(Latitude, Longitude);
        }
        else {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }*/
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //onCreate上では不可（現在地取得がされないため）、onLocationChangedで実装
        //adapter用リスト更新（距離計算用）
        updateItemList();
        //ソート
        sortItem(narrowing_flag, sorting_flag);
        //再度リスト更新
        updateItemList();

        //Toast.makeText(getApplicationContext(),"onlocationchanged lat="+latitude+" lon="+longitude+"", Toast.LENGTH_SHORT).show();
        //Log.d("SpotActivity","onlocationchanged lat="+latitude+" lon="+longitude+"");
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void initLocationManager(){
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
           }
           else{
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
           }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,50,1,this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,50,1,this);
    }


    public void onButton2(View v){
        //絞り込み
        if(narrowing_flag == 0){
            //narrowing_flag = 1 に遷移　1km以内
            narrowing_flag = 1;
        }
        else if(narrowing_flag == 1){
            //narrowing_flag = 2 に遷移　2km以内
            narrowing_flag = 2;
        }
        else if(narrowing_flag == 2){
            //narrowing_flag = 3 に遷移　5km以内
            narrowing_flag = 3;
        }
        else if(narrowing_flag == 3){
            //narrowing_flag = 4 に遷移　10km以内
            narrowing_flag = 4;
        }
        else{
            //narrowing_flag = 0 に遷移　絞り込みなし
            narrowing_flag = 0;
        }
        reload();
    }

    public void onButton3(View v){
        //並び替え
        if(sorting_flag == 0){
            //sorting_flag = 1 に遷移　距離順
            sorting_flag = 1;
        }
        else if(sorting_flag == 1){
            //sorting_flag = 2 に遷移　五十音順
            sorting_flag = 2;
        }
        else if(sorting_flag == 2){
            //sorting_flag = 3 に遷移　メモリーフロート数順
            sorting_flag = 3;
        }
        else if(sorting_flag == 3){
            //sorting_flag = 4 に遷移　更新順
            sorting_flag = 4;
        }
        else{
            //sorting_flag = 0 に遷移　id順
            sorting_flag = 0;
        }
        reload();
    }

    private void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);

        //位置情報関連
        initLocationManager();

        //DBからデータ取得　並び替え絞り込み実装前
        createSpot(narrowing_flag, sorting_flag);


        // activity_route_choice_spot の id と 合ってるか確認すること

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(rLayoutManager);

        // specify an adapter (see also next example)
        MyAdapter3 rAdapter = new MyAdapter3(itemIds, itemNames, itemCaptions, itemLatitudes, itemLongitudes, itemImages);
        recyclerView.setAdapter(rAdapter);
        rAdapter.setOnItemClickListener(new MyAdapter3.onItemClickListener(){
            @Override
            public void onClick(View view, int id){
                //Toast.makeText(SpotActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SpotActivity.this, SpotDetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    public void onClose(View v) {
        finish();
    }

    public void createSpot(int narrowing_flag, int sorting_flag){
/*        itemIds.clear();
        itemNames.clear();
        itemRubys.clear();
        itemCaptions.clear();
        itemLatitudes.clear();
        itemLongitudes.clear();
        itemImages.clear();*/

        items.clear();

        mDbHelper = new MyDbHelper(getApplicationContext());
        SQLiteDatabase reader = mDbHelper.getReadableDatabase();
        //SQLiteDatabase writer = mDbHelper.getWritableDatabase();

        // SELECT
        String[] projection = { // SELECT する列
                MyDbContract.MyTable.COL_ID,
                MyDbContract.MyTable.COL_NAME,
                MyDbContract.MyTable.COL_RUBY,
                MyDbContract.MyTable.COL_DESCRIPTION,
                MyDbContract.MyTable.COL_LATITUDE,
                MyDbContract.MyTable.COL_LONGITUDE,
                MyDbContract.MyTable.COL_IMAGES_BIN
        };

        /*
        String selection = MyDbContract.MyTable.COL_ID + " = ?"; // WHERE 句
        String[] selectionArgs = { "1" };
        */

        String sortOrder = MyDbContract.MyTable.COL_ID + " ASC"; // ORDER 句
        Cursor cursor = reader.query(
                MyDbContract.MyTable.TABLE_NAME, // The table to query
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
        while(cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(MyDbContract.MyTable.COL_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.MyTable.COL_NAME));
            String ruby = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.MyTable.COL_RUBY));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.MyTable.COL_DESCRIPTION));
            Float latitude = cursor.getFloat(cursor.getColumnIndexOrThrow(MyDbContract.MyTable.COL_LATITUDE));
            Float longitude = cursor.getFloat(cursor.getColumnIndexOrThrow(MyDbContract.MyTable.COL_LONGITUDE));
            String image_bin = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.MyTable.COL_IMAGES_BIN));
/*            itemIds.add(id);
            itemNames.add(name);
            itemRubys.add(ruby);
            itemCaptions.add(description);
            itemLatitudes.add(latitude);
            itemLongitudes.add(longitude);
            itemImages.add(image_bin);*/

            //距離計算はonCreateにて
            items.add(new MyClass(id, name, ruby, description, latitude, longitude, image_bin, (Float)null));
        }
        cursor.close();
    }

    //itemListの更新、距離の計算
    public void updateItemList(){
        //極半径、赤道半径(km)
        double pole_radius = 6356.752;
        double equatorial_radius =6378.137;

        //緯度1度の距離
        double lat = pole_radius * 2 * Math.PI / 360;
        //経度1度の距離
        double lon_radius = Math.cos(latitude / 180 * Math.PI) * equatorial_radius;
        double lon = lon_radius * 2 * Math.PI / 360;

        //List作成
        itemIds.clear();
        itemNames.clear();
        itemRubys.clear();
        itemCaptions.clear();
        itemLatitudes.clear();
        itemLongitudes.clear();
        itemImages.clear();
        itemDistances.clear();
        for(MyClass item: items){
            itemIds.add(item.getItemIds());
            itemNames.add(item.getItemNames());
            itemRubys.add(item.getItemRubys());
            itemCaptions.add(item.getItemCaptions());
            itemLatitudes.add(item.getItemLatitudes());
            itemLongitudes.add(item.getItemLongitudes());
            itemImages.add(item.getItemImages());

            //距離計算
            if(latitude != 0 && longitude != 0){
/*                double x1 = latitude*1000000;
                double y1 = longitude*1000000;
                double x2 = item.getItemLatitudes()*1000000;
                double y2 = item.getItemLongitudes()*1000000;*/
                double x1 = latitude;
                double y1 = longitude;
                double x2 = item.getItemLatitudes();
                double y2 = item.getItemLongitudes();
                double lat_dis = Math.abs(x1-x2) * lat;
                double lon_dis = Math.abs(y1-y2) * lon;
                double distance = Math.sqrt(Math.pow(lat_dis, 2) + Math.pow(lon_dis, 2));
                itemDistances.add(distance);
                Log.d("Distance Debug: ", String.valueOf(distance));
            }
            else{
                //現在地取得できていない
                ;
            }
        }
    }

    public void sortItem(int narrowing_flag, int sorting_flag){
        //itemを絞り込み、並び替え
        //latitude1°は110.9463km     仙台駅　　（緯度：38.26°、緯度： 140.88°）　経度 1°の距離： 87.4082Km
        //並び替え用フラグ default(登録id順):0  距離順:1  五十音順:2 メモリーフロート数順:3　 更新順:4

        //絞り込み用フラグ default(絞り込みなし):0  1km以内:1  2km以内:2  5km以内:3 10km以内:4
        switch(narrowing_flag){
            case 0:
                break;
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:

        }

        switch(sorting_flag){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:

        }
    }

}
