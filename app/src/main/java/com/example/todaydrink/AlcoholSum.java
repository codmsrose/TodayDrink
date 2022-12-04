package com.example.todaydrink;

public class AlcoholSum {
    // double, long으로도 잘 안되길래 String으로 시도해 본 거입니당.
    String alcohol_sum;

    public AlcoholSum() { }

    public String getAlcohol_sum() {
        return alcohol_sum;
    }

    public void setAlcohol_sum(String alcohol_sum) {
        this.alcohol_sum = alcohol_sum;
    }

    public AlcoholSum(String alcohol_sum) {
        this.alcohol_sum = alcohol_sum;
    }
}
