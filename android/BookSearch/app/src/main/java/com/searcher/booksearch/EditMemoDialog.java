package com.searcher.booksearch;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import org.searcher.booksearch.R;

public class EditMemoDialog {
    private Context context;

    public EditMemoDialog(Context context) {
        this.context = context;
    }

    // EditMemoDialog 호출 함수
    public void callEditMemoDialog(final MyBookListItemAdapter adapter, final RecyclerViewEmptySupport recyclerView, final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_memo_dialog);
        dialog.setCancelable(false);    // 화면 밖 터치 시 다이얼로그 종료되지 않도록 설정

        MyBookListItem item = adapter.getItem(position);    // 짧게 클릭된 아이템을 item 변수에 저장
        final String ISBN = item.getISBN();                 // 클릭된 아이템의 ISBN 저장

        // 컴포넌트 연결
        final EditText memo_content = dialog.findViewById(R.id.memoEditText);
        Button cancel = dialog.findViewById(R.id.cancelButton);
        Button save = dialog.findViewById(R.id.saveButton);

        // 기존에 입력해둔 메모 내용이 존재할 경우 edittext에 해당 내용 보여주기
        if(item.getMemo() != null) {
            memo_content.setText(item.getMemo());
        }

        dialog.show();

        // '취소' 버튼을 누른 경우
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();   // 다이얼로그 종료
            }
        });

        // '저장' 버튼을 누른 경우
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memo = memo_content.getText().toString();

                // 리싸이클러뷰에서 수정
                adapter.modifyItem(recyclerView, position, ISBN, memo);
                recyclerView.setAdapter(adapter);

                // 데이터베이스 update
                ManageDatabase manageDatabase = new ManageDatabase(context);
                manageDatabase.updateData(ISBN, memo);

                dialog.dismiss();   // 다이얼로그 종료
            }
        });
    }
}

