package com.example.hearthrate;

public class Filter {
    private String state;

    public String getState() {
        return state;
    }

    public String getTime() {
        return time;
    }

    public int getMode() {
        return mode;
    }

    private String time;
    private int mode;
    public Filter(String state, String time, int mode){
        this.state = state;
        this.time = time;
        this.mode = mode;
    }

}
