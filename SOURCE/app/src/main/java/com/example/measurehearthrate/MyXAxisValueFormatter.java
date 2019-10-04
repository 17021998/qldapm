package com.example.measurehearthrate;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class MyXAxisValueFormatter implements IAxisValueFormatter {
    private ArrayList<String> labels;

    public MyXAxisValueFormatter(ArrayList<String> labels) {
        this.labels = labels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try {
            int index = (int) value;
            return labels.get(index);
        } catch (Exception e) {
            return "";
        }
    }
}