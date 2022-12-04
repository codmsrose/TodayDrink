package com.example.todaydrink;

public class Drink {
    int bottle;
    int glass;
    int ml;

    public Drink() { }

    public int getBottle() {
        return bottle;
    }

    public void setBottle(int bottle) {
        this.bottle = bottle;
    }

    public int getGlass() {
        return glass;
    }

    public void setGlass(int glass) {
        this.glass = glass;
    }

    public int getMl() {
        return ml;
    }

    public void setMl(int ml) {
        this.ml = ml;
    }

    public Drink(int bottle, int glass, int ml) {
        this.bottle = bottle;
        this.glass = glass;
        this.ml = ml;
    }
}
