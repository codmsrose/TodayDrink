package com.example.todaydrink;

public class GroupMember {
    String id;
    String name;

    public GroupMember() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupMember(String id) {
        this.id = id;
    }
    public GroupMember(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
