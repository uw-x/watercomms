package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;
import static com.example.root.ffttest2.Constants.SPEECH_OUT;
import static com.example.root.ffttest2.Constants.debugPane;
import static com.example.root.ffttest2.Constants.valid_carrier_preamble;
import static com.example.root.ffttest2.Constants.xcorr_method;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Locale;

public class Decoder {

    public static void extract(Context cxt, double[] rec, int[] valid_bins) {
        Log.e(Constants.LOG, "SignalProcessing_extract");
        double[] preamble = ChirpGen.preamble_d();
        double[] filt = Utils.filter(rec);
//        int start_point = Utils.xcorr(preamble, filt, rec, rec.length);
        int start_point=0;
        start_point += preamble.length+Constants.ChirpGap+1;

        int rx_start = start_point;
        int rx_end = start_point+(Constants.sym_len*Constants.Nsyms)-1;
        int rx_len = (rx_end-rx_start)+1;
        Log.e(Constants.LOG, ">>"+rec.length+","+rx_start+","+rx_end+","+rx_len);
        if (rx_end-1 > rec.length || rx_start < 0) {
            Utils.log("Error extracting preamble from data signal");
            return;
        }
        double[] rxsig = Utils.segment(rec, rx_start, rx_end);

        short[] bits = SymbolGeneration.rand_bits(valid_bins.length*Constants.Nsyms);
        short[] txsig = SymbolGeneration.generate(bits, valid_bins,Constants.data_symreps, false,
                Constants.SignalType.DataAdapt);
        decode(rxsig, Utils.convert(txsig));
    }

//    public static void test_decode(Context cxt) {
//        long t1 = System.currentTimeMillis();
//
//        double[] sounding=FileOperations.readrawasset2(cxt, R.raw.decoder_sounding,30000);
//        Log.e(LOG,"rx read " +(System.currentTimeMillis()-t1));
//
//        t1 = System.currentTimeMillis();
//        // tx file should not have warmup, preamble or first gap
//        double[] data=FileOperations.readrawasset2(cxt, R.raw.decoder_data,30000);
//        Log.e(LOG,"tx read " +(System.currentTimeMillis()-t1));
//        int numsegs=(int)(data.length/24e3);
////        double[] tx_preamble = ChirpGen.preamble_d();
//        double[] tx_preamble = Utils.convert(ChirpGen.chirp_get());
//
//        Log.e("decoder",data.length+","+numsegs);
//        int maxidx=0;
//        double maxval=0;
//        for (int i = 1; i < numsegs; i++) {
//            int ss = (int)24e3*i;
//            int ee= (int)(ss+24e3-1);
//            double[] seg = Utils.segment(data,ss,ee);
//            double[] filt = Utils.filter(seg);
//            double[] corr=Utils.xcorr_helper(tx_preamble,filt);
//            int max_idx = (int)Utils.max_idx(corr)[0];
//            int xcorr_idx = (Utils.transform_idx(max_idx, seg.length));
//            if (maxval>corr[1]){
//                maxval=corr[1];
//                maxidx=ss+xcorr_idx;
//            }
//        }
//        // calculate # of symbols, and segment
////        decode(rx_file,tx_file);
//    }

//    numrounds = (int) Math.ceil((double)bits.length/valid_carrier.length);
    public static void test_decode(Activity cxt) {
        int tcase=3;
        int[] valid_bins=null;
        double[] data = null;

        if (tcase==1) {
            valid_bins = new int[]{0,59};
            data = Utils.convert(FileOperations.readrawasset_binary(cxt, R.raw.data));
        }
        else if (tcase==2) {
            valid_bins = new int[]{0,9};
            data = Utils.convert(FileOperations.readrawasset_binary(cxt, R.raw.data2));
        }
        else if (tcase==3) {
            valid_bins = new int[]{0,59};
            data = (FileOperations.readrawasset2(cxt, R.raw.data3,1));
        }
        decode_helper(cxt,data,valid_bins);
    }

