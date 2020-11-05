package com.searcher.booksearch;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.searcher.booksearch.R;

public class MyBookList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_book_list_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        RecyclerView recyclerView = findViewById(R.id.recyclerView2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        MyBookListItemAdapter adapter = new MyBookListItemAdapter();

        // 테스트
        adapter.addItem(new MyBookListItem("테스트1"));
        adapter.addItem(new MyBookListItem("테스트2"));
        adapter.addItem(new MyBookListItem("테스트3"));
        adapter.addItem(new MyBookListItem("테스트4"));

        recyclerView.setAdapter(adapter);
    }
}
