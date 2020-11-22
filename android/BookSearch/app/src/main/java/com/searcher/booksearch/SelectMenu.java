package com.searcher.booksearch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
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

        // 네트워크 연결상태 확인
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if (!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())) {
            new AlertDialog.Builder(this)
                    .setMessage("네트워크 상태가 불안정합니다. 셀룰러 데이터 또는 Wi-fi 연결 후 사용해 주세요.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    }).show();
        }



        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 바코드로 도서 검색 button
        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScanBarcode.class); //관심 도서.class연결
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_바코드로_도서찾기); //REQUEST_CODE_관심도서
            }
        });


        // 내 관심 도서 button
        Button myLibButton = (Button) findViewById(R.id.myLibButton);
        myLibButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyBookList.class); //관심 도서.class연결
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_관심도서); //REQUEST_CODE_관심도서
            }
        });
    }
}