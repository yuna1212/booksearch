package com.searcher.booksearch;

import android.view.View;

public interface OnMyBookListItemClickListener {
    public void onItemLongClick(MyBookListItemAdapter.ViewHolder holder, View view, int position);
    public void onItemClick(MyBookListItemAdapter.ViewHolder holder, View view, int position);
}
