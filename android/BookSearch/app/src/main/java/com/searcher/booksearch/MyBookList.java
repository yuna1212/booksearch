package com.searcher.booksearch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.searcher.booksearch.R;

public class MyBookList extends AppCompatActivity {

    public static final int REQUEST_CODE_SEARCH_IN_MY_BOOK_LIST = 106;

    Context context = this;
    ManageDatabase manageDatabase = new ManageDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_book_list_activity);

        // 액션바 숨김
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setResult(REQUEST_CODE_SEARCH_IN_MY_BOOK_LIST);

        final RecyclerViewEmptySupport recyclerView = (RecyclerViewEmptySupport) findViewById(R.id.recyclerView2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setEmptyView(findViewById(R.id.noticeEmptyText2));
        final MyBookListItemAdapter adapter = new MyBookListItemAdapter();

        // 데이터베이스에 존재하는 데이터들 카드뷰로 출력
        Cursor cursor = manageDatabase.getAllData();
        int record_count = cursor.getCount();    // 데이터베이스 내에 있는 데이터의 개수 저장

        // MyBookListItem 객체 생성하여 카드뷰에 설정해준 후 리싸이클러뷰에 add
        for (int i = 0; i < record_count; i++) {
            cursor.moveToNext();
            adapter.addItem(new MyBookListItem(cursor.getString(0), cursor.getString(1)));
        }

        cursor.close();
        recyclerView.setAdapter(adapter);


        // 아이템 클릭했을 경우
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
                        // 리싸이클러뷰에서 삭제
                        adapter.removeItem(recyclerView, position);
                        recyclerView.setAdapter(adapter);
                        Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show();

                        // 데이터베이스에서 delete
                        ManageDatabase manageDatabase = new ManageDatabase(context);
                        manageDatabase.deleteData(item.getISBN());
                    }
                });

                // '아니오'를 선택한 경우
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            // 메모 내용 텍스트뷰를 짧게 클릭하는 경우 - 메모 작성
            @Override
            public void onItemClick(MyBookListItemAdapter.ViewHolder holder, View view, final int position) {
                // EditMemoDialog 객체 생성하여 dialog 보이기
                EditMemoDialog dialog = new EditMemoDialog(context);
                dialog.callEditMemoDialog(adapter, recyclerView, position);
            }
        });

        // 검색 button
        ImageButton searchInListButton = findViewById(R.id.searchInListButton2);
        searchInListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchInBookList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_SEARCH_IN_MY_BOOK_LIST);
            }
        });
    }

    //검색결과에서 삭제(수정)했을 경우, booklist에 결과반영
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 107){
            finish();
            startActivity(getIntent());
        }
    }
}

