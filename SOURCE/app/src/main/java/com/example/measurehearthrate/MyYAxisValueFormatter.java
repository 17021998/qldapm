package com.example.measurehearthrate;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public abstract class MyYAxisValueFormatter implements IAxisValueFormatter {
    private ArrayList<String> labels;

    public MyYAxisValueFormatter(ArrayList<String> labels) {
        this.labels = labels;
    }

}