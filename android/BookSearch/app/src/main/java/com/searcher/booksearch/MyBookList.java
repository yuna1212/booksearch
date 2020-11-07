package com.searcher.booksearch;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.searcher.booksearch.R;

public class MyBookList extends AppCompatActivity {
    Context context = this;
    ManageDatabase manageDatabase = new ManageDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_book_list_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final RecyclerView recyclerView = findViewById(R.id.recyclerView2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final MyBookListItemAdapter adapter = new MyBookListItemAdapter();

        // 데이터베이스에 존재하는 데이터들 카드뷰로 출력
        Cursor cursor = manageDatabase.selectData();
        int recordCount = cursor.getCount();    // 데이터베이스 내에 있는 데이터의 개수 저장

        // MyBookListItem 객체 생성하여 카드뷰에 설정한 후 리싸이클러뷰에 add
        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();
            adapter.addItem(new MyBookListItem(cursor.getString(0), cursor.getString(1)));
        }

        cursor.close();

        recyclerView.setAdapter(adapter);


        // 클릭했을 경우
        adapter.setOnItemClickListener(new OnMyBookListItemClickListener() {

            // 아이템 길게 클릭하는 경우 - 삭제
            @Override
            public void onItemLongClick(MyBookListItemAdapter.ViewHolder holder, View view, final int position) {
                final MyBookListItem item = adapter.getItem(position);    // 길게 클릭된 아이템을 item 변수에 저장

                // 알림창 표시
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("항목 삭제").setMessage("해당 항목을 내 관심 도서 목록에서 삭제하시겠습니까?");

                // '예'를 선택한 경우 - 항목 삭제
                builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // 데이터베이스에서 delete
                        ManageDatabase manageDatabase = new ManageDatabase(context);
                        manageDatabase.deleteData(item.getISBN());
                        Toast.makeText(context, "삭제 완료", Toast.LENGTH_LONG).show();

                        // 리싸이클러뷰에서 삭제
                        adapter.removeItem(recyclerView, position);
                        recyclerView.setAdapter(adapter);
                    }
                });

                // '아니오'를 선택한 경우
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            // 메모 내용 텍스트뷰를 짧게 클릭하는 경우 - 메모 작성
            @Override
            public void onItemClick(MyBookListItemAdapter.ViewHolder holder, View view, final int position) {
                final MyBookListItem item = adapter.getItem(position);    // 짧게 클릭된 아이템을 item 변수에 저장
                final String ISBN = item.getISBN();

                // 입력창 뜨도록
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final EditText input = new EditText(context);

                builder.setTitle("메모");
                builder.setMessage("메모를 입력해주세요.");
                builder.setView(input);
                builder.setPositiveButton("저장",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String memo = input.getText().toString();

                                // 데이터베이스 update
                                ManageDatabase manageDatabase = new ManageDatabase(context);
                                manageDatabase.updateData(ISBN, memo);

                                // 리싸이클러뷰에서 수정
                                adapter.modifyItem(recyclerView, position, ISBN, memo);
                                recyclerView.setAdapter(adapter);
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}

