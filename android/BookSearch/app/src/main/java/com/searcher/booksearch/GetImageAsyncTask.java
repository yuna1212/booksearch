package com.searcher.booksearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    /*
     * Image 링크 연결해서 view에 셋해줌
     */

    @Override
    protected Bitmap doInBackground(String... str_url) {
        try {
            URL url = new URL(str_url[0]);
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            return bm;

        } catch (Exception e) {
            return null;
        }
    }

}