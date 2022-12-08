package com.example.todaydrink;

public class State {
    String density;
    String state;

    public State() { }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public State(String density, String state) {
        this.density = density;
        this.state = state;
    }
}
