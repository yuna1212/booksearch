package com.searcher.booksearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class BookInformation {
    private ApiHanddling api;

    String TAG = "YUNA_DBG"; // logcat 디버깅 태그

    // AsyncTask에서 get을 쓰지 말고 메시지 핸들로 처리하는게 낫다고는 함
    public BookInformation(String isbn) throws ExecutionException, InterruptedException {
        RequestAPIAsyncTask api_task = new RequestAPIAsyncTask();
        this.api = api_task.execute(isbn).get();
        Log.d(TAG, "BookInformation: api로 가져온 book information 확인하기 "+api.book_info.toString());
    }

    // DB작업을 위해 책 information 반환하는 메소드
    public HashMap<String, String> getBookInfo(){
        return api.book_info;
    }

    public void set_title_on_component(TextView textView){
        // title set
        textView.append(this.api.get_title());

    }

    public void set_author_on_component(TextView textView){
        // author set
        textView.append(this.api.get_author());
    }

    public void set_image_on_component(ImageView imageView) throws ExecutionException, InterruptedException {
        // 이미지 링크에서 이미지 가져오기
        GetImageAsyncTask task = new GetImageAsyncTask();
        Bitmap bm_bookcover = task.execute(this.api.get_image_link()).get();
        imageView.setImageBitmap(bm_bookcover);
    }

    public void set_publisher_on_component(TextView textView){
        textView.append(this.api.get_publisher());
    }

    public void set_pubdate_on_component(TextView textView){
        textView.append(this.api.get_pubdate());
    }

    public void set_price_on_component(TextView textView){
        textView.append(this.api.get_price());
    }

    public void set_description_on_component(TextView textView){
        textView.append(this.api.get_description());
    }

    // 나중에 상세 내용 String으로 반환하는 코드로 수정
    public String get_detailContents(){
        return api.get_detailLink();
    }

}
