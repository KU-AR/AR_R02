package com.example.memorial_app;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
/*import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;*/
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Date;
import java.util.List;

public class SpotARActivity extends FragmentActivity /*implements OnMapReadyCallback, SensorEventListener, LocationListener, View.OnClickListener*/ {

/*    private GoogleMap mMap;*/

/*    private SensorManager sensorManager;
    private float[] accelerometerValues = new float[3];
    private float[] magneticValues = new float[3];*/

/*    List<Sensor> listMag;
    List<Sensor> listAcc;*/

    private MyDbSpots mDbSpots = null;

    private ARunit arView;

/*    private LocationManager locationManager;
    private GeomagneticField geomagneticField;

    private final static String DB_NAME = "gps_data.db";
    private final static String DB_TABLE = "gps_data";
    private final static int DB_VERSION = 1;
    private SQLiteDatabase db;*/
    Cursor cursor;

    EditText editText;

    private ImageView imageView;
    private Animation animation;

    static int id = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_ar);

        //initData();
/*        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);*/

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 999);

        //arView = new ARunit(this, id);
        //cursor.close();

/*        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        listMag = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        listAcc = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);*/

        setContentView(new Camera1(this));
        //addContentView(arView, new WindowManager.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT));
        //addContentView();

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(5000);
        view.startAnimation(aa);
    }

/*    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        *//*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        *//*
    }*/

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
/*        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        sensorManager.registerListener(this, listMag.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, listAcc.get(0), SensorManager.SENSOR_DELAY_NORMAL);*/
    }

    @Override
    public void onStop() {
        super.onStop();
/*        locationManager.removeUpdates(this);
        sensorManager.unregisterListener(this);*/
    }

/*    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticValues = event.values.clone();
                break;
        }

        if (magneticValues != null && accelerometerValues != null && geomagneticField != null) {
            float[] R = new float[16];
            float[] I = new float[16];

            SensorManager.getRotationMatrix(R, I, accelerometerValues, magneticValues);

            float[] actual_orientation = new float[3];

            SensorManager.getOrientation(R, actual_orientation);

            //float direction = (float) Math.toDegrees(actual_orientation[0]) + geomagneticField.getDeclination();
            //arView.drawScreen(direction, geoPoint);
        }
    }*/

/*    @Override
    public void onLocationChanged(Location arg0) {
        geomagneticField = new GeomagneticField((float) arg0.getLatitude(), (float) arg0.getLongitude(), (float) arg0.getAltitude(), new Date().getTime());
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }*/

/*    public void initData() {
        //cursor
        mDbSpots = new MyDbSpots(getApplicationContext());
        SQLiteDatabase reader = mDbSpots.getReadableDatabase();

        // SELECT
        String[] projection = { // SELECT する列
                MyDbContract.SpotsTable.COL_NAME,
                MyDbContract.SpotsTable.COL_LATITUDE,
                MyDbContract.SpotsTable.COL_LONGITUDE
        };

        String sortOrder = MyDbContract.SpotsTable.COL_ID + " ASC"; // ORDER 句
        Cursor dbcursor = reader.query(
                MyDbContract.SpotsTable.TABLE_NAME, // The table to query
                projection,         // The columns to return
                null,          // The columns for the WHERE clause
                null,      // The values for the WHERE clause
                null,               // don't group the rows
                null,               // don't filter by row groups
                sortOrder           // The sort order
        );
        while (dbcursor.moveToNext()) {
            String name = dbcursor.getString(dbcursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_NAME));
            Float latitude = dbcursor.getFloat(dbcursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_LATITUDE));
            Float longitude = dbcursor.getFloat(dbcursor.getColumnIndexOrThrow(MyDbContract.SpotsTable.COL_LONGITUDE));

        }
        dbcursor.close();
    }*/

/*    @Override
    public void onClick(View v) {
        if (editText.getText().toString().equals("")) {
            Toast.makeText(this, "�e�L�X�g����͂��Ă�������", Toast.LENGTH_LONG).show();
        } *//*else if (geoPoint == null) {
			Toast.makeText(this, "�ʒu��񂪎擾�ł��܂���", Toast.LENGTH_LONG).show();
		}*//* else {
            ContentValues values = new ContentValues();
            values.put("info", editText.getText().toString());
            //values.put("latitude", geoPoint.getLatitudeE6());
            //values.put("longitude", geoPoint.getLongitudeE6());
            arView.MakeTable(cursor);
            cursor.close();
            editText.setText("");
            Toast.makeText(this, "�e�L�X�g���o�^����܂���", Toast.LENGTH_LONG).show();
        }
    }*/

/*    private void presetTable() {
        ContentValues values = new ContentValues();
        values.put("info", "�̈��");
        values.put("latitude", 38276102);
        values.put("longitude", 140752285);
        db.insert(DB_TABLE, "", values);

        values.put("info", "�}����");
        values.put("latitude", 38275709);
        values.put("longitude", 140751810);
        db.insert(DB_TABLE, "", values);

        values.put("info", "���C��");
        values.put("latitude", 38276701);
        values.put("longitude", 140751636);
        db.insert(DB_TABLE, "", values);
    }*/

/*    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public class SQLiteOpenHelperEx extends SQLiteOpenHelper {
        public SQLiteOpenHelperEx(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table if not exists " + DB_TABLE + "(info text, latitude numeric, longitude numeric)";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + DB_TABLE);
            onCreate(db);
        }
    }*/
}