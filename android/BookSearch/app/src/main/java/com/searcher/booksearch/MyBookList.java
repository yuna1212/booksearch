package com.searcher.booksearch;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import org.searcher.booksearch.R;

public class MyBookList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_book_list_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
