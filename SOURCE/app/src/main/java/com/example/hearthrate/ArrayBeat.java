package com.example.hearthrate;

import android.util.Log;

import java.util.ArrayList;

public class ArrayBeat {
    private ArrayList<Double> brightness;
    private Calc calc = new Calc();
    private int fps = 28;
    private int TIME_AVAILABLE = 7;
    private Boolean started;
    private long startTime;
    private double save_avg = 0;

    public ArrayBeat(ArrayList<Double> _brightness) {
        this.brightness = _brightness;
        this.started = false;
    }

    public Boolean checker(double a, double b){
        if(a >= 60 && a <= 110 && ( Math.abs(b - a) < 50 || b == 0)) return true;
        return false;
    }

    public void reset(){
        this.brightness.clear();
        this.started = false;
        save_avg = 0;
    }

    public void doStartTime(){
        if(this.started == false) {
            this.started = true;
            this.startTime = System.currentTimeMillis();
        }
    }

    public long getTimeStart() {
        return this.startTime;
    }
    public long getCurrentTime(){
        return System.currentTimeMillis();
    }
    public int getFps(){
        return this.fps;
    }
    public void add(double item){
        doStartTime();
        this.brightness.add(item);
    }

    public void add(ArrayList<Double> list){
        if(list == null ) return;
        for(Double item:list)
        this.brightness.add(item);
    }

    public void clear(){
        save_avg = 0;
        this.brightness.clear();
    }

    private double getTime(int length){
        long totalTimes = (this.getCurrentTime() - this.getTimeStart()) / 1000l;
        this.fps = (int) (length*1.0/totalTimes);
        return totalTimes;
    }
    private Boolean isAvailableToExec(int length){
        return getTime(length) >= TIME_AVAILABLE;
    }

    public double exec(ArrayList<Double> input, int ws, int fps){
        return calc.exec(input, ws, fps);
    }
    public ArrayList<Double> getBrightness(){
        return this.brightness;
    }

    public int getSeconds(){
        return (int)((this.getCurrentTime() - this.getTimeStart()) / 1000l);
    }
    private ArrayList<Double> convert(ArrayList<Double> brightness, int fps){
        if(brightness.size() < fps) return brightness;
        int length = (int) (fps*Math.ceil(brightness.size()/fps)) - 1;
        Log.d("check: ", String.valueOf(brightness.size())+ " " + String.valueOf(length) + " " + String.valueOf(fps));
        return new ArrayList<>(brightness.subList(0, length));
    }
    public double exec(int fps, int __time){
        if(!isAvailableToExec(this.brightness.size())) return save_avg;
//        Log.d("Measure: ", String.valueOf(fps));
        double temp = calc.exec(convert(brightness, 16), __time, 16);
        if(temp > 0) save_avg = temp;
        return save_avg;
    }
}
