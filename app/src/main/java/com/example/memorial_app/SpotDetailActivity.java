package com.example.memorial_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SpotDetailActivity extends AppCompatActivity {

    private MyDbSpots mDbSpots = null;
    static String[] detail = new String[3];

    static int id = 999;

    private Context context = null;

    private FusedLocationProviderClient fusedLocationClient;
    private Location location;
    private GeomagneticField geomagneticField;
    private static double latitude = 0;
    private static double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 999);
        getImage(id);
        Bitmap bitmapImage = null;
        try{
            byte[] byteImage = android.util.Base64.decode((detail[2]), android.util.Base64.DEFAULT);
            bitmapImage = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        }
        catch(Exception e){
            System.out.println(e);
        }
        ImageView imageView3 = findViewById(R.id.imageView3);
        imageView3.setImageBitmap(bitmapImage);
        //画面更新されるか未確認
        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText(detail[0]);
        //記載する事項がなければ、recyclerviewの部分をtextViewに変更、detail[1]をセット
    }

    public void onButtonMemoryFloatMain(View v) {
        // 画面指定
        Intent intent = new Intent(this,MemoryFloatMainActivity.class);
        intent.putExtra("id", id);
        // 画面を開く
        startActivity(intent);
    }

    public void onButtonSpotAR(View v){
        //一定範囲内でないと起動できなくする
        //現在地取得
        getLastLocation();
        //距離計算
        double distance = getDistance();
        if(distance < 5){
            Intent intent = new Intent(this, SpotARActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
        else{
            Toast.makeText(context, "十分に近い距離で再度お試しください", Toast.LENGTH_LONG).show();
        }
    }

    public void getImage(int id){
        mDbSpots = new MyDbSpots(getApplicationContext());
        SQLiteDatabase reader = mDbSpots.getReadableDatabase();
        //SQLiteDatabase writer = mDbHelper.getWritableDatabase();

        // SELECT
        String[] projection = { // SELECT する列
                MyDbContract.SpotsTable.COL_NAME,
                MyDbContract.SpotsTable.COL_DESCRIPTION,
                MyDbContract.SpotsTable.COL_IMAGES_BIN
        };

        String selection = MyDbContract.SpotsTable.COL_ID + " = ?"; // WHERE 句
        String[] selectionArgs = { String.valueOf(id) };
        String sortOrder = MyDbContract.SpotsTable.COL_ID + " ASC"; // ORDER 句
        Cursor cursor = reader.query(
                MyDbContract.SpotsTable.TABLE_NAME, // The table to query
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
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_DESCRIPTION));
            String image_bin = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_IMAGES_BIN));
            detail[0] = name;
            detail[1] = description;
            detail[2] = image_bin;
        }
        cursor.close();
    }

    public double getDistance(){
        //極半径、赤道半径(km)
        double pole_radius = 6356.752;
        double equatorial_radius = 6378.137;

        //緯度1度の距離
        double lat = pole_radius * 2 * Math.PI / 360;
        //経度1度の距離
        double lon_radius = Math.cos(latitude / 180 * Math.PI) * equatorial_radius;
        double lon = lon_radius * 2 * Math.PI / 360;

        double x1 = latitude;
        double y1 = longitude;

        Float spot_latitude = Float.valueOf(0);
        Float spot_longitude = Float.valueOf(0);

        //dbから位置取得
        mDbSpots = new MyDbSpots(getApplicationContext());
        SQLiteDatabase reader = mDbSpots.getReadableDatabase();
        // SELECT
        String[] projection = { // SELECT する列
                MyDbContract.SpotsTable.COL_LATITUDE,
                MyDbContract.SpotsTable.COL_LONGITUDE
        };
        String selection = MyDbContract.SpotsTable.COL_ID + " = ?"; // WHERE 句
        String[] selectionArgs = { String.valueOf(id) };
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
        while (cursor.moveToNext()) {
            spot_latitude = cursor.getFloat(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_LATITUDE));
            spot_longitude = cursor.getFloat(cursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_LONGITUDE));
        }

        double x2 = spot_latitude;
        double y2 = spot_longitude;
        double lat_dis = Math.abs(x1 - x2) * lat;
        double lon_dis = Math.abs(y1 - y2) * lon;
        double distance = Math.sqrt(Math.pow(lat_dis, 2) + Math.pow(lon_dis, 2));
        return distance;
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
                                } else {
                                    Log.d("debug", "計測不可");
                                }
                            }
                        }
                );
    }
}
