package com.searcher.booksearch;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

// 네이버 검색 API 예제 - blog 검색
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ApiHanddling{
    private static final String TAG = "YUNA_DBG";

    final String isbn;

    private String[] tags; // 찾을 태그
    public HashMap<String, String> book_info; // api 요청해서 받은 결과

    public ApiHanddling(String isbn) {
        this.isbn = isbn;

        // 찾을 태그 정하기
        String[] arr = {"title", "author", "image", "publisher", "pubdate", "price", "description"};
        this.tags = arr;

        // api 요청하고 결과 저장: sub thread에서 작업해야함..!
        this.book_info = get_withTag();
    }

    private HashMap<String, String> get_withTag() {
        /*
         * dictionary로 리턴값 지정
         */
        final String clientID = "RwcK5fAttyJmm46CWiu4";
        final String clientSecret = "jeKAQqum7j";
        final StringBuffer sb = new StringBuffer();

        final HashMap<String, String> ret = new HashMap<>();
        final ArrayList<String> request_tags = new ArrayList<>(Arrays.asList(tags));
        try {
            String apiURL = "https://openapi.naver.com/v1/search/book_adv.xml?d_isbn=" + isbn;
            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Naver-Client-Id", clientID);
            conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            Log.d(TAG, "get_withTag: xmlparser 선언 전");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            Log.d(TAG, "get_withTag: xml파서 객체 만듦");
            XmlPullParser xpp = factory.newPullParser();
            String tag;

            // input stream으로부터 xml값 받음
            xpp.setInput(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            xpp.next();
            int eventType = xpp.getEventType();

            int item_count = 0; // 도서 여러 개인 경우 첫번째 항목만 적용시키기 위함
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(item_count == 2) // 첫번째 item은 Naver 헤더, 두번째부터 도서
                    break;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기
                        if (tag.equals("item")){
                            item_count++;
                        }
                        else if (request_tags.contains(tag)) {
                            xpp.next();
                            ret.put(tag, xpp.getText());
                            xpp.next();
                        }
                }

                eventType = xpp.next();

            }
            return ret;

        } catch (Exception e) {
            ret.put("Error", e.toString());
            return ret;

        }

    }

    public String get_title(){
        return book_info.get("title");
    }

    public String get_author(){
        return book_info.get("author");
    }

    public String get_image_link(){
        return book_info.get("image");
    }

    public String get_publisher(){
        return book_info.get("publisher");
    }

    public String get_pubdate(){
        // 출간일 구분자 추가
        StringBuffer pubdate = new StringBuffer();
        pubdate.append(book_info.get("pubdate").substring(0, 4));
        pubdate.append(".");
        pubdate.append(book_info.get("pubdate").substring(4, 6));
        pubdate.append(".");
        pubdate.append(book_info.get("pubdate").substring(6, 8));

        return pubdate.toString();
    }

    public String get_price(){
        return book_info.get("price")+" 원";
    }

    public String get_description(){
        return book_info.get("description");
    }
}


