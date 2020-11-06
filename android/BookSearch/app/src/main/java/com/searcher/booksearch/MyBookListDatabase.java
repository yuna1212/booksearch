package com.searcher.booksearch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyBookListDatabase {
    private static final String TAG = "MyBookListDatabase";

    private static MyBookListDatabase database;
    public static int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    private MyBookListDatabase(Context context) {
        this.context = context;
    }

    // 인스턴스 가져오기
    public static MyBookListDatabase getInstance(Context context) {
        if (database == null) {
            database = new MyBookListDatabase(context);
        }

        return database;
    }

    // 데이터베이스 열기
    public boolean open() {
        println("opening database [" + MyBookListContract.MyBookListEntry.DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    // 데이터베이스 닫기
    public void close() {
        println("closing database [" + MyBookListContract.MyBookListEntry.DATABASE_NAME + "].");
        db.close();

        database = null;
    }

    // 쿼리 실행 시 사용하는 메소드 - 데이터 조회
    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
            println("cursor count : " + c1.getCount());
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return c1;
    }

    // 쿼리 실행 시 사용하는 메소드
    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");

        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }

        return true;
    }


    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, MyBookListContract.MyBookListEntry.DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            println("creating database [" + MyBookListContract.MyBookListEntry.DATABASE_NAME + "].");

            // TABLE_NOTE
            println("creating table [" + MyBookListContract.MyBookListEntry.TABLE_NAME + "].");

            // drop existing table
            try {
                db.execSQL(MyBookListContract.MyBookListEntry.SQL_DROP_TABLE);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            // create table
            try {
                db.execSQL(MyBookListContract.MyBookListEntry.SQL_CREATE_TABLE);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }
        }

        public void onOpen(SQLiteDatabase db) {
            println("opened database [" + MyBookListContract.MyBookListEntry.DATABASE_NAME + "].");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }
    }

    private void println(String msg) {
        Log.d(TAG, msg);
    }
}