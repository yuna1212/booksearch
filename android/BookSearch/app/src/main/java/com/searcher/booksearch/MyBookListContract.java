package com.searcher.booksearch;

import android.provider.BaseColumns;

// 데이터베이스에 필요한 각종 상수들 한번에 모아서 정리하는 클래스
public final class MyBookListContract {
    private MyBookListContract() {
    }

    public static class MyBookListEntry implements BaseColumns {
        public static final String DATABASE_NAME = "booksearch";
        public static final String TABLE_NAME = "my_book_list";
        public static final String COLUMN_ISBN = "ISBN";
        public static final String COLUMN_MEMO = "memo";
        public static final String COLUMN_ADDDATE = "addDate";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        COLUMN_ISBN + " TEXT PRIMARY KEY," +
                        COLUMN_MEMO + " TEXT," +
                        COLUMN_ADDDATE + " TEXT)";
        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
