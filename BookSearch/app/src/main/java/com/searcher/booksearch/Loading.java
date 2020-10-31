package com.searcher.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.searcher.booksearch.R;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        startLoading();
    }

    //로딩화면 구현
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent); //로딩끝난 후, Login으로 넘어감
                finish();
            }
        },2000);
    }

    protected void onPause() {
        super.onPause();
        finish();
    }
}