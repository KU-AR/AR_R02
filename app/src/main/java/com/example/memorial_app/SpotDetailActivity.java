package com.example.memorial_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class SpotDetailActivity extends AppCompatActivity {

    private MyDbHelper mDbHelper = null;
    static String[] detail = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);

        Intent intent = getIntent();
        getImage(intent.getIntExtra("id", 999));
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
    }

    public void onButtonMemoryFloatMain(View v) {
        // 画面指定
        Intent intent = new Intent(this,MemoryFloatMainActivity.class);
        // 画面を開く
        startActivity(intent);
    }

    public void getImage(int id){
        mDbHelper = new MyDbHelper(getApplicationContext());
        SQLiteDatabase reader = mDbHelper.getReadableDatabase();
        //SQLiteDatabase writer = mDbHelper.getWritableDatabase();

        // SELECT
        String[] projection = { // SELECT する列
                MyDbContract.MyTable.COL_NAME,
                MyDbContract.MyTable.COL_DESCRIPTION,
                MyDbContract.MyTable.COL_IMAGES_BIN
        };

        String selection = MyDbContract.MyTable.COL_ID + " = ?"; // WHERE 句
        String[] selectionArgs = { String.valueOf(id) };
        String sortOrder = MyDbContract.MyTable.COL_ID + " ASC"; // ORDER 句
        Cursor cursor = reader.query(
                MyDbContract.MyTable.TABLE_NAME, // The table to query
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
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.MyTable.COL_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.MyTable.COL_DESCRIPTION));
            String image_bin = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.MyTable.COL_IMAGES_BIN));
            detail[0] = name;
            detail[1] = description;
            detail[2] = image_bin;
        }
        cursor.close();
    }
}
