package com.searcher.booksearch;

import android.database.Cursor;
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

        // 데이터베이스에 존재하는 데이터들 카드뷰로 출력
        ManageDatabase manageDatabase = new ManageDatabase(this);
        Cursor cursor = manageDatabase.selectData();

        int recordCount = cursor.getCount();    // 데이터베이스 내에 있는 데이터 개수 저장

        // 현재 ISBN 정보 보이도록 구현해둔 상태
        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();
            adapter.addItem(new MyBookListItem(cursor.getString(0)));
        }

        cursor.close();

        // 테스트
        adapter.addItem(new MyBookListItem("테스트1"));
        adapter.addItem(new MyBookListItem("테스트2"));

        recyclerView.setAdapter(adapter);
    }
}
