package com.searcher.booksearch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.searcher.booksearch.R;

public class bookinfo_more_activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 팝업 타이틀 제거
        setContentView(R.layout.activity_bookinfo_more_activity);


        // 내용 작성
        Intent intent = getIntent();
        TextView detail_title = findViewById(R.id.book_detail_title);
        TextView detail_content = findViewById(R.id.book_detail_content);

        String first_sentence = intent.getStringExtra("detail_title");
        
        if(first_sentence != null)
            detail_title.setText(first_sentence); // 첫 문장 set
        else {
            // view 객체 삭제
            LinearLayout content_parent_layout = findViewById(R.id.content_parent);
            TextView title_view = findViewById(R.id.book_detail_title);
            View title_line = findViewById(R.id.view1);
            content_parent_layout.removeView(title_view);
            content_parent_layout.removeView(title_line);
        }
        detail_content.setText(intent.getStringExtra("detail_content")); // 내용 set

        Button close_button = findViewById(R.id.close_button);

        close_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}