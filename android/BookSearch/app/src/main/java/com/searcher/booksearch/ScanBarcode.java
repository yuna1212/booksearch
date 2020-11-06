package com.searcher.booksearch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.searcher.booksearch.R;

import java.util.List;

public class ScanBarcode extends AppCompatActivity {

    public static final int REQUEST_CODE_GO_TO_MY_BOOK_LIST = 105;

    private IntentIntegrator barcodeScan;
    private WebView webView;
    private TextView textView;
    private String ISBN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result_activity);

        // 액션바 숨김
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        webView = findViewById(R.id.searchWebsite);
        textView = findViewById(R.id.bookReviews);

        // 바코드 스캔
        barcodeScan = new IntentIntegrator(this);
        barcodeScan.setOrientationLocked(false);    // 휴대폰 방향에 따라 가로, 세로로 자동 변경
        barcodeScan.setPrompt("도서 뒷면의 바코드를 사각형 안에 비춰주세요");  //바코드 안의 텍스트 설정
        barcodeScan.setBeepEnabled(false);  //바코드 인식시 소리 여부
        barcodeScan.initiateScan();

        // 내 관심 도서 목록으로 이동하는 button
        Button goToListButton = (Button) findViewById(R.id.goToListButton);
        goToListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyBookList.class); //관심 도서.class연결
                startActivityForResult(intent, REQUEST_CODE_GO_TO_MY_BOOK_LIST); //REQUEST_CODE_관심도서
            }
        });

    /*
        // 내 관심 도서 목록에 해당 도서 추가하는 button
        ImageButton addToListButton = (ImageButton) findViewById(R.id.addToListButton);
        addToListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String sql = "insert into " + "my_book_list" +
                        "values(" + "'"+ ISBN + "', " + "NULL, " + "'" + "datetime('now')" + "'" + "')";

                MyBookListDatabase database = MyBookListDatabase.getInstance(context);
                database.execSQL(sql);
            }
        });

     */
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {  // 취소 누른 경우 메뉴 선택 페이지로 이동
                Intent intent = new Intent(getApplicationContext(), SelectMenu.class);
                startActivity(intent);
            } else {
                ISBN = result.getContents();    // 바코드 스캔 결과값을 ISBN 변수에 저장

                // 리뷰 크롤링
                CrawlingReviews reviews = new CrawlingReviews(webView, textView);
                reviews.runCrawling(ISBN);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}