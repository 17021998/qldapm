package com.example.measurehearthrate;

import android.util.Log;

import java.util.ArrayList;

public class StatCamera {
    private ArrayList<Double> arr;
    private int fps = 2;
    /*
        MODE
        0: isBlackImage, it mean something in front of camera
        1: isRed, it mean after blackTime and turn on Flash, it mean `finger` in front of camera
    */
    private int mode = 0;
    private int maxSize = fps; // default 1s
    private int index = -1; // start index = -1;
    private int isAvailable = 0;
    public StatCamera(double time, int mode){
        this.init(time, mode);
    }
    /*
       state
       0: can not
       1: waitting/keep state
       2: start with flash
    */
    public int state(int imgAvg, double percent, double g, double b){
        if(!checkMode(imgAvg, this.mode, percent, g, b)) return 0;
        this.update(imgAvg);
        switch (this.mode) {
            case 1:
                if(this.isAvailable == 0) return 1;
                if(upgrade( this.mode + 1) == false)
                    return 0;
                return 2;
            case 2:
                return 1;
            default:
                return 0;
        }
    }

    public int getMode(){
        return this.mode;
    }
    public int getIndex(){
        return this.index;
    }

    public int getIsAvailable(){
        return this.isAvailable;
    }

    public Boolean getCondition(){
        return (this.mode == 2);
    }

    public ArrayList<Double> getArr(){
        if(this.index >= this.arr.size())
            return this.arr;
        return null;
    }

    public void reset(){
        this.index = -1;
        arr.clear();
        this.mode = 1;
        this.isAvailable = 0;
    }

    private void init(double time, int mode){
        this.maxSize = (int) time*fps;
        arr = new ArrayList<>(this.maxSize);
        this.mode = mode;
    }

    private Boolean upgrade(int mode){
        Log.d("","Upgraded mode = 2");
//        if((mode - this.mode) != 1) return false;
        this.arr.clear();
        this.mode = mode;
        this.isAvailable = 0;
        this.index = 0;
        return true;
    }

    private void update(int imgAvg){
        // update index into circle in arr
        this.index += 1;
        if(this.index >= this.maxSize){
            this.isAvailable = 1;
            this.index = 0;
        }
        try {
            if (this.isAvailable == 1)
                this.arr.set(this.index, imgAvg * 1.0);
            else
                this.arr.add(imgAvg * 1.0);
        } catch(Throwable err){}
    }

    private Boolean checkMode(int imgAvg, int mode, double percent, double g, double b){
        boolean result = false;
        switch (mode){
            case 1:
                /* MODE = 0, Flash is off
                   isAvailable = 0 (force)
                */
                //result = (imgAvg <= 30 && imgAvg >= 0) || (imgAvg >= 150);
                result = (imgAvg <= 30 && g <= 30 && b <= 30) || (percent >= 50 && imgAvg >= 127 && g <= 80 && b <= 80);
                break;
            case 2:
                /* MODE = 1, Flash is on
                   isAvailable = 0: waitting a time, it mean return true
                   isAvailable = 1: keep compare
                */
                if(this.isAvailable == 0)
                    result = true;
                else
                    result = (percent >= 50 && imgAvg >= 127 && g <= 80 && b <= 80);
//                Log.d("kaka:", String.valueOf(percent) + " " + String.valueOf(imgAvg) + " " + String.valueOf(g) + " " + String.valueOf(b));
                break;
            default:
                break;
        }
        return result;
    }
}
