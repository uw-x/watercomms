package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;

import android.util.Log;

public class ChirpGen {

    public static short[] sounding_signal_s() {
        Log.e(LOG,"ChirpGen_sounding_signal");
//        short[] bits = SymbolGeneration.rand_bits(Constants.valid_carrier_preamble.length);
        short[] bits = Constants.pn20_bits;
        if (Constants.subcarrier_number_default==20) {
            bits=Constants.pn20_bits;
        }
        else if (Constants.subcarrier_number_default==40) {
            bits=Constants.pn40_bits;
        }
        else if (Constants.subcarrier_number_default==60) {
            bits=Constants.pn60_bits;
        }
        else if (Constants.subcarrier_number_default==120) {
            bits=Constants.pn120_bits;
        }
        else if (Constants.subcarrier_number_default==300) {
            bits=Constants.pn300_bits;
        }
        else if (Constants.subcarrier_number_default==600) {
            bits=Constants.pn600_bits;
        }
        short[] out = SymbolGeneration.generate(bits, Constants.valid_carrier_data,
                Constants.chanest_symreps, true, Constants.SignalType.Sounding);
        return out;
    }

    public static double[] sounding_signal_d() {
        Log.e(LOG,"ChirpGen_sounding_signal");
        return Utils.convert(sounding_signal_s());
    }

    public static short[] preamble_s() {
        return Utils.convert_s(preamble_d());
    }

    public static double[] preamble_d() {
        if (Constants.NAISER) {
            return (Constants.naiser);
        }
        else {
            if (Constants.exp_num == 5) {
                double[] c1 = generateChirpSpeaker_d(Constants.preambleEndFreq,
                        Constants.preambleStartFreq,
                        (Constants.preambleTime / 2) / 1000.0,
                        Constants.fs, 0, 1);
                double[] c2 = generateChirpSpeaker_d(Constants.preambleStartFreq,
                        Constants.preambleEndFreq,
                        (Constants.preambleTime / 2) / 1000.0,
                        Constants.fs, 0, 1);
                return Utils.concat(c1, c2);
            } else {
                return generateChirpSpeaker_d(Constants.preambleStartFreq,
                        Constants.preambleEndFreq,
                        Constants.preambleTime / 1000.0,
                        Constants.fs, 0, 1);
            }
        }
    }

    public static short[] chirp_get() {
        return Utils.convert_s(generateChirpSpeaker_d(Constants.preambleStartFreq,
                Constants.preambleEndFreq,
                Constants.chirpPreambleTime / 1000.0,
                Constants.fs, 0, 1));
    }

    public static double[] generateChirpSpeaker_d(double startFreq, double endFreq, double time, double fs, double initialPhase,double scale) {
        int N = (int) (time * fs);
        double[] ans = new double[N];
        double f = startFreq;
        double k = (endFreq - startFreq) / time;
        double mult = (32767) * scale;
        for (int i = 0; i < N; i++) {
            double t = (double) i / fs;
            double phase = initialPhase + 2 * Math.PI * (startFreq * t + 0.5 * k * t * t);
            phase = Normalize(phase);
            ans[i] = (short) (Math.sin(phase) * mult);
        }

        return ans;
    }

    public static short[] generateChirpSpeaker_s(double startFreq, double endFreq, double time, double fs, double initialPhase,double scale) {
        return Utils.convert_s(generateChirpSpeaker_d(startFreq, endFreq, time, fs, initialPhase, scale));
    }

    public static double Normalize(double ang) {
        double angle = ang;
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }

        return angle;
    }
}
