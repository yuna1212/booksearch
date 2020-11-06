package com.searcher.booksearch;

import android.os.AsyncTask;

public class RequestAPIAsyncTask extends AsyncTask<String, Void, ApiHanddling> { // 두번째 generic parameter는 쓰지 않는데 선언해주는 의미가 있나..?
    /*
     * API 호출하고, 호출 결과를 반환함
     * 호출 결과는 우선 get으로 얻게 함
     */

    @Override
    protected ApiHanddling doInBackground(String... isbn) {
        ApiHanddling api = new ApiHanddling(isbn[0]);
        return api;
    }

}
