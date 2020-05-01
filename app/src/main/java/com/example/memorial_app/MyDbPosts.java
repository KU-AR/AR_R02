package com.example.memorial_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.memorial_app.MyDbContract.PostsTable;

public class MyDbPosts extends SQLiteOpenHelper {
    // スキーマに変更があれば VERSION をインクリメントします。
    public static final int DATABASE_VERSION = 1;

    // SQLite ファイル名を指定します。
    public static final String DATABASE_NAME = "DbPosts.db";

    // SQLite ファイルが存在しない場合や VERSION が変更された際に実行する SQL を定義します。
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + PostsTable.TABLE_NAME + " (" +
                    PostsTable.COL_ID + " INTEGER PRIMARY KEY," +
                    PostsTable.COL_SPOTS_ID + " INTEGER," +
                    PostsTable.COL_UPDATED_AT+ " NUMERIC," +
                    PostsTable.COL_NICKNAME + " TEXT," +
                    PostsTable.COL_PAST_ADDRESS + " TEXT," +
                    PostsTable.COL_CURRENT_ADDRESS + " TEXT," +
                    PostsTable.COL_AGE + " INT," +
                    PostsTable.COL_JOB + " TEXT," +
                    PostsTable.COL_MEMORY + " TEXT," +
                    PostsTable.COL_TIME + " TEXT," +
                    PostsTable.COL_EMOTION + " TEXT," +
                    PostsTable.COL_IMAGES_BIN + " BLOB)";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + PostsTable.TABLE_NAME;

    public MyDbPosts(Context context) {
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
