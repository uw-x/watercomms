package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;
import static com.example.root.ffttest2.Constants.xcorr_method;

import android.app.Activity;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class ChannelEstimate {
//    public static int[] extractSignal_withchirp(Activity av, double[] rec) {
//        Log.e(LOG,"ChannelEstimate_extractSignal "+rec.length);
//
//        double[] filt=Utils.copyArray(rec);
//
//        filt = Utils.filter(filt);
//        Log.e(LOG,"finish filtering");
//
//        long t1 = System.currentTimeMillis();
//        double[] tx_preamble = ChirpGen.preamble_d();
//        int start_point = Utils.xcorr(tx_preamble, filt, filt.length);
//        Log.e(LOG,"xcorr "+start_point+":"+(System.currentTimeMillis()-t1)+":"+filt.length);
//
//        int rx_preamble_start = start_point;
//        int rx_preamble_end = rx_preamble_start+(int)(((Constants.preambleTime/1000.0)*Constants.fs))-1;
//        int rx_preamble_len = (rx_preamble_end-rx_preamble_start)+1;
////        Utils.log("preamble "+rec.length+","+rx_preamble_start+","+rx_preamble_end+","+rx_preamble_len);
//
//        if (rx_preamble_end-1 > rec.length || rx_preamble_start < 0) {
//            Utils.log("Error extracting preamble from sounding signal "+rx_preamble_start+","+rx_preamble_end);
//            return Constants.valid_carrier_preamble;
//        }
//        double[] rx_preamble = Utils.segment(rec, rx_preamble_start, rx_preamble_end);
//
////        int rx_sym_start = rx_preamble_end+1+Constants.ChirpGap+Constants.Cp;
////        int rx_sym_end = rx_sym_start+Constants.Ns-1;
////        int rx_sym_len = (rx_sym_end-rx_sym_start)+1;
////
////        if (rx_sym_end-1 > rec.length || rx_sym_start < 0) {
////            Utils.log("Error extracting preamble from channel estimate");
////            return Constants.default_valid_carrier;
////        }
////        Log.e(LOG, "sym "+rec.length+","+rx_sym_start+","+rx_sym_end+","+rx_sym_len);
////        double[] rx_symbol = Utils.segment(rec, rx_sym_start, rx_sym_end);
////
////        double[] spec_symbol = Utils.fftnative_double(rx_symbol, Constants.Ns);
////
//        int noise_start = rx_preamble_start-rx_preamble.length;
////        if (rx_preamble_start > 6000) {
////            noise_start = 5000;
////        }
////        else {
////            noise_start = rx_preamble_end + 1000;
////        }
//
//        int noise_end = noise_start+rx_preamble.length-1;
////        int noise_len = (noise_end-noise_start)+1;
////        Log.e(LOG, "noise "+rec.length+","+noise_start+","+noise_end+","+noise_len);
////
//        if (noise_end-1 > rec.length || noise_start < 0) {
//            Utils.log("Error extracting noise from sounding signal "+noise_start+","+noise_end);
//            return Constants.valid_carrier_preamble;
//        }
//        double[] noise = Utils.segment(rec, noise_start, noise_end);
//        double[] spec_noise = Utils.fftnative_double(noise, noise.length);
//        double[] spec_noise_db = Utils.mag2db(spec_noise);
//
//        double[] spec_preamble = Utils.fftnative_double(rx_preamble, rx_preamble.length);
//        double[] spec_preamble_db = Utils.mag2db(spec_preamble);
////        double[] spec_symbol_db = Utils.mag2db(spec_symbol);
//
//        av.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Display.plotSpectrum(Constants.gview, spec_preamble_db, true, MainActivity.av.getResources().getColor(R.color.purple_500),"");
//                Display.plotSpectrum(Constants.gview, spec_noise_db, false, MainActivity.av.getResources().getColor(R.color.black),"S/N of preamble");
//                Display.plotVerticalLine(Constants.gview, Constants.f_seq.get(Constants.nbin1_chanest));
//                Display.plotVerticalLine(Constants.gview, Constants.f_seq.get(Constants.nbin2_chanest));
//
////                Display.plotSpectrum(Constants.gview2, spec_symbol_db, true,MainActivity.av.getResources().getColor(R.color.purple_500),"");
////                Display.plotSpectrum(Constants.gview2, spec_noise_db, false,MainActivity.av.getResources().getColor(R.color.black),"S/N of symbol");
//
////                Display.plotHorizontalLine(Constants.gview2, Constants.SNR_THRESH);
////                Display.plotVerticalLine(Constants.gview2, Constants.f_seq.get(Constants.nbin1));
////                Display.plotVerticalLine(Constants.gview2, Constants.f_seq.get(Constants.nbin2));
//            }
//        });
//
////        Fre_adapt_ret est_preamble = Fre_adaptation.select_fre_bins(rx_preamble, rx_preamble,
////                Constants.preambleStartFreq, Constants.preambleEndFreq, -20);
////        int[] est_symbol = estimate(spec_symbol, spec_noise);
//
////        String est_preamble_freqs="";
////        for (Integer i : est_preamble.freqs) {
////            est_preamble_freqs+=Constants.f_seq.get(i)+",";
////        }
//
////        String est_sym_freqs="";
////        for (Integer i : est_symbol) {
////            est_sym_freqs+=Constants.f_seq.get(i)+",";
////        }
////        Utils.log("preamble freqs "+est_preamble.freqs.length+"\n"+est_preamble_freqs);
////        Utils.log("symbol freqs "+est_symbol.length+"\n"+est_sym_freqs);
//
////        if (Constants.est_sig.equals(Constants.EstSignalType.Chirp)) {
////            return est_preamble.freqs;
////        }
////        else if (Constants.est_sig.equals(Constants.EstSignalType.Symbol)) {
////            return est_symbol;
////        }
//        return null;
//    }

    public static int xcorr_helper(double[] rec, Constants.SignalType sigType) {
        double[] filt = Utils.copyArray(rec);

        filt=Utils.filter(filt);
        Log.e(LOG, "finish filtering");

        double[] tx_preamble = ChirpGen.preamble_d();

        int start_point = Utils.xcorr(tx_preamble, filt, rec, filt.length, sigType);
//        if (start_point > Constants.butterworthFiltOffset) {
//            start_point -= Constants.butterworthFiltOffset;
//        }
        if (start_point-Constants.besselFiltOffset >= 0) {
            start_point -= Constants.besselFiltOffset;
        }
        return start_point;
    }

    public static int[] extractSignal_withsymbol(Activity av, double[] rec, int m_attempt, Constants.SignalType sigType) {
        Log.e(LOG, "ChannelEstimate_extractSignal " + rec.length);
        int start_point = xcorr_helper(rec, sigType);
        Log.e("start",""+start_point);
        return extractSignal_withsymbol_helper(av, rec, start_point, m_attempt);
    }

    public static int[] extractSignal_withsymbol_helper(Activity av, double[] rec, int start_point, int m_attempt) {
        long t1 = System.currentTimeMillis();

//        Log.e("xcorr", "runtime " + (System.currentTimeMillis() - t1) + "");

        Log.e(LOG, "xcorr " + start_point + ":" + (System.currentTimeMillis() - t1) + ":" + rec.length);

        int rx_preamble_start = start_point;
        rx_preamble_start+=240;

        int rx_preamble_end = rx_preamble_start + (int) (((Constants.preambleTime / 1000.0) * Constants.fs)) - 1;
//        int rx_preamble_len = (rx_preamble_end - rx_preamble_start) + 1;
//        Utils.log("preamble "+rec.length+","+rx_preamble_start+","+rx_preamble_end+","+rx_preamble_len);

        if (rx_preamble_end - 1 > rec.length || rx_preamble_start < 0) {
            Utils.log("Error extracting preamble from sounding signal " + rx_preamble_start + "," + rx_preamble_end);
//            return Constants.valid_carrier_default;
            return new int[]{};
        }
        double[] rx_preamble = Utils.segment(rec, rx_preamble_start, rx_preamble_end);
        double[] rx_preamble_db = Utils.mag2db(Utils.fftnative_double(rx_preamble, rx_preamble.length));

        ////////////////////////////////////////////////////////////////////////////////////

//        int rx_sym_start = rx_preamble_end + Constants.ChirpGap + 1 + (Constants.Cp * Constants.chanest_symreps);
        int rx_sym_start = rx_preamble_end + Constants.ChirpGap + 1;
        int rx_sym_end = rx_sym_start + ((Constants.Ns + Constants.Cp)* Constants.chanest_symreps) - 1;
        int rx_sym_len = (rx_sym_end - rx_sym_start) + 1;

        if (rx_sym_end - 1 > rec.length || rx_sym_start < 0) {
            Utils.log("Error extracting preamble from sounding signal");
//            return Constants.valid_carrier_default;
            return new int[]{};
        }
        Log.e(LOG, "sym " + rec.length + "," + rx_sym_start + "," + rx_sym_end + "," + rx_sym_len);
        double[] rx_symbols = Utils.segment(rec, rx_sym_start, rx_sym_end);
        rx_symbols = Utils.div(rx_symbols,30000);

//        double[] spec_symbol = Utils.fftnative_double(rx_symbols, rx_symbols.length);
//        double[] spec_symbol_db = Utils.mag2db(Utils.fftnative_double(rx_symbols, rx_symbols.length));

        double[] finalRx_symbols = rx_symbols;
        av.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Display.plotSpectrum(Constants.gview, rx_preamble_db, true,
                        MainActivity.av.getResources().getColor(R.color.purple_500),
                        "Rx preamble");
                Display.plotVerticalLine(Constants.gview, Constants.f_seq.get(Constants.nbin1_default));
                Display.plotVerticalLine(Constants.gview, Constants.f_seq.get(Constants.nbin2_default));

                int cc=Constants.Cp;
                for (int i = 0; i < Constants.chanest_symreps; i++) {
//                for (int i = 0; i < 1; i++) {
                    double[] seg = Utils.segment(finalRx_symbols, cc, cc + Constants.Ns - 1);
                    double[] spec = Utils.mag2db(Utils.fftnative_double(seg,seg.length));
                    if (i==0) {
                        Display.plotSpectrum(Constants.gview2, spec, true,
                                MainActivity.av.getResources().getColor(R.color.red), "Symbol");
                    }
                    else if (i==1) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.orange), "Symbol");
                    }
                    else if (i==2) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.yellow), "Symbol");
                    }
                    else if (i==3) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.green), "Symbol");
                    }
                    else if (i==4) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.blue), "Symbol");
                    }
                    else if (i==5) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.purple), "Symbol");
                    }
                    else if (i==6) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.black), "Symbol");
                    }
                    else {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.purple_500), "Symbol");
                    }
                    cc+=Constants.Ns+Constants.Cp;
                }

