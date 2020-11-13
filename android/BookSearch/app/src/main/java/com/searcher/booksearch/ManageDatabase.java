package com.searcher.booksearch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import org.searcher.booksearch.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class ManageDatabase {
    private static final String TAG = "MyBookListDatabase";
    Context context;

    public static MyBookListDatabase database = null;

    // 생성자
    public ManageDatabase(Context context) {
        this.context = context;
    }

    // 데이터베이스 열기 (데이터베이스가 없을 때는 만들기)
    public void openDatabase() {
        // open database
        if (database != null) {
            database.close();
            database = null;
        }

        database = MyBookListDatabase.getInstance(context);
        boolean isOpen = database.open();
        if (isOpen) {
            Log.d(TAG, "database is open.");
        } else {
            Log.d(TAG, "database is not open.");
        }
    }

    // 데이터 삽입
    public void insertData(String ISBN, String title, String author) {
        openDatabase(); // 데이터베이스 열기

        String sql = "INSERT INTO " + MyBookListContract.MyBookListEntry.TABLE_NAME +
                " VALUES ('" + ISBN + "', '" + title + "', '" + author + "', NULL, datetime('now','+9 hours'))";

        database.execSQL(sql);  // 쿼리 실행
        database.close();   // 데이터베이스 닫기
        Log.d(TAG, "insert success");
    }

    // 데이터 수정
    public void updateData(String ISBN, String memo) {
        openDatabase();  // 데이터베이스 열기

        String sql = "UPDATE " + MyBookListContract.MyBookListEntry.TABLE_NAME +
                " SET " + "memo = '" + memo + "' WHERE ISBN = '" + ISBN + "'";

        database.execSQL(sql);  // 쿼리 실행
        database.close();   // 데이터베이스 닫기
        Log.d(TAG, "update success");
    }

    // 데이터 삭제
    public void deleteData(String ISBN) {
        openDatabase();  // 데이터베이스 열기

        String sql = "DELETE FROM " + MyBookListContract.MyBookListEntry.TABLE_NAME
                + " WHERE " + "ISBN = '" + ISBN + "'";

        database.execSQL(sql);  // 쿼리 실행
        database.close();   // 데이터베이스 닫기
        Log.d(TAG, "delete success");
    }

    // 모든 행의 ISBN, memo 받아오기 - 최근 날짜순으로
    public Cursor getAllData() {
        openDatabase();  // 데이터베이스 열기

        String sql = "SELECT ISBN, memo FROM " + MyBookListContract.MyBookListEntry.TABLE_NAME +
                " ORDER BY addDate DESC";

        Cursor cursor = database.rawQuery(sql);  // 쿼리 실행
        database.close();   // 데이터베이스 닫기
        Log.d(TAG, "select success");

        return cursor;
    }

    /// 도서명, 저자에 검색어가 포함되어 있는 행 반환
    public Cursor searchData(String searchTerm) {
        openDatabase();  // 데이터베이스 열기

        String sql = "SELECT ISBN, memo FROM " + MyBookListContract.MyBookListEntry.TABLE_NAME +
                " WHERE title LIKE '%" + searchTerm + "%' OR author LIKE '%" + searchTerm +
                "%' ORDER BY addDate DESC";

        Cursor cursor = database.rawQuery(sql);  // 쿼리 실행
        database.close();   // 데이터베이스 닫기
        Log.d(TAG, "select success");

        return cursor;
    }

    // 데이터베이스에 해당 데이터 존재하는지 검사
    public boolean isDataExist(String ISBN) {
        boolean exist;
        openDatabase();  // 데이터베이스 열기

        String sql = "SELECT ISBN FROM " + MyBookListContract.MyBookListEntry.TABLE_NAME
                + " WHERE ISBN = '" + ISBN + "'";

        Cursor cursor = database.rawQuery(sql);  // 쿼리 실행

        if (cursor.getCount() == 0) {   // 존재하지 않음
            exist = false;
        }

        else {  // 존재함
            exist = true;
        }

        cursor.close();
        database.close();   // 데이터베이스 닫기
        Log.d(TAG, "check success");

        return exist;
    }

}
