package com.searcher.booksearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class GetDetailedDescAsyncTask extends AsyncTask<String, Void, String[]> {
    /*
     * 초기 파라메터로 교보문고 책 결과 페이지 웹주소 받음
     * Jsoup을 이용해서 교보문고 페이지 연결
     * 책 상세 설명부분 크롤링
     */
    final String TAG = "YUNA_DBG";
    @Override
    protected String[] doInBackground(String... url) {
        String selector_fullContent = "div.box_detail_article"; // 전체 내용 파싱할 태그
        String selector_firstSenetence = "h3.title_detail_basic2"; // 첫번째 문장 가져오기
        Document doc = null;
        try {
            doc = Jsoup.connect(url[0]).get();

        } catch (IOException e) {
            Log.e(TAG, "parser: ", e);
        }

        Elements full_content = doc.select(selector_fullContent);
        Elements first_sentence = doc.select(selector_firstSenetence);

        // 첫번째 컨텐트씩 가져오면 됨
        // 첫번째 원소는 title, 두번째 원소는 content
        String[] ret = {first_sentence.first().text(), full_content.first().text()};
        return ret;
    }

}