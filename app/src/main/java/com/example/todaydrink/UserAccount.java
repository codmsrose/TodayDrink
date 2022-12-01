package com.example.todaydrink;

/**
 * 사용자 계정 정보 모델 클래스
 */
public class UserAccount {
    String id;
    String pwd;
    String weight;
    String drink;
    String account;
    String profile;

    public UserAccount() { }

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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
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

    public UserAccount(String id, String pwd, String weight, String drink, String account, String profile) {
        this.id = id;
        this.pwd = pwd;
        this.weight = weight;
        this.drink = drink;
        this.account = account;
        this.profile = profile;
    }
}