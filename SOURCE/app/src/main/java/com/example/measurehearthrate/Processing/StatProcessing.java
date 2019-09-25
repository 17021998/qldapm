package com.example.measurehearthrate.Processing;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.measurehearthrate.R;

public class StatProcessing extends Activity {
    private TextView result_min;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chart);
        result_min = (TextView) findViewById(R.id.result_avg);
        result_min.setText("PO");
    }
}