    public static void decode_helper(Activity av, double[] data, int[] valid_bins) {
        valid_bins[0]=valid_bins[0]+20;
        valid_bins[1]=valid_bins[1]+20;
        Log.e("decoder",data.length+","+valid_bins[0]+valid_bins[1]);

        int[] meta = SymbolGeneration.getMeta(Utils.arange(valid_bins[0],valid_bins[1]));

        data = Utils.filter(data);

        int ptime = (int)((Constants.preambleTime/1000.0)*Constants.fs);
        int start = ptime+Constants.ChirpGap;
        double[] rx_train=Utils.segment(data,start+Constants.Cp,start+Constants.Cp+Constants.Ns-1);
        start = start+Constants.Cp+Constants.Ns;

        double [] tx_train = Utils.convert(SymbolGeneration.getTrainingSymbol(Utils.arange(valid_bins[0],valid_bins[1])));
        tx_train = Utils.segment(tx_train,Constants.Cp,Constants.Cp+Constants.Ns-1);

        double[][] tx_spec = Utils.fftcomplexoutnative_double(tx_train, tx_train.length);
        double[][] rx_spec = Utils.fftcomplexoutnative_double(rx_train, rx_train.length);

        double[][] weights = Utils.dividenative(tx_spec, rx_spec);

        double[][] rx_spec2 = Utils.timesnative(rx_spec, weights);
        short[] training_bits = Modulation.pskdemod(rx_spec2, Utils.arange(valid_bins[0], valid_bins[1]));

        String coded = "";
        if (Constants.DIFFERENTIAL) {
            int numsyms = meta[0]; // number of data symbols
            double[][][] symbols = new double[numsyms + 1][][];
            symbols[0] = rx_spec2;

            for (int i = 0; i < numsyms; i++) {
                double[] sym = Utils.segment(data, start + Constants.Cp, start + Constants.Cp + Constants.Ns - 1);
                start = start + Constants.Cp + Constants.Ns;

                double[][] sym_spec = Utils.fftcomplexoutnative_double(sym, sym.length);

                sym_spec = Utils.timesnative(sym_spec, weights);
                symbols[i + 1] = sym_spec;
            }

            short[][] bits = Modulation.pskdemod_differential(symbols, valid_bins);

            Log.e("meta", "number of symbols " + bits.length);
            // for each symbol
            for (int i = 0; i < bits.length; i++) {
                short[] newbits = bits[i];
                if (Constants.INTERLEAVE) {
                    newbits = SymbolGeneration.unshuffle(bits[i], i);
                }
                // extract the data bits
                for (int j = 0; j < meta[i + 1]; j++) {
                    coded += newbits[j] + "";
                }
            }
        }
        else {
            int numsyms = meta[0];
            for (int i = 0; i < numsyms; i++) {
                double[] sym = Utils.segment(data, start + Constants.Cp, start + Constants.Cp + Constants.Ns - 1);
                start = start + Constants.Cp + Constants.Ns;

                double[][] sym_spec = Utils.fftcomplexoutnative_double(sym, sym.length);

                sym_spec = Utils.timesnative(sym_spec, weights);

                short[] bits = Modulation.pskdemod(sym_spec, Utils.arange(valid_bins[0], valid_bins[1]));
                short[] newbits = bits;
                if (Constants.INTERLEAVE) {
                    newbits = SymbolGeneration.unshuffle(bits, i);
                }
                for (int j = 0; j < meta[i + 1]; j++) {
                    coded += newbits[j] + "";
                }
            }
        }

        String uncoded = "";
        if (Constants.CODING) {
            uncoded = Utils.decode(coded, Constants.cc[0],Constants.cc[1],Constants.cc[2]);
        }
        else {
            uncoded = coded;
        }

        int messageID=Integer.parseInt(uncoded,2);

        String message="Error";
        if (Constants.mmap.containsKey(messageID)) {
            message = Constants.mmap.get(messageID);
        }
        Utils.log(coded +"=>"+uncoded+"=>"+message);

        String finalMessage = message;

        if (SPEECH_OUT) {
            Constants.tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }

        av.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(av, finalMessage,
//                        Toast.LENGTH_LONG).show();

//                Snackbar sb = Snackbar.make(Constants.vv, finalMessage, Snackbar.LENGTH_INDEFINITE);
//                sb.getView().setBackgroundColor(av.getColor(R.color.colorDarkRed));
//                sb.show();
                Utils.sendNotification(av, "Notification",finalMessage, R.drawable.warning2);
                Constants.msgview.setText(finalMessage);
            }
        });
    }

    public static void decode(double[] rx_file, double[] tx_file) {
        Log.e(Constants.LOG, "SignalProcessing_decode "+rx_file.length+","+tx_file.length);
        long t1 = System.currentTimeMillis();

        int rx_idx = 0;
        int tx_idx =  0;

        short[][] bits_rx = new short[Constants.Nsyms][valid_carrier_preamble.length];
        short[][] bits_tx = new short[Constants.Nsyms][valid_carrier_preamble.length];

        double sym_level_ber = 0;
//        for (int sym = 0; sym < Constants.Nsyms; sym++) {
        for (int sym = 0; sym < 24; sym++) {
            int len_rx = 0;
            if (Constants.eqMethod == Constants.EqMethod.Freq) {
                len_rx = Constants.Ns;
            }
            else if (Constants.eqMethod == Constants.EqMethod.Time) {
                len_rx = Constants.Ns + Constants.tap_num - 1;
            }

            int tx_start = tx_idx+Constants.Cp;
            int tx_end = tx_idx + Constants.Cp + Constants.Ns - 1;
            int rx_start = (rx_idx-Constants.sync_offset)+Constants.Cp;
            int rx_end = (rx_idx-Constants.sync_offset)+Constants.Cp+Constants.Ns-1;

            if (tx_end-1 > tx_file.length) {
                Utils.log("Error extracting tx symbol from decoder");
                break;
            }
            if (rx_end-1 > rx_file.length) {
                Utils.log("Error extracting rx symbol from decoder");
                break;
            }

            double[] symbol_tx = Utils.segment(tx_file, tx_start, tx_end);
            double[] symbol_rx = Utils.segment(rx_file, rx_start, rx_end);

//            if (Constants.pilots.contains(sym)) {
            if (sym==0) {
//                t1 = System.currentTimeMillis();
                Equalizer.equalizer_estimation(symbol_tx, symbol_rx);
//                Log.e(Constants.LOG,"train "+(System.currentTimeMillis()-t1));
            }

//            t1 = System.currentTimeMillis();
            double[][] spec_rx = null;
            double[] symbol_pred = null;
            if (Constants.eqMethod == Constants.EqMethod.Freq) {
                spec_rx = Equalizer.equalizer_recover_freq(symbol_rx, sym);
            }
            else if (Constants.eqMethod == Constants.EqMethod.Time) {
                //symbol_pred = Equalizer.equalizer_recover_time(symbol_rx);
                spec_rx = Utils.fftcomplexoutnative_double(symbol_pred, Constants.Ns);
            }

            double[][] spec_tx = Utils.fftcomplexoutnative_double(symbol_tx, Constants.Ns);

//            t1 = System.currentTimeMillis();
            bits_rx[sym] = Modulation.pskdemod(spec_rx, valid_carrier_preamble);
            bits_tx[sym] = Modulation.pskdemod(spec_tx, valid_carrier_preamble);

//            double ber = ber(bits_rx[sym],bits_tx[sym]);
//            sym_level_ber += ber;
//            Log.e(Constants.LOG,String.format("demod %d %.2f",sym,ber));

            tx_idx += Constants.sym_len;
            rx_idx += Constants.sym_len;
        }
        Log.e("timer2",""+(System.currentTimeMillis()-t1));

        Log.e(LOG, "BER by sym "+sym_level_ber);
        ber_by_freq(bits_rx, bits_tx);
    }

    public static void ber_by_freq(short[][] all_bits_rx, short[][] all_bits_tx) {
        Utils.log("SignalProcessing_ber_by_freq");
        double mean_err=0;
        for (int i = 0; i < valid_carrier_preamble.length; i++) {
            short[] bits_rx = new short[Constants.Nsyms];
            short[] bits_tx = new short[Constants.Nsyms];
            for (int sym = 0; sym < Constants.Nsyms; sym++) {
                bits_rx[sym] = all_bits_rx[sym][i];
                bits_tx[sym] = all_bits_tx[sym][i];
            }
            double err = ber(bits_rx,bits_tx);
            mean_err += err;
            Utils.log(String.format("%d %d %.2f", valid_carrier_preamble[i],Constants.f_seq.get(valid_carrier_preamble[i]),err));
        }
        Utils.log( "Mean BER "+(mean_err/ valid_carrier_preamble.length));
    }

    public static double ber(short[] bits_rx, short[] bits_tx) {
        int matches=0;
        for (int i = 0; i < bits_rx.length; i++) {
            matches += (bits_rx[i] == bits_tx[i]) ? 1 : 0;
        }
        double acc = (double)matches /  bits_rx.length;
        return 1-acc;
    }
}
