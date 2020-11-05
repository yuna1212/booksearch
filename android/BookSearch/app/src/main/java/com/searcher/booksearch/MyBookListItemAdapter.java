package com.searcher.booksearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.searcher.booksearch.R;

import java.util.ArrayList;

public class MyBookListItemAdapter extends RecyclerView.Adapter<MyBookListItemAdapter.ViewHolder> {
    ArrayList<MyBookListItem> items = new ArrayList<MyBookListItem>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.my_book_list_item, parent, false);

        return new ViewHolder(itemView);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public TextView textView5;

        public ViewHolder(View view) {
            super(view);

            imageView = (ImageView)view.findViewById((R.id.bookCover2));
            textView1 = (TextView)view.findViewById(R.id.bookTitle2);
            textView2 = (TextView)view.findViewById(R.id.bookAuthor2);
            textView3 = (TextView)view.findViewById(R.id.bookPublisher2);
            textView4 = (TextView)view.findViewById(R.id.bookPublishedDate2);
            textView5 = (TextView)view.findViewById(R.id.bookPrice2);
        }

        public void setItem(MyBookListItem item) {
            imageView.setImageAlpha(item.getCover_image());
            textView1.setText(item.getTitle());
            textView2.setText(item.getAuthor());
            textView3.setText(item.getPublisher());
            textView4.setText(item.getPublished_date());
            textView5.setText(item.getPrice());
        }

    }
}
