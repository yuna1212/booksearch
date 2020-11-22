package com.searcher.booksearch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.searcher.booksearch.R;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class bookinfo_from_booklist extends Activity {
    String TAG="YUNA_DBG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 팝업 타이틀 제거
        setContentView(R.layout.activity_bookinfo_from_booklist);

        Log.d("YUNA_DBG", "onCreate: 북리스트 자바");

        // ISBN 꺼내기
        String isbn = getIntent().getStringExtra("isbn");


        // 상세설명의 제목 작성할 텍스트뷰
        // TextView title_description = findViewById(R.id.book_detail_title);
        // 설명 작성할 텍스트뷰
        TextView description = findViewById(R.id.bookIntroContent);
        description.setMovementMethod(new ScrollingMovementMethod());
        // 리뷰 작성할 텍스트뷰
        TextView review_textView = findViewById(R.id.bookReviews);
        review_textView.setMovementMethod(new ScrollingMovementMethod());

        // 교보문고 책 상세 설명 크롤링 해옴
        GetDetailedDescAsyncTask detail_asynctask = new GetDetailedDescAsyncTask();

        String[] detail_info = null;
        try {

            detail_info = detail_asynctask.execute("http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=" + isbn + "&orderClick=LEa&Kc=").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        description.setText(detail_info[0]+"\n");
        description.append(detail_info[1]);


        // 리뷰 크롤링
        WebView search_webView = findViewById(R.id.searchWebsite);
        CrawlingReviews reviews = new CrawlingReviews(search_webView, review_textView);
        reviews.runCrawling(isbn);


    }
}