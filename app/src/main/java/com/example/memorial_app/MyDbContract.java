package com.example.memorial_app;

import android.provider.BaseColumns;

public class MyDbContract {
    private MyDbContract(){}

    public static class MyTable implements BaseColumns{
        public static final String TABLE_NAME = "spots_table";
        public static final String COL_ID = "spots_id";
        public static final String COL_UPDATED_AT = "spots_updated_at";
        public static final String COL_CREATED_AT = "spots_created_at";
        public static final String COL_NAME = "spots_name";
        public static final String COL_RUBY = "spots_ruby";
        public static final String COL_DESCRIPTION = "spots_description";
        public static final String COL_LATITUDE = "spots_latitude";
        public static final String COL_LONGITUDE = "spots_longitude";
        public static final String COL_IMAGES_BIN = "spots_images_bin";
    }
}
