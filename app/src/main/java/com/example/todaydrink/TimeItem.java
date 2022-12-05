package com.example.todaydrink;

public class TimeItem {
    String time;
    String name;

    public TimeItem() { }

    public String getTime() {
        return time;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public TimeItem(String time) {
        this.time = time;
    }

    public TimeItem(String time, String name){
        this.time=time;
        this.name=name;
    }

}


