package com.searcher.booksearch;

public class MyBookListItem {
    public int cover_image;  // bookcover 자료형 무엇인지 확인하기
    public String title;
    public String author;
    public String publisher;
    public String published_date;
    public String price;

    public MyBookListItem(String ISBN){
        // 인자로 받은 ISBN 통해 API 호출하여 각 인스턴스에 알맞은 값 넣기

        // 테스트
        this.title = ISBN;
        this.author = ISBN;
        this.publisher = ISBN;
        this.published_date = ISBN;
        this.price = ISBN;
    }

    public int getCover_image() {
        return cover_image;
    }

    public void setCover_image(int cover_image) {
        this.cover_image = cover_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
