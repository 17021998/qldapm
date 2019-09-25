package com.example.measurehearthrate;

import android.util.Log;

import uk.me.berndporr.iirj.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.jtransforms.fft.DoubleFFT_1D;

public class Calc {

    private static int fps = 27;
    private static int WINDOW_SECONDS;
    private static int FINE_TUNING_FREQ_INCREMENT = 1;
    private static final int MAX_HEART_BEAT = 230;
    private static final int MIN_HEART_BEAT = 40;

    public double exec(ArrayList<Double> Brightness, int ws, int _fps){
        if(Brightness.size() <= _fps) return 0;
        WINDOW_SECONDS = 10;
        fps = _fps;
        int size = Brightness.size() - fps; //size cua data sau khi cat
        ArrayList<Double> filteredSignal; // luu data sau khi filter
        ArrayList<Double> afterCutSignal = new ArrayList<>();//luu data sau khi cat
        double[] inputFFT = new double[size * 2]; // Lưu dữ liệu để input cho hàm FFT
        ArrayList<Double> FFTSignal = new ArrayList<>(size); // Lưu dữ liệu sau sau khi abs dữ liệu từ FFT
        try {
            filteredSignal = bandPassFilter(Brightness); //Filter dung ButterWorth
            for(int i = fps-1; i <  filteredSignal.size(); i++) { //Cắt 1 s đầu tiên của dữ liệu bỏ đi
                afterCutSignal.add(filteredSignal.get(i));
            }
            afterCutSignal = HanningWindow(afterCutSignal, 0, afterCutSignal.size()); // Cho dữ liệu sau khi cắt qua Hann Window
        } catch (Exception e){
            return 0;
        }
        // Tạo data input cho FFT
        for (int i = 0; i < size; i++){
            inputFFT[2*i] = afterCutSignal.get(i); // Phần real
            inputFFT[2*i+1] = 0.0; // Phần ảo
        }
        DoubleFFT_1D fft = new DoubleFFT_1D(size);
        fft.complexForward(inputFFT);
        // Kết hợp real, ảo và abs lên
        for (int i = 0; i < size; i++){
            FFTSignal.add(Math.hypot(inputFFT[2*i], inputFFT[2*i+1]));
            //System.out.println(FFTSignal[i]);
        }
        // FFT indices of frequencies where the human heartbeat is
        double frequency_low = (double) MIN_HEART_BEAT/60;
        double frequency_high = (double) MAX_HEART_BEAT/60;
        int index_low = (int) Math.floor(frequency_low * ((double)size / fps));
        int index_high = (int) Math.floor(frequency_high * ((double)size / fps));
        ArrayList<Double> peaks = new ArrayList<>();
        ArrayList<Integer> locations = new ArrayList<>();
        // Tìm local peaks trong đoạn từ low đến high
        for(int i = index_low; i < index_high; i++){
            if ((FFTSignal.get(i) > FFTSignal.get(i-1)) && (FFTSignal.get(i) >= FFTSignal.get(i+1))) {
                peaks.add(FFTSignal.get(i));
                locations.add(i);
            }
        }
        // Tìm max peak
        double maxPeak = 0;
        int maxPeakLocation = -1;
        for(int i = 0; i < peaks.size(); i++){
            if (peaks.get(i) > maxPeak) {
                maxPeak = peaks.get(i) ;
                maxPeakLocation = locations.get(i);
            }
        }
        //Từ max peak suy ra được heart rate tạm thời. Heart rate tạm thời sẽ là 40 50 60 70 80 90...
        double bpm = 0;
        if(maxPeakLocation != -1) {
            bpm = maxPeakLocation * ((double)fps / size) * 60;
        }
        //Smooth the highest peak frequency by finding the frequency that best "correlates" in the resolution range around the peak
        // Đoạn dưới này là smooth thực sự nó làm cái gi thì éo hiểu lắm
        // Chủ yếu là dựa vào hình dạng của tín hiệu suy ra heart rate smooth
        double freq_resolution = 1.0 / WINDOW_SECONDS;
        double lowf = bpm / 60 - 0.5 * freq_resolution;
        double freq_inc = (double)FINE_TUNING_FREQ_INCREMENT / 60;
        int test_freqs = (int) Math.round(freq_resolution / freq_inc); // 10
        ArrayList<Double> power = new ArrayList<>(test_freqs + 1);
        ArrayList<Double> freqs = new ArrayList<>(test_freqs + 1);
        for(int i = 1; i <= test_freqs; i++){
            freqs.add((i-1) * freq_inc + lowf);
        }
        Log.d("Dien ak: ", String.valueOf(freqs.size()) +" "+ String.valueOf(WINDOW_SECONDS) + " " + String.valueOf(test_freqs));
        for(Double item: freqs){
            double re = 0;
            double im = 0;
            for(int j = 0; j < size; j++){
                double phi = 2 * Math.PI * item * ((double) j / fps);
                re = re + afterCutSignal.get(j) * Math.cos(phi);
                im = im + afterCutSignal.get(j) * Math.sin(phi);
            }
            power.add(re * re + im * im);
        }
        double max = 0;
        int maxLocation = -1;
        for (int i = 0; i < power.size(); i++){
            if(power.get(i) > max) {
                max = power.get(i);
                maxLocation = i;
            }
        }
        if(maxLocation < 0) maxLocation = 0;
        double bpm_smooth = 0;
        if(freqs.size() > 0)
            bpm_smooth =  60 * freqs.get(maxLocation);
        return bpm_smooth;
    }

    private static ArrayList<Double> HanningWindow(ArrayList<Double> signal_in, int pos, int size) throws Exception{
        ArrayList<Double> result = signal_in;
        for (int i = pos; i < pos + size; i++) {
            int j = i - pos; // j = index into Hannwindow function
            result.set(i, (double) (signal_in.get(i) * 0.5 * (1.0 - Math.cos(2.0 * Math.PI * j / (size-1)))));
        }
        return result;
    }

    private static ArrayList<Double> bandPassFilter(ArrayList<Double> signal) throws Exception {
        ArrayList<Double> result = new ArrayList<>();
        Butterworth butterworth = new Butterworth();
        butterworth.bandPass(2, 16,2.25, 3.1113); //Chuan bai`
        // let's do an impulse response
        for (double item: signal) {
            double v = butterworth.filter(item);
            result.add(v);
        }
        return result;
    }
}