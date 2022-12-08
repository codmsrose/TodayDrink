package com.example.todaydrink;

public class NewDrinkDetail {
    String name;
    String degree;
    String bottle;
    String glass;

    public NewDrinkDetail() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosu() {
        return degree;
    }

    public void setDosu(String dosu) {
        this.degree = degree;
    }

    public String getBottle() {
        return bottle;
    }

    public void setBottle(String bottle) {
        this.bottle = bottle;
    }

    public String getGlass() {
        return glass;
    }

    public void setGlass(String glass) {
        this.glass = glass;
    }

    public NewDrinkDetail(String name, String degree, String bottle, String glass) {
        this.name = name;
        this.degree = degree;
        this.bottle = bottle;
        this.glass = glass;
    }
}
