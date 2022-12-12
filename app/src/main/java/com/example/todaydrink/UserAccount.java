package com.example.todaydrink;

/**
 * 사용자 계정 정보 모델 클래스
 */
public class UserAccount {
    String name;
    String id;
    String pwd;
    int gender;
    String weight;
    String account;
    String profile;

    public UserAccount() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public UserAccount(String name, String id, String pwd, int gender, String weight, String account, String profile) {
        this.name = name;
        this.id = id;
        this.pwd = pwd;
        this.gender = gender;
        this.weight = weight;
        this.account = account;
        this.profile = profile;
    }
}