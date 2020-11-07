package com.searcher.booksearch;

public class MyBookListItem {
    private String ISBN;
    private String memo;

    // 생성자 통해 인스턴스에 데이터베이스 정보 저장해두기
    public MyBookListItem(String ISBN, String memo){
        this.ISBN = ISBN;
        this.memo = memo;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
