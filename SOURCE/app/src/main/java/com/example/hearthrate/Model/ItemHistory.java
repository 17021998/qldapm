package com.example.hearthrate.Model;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class ItemHistory {
    int image;
    String date, result, state;

    public int getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public String getState() { return state; }

    public int remove = 1;

    public void setRemove(int x){
        this.remove = x;
    }

    public int getRemove(){
        return this.remove;
    }
    public int getDay() {
        String date = getDate();
        int day;
        try{
            day = Integer.parseInt(date.split(", ")[1].split("-")[0]);
        } catch (NumberFormatException e){
            day = 0;
        }
        return day;
    }

    public String getResult() {
        return result;
    }

    public Boolean isState(String x) {
        return (x == this.state);
    }

    public Boolean isTime(String x){
        return true;
    }

    public int getResultByInt(){
        String resp = this.getResult();
        int result;
        try{
            result = Integer.parseInt(resp);

        } catch(NumberFormatException err){
            result = 0;
        }
        if(!(result >= 40 && result <= 120)) result = 0;
        return result;
    }
    public Boolean checkOk(){
        if(this.getResultByInt() != 0) return true;
        else return false;
    }
     public ItemHistory(int image, String date, String result, String state) {
        this.image = image;
        this.date = date;
        this.result = result;
        this.state = state;
    }
}
