package com.example.todaydrink;

public class Drink {
    int bottle;
    int glass;
    double ml;

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

    public double getMl() {
        return ml;
    }

    public void setMl(double ml) {
        this.ml = ml;
    }

    public Drink(int bottle, int glass, double ml) {
        this.bottle = bottle;
        this.glass = glass;
        this.ml = ml;
    }
}
