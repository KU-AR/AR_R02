package com.example.memorial_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class TestTask extends AsyncTask<String, Integer, String> {
    //String json_string;
    private Listener listener;
    ProgressDialog dialog;
    Context context;
    private Activity mActivity;

    public TestTask(Activity activity){
        mActivity = activity;
    }

    //前処理
/*    @Override
    protected void onPreExecute() {
        //URL設定
        Log.d(TAG, "onPreExecute");
*//*        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setMessage("Downloading data...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(true);
        //dialog.setOnCancelListener(this);
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.show();*//*
    }*/
    // 非同期処理
    @Override
    protected String doInBackground(String... params) {
        try {
            String strPostUrl = params[0];
            String json_input = params[1];
            String result = HttpSendJSON.postJson(strPostUrl, json_input);
            Log.d("debug","accessed "+params[0]);
            return result;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        return "error";
    }

    // 途中経過をメインスレッドに返す
    @Override
    protected void onProgressUpdate(Integer... progress) {
/*        if (listener != null) {
            listener.onSuccess(progress[0]);
        }*/
    }

    @Override
    protected void onCancelled(){
        System.out.println("Cancelled");
    }

    // 非同期処理が終了後、結果をメインスレッドに返す
    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            listener.onSuccess(result);
        }
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onSuccess(String result);
    }
}