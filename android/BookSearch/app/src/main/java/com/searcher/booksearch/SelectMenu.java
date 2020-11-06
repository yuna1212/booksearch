package com.searcher.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.searcher.booksearch.R;

public class SelectMenu extends AppCompatActivity {
    // REQUEST_CODE_
    public static final int REQUEST_CODE_바코드로_도서찾기 = 102;
    public static final int REQUEST_CODE_관심도서 = 103;
    public static final int REQUEST_CODE_LOGOUT = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectmenu_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 바코드로 도서 검색 button
        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScanBarcode.class); //관심 도서.class연결
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_바코드로_도서찾기); //REQUEST_CODE_관심도서
            }
        });


        // 내 관심 도서 button
        Button myLibButton = (Button) findViewById(R.id.myLibButton);
        myLibButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyBookList.class); //관심 도서.class연결
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_관심도서); //REQUEST_CODE_관심도서
            }
        });
        

        // 로그아웃 click 로그인 화면으로 되돌아가기
        TextView logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, REQUEST_CODE_LOGOUT);
            }
        });
    }
}