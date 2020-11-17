package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//db
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static com.example.memorial_app.MyDbContract.SpotsTable;
import static com.example.memorial_app.MyDbContract.PostsTable;
import static java.lang.String.valueOf;

//http

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String SPOTS_URL = "http://andolabo.sakura.ne.jp/arproject/show_spot.php";
    private static final String POSTS_URL = "http://andolabo.sakura.ne.jp/arproject/get_memory.php";
    public static int spots_length = 20;

    //private JSONObject json = null;
    private TestTask testTask;
    //private String json_string;

    private Context context = null;

    // DB を操作するためのインスタンス
    private MyDbSpots mDbSpots = null;
    private MyDbPosts mDbPosts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initDB();
        context = getApplicationContext();
        getSpots();
/*
        for(int index = 0; index < spots_length; index++){
            getPosts(index);
        }
*/


        //System.out.println("test");
    }

    //見学スポット取得
    public void getSpots(){
        final String json_input = "{\"test\":\"abc\"}";
        testTask = new TestTask(this);
        testTask.setListener(createListener());
        testTask.execute(SPOTS_URL, json_input);
        Toast.makeText(context, "スポット取得開始", Toast.LENGTH_SHORT).show();

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

    //メモリーフロート取得,スポット一か所分
    public void getPosts(int spot_id){
        final String json_input = "{\"spots_id\":\"" + String.valueOf(spot_id) + "\"}";
        testTask = new TestTask(this);
        testTask.setListener(createListener());
        testTask.execute(POSTS_URL, json_input);
        Toast.makeText(context, "スポットID：" + String.valueOf(spot_id) + "　メモリーフロート取得開始", Toast.LENGTH_SHORT).show();
    }

    public void initDBSpots(String result){

        Toast.makeText(context, "スポットDB作成開始", Toast.LENGTH_SHORT).show();

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

        mDbSpots = new MyDbSpots(getApplicationContext());
        SQLiteDatabase reader = mDbSpots.getReadableDatabase();
        SQLiteDatabase writer = mDbSpots.getWritableDatabase();

        // INSERT
        spots_length = spots.length();
        for(int i = 0; i < spots.length(); i++){
            try {
                JSONObject spot = spots.getJSONObject(valueOf(i+1));
                ContentValues values = new ContentValues();
                values.put(SpotsTable.COL_ID, spot.getString("spots_id"));
                values.put(SpotsTable.COL_UPDATED_AT, spot.getString("spots_updated_at"));
                values.put(SpotsTable.COL_CREATED_AT, spot.getString("spots_created_at"));
                values.put(SpotsTable.COL_NAME, spot.getString("spots_name"));
                values.put(SpotsTable.COL_RUBY, spot.getString("spots_ruby"));
                values.put(SpotsTable.COL_DESCRIPTION, spot.getString("spots_description"));
                values.put(SpotsTable.COL_LATITUDE, spot.getString("spots_latitude"));
                values.put(SpotsTable.COL_LONGITUDE, spot.getString("spots_longitude"));
                values.put(SpotsTable.COL_IMAGES_BIN, spot.getString("spots_images_bin"));
                writer.insert(SpotsTable.TABLE_NAME, null, values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Toast.makeText(context, "スポットDB作成終了", Toast.LENGTH_SHORT).show();

        checkDBSpots();
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

    public void initDBPosts(String json_input, String result){
        //スポットID取得
        String spots_id = null;
        try{
            JSONObject json = new JSONObject(result);
            spots_id = json.getJSONObject("Post_all").getJSONObject("posts").getJSONObject(valueOf(1)).getString("posts_spots_id");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        //debug
/*        Toast.makeText(context, "メモリーフロートDB(" +
                "スポットID:" + spots_id +
                "作成開始", Toast.LENGTH_SHORT).show();*/

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

        mDbPosts = new MyDbPosts(getApplicationContext());
        SQLiteDatabase reader = mDbPosts.getReadableDatabase();
        SQLiteDatabase writer = mDbPosts.getWritableDatabase();

        // INSERT
        int posts_length = posts.length();
        for(int i = 0; i < posts_length; i++){
            try {
                JSONObject post = posts.getJSONObject(valueOf(i+1));
                ContentValues values = new ContentValues();
                values.put(PostsTable.COL_ID, post.getString("posts_id"));
                values.put(PostsTable.COL_SPOTS_ID, spots_id);
                values.put(PostsTable.COL_UPDATED_AT, post.getString("posts_updated_at"));
                values.put(PostsTable.COL_NICKNAME, post.getString("posts_nickname"));
                values.put(PostsTable.COL_PAST_ADDRESS, post.getString("posts_past_address"));
                values.put(PostsTable.COL_CURRENT_ADDRESS, post.getString("posts_current_address"));
                values.put(PostsTable.COL_AGE, post.getString("posts_age"));
                values.put(PostsTable.COL_JOB, post.getString("posts_job"));
                values.put(PostsTable.COL_MEMORY, post.getString("posts_memory"));
                values.put(PostsTable.COL_TIME, post.getString("posts_time"));
                values.put(PostsTable.COL_EMOTION, post.getString("posts_emotion"));
                values.put(PostsTable.COL_IMAGES_BIN, post.getString("posts_images_bin"));
                writer.insert(PostsTable.TABLE_NAME, null, values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //debug
/*        Toast.makeText(context, "メモリーフロートDB(" +
                "スポットID:" + spots_id +
                "作成終了", Toast.LENGTH_SHORT).show();*/
        checkDBPosts();
    }

    public void checkDBSpots(){

        //Toast.makeText(context, "DBチェック開始", Toast.LENGTH_SHORT).show();

        //mDbHelper = new MyDbSpots(getApplicationContext());
        SQLiteDatabase reader = mDbSpots.getReadableDatabase();
        //SQLiteDatabase writer = mDbHelper.getWritableDatabase();


        // SELECT
        String[] projection = { // SELECT する列
                SpotsTable.COL_ID,
                SpotsTable.COL_UPDATED_AT,
                SpotsTable.COL_CREATED_AT,
                SpotsTable.COL_NAME,
                SpotsTable.COL_RUBY,
                SpotsTable.COL_DESCRIPTION,
                SpotsTable.COL_LATITUDE,
                SpotsTable.COL_LONGITUDE,
                SpotsTable.COL_IMAGES_BIN
        };

        String selection = SpotsTable.COL_ID + " = ?"; // WHERE 句
        String[] selectionArgs = { "1" };
        String sortOrder = SpotsTable.COL_ID + " ASC"; // ORDER 句
        Cursor cursor = reader.query(
                SpotsTable.TABLE_NAME, // The table to query
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
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(SpotsTable.COL_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SpotsTable.COL_NAME));
            //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            //System.out.println("id: " + String.valueOf(id) + ", name: " + name);
            Log.d(TAG, "id: " + String.valueOf(id) + ", name: " + name);
        }
        cursor.close();


        for(int index = 1; index <= spots_length; index++){
            getPosts(index);
        }

        //Toast.makeText(context, "DBチェック終了", Toast.LENGTH_SHORT).show();
    }

    public void checkDBPosts(){

        //Toast.makeText(context, "DBチェック開始", Toast.LENGTH_SHORT).show();

        //mDbHelper = new MyDbPosts(getApplicationContext());
        SQLiteDatabase reader = mDbPosts.getReadableDatabase();
        //SQLiteDatabase writer = mDbHelper.getWritableDatabase();


        // SELECT
        String[] projection = { // SELECT する列
                PostsTable.COL_ID,
                PostsTable.COL_SPOTS_ID,
                PostsTable.COL_UPDATED_AT,
                PostsTable.COL_NICKNAME,
                PostsTable.COL_PAST_ADDRESS,
                PostsTable.COL_CURRENT_ADDRESS,
                PostsTable.COL_AGE,
                PostsTable.COL_JOB,
                PostsTable.COL_MEMORY,
                PostsTable.COL_TIME,
                PostsTable.COL_EMOTION,
                PostsTable.COL_IMAGES_BIN
        };

        String selection = PostsTable.COL_SPOTS_ID + " = ?"; // WHERE 句
        String[] selectionArgs = { "10" };
        String sortOrder = PostsTable.COL_ID + " ASC"; // ORDER 句
        Cursor cursor = reader.query(
                PostsTable.TABLE_NAME, // The table to query
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
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(PostsTable.COL_ID));
            String nickname = cursor.getString(cursor.getColumnIndexOrThrow(PostsTable.COL_NICKNAME));
            //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            //System.out.println("id: " + String.valueOf(id) + ", name: " + name);
            //Log.d(TAG, "id: " + String.valueOf(id) + ", nickname: " + nickname);
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
            public void onSuccess(String strPostURL, String json_input, String result) {
                if(strPostURL == SPOTS_URL){
                    //json_string = result;
                    initDBSpots(result);
                }
                else if(strPostURL == POSTS_URL){
                    initDBPosts(json_input, result);
                }

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