package com.searcher.booksearch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.searcher.booksearch.R;

public class ScanBarcode extends AppCompatActivity {
    private IntentIntegrator barcodeScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_barcode_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        barcodeScan = new IntentIntegrator(this);
        // 휴대폰 방향에 따라 가로, 세로로 자동 변경
        barcodeScan.setOrientationLocked(false);
        //바코드 스캔 호출 시 보이는 텍스트
        barcodeScan.setPrompt("도서 뒷면의 바코드를 사각형 안에 비춰주세요.");
        //바코드 인식 시 소리 나지 않도록
        barcodeScan.setBeepEnabled(false);
        barcodeScan.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                // todo
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}