package com.ndlp.socialstudy.Notifications;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ndlp on 21.02.2018.
 */

public class UmfragenAsyncTask  extends AsyncTask<String, Integer, String> {

    private String name;

    public UmfragenAsyncTask(String name){
        this.name = name;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("message", "Thema: " + name)
                .add("title", "Neue Umfrage :)")
                .build();

        Request request = new Request.Builder()
                .url("http://hellownero.de/SocialStudy/PHP-Dateien/Notifications/push_notification.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "useles return statement";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
