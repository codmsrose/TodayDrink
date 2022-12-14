package com.example.todaydrink;

public class State {
    int density;
    String state;

    public State() { }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public State(int density, String state) {
        this.density = density;
        this.state = state;
    }
}