//                Display.plotHorizontalLine(Constants.gview2, Constants.SNR_THRESH);
//                Display.plotVerticalLine(Constants.gview2, Constants.f_seq.get(Constants.nbin1));
//                Display.plotVerticalLine(Constants.gview2, Constants.f_seq.get(Constants.nbin2));
            }
        });

        int freqSpacing = Constants.fs/Constants.Ns;
        int[] fseq = Utils.linspace(Constants.f_range[0],freqSpacing,Constants.f_range[1]);

//        int method = 2;
        double[] snrs=null;
        int thresh=0;
        Log.e("asdf","SNR METHOD "+Constants.snr_method);
//        if (Constants.snr_method == 1) {
//            snrs = calculateSNR_null(spec_symbol);
//            thresh=Constants.SNR_THRESH1;
//        } else if (Constants.snr_method == 2) {
            int cc=Constants.Cp;
            double [][][] spec_est = new double[2][Constants.subcarrier_number_default][Constants.chanest_symreps];
            for (int i = 0; i < Constants.chanest_symreps; i++) {
                double[] seg = Utils.segment(rx_symbols,cc,cc+Constants.Ns-1);
                double[][] spec = Utils.fftcomplexoutnative_double(seg,seg.length);

                int bin_counter=0;
                for (Integer bin : Constants.valid_carrier_default) {
                    double realPart=spec[0][bin];
                    double imagPart=spec[1][bin];
                    spec_est[0][bin_counter][i] = realPart;
                    spec_est[1][bin_counter++][i] = imagPart;
                }

                cc+=Constants.Ns+Constants.Cp;
            }
