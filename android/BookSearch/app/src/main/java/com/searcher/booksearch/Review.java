package com.searcher.booksearch;

public class Review {
    public String id;    // 회원 아이디
    public String date;  // 작성 날짜
    public String comment;   // 리뷰 내용

    public Review(String id, String date, String comment) {
        this.id = id;
        this.date = date;
        this.comment = comment;
    }
}
