package com.example.memorial_app;

//ARの表示を行う
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class ARunit extends View{

    int dire;
    int lon;
    int lat;

    private int ang = 30;
    private float range = 5;
    private int dis;
    private ArrayList<ARData> list;

    private MyDbSpots mDbSpots = null;

    public ARunit(Context context, int id) {
        super(context);

        MakeTable(id);
        Display DX = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        dis = DX.getWidth();
    }

    public void MakeTable(int id) {

       // mDbSpots = new MyDbSpots(getApplicationContext());
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
    }

    class ARData {
        public String info;
        public int latitude;
        public int longitude;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        for (int i = 0; i < list.size(); i++) {

            ARData data = list.get(i);
            String info = data.info;
            int y = data.latitude;
            int x = data.longitude;

            double dx = (x - lon);
            double dy = (y - lat);
            float distance = (float) Math.sqrt(Math.pow(dy, 2) + Math.pow(dx, 2));

            if (distance > 300) {
                continue;
            }

            double angle = Math.atan2(dy, dx);
            float degree = (float) Math.toDegrees(angle);
            degree = -degree + 90;
            if (degree < 0) degree = 360 + degree;

            float sub = degree - dire;
            if (sub < -180.0) sub += 360;
            if (sub > 180.0) sub -= 360;

            if (Math.abs(sub) < (30)) {

                float textSize = 50*(float) (range - distance) / range;
                paint.setTextSize(textSize);

                float textWidth = paint.measureText(info);
                float diff = (sub / (30)) / 2;
                float left = (dis / 2 + dis * diff) - (textWidth / 2);
                drawBalloonText(canvas, paint, info, left, 55);
            }
        }
    }

    private void drawBalloonText(Canvas canvas, Paint paint, String text, float left, float top) {
        float textWidth = paint.measureText(text);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        float bLeft = left - 5;
        float bRight = left + textWidth + 5;
        float bTop = top + fontMetrics.ascent - 5;
        float bBottom = top + fontMetrics.descent + 5;

        RectF rectF = new RectF(bLeft, bTop, bRight, bBottom);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF, 5, 5, paint);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();
        float center = left + textWidth / 2;
        float triangleSize = paint.getTextSize() / 3;
        path.moveTo(center, bBottom + triangleSize);
        path.lineTo(center - triangleSize / 2, bBottom - 1);
        path.lineTo(center + triangleSize / 2, bBottom - 1);
        path.lineTo(center, bBottom + triangleSize);
        canvas.drawPath(path, paint);

        paint.setColor(Color.BLACK);
        canvas.drawText(text, left, top, paint);
    }
}
