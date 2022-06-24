package com.example.root.ffttest2;

import android.app.Activity;

public class Decoder {
    public static void decode_helper(Activity av, double[] data, int[] valid_bins) {
        data = Utils.filter(data);

        valid_bins[0]=valid_bins[0]+Constants.nbin1_default;
        valid_bins[1]=valid_bins[1]+Constants.nbin1_default;

        // bin fill order
        // element 0 => number of transmitted data symbols
        // element 1...n => number of bits in a symbol corresponding to data bits (the remaining are padding bits)
        int[] binFillOrder = SymbolGeneration.binFillOrder(Utils.arange(valid_bins[0],valid_bins[1]));

        // extract pilot symbols from the first OFDM symbol
        // compare this to the transmitted the transmitted pilot symbols
        // and perform frequency domain equalization
        int ptime = (int)((Constants.preambleTime/1000.0)*Constants.fs);
        int start = ptime+Constants.ChirpGap;
        double[] rx_pilots=Utils.segment(data,start+Constants.Cp,start+Constants.Cp+Constants.Ns-1);
        start = start+Constants.Cp+Constants.Ns;

        double [] tx_pilots = Utils.convert(SymbolGeneration.getTrainingSymbol(Utils.arange(valid_bins[0],valid_bins[1])));
        tx_pilots = Utils.segment(tx_pilots,Constants.Cp,Constants.Cp+Constants.Ns-1);

        // obtain weights from frequency domain equalization
        double[][] tx_spec = Utils.fftcomplexoutnative_double(tx_pilots, tx_pilots.length);
        double[][] rx_spec = Utils.fftcomplexoutnative_double(rx_pilots, rx_pilots.length);
        double[][] weights = Utils.dividenative(tx_spec, rx_spec);
        double[][] recovered_pilot_sym = Utils.timesnative(rx_spec, weights);

        // differential decoding
        int numsyms = binFillOrder[0]; // number of data symbols
        double[][][] symbols = new double[numsyms + 1][][];
        symbols[0] = recovered_pilot_sym;

        // extract each symbol and equalize with weights
        for (int i = 0; i < numsyms; i++) {
            double[] sym = Utils.segment(data, start + Constants.Cp, start + Constants.Cp + Constants.Ns - 1);
            start = start + Constants.Cp + Constants.Ns;

            double[][] sym_spec = Utils.fftcomplexoutnative_double(sym, sym.length);
            sym_spec = Utils.timesnative(sym_spec, weights);
            symbols[i + 1] = sym_spec;
        }

        // demodulate the symbols to bits
        short[][] bits = Modulation.pskdemod_differential(symbols, valid_bins);

        // for each symbol reorder the bits that were shuffled from interleaving
        // extract bits from the symbol corresponding to valid data
        String coded = "";
        for (int i = 0; i < bits.length; i++) {
            short[] newbits = bits[i];
            newbits = SymbolGeneration.unshuffle(bits[i], i);
            // extract the data bits
            for (int j = 0; j < binFillOrder[i + 1]; j++) {
                coded += newbits[j] + "";
            }
        }

        // perform viterbi decoding
        String uncoded = Utils.decode(coded, Constants.cc[0],Constants.cc[1],Constants.cc[2]);

        // extract messageID from bits
        int messageID=Integer.parseInt(uncoded,2);

        // display message
        String message="Error";
        if (Constants.mmap.containsKey(messageID)) { message = Constants.mmap.get(messageID); }
        Utils.log(coded +"=>"+uncoded+"=>"+message);

        String finalMessage = message;
        av.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Utils.sendNotification(av, "Notification",finalMessage, R.drawable.warning2);
                Constants.msgview.setText(finalMessage);
            }
        });
    }
}
