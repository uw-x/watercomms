package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;
import static com.example.root.ffttest2.Constants.xcorr_method;

import android.app.Activity;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class ChannelEstimate {
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

        Log.e(LOG, "xcorr " + start_point + ":" + (System.currentTimeMillis() - t1) + ":" + rec.length);

        int rx_preamble_start = start_point;
        rx_preamble_start+=240;

        int rx_preamble_end = rx_preamble_start + (int) (((Constants.preambleTime / 1000.0) * Constants.fs)) - 1;

        if (rx_preamble_end - 1 > rec.length || rx_preamble_start < 0) {
            Utils.log("Error extracting preamble from sounding signal " + rx_preamble_start + "," + rx_preamble_end);
            return new int[]{};
        }
        double[] rx_preamble = Utils.segment(rec, rx_preamble_start, rx_preamble_end);
        double[] rx_preamble_db = Utils.mag2db(Utils.fftnative_double(rx_preamble, rx_preamble.length));

        ////////////////////////////////////////////////////////////////////////////////////

        int rx_sym_start = rx_preamble_end + Constants.ChirpGap + 1;
        int rx_sym_end = rx_sym_start + ((Constants.Ns + Constants.Cp)* Constants.chanest_symreps) - 1;
        int rx_sym_len = (rx_sym_end - rx_sym_start) + 1;

        if (rx_sym_end - 1 > rec.length || rx_sym_start < 0) {
            Utils.log("Error extracting preamble from sounding signal");
            return new int[]{};
        }
        Log.e(LOG, "sym " + rec.length + "," + rx_sym_start + "," + rx_sym_end + "," + rx_sym_len);
        double[] rx_symbols = Utils.segment(rec, rx_sym_start, rx_sym_end);
        rx_symbols = Utils.div(rx_symbols,30000);

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
            }
        });

        int freqSpacing = Constants.fs/Constants.Ns;
        int[] fseq = Utils.linspace(Constants.f_range[0],freqSpacing,Constants.f_range[1]);

        double[] snrs=null;
        int thresh=0;
        Log.e("asdf","SNR METHOD "+Constants.snr_method);
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

        if (Constants.subcarrier_number_default == 20) {
            snrs = SNR_freq.calculate_snr(spec_est, Constants.pn20_syms, 1, Constants.chanest_symreps);
        }
        else if (Constants.subcarrier_number_default == 40) {
            snrs = SNR_freq.calculate_snr(spec_est, Constants.pn40_syms, 1, Constants.chanest_symreps);
        }
        else if (Constants.subcarrier_number_default == 60) {
            snrs = SNR_freq.calculate_snr(spec_est, Constants.pn60_syms, 1, Constants.chanest_symreps);
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

        thresh=Constants.SNR_THRESH2;

        FileOperations.writetofile(MainActivity.av, Constants.snr_method + "",
                Utils.genName(Constants.SignalType.SNRMethod, m_attempt) + ".txt");

        int[] freqs = new int[]{-1,-1};
        int[] selected = null;
        selected = Fre_adaptation.select_fre_bins(snrs, thresh);

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

        FileOperations.writetofile(MainActivity.av, Utils.trim(Arrays.toString(snrs)),
                Utils.genName(Constants.SignalType.SNRs, m_attempt) + ".txt");
        FileOperations.writetofile(MainActivity.av, freqs,
                Utils.genName(Constants.SignalType.FreqEsts, m_attempt) + ".txt");

        return selected;
    }
}
