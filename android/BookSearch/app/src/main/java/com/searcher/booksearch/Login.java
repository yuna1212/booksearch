package com.searcher.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.searcher.booksearch.R;

public class Login extends AppCompatActivity {

    public static final int REQUEST_CODE_SELETMENU = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //loginButton 클릭했을 때, selectmenu로 전환
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectMenu.class);
                startActivityForResult(intent, REQUEST_CODE_SELETMENU);
            }
        });
    }
}