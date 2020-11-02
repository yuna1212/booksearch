package com.searcher.booksearch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.searcher.booksearch.R;

import java.util.List;

public class ScanBarcode extends AppCompatActivity {
    private IntentIntegrator barcodeScan;
    Review[] reviews;
    private WebView webView;
    TextView textView;
    String ISBN = "";
    String source = "";
    String all_reviews = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_barcode_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 바코드 스캔
        barcodeScan = new IntentIntegrator(this);
        // 휴대폰 방향에 따라 가로, 세로로 자동 변경
        barcodeScan.setOrientationLocked(false);
        //바코드 안의 텍스트
        barcodeScan.setPrompt("도서 뒷면의 바코드를 사각형 안에 비춰주세요");
        //바코드 인식시 소리 여부
        barcodeScan.setBeepEnabled(false);

        barcodeScan.initiateScan();
    }

    private class WebViewClientClass extends WebViewClient { // 페이지 이동시 새창으로 안뜨게
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);");
        }
    }

    public class WebViewJavascriptInterface {
        @JavascriptInterface
        public void getHtml(String html) {
            source = html;

            Document doc = Jsoup.parse(source);

            List<Element> elements_content = doc.select(".simple_review_list").select("p[name=memo]");
            List<Element> elements_id = doc.select(".simple_review_info").select("strong");
            List<Element> elements_date = doc.select(".simple_review_info").select("span");

            reviews = new Review[elements_id.size()];

            for(int i = 0; i < reviews.length; i++) {
                reviews[i] = new Review(elements_id.get(i).text(),
                        elements_date.get(4+(i*6)).text(),
                        elements_content.get(i).text());
            }

            for(int i = 0; i < reviews.length; i++) {
                // 확인용 출력
                System.out.println(reviews[i].id + "\t" + reviews[i].date + "\n" + reviews[i].comment);
                System.out.println("------------------------------------------------------------------");

                all_reviews += (reviews[i].id + "    " + reviews[i].date + "\n" + reviews[i].comment + "\n\n");
            }

            // id 매핑
            textView = (TextView)findViewById(R.id.textView1);
            // 텍스트 set
            textView.setText(all_reviews);
            textView.setMovementMethod(new ScrollingMovementMethod());

            //Log.e("jieun2: ",id.text());
            //Log.e("jieun3: ",content.text());

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Intent intent = new Intent(getApplicationContext(), SelectMenu.class);
                startActivity(intent);
            } else {
                ISBN = result.getContents();

                // 리뷰 크롤링
                String url = "http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=" + ISBN + "&orderClick=LEa&Kc=";
                System.out.println(url);
                webView = findViewById(R.id.webView1);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(url); //웹뷰 실행
                webView.setWebChromeClient(new WebChromeClient()); // 웹뷰에 크롬 사용 허용, 이 부분이 없으면 크롬에서 alert 뜨지 않음
                webView.setWebViewClient(new WebViewClientClass()); // 새창열기 없이 웹뷰 내에서 다시 열기, 페이지 이동 원활히 하기 위해 사용
                webView.addJavascriptInterface(new WebViewJavascriptInterface(), "Android");
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}