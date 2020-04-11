package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

//db
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.Task;

import static com.example.memorial_app.MyDbContract.MyTable;
import static java.lang.String.valueOf;

//http
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //private JSONObject json = null;
    private TestTask testTask;
    //private String json_string;

    private Context context = null;

    // DB を操作するためのインスタンス
    private MyDbHelper mDbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initDB();
        context = getApplicationContext();
        getSpot();


        //System.out.println("test");
    }

    //見学スポット取得
    public void getSpot(){
        final String strPostUrl = "http://andolabo.sakura.ne.jp/arproject/show_spot.php";
        final String json_input = "{\"test\":\"abc\"}";
        testTask = new TestTask(this);
        testTask.setListener(createListener());
        testTask.execute(strPostUrl, json_input);
        Toast.makeText(context, "スポット入手開始", Toast.LENGTH_SHORT).show();

/*        while(json_string == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
/*        try{
            JSONObject json_output = new JSONObject(json_string);
            //System.out.println(json_output.toString());
            JSONObject spots = json_output.getJSONObject("Spot_all").getJSONObject("spots");
            //System.out.println(spots.getJSONObject("1"));
            //System.out.println(spots.getJSONObject("1").getString("spots_images_bin"));
            return spots;
        }
        catch(JSONException e){
            System.out.println(e);
        }*/
    }

    public void initDB(String result){

        Toast.makeText(context, "DB作成開始", Toast.LENGTH_SHORT).show();

        //データ取得処理
        //json = getSpot();
        JSONObject spots = null;
        try{
            JSONObject json = new JSONObject(result);
            spots = json.getJSONObject("Spot_all").getJSONObject("spots");
        }
        catch(JSONException e) {
            return;
        }

        mDbHelper = new MyDbHelper(getApplicationContext());
        SQLiteDatabase reader = mDbHelper.getReadableDatabase();
        SQLiteDatabase writer = mDbHelper.getWritableDatabase();

        // INSERT
        for(int i = 0; i < spots.length(); i++){
            try {
                JSONObject spot = spots.getJSONObject(valueOf(i+1));
                ContentValues values = new ContentValues();
                values.put(MyTable.COL_ID, spot.getString("spots_id"));
                values.put(MyTable.COL_UPDATED_AT, spot.getString("spots_updated_at"));
                values.put(MyTable.COL_CREATED_AT, spot.getString("spots_created_at"));
                values.put(MyTable.COL_NAME, spot.getString("spots_name"));
                values.put(MyTable.COL_RUBY, spot.getString("spots_ruby"));
                values.put(MyTable.COL_DESCRIPTION, spot.getString("spots_description"));
                values.put(MyTable.COL_LATITUDE, spot.getString("spots_latitude"));
                values.put(MyTable.COL_LONGITUDE, spot.getString("spots_longitude"));
                values.put(MyTable.COL_IMAGES_BIN, spot.getString("spots_images_bin"));
                writer.insert(MyTable.TABLE_NAME, null, values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Toast.makeText(context, "DB作成終了", Toast.LENGTH_SHORT).show();

        checkDB();
        //System.out.println(checkDB());

/*        // SELECT
        String[] projection = { // SELECT する列
                MyTable._ID,
                MyTable.COLUMN_NAME_INT_COL,
                MyTable.COLUMN_NAME_STR_COL
        };
        String selection = MyTable.COLUMN_NAME_INT_COL + " = ?"; // WHERE 句
        String[] selectionArgs = { "123" };
        String sortOrder = MyTable.COLUMN_NAME_STR_COL + " DESC"; // ORDER 句
        Cursor cursor = reader.query(
                MyTable.TABLE_NAME, // The table to query
                projection,         // The columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // don't group the rows
                null,               // don't filter by row groups
                sortOrder           // The sort order
        );
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MyTable._ID));
            String str = cursor.getString(cursor.getColumnIndexOrThrow(MyTable.COLUMN_NAME_STR_COL));
            Log.d(TAG, "id: " + String.valueOf(id) + ", str: " + str);
        }
        cursor.close();

        // DELETE
        String deleteSelection = MyTable._ID + " > ?"; // WHERE 句
        String[] deleteSelectionArgs = { "5" };
        writer.delete(MyTable.TABLE_NAME, deleteSelection, deleteSelectionArgs);

        // UPDATE
        ContentValues updateValues = new ContentValues();
        updateValues.put(MyTable.COLUMN_NAME_STR_COL, "bbb");
        String updateSelection = MyTable.COLUMN_NAME_STR_COL + " = ?";
        String[] updateSelectionArgs = { "aaa" };
        writer.update(
                MyTable.TABLE_NAME,
                updateValues,
                updateSelection,
                updateSelectionArgs);*/
    }

    public void checkDB(){

        //Toast.makeText(context, "DBチェック開始", Toast.LENGTH_SHORT).show();

        //mDbHelper = new MyDbHelper(getApplicationContext());
        SQLiteDatabase reader = mDbHelper.getReadableDatabase();
        //SQLiteDatabase writer = mDbHelper.getWritableDatabase();


        // SELECT
        String[] projection = { // SELECT する列
                MyTable.COL_ID,
                MyTable.COL_UPDATED_AT,
                MyTable.COL_CREATED_AT,
                MyTable.COL_NAME,
                MyTable.COL_RUBY,
                MyTable.COL_DESCRIPTION,
                MyTable.COL_LATITUDE,
                MyTable.COL_LONGITUDE,
                MyTable.COL_IMAGES_BIN
        };

        String selection = MyTable.COL_ID + " = ?"; // WHERE 句
        String[] selectionArgs = { "1" };
        String sortOrder = MyTable.COL_ID + " ASC"; // ORDER 句
        Cursor cursor = reader.query(
                MyTable.TABLE_NAME, // The table to query
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
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MyTable.COL_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MyTable.COL_NAME));
            //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            //System.out.println("id: " + String.valueOf(id) + ", name: " + name);
            Log.d(TAG, "id: " + String.valueOf(id) + ", name: " + name);
        }
        cursor.close();

        //Toast.makeText(context, "DBチェック終了", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){
        testTask.setListener(null);
        super.onDestroy();
    }

    private TestTask.Listener createListener(){
        return new TestTask.Listener() {
            @Override
            public void onSuccess(String result) {
                //json_string = result;
                initDB(result);
            }
        };
    }

    public void onButton1(View v) {
        // 画面指定
        Intent intent = new Intent(this,RouteChoiceActivity.class);
        // 画面を開く
        startActivity(intent);
    }

    public void onButton2(View v) {
        // 画面指定
        Intent intent = new Intent(this,SpotActivity.class);
        // 画面を開く
        startActivity(intent);
    }

    public void onButton3(View v) {
        // 画面指定
        Intent intent = new Intent(this,SettingActivity.class);
        // 画面を開く
        startActivity(intent);
    }

    public void onClose(View v) {
        finish();
    }
}