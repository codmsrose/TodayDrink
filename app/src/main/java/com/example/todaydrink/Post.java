package com.example.todaydrink;

import java.text.SimpleDateFormat;

public class Post {
    private String id;
    private String title;
    private String contents;
    private String date;

    public Post() { }

    public Post(String id, String title, String contents, String date) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
