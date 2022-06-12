package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;

import android.graphics.Color;
import android.util.Log;

public class Equalizer {
    static double[][] G;
    public static void equalizer_estimation(double[] tx, double[] rx) {
            G = equalizer_estimation_freq(tx, rx);
    }

    public static double[][] equalizer_estimation_freq(double[] tx, double[] rx) {
        if (tx.length != rx.length) {
            Log.e(LOG, "warinig tx and rx different size");
            return null;
        }

        double[][] tx_spec = Utils.fftcomplexoutnative_double(tx, tx.length);
        double[][] rx_spec = Utils.fftcomplexoutnative_double(rx, rx.length);
        tx_spec[0] = Utils.segment(tx_spec[0], Constants.nbin1_chanest, Constants.nbin2_chanest);
        rx_spec[0] = Utils.segment(rx_spec[0], Constants.nbin1_chanest, Constants.nbin2_chanest);
        tx_spec[1] = Utils.segment(tx_spec[1], Constants.nbin1_chanest, Constants.nbin2_chanest);
        rx_spec[1] = Utils.segment(rx_spec[1], Constants.nbin1_chanest, Constants.nbin2_chanest);

        double[][] div = Utils.dividenative(tx_spec, rx_spec);
        return div;
    }

    public static double[][] equalizer_recover_freq(double[] rx, int sym_number) {
        double[][] rx_spec = Utils.fftcomplexoutnative_double(rx, rx.length);

        double[] spec_rx_abs = Utils.abs(rx_spec);

        MainActivity.av.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                double[] spectrum_db = Utils.mag2db(spec_rx_abs);
                Display.plotSpectrum(Constants.gview3,spectrum_db,sym_number==0?true:false, Color.BLUE,"Rx symbols");
            }
        });

        rx_spec[0] = Utils.segment(rx_spec[0], Constants.nbin1_chanest, Constants.nbin2_chanest);
        rx_spec[1] = Utils.segment(rx_spec[1], Constants.nbin1_chanest, Constants.nbin2_chanest);
        return Utils.timesnative(rx_spec, G);
    }
}
