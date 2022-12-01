package com.example.todaydrink;

public class CheckItems {

    // 이미지1, 스트링1
    int resId;
    String name;


    public int getId() {
        return resId;
    }

    public void setId(int id) {
        this.resId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public CheckItems(int resId, String name){
        this.resId=resId;
        this.name=name;
    }
}
