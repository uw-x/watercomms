package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;

import android.graphics.Color;
import android.util.Log;

//import org.ejml.simple.SimpleMatrix;


public class Equalizer {

    static double[][] G;
//    static SimpleMatrix g;

    public static void equalizer_estimation(double[] tx, double[] rx) {
        if (Constants.eqMethod == Constants.EqMethod.Freq) {
            G = equalizer_estimation_freq(tx, rx);
        }
        else if (Constants.eqMethod == Constants.EqMethod.Time) {
//            g = equalizer_estimation_time(tx, rx, Constants.tap_num);
        }
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
//        String ss=Arrays.toString(SignalProcessing.abs(div));
        return div;
    }

//    public static SimpleMatrix equalizer_estimation_time(double[] tx, double[] rx, int tap_num) {
//        double lambda = 1e-6;
//        if (tx.length + tap_num - 1 != rx.length) {
//            Log.e(LOG, "warinig tx and rx different size");
//            return null;
//        }
//
//        if (tap_num > tx.length) {
//            Log.e(LOG, "tap number is too large");
//        }
//
//        int P = tx.length;
//        int L= tap_num;
//
////        Log.e(LOG,"set up matrix");
//        // rows/cols, reverse of matlab
//        // 480 rows, 960 columns
//        SimpleMatrix M = new SimpleMatrix(P,L);
//
//        for (int b = 0; b < P; b++) {
////            Log.e(LOG,String.format("segment %d %d %d",b,L+b-1,rx.length));
//            double[] row=Utils.segment(rx, b, L + b - 1);
//            M.setRow(b,0, row);
//        }
//
//        SimpleMatrix Y = new SimpleMatrix(tx.length,1,true,tx);
//
////        g = pinv((M'*M) + lambda*eye(L))*M'*Y;
//        SimpleMatrix Mt=M.transpose();
//        SimpleMatrix t1 = (Mt.mult(M));
//
//        SimpleMatrix pinv = ( t1 .plus(SimpleMatrix.identity(L).scale(lambda))).pseudoInverse();
//        SimpleMatrix g = pinv.mult(Mt).mult(Y);
//        String aa = Arrays.toString(g.getDDRM().getData());
//        return g;
//    }

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

//    public static double[] equalizer_recover_time(double[] rx) {
//        int L= g.getMatrix().getNumRows();
//        int P = rx.length - L + 1;
//
//        SimpleMatrix M = new SimpleMatrix(P,L);
//
//        for (int i=0; i < P; i++) {
////            Log.e(LOG,String.format("segment %d %d %d",i,L+i-1,rx.length));
//            double[] col=Utils.segment(rx,i,L+i-1);
//            M.setRow(i,0,col);
//        }
//
//        SimpleMatrix tx = M.mult(g);
//        return tx.getDDRM().getData();
//    }
}
