package com.searcher.booksearch;

import android.text.method.ScrollingMovementMethod;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class CrawlingReviews {
    private Review[] reviews;
    private WebView webView;
    private TextView textView;
    private String source = "";
    private String all_reviews = "";

    public CrawlingReviews(WebView webView, TextView textView) {
        this.webView = webView;
        this.textView = textView;
    }

    public void runCrawling(String ISBN) {
        String url = "http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=" + ISBN + "&orderClick=LEa&Kc=";

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url); //웹뷰 실행
        webView.setWebChromeClient(new WebChromeClient()); // 웹뷰에 크롬 사용 허용
        webView.setWebViewClient(new WebViewClientClass()); // 새창열기 없이 웹뷰 내에서 다시 열기, 페이지 이동 원활히 하기 위해 사용
        webView.addJavascriptInterface(new WebViewJavascriptInterface(), "Android");
    }

    // 페이지 이동시 새창으로 안뜨게
    private class WebViewClientClass extends WebViewClient {
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
            doCrawling(source);
        }

        public void doCrawling (String source) {
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
                // System.out.println(reviews[i].id + "\t" + reviews[i].date + "\n" + reviews[i].comment);
                // System.out.println("------------------------------------------------------------------");

                all_reviews += (reviews[i].id + "    " + reviews[i].date + "\n" + reviews[i].comment + "\n\n");
            }

            textView.setText(all_reviews);  // 텍스트 set
            textView.setMovementMethod(new ScrollingMovementMethod());  // textView 스크롤 가능하도록

        }
    }

}
