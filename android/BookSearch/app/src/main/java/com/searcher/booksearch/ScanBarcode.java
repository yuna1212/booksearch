package com.searcher.booksearch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.searcher.booksearch.R;

public class ScanBarcode extends AppCompatActivity {

    public static final int REQUEST_CODE_GO_TO_MY_BOOK_LIST = 105;

    private String ISBN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result_activity);

        // 액션바 숨김
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 바코드 스캔
        IntentIntegrator barcodeScan = new IntentIntegrator(this);
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
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_GO_TO_MY_BOOK_LIST); //REQUEST_CODE_관심도서
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {  // 취소 누른 경우 메뉴 선택 페이지로 이동
                Intent intent = new Intent(getApplicationContext(), SelectMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                ISBN = result.getContents();    // 바코드 스캔 결과값을 ISBN 변수에 저장

                // 리뷰 크롤링
                WebView search_webView = findViewById(R.id.searchWebsite);
                TextView review_textView = findViewById(R.id.bookReviews);
                CrawlingReviews reviews = new CrawlingReviews(search_webView, review_textView);
                reviews.runCrawling(ISBN);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}