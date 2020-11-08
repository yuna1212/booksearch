package com.searcher.booksearch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.searcher.booksearch.R;

public class bookinfo_more_activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 팝업 타이틀 제거
        setContentView(R.layout.activity_bookinfo_more_activity);


        // 내용 직성
        Intent intent = getIntent();
        TextView textView = findViewById(R.id.book_detail);
        textView.setText(intent.getStringExtra("url"));

        Button close_button = findViewById(R.id.close_button);
        close_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}