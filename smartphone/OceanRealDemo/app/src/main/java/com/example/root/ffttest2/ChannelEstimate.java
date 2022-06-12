package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;

import android.app.Activity;
import android.util.Log;

import java.util.Arrays;

public class ChannelEstimate {
    public static int[] extractSignal_withsymbol_helper(Activity av, double[] rec, int start_point, int m_attempt) {
        int rx_preamble_start = start_point;
        rx_preamble_start+=240;

        int rx_preamble_end = rx_preamble_start + (int) (((Constants.preambleTime / 1000.0) * Constants.fs)) - 1;

        if (rx_preamble_end - 1 > rec.length || rx_preamble_start < 0) {
            Utils.log("Error extracting preamble from sounding signal " + rx_preamble_start + "," + rx_preamble_end);
            return new int[]{};
        }

        ////////////////////////////////////////////////////////////////////////////////////

        int rx_sym_start = rx_preamble_end + Constants.ChirpGap + 1;
        int rx_sym_end = rx_sym_start + ((Constants.Ns + Constants.Cp)* Constants.chanest_symreps) - 1;

        if (rx_sym_end - 1 > rec.length || rx_sym_start < 0) {
            Utils.log("Error extracting preamble from sounding signal");
            return new int[]{};
        }

        double[] rx_symbols = Utils.segment(rec, rx_sym_start, rx_sym_end);
        rx_symbols = Utils.div(rx_symbols,30000);

        plotRxSyms(av, rx_symbols);

        int freqSpacing = Constants.fs/Constants.Ns;
        int[] fseq = Utils.linspace(Constants.f_range[0],freqSpacing,Constants.f_range[1]);

        double[] snrs=null;
        int thresh=0;
        Log.e("asdf","SNR METHOD "+Constants.snr_method);
        int cc=Constants.Cp;
        double [][][] spec_est = new double[2][Constants.subcarrier_number_default][Constants.chanest_symreps];
        for (int i = 0; i < Constants.chanest_symreps; i++) {
            Log.e("asdf","fft");
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

        snrs = SNR_freq.calculate_snr(spec_est, Constants.pn60_syms, 1, Constants.chanest_symreps);

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

    public static void plotRxSyms(Activity av, double[] rx_symbols) {
        double[] finalRx_symbols = rx_symbols;
        av.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int cc=Constants.Cp;
                for (int i = 0; i < Constants.chanest_symreps; i++) {
                    double[] seg = Utils.segment(finalRx_symbols, cc, cc + Constants.Ns - 1);
                    double[] spec = Utils.mag2db(Utils.fftnative_double(seg,seg.length));
                    if (i==0) {
                        Display.plotSpectrum(Constants.gview2, spec, true,
                                MainActivity.av.getResources().getColor(R.color.red), "");
                    }
                    else if (i==1) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.orange), "");
                    }
                    else if (i==2) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.yellow), "");
                    }
                    else if (i==3) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.green), "");
                    }
                    else if (i==4) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.blue), "");
                    }
                    else if (i==5) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.purple), "");
                    }
                    else if (i==6) {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.black), "");
                    }
                    else {
                        Display.plotSpectrum(Constants.gview2, spec, false,
                                MainActivity.av.getResources().getColor(R.color.purple_500), "Symbol");
                    }
                    cc+=Constants.Ns+Constants.Cp;
                    break;
                }
            }
        });
    }
}
