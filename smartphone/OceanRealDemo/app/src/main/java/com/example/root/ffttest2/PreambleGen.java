package com.example.root.ffttest2;

public class PreambleGen {

    public static short[] sounding_signal_s() {
        return SymbolGeneration.generatePreamble(Constants.pn60_bits, Constants.valid_carrier_data,
                Constants.chanest_symreps, true, Constants.SignalType.Sounding); }

    public static short[] preamble_s() {
        return Utils.convert_s(preamble_d());
    }

    public static double[] preamble_d() { return (Constants.naiser); }
}
