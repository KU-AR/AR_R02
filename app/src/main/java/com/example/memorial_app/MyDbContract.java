package com.example.memorial_app;

import android.provider.BaseColumns;

public class MyDbContract {
    private MyDbContract(){}

    public static class SpotsTable implements BaseColumns{
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

    //postsを追加,MyDbPostsをMyDbSpotsをもとに作成
    public static class PostsTable implements BaseColumns{
        public static final String TABLE_NAME = "posts_table";
        public static final String COL_ID = "posts_id";
        public static final String COL_SPOTS_ID = "posts_spots_id";
        public static final String COL_UPDATED_AT = "posts_updated_at";
        public static final String COL_NICKNAME = "posts_nickname";
        public static final String COL_PAST_ADDRESS = "posts_past_address";
        public static final String COL_CURRENT_ADDRESS = "posts_current_address";
        public static final String COL_AGE = "posts_age";
        public static final String COL_JOB = "posts_job";
        public static final String COL_MEMORY = "posts_memory";
        public static final String COL_TIME = "posts_time";
        public static final String COL_EMOTION = "posts_emotion";
        public static final String COL_IMAGES_BIN = "posts_images_bin";
    }
}
