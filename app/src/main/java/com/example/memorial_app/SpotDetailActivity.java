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
import android.widget.TextView;

public class SpotDetailActivity extends AppCompatActivity {

    private MyDbSpots mDbSpots = null;
    static String[] detail = new String[3];

    static int id = 999;

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
        //SpotARActivity未実装
        Intent intent = new Intent(this, SpotARActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
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
}
