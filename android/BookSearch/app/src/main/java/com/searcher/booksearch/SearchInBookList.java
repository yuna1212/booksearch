package com.searcher.booksearch;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.searcher.booksearch.R;

public class SearchInBookList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_book_list_search);

        // 액션바 숨김
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Context context = this;

        // 컴포넌트 연결
        final RecyclerViewEmptySupport recyclerView = (RecyclerViewEmptySupport) findViewById(R.id.recyclerView3);
        final EditText editText = findViewById(R.id.searchTerm);
        ImageButton button = findViewById(R.id.searchInListButton3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setEmptyView(findViewById(R.id.noticeEmptyText3));
        final MyBookListItemAdapter adapter = new MyBookListItemAdapter();

        // 검색어 입력하고 엔터키 누를 경우 - 검색 실행
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchInList(editText, adapter, recyclerView);
                return true;
            }
        });

        // 검색어 입력하고 돋보기 이미지 버튼 클릭할 경우 - 검색 실행
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInList(editText, adapter, recyclerView);
            }
        });

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
                        // 데이터베이스에서 delete
                        ManageDatabase manageDatabase = new ManageDatabase(context);
                        manageDatabase.deleteData(item.getISBN());
                        Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show();

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

    // 입력받은 조건과 일치하는 항목이 데이터베이스에 존재하는지 확인하고 결과를 리싸이클러뷰에 출력하는 메소드
    public void searchInList(EditText editText, MyBookListItemAdapter adapter, RecyclerViewEmptySupport recyclerView) {
        // 사용자로부터 입력받은 문자열을 input에 저장
        String input = editText.getText().toString().replaceAll("\\p{Z}", "");

        // 검색 조건과 일치하는 데이터를 cursor에 저장
        ManageDatabase manageDatabase = new ManageDatabase(this);
        Cursor cursor = manageDatabase.searchData(input);   // 입력받은 검색어를 포함하는 데이터 저장
        int record_count = cursor.getCount();    // 조건에 일치하는 데이터의 개수 저장

        // 사용자가 공백 입력 또는 아무것도 입력하지 않고 검색을 눌렀을 경우 해당하는 항목이 없다는 텍스트뷰 보여줌
        if(input.getBytes().length <= 0) {
            adapter.removeItems();  // clear adapter
            recyclerView.setAdapter(adapter);
        }

        // 조건에 일치하는 데이터가 없을 경우 해당하는 항목이 없다는 텍스트뷰 보여줌
        else if (record_count == 0) {
            adapter.removeItems();  // clear adapter
            recyclerView.setAdapter(adapter);
        }

        // 조건에 일치하는 항목이 있을 경우 리싸이클러뷰 사용하여 항목 보여줌
        else {
            // MyBookListItem 객체 생성하여 카드뷰에 설정한 후 리싸이클러뷰에 add
            adapter.removeItems();  // clear adapter

            for (int i = 0; i < record_count; i++) {
                cursor.moveToNext();
                adapter.addItem(new MyBookListItem(cursor.getString(0), cursor.getString(1)));
            }

            cursor.close();
            recyclerView.setAdapter(adapter);
        }
    }
}
