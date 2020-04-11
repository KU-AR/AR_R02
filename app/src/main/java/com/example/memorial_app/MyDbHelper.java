package com.example.memorial_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.memorial_app.MyDbContract.MyTable;

public class MyDbHelper extends SQLiteOpenHelper {
    // スキーマに変更があれば VERSION をインクリメントします。
    public static final int DATABASE_VERSION = 1;

    // SQLite ファイル名を指定します。
    public static final String DATABASE_NAME = "DbSpots.db";

    // SQLite ファイルが存在しない場合や VERSION が変更された際に実行する SQL を定義します。
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + MyTable.TABLE_NAME + " (" +
                    MyTable.COL_ID + " INTEGER PRIMARY KEY," +
                    MyTable.COL_UPDATED_AT+ " NUMERIC," +
                    MyTable.COL_CREATED_AT + " NIMERIC," +
                    MyTable.COL_NAME + " TEXT," +
                    MyTable.COL_RUBY + " TEXT," +
                    MyTable.COL_DESCRIPTION + " TEXT," +
                    MyTable.COL_LATITUDE + " REAL," +
                    MyTable.COL_LONGITUDE + " REAL," +
                    MyTable.COL_IMAGES_BIN + " BLOB)";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + MyTable.TABLE_NAME;

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // SQLite ファイルが存在しない場合
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // VERSION が上がった場合に実行されます。本サンプルでは単純に DROP して CREATE し直します。
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // VERSION が下がった場合に実行されます。本サンプルでは単純に DROP して CREATE し直します。
        onUpgrade(db, oldVersion, newVersion);
    }
}
