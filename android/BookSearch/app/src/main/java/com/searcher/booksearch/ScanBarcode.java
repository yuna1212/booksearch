package com.searcher.booksearch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.searcher.booksearch.R;

import java.util.concurrent.ExecutionException;

public class ScanBarcode extends AppCompatActivity {

    public static final int REQUEST_CODE_GO_TO_MY_BOOK_LIST = 105;

    private String ISBN;
    private String title;
    private String author;
    Context context = this;

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
        barcodeScan.setPrompt("도서 뒷면의 바코드를 사각형 안에 비춰주세요.");  //바코드 안의 텍스트 설정
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

        // 내 관심 도서 목록에 해당 도서 추가하는 button
        final Button addToListButton = (Button) findViewById(R.id.addToListButton);
        addToListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ManageDatabase manageDatabase = new ManageDatabase(context);

                // 해당 도서가 데이터베이스에 존재하는지 검사
                if (manageDatabase.isDataExist(ISBN)) {     // 존재할 경우
                    manageDatabase.deleteData(ISBN);    // 데이터베이스에서 해당 도서 삭제
                    // 빈 하트로 설정
                    addToListButton.setBackgroundResource(R.drawable.empty_heart);

                    Toast.makeText(context, "내 관심 도서 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }

                else {      // 존재하지 않을 경우
                    manageDatabase.insertData(ISBN, title, author);    // 데이터베이스에 해당 도서 추가
                    // 채워진 하트 설정
                    addToListButton.setBackgroundResource(R.drawable.full_heart);

                    Toast.makeText(context, "내 관심 도서 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Button addToListButton = (Button) findViewById(R.id.addToListButton);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {  // 취소 누른 경우 메뉴 선택 페이지로 이동
                Intent intent = new Intent(getApplicationContext(), SelectMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                ISBN = result.getContents();    // 바코드 스캔 결과값을 ISBN 변수에 저장

                // 해당 도서가 데이터베이스에 존재하는지 검사
                ManageDatabase manageDatabase = new ManageDatabase(context);

                if (manageDatabase.isDataExist(ISBN)) {     // 존재할 경우, 채워진 하트
                    addToListButton.setBackgroundResource(R.drawable.full_heart);
                }
                else {      // 존재하지 않을 경우, 빈 하트
                    addToListButton.setBackgroundResource(R.drawable.empty_heart);
                }

                // 네이버 API 요청, 도서 상세 페이지 쓰기
               try {
                    final BookInformation book = new BookInformation(ISBN); // final로 쓰는게 맞나...

                    // 책 정보 작성할 컴포넌트 찾기
                    TextView bookTitle = findViewById(R.id.bookTitle1);
                    TextView bookAuthor1 = findViewById(R.id.bookAuthor1);
                    TextView bookPublisher1 = findViewById(R.id.bookPublisher1);
                    TextView bookPublishedDate1 = findViewById(R.id.bookPublishedDate1);
                    TextView bookPrice1 = findViewById(R.id.bookPrice1);
                    TextView bookIntroContent = findViewById(R.id.bookIntroContent);
                    ImageView bookCover1 = findViewById(R.id.bookCover1);

                    // 책 정보 컴포넌트에 작성
                    book.set_title_on_component(bookTitle);
                    book.set_author_on_component(bookAuthor1);
                    book.set_publisher_on_component(bookPublisher1);
                    book.set_pubdate_on_component(bookPublishedDate1);
                    book.set_price_on_component(bookPrice1);
                    book.set_description_on_component(bookIntroContent);
                    book.set_image_on_component(bookCover1);

                   // 내 관심 도서 목록에 추가할 때 데이터베이스에 저장해야 되는 내용들 변수에 저장
                   title = book.getBookInfo().get("title");
                   author = book.getBookInfo().get("author");

                   // 책 소개 자세히를 눌렀다면 책 상세 페이지 팝업창 띄우기
                   // 이렇게 try catch에 다 넣어놔도 되나 모르겠다
                   TextView bookInfoDetail = findViewById(R.id.bookInfoDetail);
                   bookInfoDetail.setOnClickListener(new View.OnClickListener(){
                       @Override
                       public void onClick(View v) {
                           // 교보문고 책 상세 설명 크롤링 해옴
                           GetDetailedDescAsyncTask detail_asynctask = new GetDetailedDescAsyncTask();
                           String[] detail_info  = null;
                           try {
                               detail_info = detail_asynctask.execute("http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=" + ISBN + "&orderClick=LEa&Kc=").get();
                           } catch (ExecutionException e) {
                               e.printStackTrace();
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }

                           // 상세 설명 인텐트에 넣어서 팝업창에 보내기
                           Intent intent = new Intent(getApplicationContext(), bookinfo_more_activity.class);
                           intent.putExtra("detail_title", detail_info[0]); // 첫 문장(제목) 감싸기
                           intent.putExtra("detail_content", detail_info[1]); // 첫 문장 제외 내용 감싸기
                           startActivity(intent);
                       }
                   });

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }




                // 리뷰 크롤링
                WebView search_webView = findViewById(R.id.searchWebsite);
                TextView review_textView = findViewById(R.id.bookReviews);
                review_textView.setMovementMethod(new ScrollingMovementMethod());
                CrawlingReviews reviews = new CrawlingReviews(search_webView, review_textView);
                reviews.runCrawling(ISBN);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}