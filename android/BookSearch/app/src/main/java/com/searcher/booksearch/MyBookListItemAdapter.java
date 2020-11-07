package com.searcher.booksearch;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.searcher.booksearch.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyBookListItemAdapter extends RecyclerView.Adapter<MyBookListItemAdapter.ViewHolder> implements OnMyBookListItemClickListener {
    ArrayList<MyBookListItem> items = new ArrayList<MyBookListItem>();
    OnMyBookListItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.my_book_list_item, parent, false);

        return new ViewHolder(itemView, this);
    }

    public void setOnItemClickListener(OnMyBookListItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemLongClick(ViewHolder holder, View view, int position) {
        if(listener != null) {
            listener.onItemLongClick(holder, view, position);
        }
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyBookListItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(MyBookListItem item) { items.add(item); }
    public void setItems(ArrayList<MyBookListItem> items) { this.items = items; }
    public MyBookListItem getItem(int position) { return items.get(position); }
    public void setItem(int position, MyBookListItem item) { items.set(position, item); }

    // 항목 삭제
    public void removeItem(RecyclerView recyclerView, int position) {
        items.remove(position);
        recyclerView.removeViewAt(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    // 항목 수정
    public void modifyItem(RecyclerView recyclerView, int position, String ISBN, String memo) {
        items.set(position, new MyBookListItem(ISBN, memo));
        notifyItemChanged(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView cover;
        public TextView title;
        public TextView author;
        public TextView publisher;
        public TextView published_date;
        public TextView price;
        public TextView memo;

        public ViewHolder(View itemView, final OnMyBookListItemClickListener listener) {
            super(itemView);

            cover = (ImageView)itemView.findViewById((R.id.bookCover2));
            title = (TextView)itemView.findViewById(R.id.bookTitle2);
            author = (TextView)itemView.findViewById(R.id.bookAuthor2);
            publisher = (TextView)itemView.findViewById(R.id.bookPublisher2);
            published_date = (TextView)itemView.findViewById(R.id.bookPublishedDate2);
            price = (TextView)itemView.findViewById(R.id.bookPrice2);
            memo = (TextView)itemView.findViewById(R.id.memoContent);

            // 아이템 길게 클릭
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemLongClick(ViewHolder.this, view, position);
                    }

                    return true;   // 여기서 이벤트 처리 끝내면 true, 못 끝내면 false
                }

            });

            // 메모 내용 텍스트뷰 짧게 클릭
            getTextView().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        // 카드뷰 안의 뷰에 정보 넣어주기
        public void setItem(MyBookListItem item) {
            MyBookListItem listItem = item;
            String ISBN = listItem.getISBN();   // 인자로 받은 객체의 ISBN 정보 변수에 저장

            // ISBN 변수 통해 API로 set하는 객체 생성하고 메소드 호출하여 각 뷰의 내용들 지정
            // 네이버 API 요청하여 정보 보이기
            try {
                BookInformation book = new BookInformation(ISBN);

                // 책 정보 컴포넌트에 작성
                book.set_title_on_component(title);
                book.set_author_on_component(author);
                book.set_publisher_on_component(publisher);
                book.set_pubdate_on_component(published_date);
                book.set_price_on_component(price);
                book.set_image_on_component(cover);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            memo.setText(listItem.getMemo());  // 메모 내용 텍스트뷰에 표시
        }

        public TextView getTextView() { return memo; }  // 메모 텍스트뷰 반환 - 클릭 이벤트 처리 위해
    }
}