//            if (Constants.FLIP_SYMBOL) {
//                snrs = SNR_freq.calculate_snr2(spec_est, Constants.pn60_syms);
//            }
//            else {
                t1 = System.currentTimeMillis();
                if (Constants.subcarrier_number_default == 20) {
                    snrs = SNR_freq.calculate_snr(spec_est, Constants.pn20_syms, 1, Constants.chanest_symreps);
        //                    snrs = SNR_freq.calculate_snr(spec_est, Constants.pn20_syms, 0, 5);
                }
                else if (Constants.subcarrier_number_default == 40) {
                    snrs = SNR_freq.calculate_snr(spec_est, Constants.pn40_syms, 1, Constants.chanest_symreps);
        //                    snrs = SNR_freq.calculate_snr(spec_est, Constants.pn40_syms, 0, 5);
                }
                else if (Constants.subcarrier_number_default == 60) {
                    snrs = SNR_freq.calculate_snr(spec_est, Constants.pn60_syms, 1, Constants.chanest_symreps);
        //                    snrs = SNR_freq.calculate_snr(spec_est, Constants.pn60_syms, 0,5);
                }
                else if (Constants.subcarrier_number_default == 120) {
                    snrs = SNR_freq.calculate_snr(spec_est, Constants.pn120_syms, 1, Constants.chanest_symreps);
                }
                else if (Constants.subcarrier_number_default == 300) {
                    snrs = SNR_freq.calculate_snr(spec_est, Constants.pn300_syms, 1, Constants.chanest_symreps);
                }
                else if (Constants.subcarrier_number_default == 600) {
                    snrs = SNR_freq.calculate_snr(spec_est, Constants.pn600_syms, 1, Constants.chanest_symreps);
                }
                Log.e("timer2",(System.currentTimeMillis()-t1)+"");
//            }
            thresh=Constants.SNR_THRESH2;
//        }

        FileOperations.writetofile(MainActivity.av, Constants.snr_method + "",
                Utils.genName(Constants.SignalType.SNRMethod, m_attempt) + ".txt");

        int[] freqs = new int[]{-1,-1};
        int[] selected = null;
        t1 = System.currentTimeMillis();
        if (Constants.AdaptationMethod == 2) {
            selected = Fre_adaptation.select_fre_bins2(snrs, fseq, thresh);
        }
        else if (Constants.AdaptationMethod == 3) {
            selected = Fre_adaptation.select_fre_bins3(snrs, fseq, thresh);
        }
        Log.e("timer2",(System.currentTimeMillis()-t1)+"");
        Log.e(LOG, "selected "+Arrays.toString(selected));
//        selected=new int[]{0,1};
        if (selected.length==2&&selected[0] != -1 && selected[1] != -1) {
            if (selected[1]-selected[0]==1) {
                if (selected[0] > 0) {
                    selected[0] -= 1;
                }
                else {
                    selected[1] += 1;
                }
            }
            freqs = new int[selected.length];
            for (int i = 0; i < selected.length; i++) {
                freqs[i] = fseq[selected[i]];
            }
        }

//        Utils.log(Arrays.toString(Utils.convert(snrs)));
//        Utils.log(Arrays.toString(freqs));

        FileOperations.writetofile(MainActivity.av, Utils.trim(Arrays.toString(snrs)),
                Utils.genName(Constants.SignalType.SNRs, m_attempt) + ".txt");
        FileOperations.writetofile(MainActivity.av, freqs,
                Utils.genName(Constants.SignalType.FreqEsts, m_attempt) + ".txt");

        return selected;
    }


    public static int[] estimate(double[] spectrum, double[] noise) {
        LinkedList<Integer> bins = new LinkedList<>();
        for (Integer bin : Constants.valid_carrier_preamble) {
            int snr = (int)(20*Math.log10(spectrum[bin]) - 20*Math.log10(noise[bin]));
            if (snr > Constants.SNR_THRESH) {
                bins.add(bin);
            }
        }

        LinkedList<Integer> filtered_bins = new LinkedList<>();
        int runlen=0;
        int run_len_size = 2;
        for (int i = 0; i < bins.size()-1; i++) {
            if (bins.get(i+1) - bins.get(i) == 1) {
                runlen += 1;
            }
            else {
                if (runlen > run_len_size-1) {
                    for (int j = 0; j <= runlen; j++) {
                        filtered_bins.add(bins.get(i-j));
                    }
                }
                runlen=0;
            }
        }
        if (runlen > run_len_size-1) {
            for (int j = 0; j <= runlen; j++) {
                filtered_bins.add(bins.get(bins.size()-1-j));
            }
        }

        Collections.sort(filtered_bins);
        String snrs = "";
        for (Integer i : filtered_bins) {
            int snr = (int)(20*Math.log10(spectrum[i]) - 20*Math.log10(noise[i]));
            snrs += snr+",";
        }

//        Utils.log("SNR estimates: "+snrs);
        return Utils.convert(filtered_bins);
    }
}
