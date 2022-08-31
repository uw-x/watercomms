package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;
import static com.example.root.ffttest2.Constants.XCORR_MAX_VAL_HEIGHT_FAC;
import static com.example.root.ffttest2.Constants.fbackTime;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static double[] copyArray2(Double[] sig) {
        double[] out = new double[sig.length];
        for (int i = 0; i < sig.length; i++) {
            out[i]=sig[i];
        }
        return out;
    }

    public static String pad2(String str) {
        int padlen=Constants.maxbits-str.length();
        String out = "";
        int counter=0;
        for (int i = 0; i < padlen; i++) {
            out+="0";
        }
        return out+str;
    }

    public static void log(String s) {
        Log.e(Constants.LOG,s);
        (MainActivity.av).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Constants.debugPane.getText().toString().length() > 400){
                    Constants.debugPane.setText("");
                }
                Constants.debugPane.setText(Constants.debugPane.getText()+"\n"+s);
                scrollToBottom();
            }
        });
    }

//    public static boolean isSoundingSignal(Activity av, double[] rec) {
//        long t1 = System.currentTimeMillis();
//
//        int start_point = ChannelEstimate.xcorr_helper(rec);
//        Log.e("issounding",start_point+"");
//
////        Log.e("xcorr", "runtime " + (System.currentTimeMillis() - t1) + "");
//
//        int rx_preamble_start = start_point;
//        int rx_preamble_end = rx_preamble_start + (int) (((Constants.preambleTime / 1000.0) * Constants.fs)) - 1;
////        int rx_preamble_len = (rx_preamble_end - rx_preamble_start) + 1;
////        Utils.log("preamble "+rec.length+","+rx_preamble_start+","+rx_preamble_end+","+rx_preamble_len);
//
//        if (rx_preamble_end - 1 > rec.length || rx_preamble_start < 0) {
//            Utils.log("Error extracting preamble from sounding signal " + rx_preamble_start + "," + rx_preamble_end);
////            return Constants.valid_carrier_default;
//            return false;
//        }
//        double[] rx_preamble = Utils.segment(rec, rx_preamble_start, rx_preamble_end);
////        double[] rx_preamble_db = Utils.mag2db(Utils.fftnative_double(rx_preamble, rx_preamble.length));
//
//        ////////////////////////////////////////////////////////////////////////////////////
//
////        int rx_sym_start = rx_preamble_end + Constants.ChirpGap + 1 + (Constants.Cp * Constants.chanest_symreps);
//        int rx_sym_start = rx_preamble_end + Constants.ChirpGap + 1;
//        int rx_sym_end = rx_sym_start + (Constants.Ns * Constants.chanest_symreps) - 1;
//        int rx_sym_len = (rx_sym_end - rx_sym_start) + 1;
//
//        if (rx_sym_end - 1 > rec.length || rx_sym_start < 0) {
//            Utils.log("Error extracting preamble from sounding signal");
////            return Constants.valid_carrier_default;
//            return false;
//        }
//        Log.e(LOG, "sym " + rec.length + "," + rx_sym_start + "," + rx_sym_end + "," + rx_sym_len);
//        double[] rx_symbols = Utils.segment(rec, rx_sym_start, rx_sym_end);
//        rx_symbols = Utils.div(rx_symbols,30000);
//
//        double[] spec_symbol = Utils.fftnative_double(rx_symbols, rx_symbols.length);
//
////        int freqSpacing = Constants.fs/Constants.Ns;
////        int[] fseq = Utils.linspace(Constants.f_range[0],freqSpacing,Constants.f_range[1]);
//
//        double[] snrs = new double[Constants.valid_carrier_preamble.length];
//
//        int scounter=0;
//        Log.e("issounding",Constants.valid_carrier_preamble.length+"");
//        for (Integer bin : Constants.valid_carrier_preamble) {
//            Log.e("issounding",bin+","+snrs.length);
//            double signal = spec_symbol[bin];
//            double noise = Utils.mean(Utils.segment(spec_symbol,bin+2,bin+5));
//            double snr = signal-noise;
//            Log.e("issounding",signal+","+noise);
//            snrs[scounter++]=snr;
//        }
//
//        double msnr = Utils.mean(snrs);
//        Log.e("issounding",msnr+"");
//
//        if (msnr < 15) {
//            return false;
//        }
//
//        return true;
//    }

    public static double[] sum(double[] a, double[] b) {
        double[] out = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = a[i]+b[i];
        }
        return out;
    }

    public static double[] concat(Double[] a, Double[] b) {
        double[] out = new double[a.length+b.length];
        int counter=0;
        for (int i = 0; i < a.length; i++) {
            out[counter++] = a[i];
        }
        for (int i = 0; i < b.length; i++) {
            out[counter++] = b[i];
        }
        return out;
    }

    public static double[] concat3(Double[] a, Double[] b, Double[] c) {
        double[] out = new double[a.length+b.length+c.length];
        int counter=0;
        for (int i = 0; i < a.length; i++) {
            out[counter++] = a[i];
        }
        for (int i = 0; i < b.length; i++) {
            out[counter++] = b[i];
        }
        for (int i = 0; i < c.length; i++) {
            out[counter++] = c[i];
        }
        return out;
    }

    public static double[] concat4(Double[] a, Double[] b, Double[] c, Double[] d) {
        double[] out = new double[a.length+b.length+c.length+d.length];
        int counter=0;
        for (int i = 0; i < a.length; i++) {
            out[counter++] = a[i];
        }
        for (int i = 0; i < b.length; i++) {
            out[counter++] = b[i];
        }
        for (int i = 0; i < c.length; i++) {
            out[counter++] = c[i];
        }
        for (int i = 0; i < d.length; i++) {
            out[counter++] = d[i];
        }
        return out;
    }

    public static double[] concat(double[] a, double[] b) {
        double[] out = new double[a.length+b.length];
        int counter=0;
        for (int i = 0; i < a.length; i++) {
            out[counter++] = a[i];
        }
        for (int i = 0; i < b.length; i++) {
            out[counter++] = b[i];
        }
        return out;
    }

    public static short[] concat_short(short[] a, short[] b) {
        short[] out = new short[a.length+b.length];
        int counter=0;
        for (int i = 0; i < a.length; i++) {
            out[counter++] = a[i];
        }
        for (int i = 0; i < b.length; i++) {
            out[counter++] = b[i];
        }
        return out;
    }

    public static double sum(double[] a) {
        double val = 0;
        for (int i = 0; i < a.length; i++) {
            val += a[i];
        }
        return val;
    }

    public static double[] pow(double[] sig, int pow) {
        double[] out = new double[sig.length];
        for (int i = 0; i < sig.length; i++) {
            out[i] = Math.pow(sig[i],pow);
        }
        return out;
    }

    public static void scrollToBottom() {
        Constants.sview.post(new Runnable() {
            public void run() {
                Constants.sview.smoothScrollTo(0, Constants.debugPane.getBottom());
            }
        });
    }

    public static String trim(String s) {
        return s.substring(1,s.length()-1);
    }

    public static String trim_end(String s) {
        if (s.charAt(s.length()-2) == ',') {
            return s.substring(0, s.length() - 2);
        }
        return s;
    }

    public static short[] random_array(int N) {
        Random random = new Random(1);
        short[] out = new short[N];
        for (int i = 0; i < N; i++) {
            out[i] = (short)random.nextInt(2);
        }
        return out;
    }

    public static int LevenshteinDistance(String s0, String s1) {
        int len0 = s0.length() + 1;
        int len1 = s1.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++)
            cost[i] = i;

        // dynamicaly computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {

            // initial cost of skipping prefix in String s1
            newcost[0] = j - 1;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {

                // matching current letters in both strings
                int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete),
                        cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

    public static double[] convert(short[] s) {
        double[] out = new double[s.length];
        for (int i = 0; i < s.length; i++) {
            out[i] = s[i];
        }
        return out;
    }

    public static short[] convert(String s) {
        short[] out = new short[s.length()];
        int counter=0;
        for (int i = 0; i < s.length(); i++) {
            out[counter++]=Short.parseShort(""+s.charAt(i));
        }
        return out;
    }

    public static Double[] convert2(short[] s) {
        Double[] out = new Double[s.length];
        for (int i = 0; i < s.length; i++) {
            out[i] = (double)s[i];
        }
        return out;
    }

    public static int[] convert(double[] s) {
        int[] out = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            out[i] = (int)s[i];
        }
        return out;
    }

    public static short[] convert_s(double[] s) {
        short[] out = new short[s.length];
        for (int i = 0; i < s.length; i++) {
            out[i] = (short)s[i];
        }
        return out;
    }

    public static String genName(Constants.SignalType sigType, int m_attempt) {
//        return Constants.user.toString()+"-"+sigType.toString()+"-"+m_attempt+"-"+Constants.ts;
        return Constants.user.toString()+"-"+sigType.toString()+"-"+m_attempt;
    }

    public static String genName(Constants.SignalType sigType, int m_attempt, int chirpLoopNumber) {
//        return Constants.user.toString()+"-"+sigType.toString()+"-"+m_attempt+"-"+Constants.ts;
        return Constants.user.toString()+"-"+sigType.toString()+"-"+m_attempt+"-"+chirpLoopNumber;
    }

    public static String trimmed_ts() {
        String ts = Constants.ts+"";
        return ts.substring(ts.length()-4,ts.length());
    }

    public static double min(double[] sig, int idx1, int idx2) {
        double min = 100000;
        for (int i = idx1; i < idx2; i++) {
            if (sig[i] < min) {
                min = sig[i];
            }
        }
        return min;
    }

    public static double max(double[] sig, int idx1, int idx2) {
        double max = -100000;
        for (int i = idx1; i < idx2; i++) {
            if (sig[i] > max) {
                max = sig[i];
            }
        }
        return max;
    }

    public static double mean(double[] sig, int idx1, int idx2) {
        double val = 0;
        int counter=0;
        for (int i = idx1; i < idx2; i++) {
            val += sig[i];
            counter++;
        }
        return val/counter;
    }

    public static double mean(double[] sig) {
        double val = 0;
        int counter=0;
        for (int i = 0; i < sig.length; i++) {
            val += sig[i];
            counter++;
        }
        return val/counter;
    }

    public static double var(double[] sig) {
        double val=0;
        double mu = mean(sig);
        for (int i = 0; i < sig.length; i++) {
            val += Math.pow(sig[i]-mu,2);
        }
        return val/sig.length;
    }

    public static double[] max(double[] sig) {
        double max = -100000;
        int max_index = -1;
        for (int i = 0; i < sig.length; i++) {
            if (sig[i] > max) {
                max = sig[i];
                max_index = i;
            }
        }
        double[] max_info = new double[2];
        max_info[0] = max;
        max_info[1] = max_index;
        return max_info;
    }

    public static int[] max(short[] sig) {
        int max = -32767;
        int max_index = -1;
        for (int i = 0; i < sig.length; i++) {
            if (sig[i] > max) {
                max = sig[i];
                max_index = i;
            }
        }
        int[] max_info = new int[2];
        max_info[0] = max;
        max_info[1] = max_index;
        return max_info;
    }

    public static short[] abs(short[] sig) {
        short[] out = new short[sig.length];
        for (int i = 0; i < sig.length; i++) {
            out[i] = (short)Math.abs(sig[i]);
        }
        return out;
    }

    public static int[] convert(List<Integer> sig) {
        int[] out = new int[sig.size()];
        int counter = 0;
        for (Integer i : sig) {
            out[counter++]=i;
        }
        return out;
    }

    public static double[] add(double[] a, double[] b) {
        double[] out = new double[a.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = a[i]+b[i];
        }
        return out;
    }

    public static double[] subtract(double[] a, double[] b) {
        double[] out = new double[a.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = a[i]-b[i];
        }
        return out;
    }

    public static double[] subtract(double[] a, double b) {
        double[] out = new double[a.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = a[i]-b;
        }
        return out;
    }

    public static double mag2db(double data) {
        return 20*Math.log10(data);
    }

    public static double[] mag2db(double[] data) {
        double[] out = new double[data.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = 20*Math.log10(data[i]);
        }
        return out;
    }

    public static double pow2db(double data) {
        return 10*Math.log10(data);
    }

    public static double[] segment(double[] data, int i, int j) {
        double[] out = new double[j-i+1];
        int counter=0;
        for (int k = i; k <= j; k++) {
            out[counter++] = data[k];
        }
        return out;
    }

    public static double[] segment2(double[] data, int i, int j) {
        double[] out = new double[j-i+1];
        int total_len = data.length;

        int counter=0;
        for (int k = i; k <= j; k++) {
            int idx =(k + total_len)%total_len;
            out[counter++] = data[idx];
        }
        return out;
    }

    public static short[] segment(short[] data, int i, int j) {
        short[] out = new short[j-i+1];
        int counter=0;
        for (int k = i; k <= j; k++) {
            out[counter++] = data[k];
        }
        return out;
    }

    public static int[] freqs2bins(int[] freqs) {
        int[] bins = new int[freqs.length];
        int freqSpacing = Constants.fs/Constants.Ns;
        for (int i = 0; i < freqs.length; i++) {
            bins[i] = freqs[i]/freqSpacing;
        }
        return bins;
    }

    public static double[][] segment(double[][] data, int i, int j) {
        double[][] out = new double[data.length][j-1+1];
        for (int a = 0; a < data.length; a++) {
            double[] outi = new double[j - i + 1];
            int counter = 0;
            for (int k = i; k < j; k++) {
                out[counter++] = data[k];
            }
            out[a]=outi;
        }
        return out;
    }

    public static int xcorr(double[] preamble, double[] filt, double[] sig, int sig_len, Constants.SignalType sigType) {
        if (Constants.xcorr_method==1) {
            return xcorr_m1(preamble,filt,sig,sig_len);
        }
        else if (Constants.xcorr_method==2) {
            return xcorr_m2(preamble,filt,sig,sig_len, sigType);
        }
        return -1;
    }

    public static int xcorr_m1(double[] preamble, double[] filt, double[] sig, int sig_len) {
        int seglen=48000;
        int moveamount=24000;

        int numsegs = (filt.length/moveamount)-1;
        int counter = 0;

        int maxidx=0;
        double maxval=0;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < numsegs; i++) {
            Log.e("xcorr",counter+","+(counter+seglen-1));
            double[] seg = Utils.segment(filt,counter,counter+seglen-1);
            double[] corr = xcorr_helper(preamble,seg);
//            String aa=Arrays.toString(seg);
//            String bb=Arrays.toString(preamble);

            int max_idx = (int)max_idx(corr)[0];
            int xcorr_idx = (transform_idx(max_idx, seg.length));
            if (corr[max_idx] > maxval) {
                if ((i == numsegs-1 && xcorr_idx > seg.length-preamble.length) ||
                        (xcorr_idx > seg.length-preamble.length)) {
                    //pass
                }
                else {
                    maxval = corr[max_idx];
                    maxidx = xcorr_idx + counter;
                }
            }
            counter=counter+moveamount;
            Log.e(LOG, ">>"+counter+","+seglen+","+seg.length+","+(counter+xcorr_idx)+","+corr[max_idx]);
        }
        Log.e(LOG, ">>"+maxidx);
        Log.e("xcorr","xcorr runtime "+(System.currentTimeMillis()-t1));
        return maxidx;
    }

    public static double[] xcorr_online(double[] preamble, double[] filt) {
        double[] corr = xcorr_helper(preamble,filt);
        return evalSegv2(filt, corr);
    }

    public static double[] calculateSNR_null(double[] spec_symbol) {
        double[] snrs = new double[Constants.subcarrier_number_default];
        int bin_size=2;
        int scounter=0;
        for (Integer bin : Constants.valid_carrier_preamble) {
            double signal = Utils.pow2db(Math.pow(spec_symbol[bin],2));
            double[] seg1 = Utils.segment(spec_symbol,bin-(bin_size+1),bin-2);
            double[] seg2 = Utils.segment(spec_symbol,bin+2,bin+(bin_size+1));
            double sum_val=0;
            double counter=0;
            for (Double d : seg1) {
                sum_val+=Math.pow(d,2);
                counter++;
            }
            for (Double d : seg2) {
                sum_val+=Math.pow(d,2);
                counter++;
            }
            sum_val /= counter;

            double noise = Utils.pow2db(sum_val);

            double snr = signal-noise;
            snrs[scounter++]=(int)snr;
        }

//        Log.e("asdf","SNRS: "+snrs);

        return snrs;
    }

    public static boolean isLegit(double[] sig, Constants.SignalType sigType, double[] preamble, int start) {
        if (sigType.equals(Constants.SignalType.Sounding)) {
            int start_idx = start + preamble.length + Constants.ChirpGap;
            int end_idx = start_idx + (Constants.Ns * Constants.chanest_symreps) - 1;
            if (end_idx <= sig.length - 1) {
                double[] rx_symbols = Utils.segment(sig, start_idx, end_idx);
                double[] spec = (Utils.fftnative_double(rx_symbols,rx_symbols.length));
                double[] snrs = calculateSNR_null(spec);
                Log.e("cands_snr",Utils.mean(snrs)+"");

                FileOperations.appendtofile(MainActivity.av, Utils.mean(snrs)+"\n",
                        Utils.genName(Constants.SignalType.ASymCheckSNR, 0)+".txt");

                if (Utils.mean(snrs) > Constants.CheckSymSNRThresh) {
                    return true;
                }
            }
        } else if (sigType.equals(Constants.SignalType.Feedback)) {
            int start_idx = start + preamble.length + Constants.ChirpGap;
            int end_idx = start_idx + (int)(Constants.fs*(fbackTime/1000.0)) - 1;
            if (end_idx <= sig.length - 1) {
                double[] cand_sig = Utils.segment(sig, start_idx, end_idx);
                double[] spec = Utils.mag2db(Utils.fftnative_double(cand_sig, cand_sig.length));
                int[] freqs = FeedbackSignal.decodeFeedbackSignal(spec);
                Log.e("cands_fre", freqs[0] + "," + freqs[1]);

                FileOperations.appendtofile(MainActivity.av, freqs[0]+","+freqs[1]+"\n",
                        Utils.genName(Constants.SignalType.ASymCheckFreq, 0)+".txt");

                if (freqs[0] == -1) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static double[] evalSegv2(double[] filt, double[] corr) {
        int[] cands=Utils.getCandidateLocs(corr);

        double max=0;
        int maxidx=0;
        for (int j = 0; j < cands.length; j++) {
            int idx = (transform_idx(cands[j], filt.length));
            double[] legit2 = Naiser.Naiser_check_valid(filt, idx);
            if (legit2[1]>max) {
                max=legit2[1];
                maxidx = idx;
            }
            if (legit2[0] > 0) {
                return new double[]{corr[cands[j]], idx, legit2[1]};
            }
        }
        return new double[]{-1,maxidx,max};
    }

    public static double[] evalSegv1(double[] filt, double[] corr, int counter, double[] preamble) {

        double[] maxs=max_idx(corr);
        int max_idx = (int)maxs[0];
        double max_val = maxs[1];
        int xcorr_idx = (transform_idx(max_idx, filt.length));

        double[] cand_pre = Utils.segment(
                filt,xcorr_idx+counter,xcorr_idx+counter+preamble.length-1);

        double[] spec = Utils.mag2db(Utils.fftnative_double(cand_pre,cand_pre.length));
        double vv = Utils.var(Utils.segment(spec, 170, 180));
        int win=2400;
        int numAbove = Utils.numAbove(corr,max_idx-win,
                max_idx+win,max_val* XCORR_MAX_VAL_HEIGHT_FAC);
        Log.e("xcorr",String.format("%d %d %.2f %.2e %.2f %d",0,numAbove,XCORR_MAX_VAL_HEIGHT_FAC,corr[max_idx],vv, xcorr_idx+counter));

        String out = String.format("%.2e %d %.2f %.2f",corr[max_idx],numAbove,XCORR_MAX_VAL_HEIGHT_FAC,vv);

        if (corr[max_idx] > Constants.MinXcorrVal &&
                numAbove < Constants.XcorrAboveThresh && numAbove > 0
                && vv < Constants.VAR_THRESH
        ) {
            Log.e("xcorr",String.format("DETECTED %d,%.2f,%d,%d,%.1e %.2f %d",numAbove,XCORR_MAX_VAL_HEIGHT_FAC,(xcorr_idx+counter),0,corr[max_idx],vv,xcorr_idx+counter));
            FileOperations.appendtofile(MainActivity.av, "***"+out+"\n",
                    Utils.genName(Constants.SignalType.AXcorr, 0)+".txt");
            return new double[]{corr[max_idx],xcorr_idx};
        }
        else {
            FileOperations.appendtofile(MainActivity.av, out+"\n",
                    Utils.genName(Constants.SignalType.AXcorr, 0)+".txt");
            return new double[]{-1,-1};
        }
    }

    public static int numAbove(double[] sig, int idx1, int idx2, double thresh) {
        int num = 0;
        if (idx1 < 0) {
            idx1 = 0;
        }
        if (idx2 >= sig.length) {
            idx2 = sig.length-1;
        }
        for (int i = idx1; i < idx2; i++) {
            if (sig[i] > thresh) {
                num++;
            }
        }
        return num;
    }

    public static short[] flip(short[] bits) {
        short[] flipped = new short[bits.length];
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] == 0) {
                flipped[i] = 1;
            }
        }
        return flipped;
    }

    public static double[][] flip2(double[][] bits) {
        double[][] flipped = new double[bits.length][bits[0].length];
        for (int i = 0; i < bits.length; i++) {
            for (int j = 0; j < bits[0].length; j++) {
                if (bits[i][j] == 1) {
                    flipped[i][j] = -1;
                }
                if (bits[i][j] == -1) {
                    flipped[i][j] = 1;
                }
            }
        }
        return flipped;
    }

    public static double[] waitForChirp(Constants.SignalType sigType, int m_attempt, int chirpLoopNumber) {
        String filename = Utils.genName(sigType, m_attempt, chirpLoopNumber);
        Log.e("fifo",filename);

        Constants._OfflineRecorder = new OfflineRecorder(
                MainActivity.av, Constants.fs, filename);
        Constants._OfflineRecorder.start2();

        int MAX_WINDOWS = 0;

        int numWindowsLeft = 0;
        double timeout = 0;
        int len = 0;
        int ChirpSamples = (int)((Constants.preambleTime/1000.0)*Constants.fs);
        if (sigType.equals(Constants.SignalType.Sounding)) {
            if (Constants.Ns==960||Constants.Ns==1920) {
                MAX_WINDOWS = 2;
            }
            else if (Constants.Ns==4800) {
                MAX_WINDOWS=3;
            }
            else if (Constants.Ns==9600) {
                MAX_WINDOWS=3;
            }
            timeout = 30;
            len = ChirpSamples+Constants.ChirpGap+((Constants.Ns+Constants.Cp)*Constants.chanest_symreps);
        }
        else if (sigType.equals(Constants.SignalType.Feedback)) {
            MAX_WINDOWS = 2;
            if (Constants.Ns==960||Constants.Ns==1920) {
                timeout = 5;
            }
            else if (Constants.Ns==4800) {
                timeout=7;
            }
            else if (Constants.Ns==9600) {
                timeout=7;
            }
            len = (int)(ChirpSamples+Constants.ChirpGap+((fbackTime/1000.0)*Constants.fs));
        }
        else if (sigType.equals(Constants.SignalType.DataRx)) {
            MAX_WINDOWS = 2;
            if (Constants.exp_num==1 || Constants.exp_num == 2) {
                timeout = 11;
            }
            else if (Constants.exp_num==3 || Constants.exp_num == 4) {
                timeout = 4;
            }
            else if (Constants.exp_num==5) {
                if (Constants.Ns==960||Constants.Ns==1920) {
                    timeout = 3;
                }
                else if (Constants.Ns==4800) {
                    timeout=6;
                }
                else if (Constants.Ns==9600) {
                    timeout=6;
                }
            }
            len = ChirpSamples+Constants.ChirpGap+((Constants.Ns+Constants.Cp)*32);
        }

        int N = (int)(timeout*(Constants.fs/Constants.RecorderStepSize));
        double[] tx_preamble = PreambleGen.preamble_d();

        ArrayList<Double[]> sampleHistory = new ArrayList<>();
        ArrayList<Double> valueHistory = new ArrayList<>();
        ArrayList<Double> idxHistory = new ArrayList<>();
        int synclag = 12000;
        double[] sounding_signal = new double[]{};
        sounding_signal=new double[(MAX_WINDOWS+1)*Constants.RecorderStepSize];
        Log.e("len","sig length "+sounding_signal.length+","+sigType.toString());
        boolean valid_signal = false;
//        boolean getOneMoreFlag = false;
        int sounding_signal_counter=0;
        for (int i = 0; i < N; i++) {
            Double[] rec = Utils.convert2(Constants._OfflineRecorder.get_FIFO());
//            Log.e("timer1",m_attempt+","+rec.length+","+i+","+N+","+(System.currentTimeMillis()-t1)+"");

            if (sigType.equals(Constants.SignalType.Sounding)||
                    sigType.equals(Constants.SignalType.Feedback)||
                    sigType.equals(Constants.SignalType.DataRx)) {
//                Log.e("fifo","loop "+i);

                if (i<MAX_WINDOWS) {
                    sampleHistory.add(rec);
                    continue;
                }
                else {
                    if (numWindowsLeft==0) {
                        double[] out = null;
                        out = Utils.concat(sampleHistory.get(sampleHistory.size() - 1), rec);

                        double[] filt = Utils.copyArray(out);
                        filt = Utils.filter(filt);

                        //value,idx
                        double[] xcorr_out = Utils.xcorr_online(tx_preamble, filt);

                        long t1 = System.currentTimeMillis();
                        Utils.log(String.format("Listening... (%.2f)",xcorr_out[2]));

                        sampleHistory.add(rec);
                        valueHistory.add(xcorr_out[0]);
                        idxHistory.add(xcorr_out[1]);

                        if (xcorr_out[0] != -1) {
                            if (xcorr_out[1] + len + synclag > Constants.RecorderStepSize*MAX_WINDOWS) {
                                Log.e("copy","one more flag "+xcorr_out[1]+","+(xcorr_out[1] + len + synclag));

                                numWindowsLeft = MAX_WINDOWS-1;

//                                Log.e("copy","copying "+out[t_idx]+","+out[t_idx+1]+","+out[t_idx+2]+","+out[t_idx+3]+","+out[t_idx+4]);
                                for (int j = (int)xcorr_out[1]; j < out.length; j++) {
                                    sounding_signal[sounding_signal_counter++]=out[j];
                                }

                                Log.e("copy", "copy ("+xcorr_out[1]+","+filt.length+") to ("+sounding_signal_counter+")");
                            } else {
                                Log.e("copy","good! "+filt.length+","+xcorr_out[1]+","+filt.length);
//                                Utils.log("good");
                                int counter=0;
                                for (int k = (int) xcorr_out[1]; k < out.length; k++) {
                                    sounding_signal[counter++] = out[k];
                                }
//                                sounding_signal = Utils.segment(filt, (int) xcorr_out[1], filt.length - 1);
                                valid_signal = true;
                                break;
                            }
                        }
                    }
                    else if (sounding_signal_counter>0){
//                        Utils.log("another window");
                        Log.e("copy","another window from "+sounding_signal_counter+","+(sounding_signal_counter+rec.length)+","+sounding_signal.length);

//                        double[] filt2 = Utils.copyArray2(rec);
//                        filt2 = Utils.filter(filt2);

                        for (int j = 0; j < rec.length; j++) {
                            sounding_signal[sounding_signal_counter++]=rec[j];
                        }
                        numWindowsLeft -= 1;
                        if (numWindowsLeft==0){
                            valid_signal=true;
                            break;
                        }
                    }

                    if(sampleHistory.size() >= 6){
                        sampleHistory.remove(0);
                        valueHistory.remove(0);
                        idxHistory.remove(0);
                    }
                }
            }
        }

        Constants._OfflineRecorder.halt2();

        if (valid_signal) {
            return Utils.filter(sounding_signal);
        }
        return null;
    }

    public static int xcorr_m2(double[] preamble, double[] filt, double[] sig, int sig_len, Constants.SignalType sigType) {
        int seglen=48000;
        int moveamount=24000;

        int numsegs = (filt.length/moveamount)-1;
        int counter = 0;

        int maxidx=-1;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < numsegs; i++) {
            double[] seg = Utils.segment(filt,counter,counter+seglen-1);
            double[] corr = xcorr_helper(preamble,seg);

            double[] maxs=max_idx(corr);
            int max_idx = (int)maxs[0];
            int xcorr_idx = (transform_idx(max_idx, seg.length));
            Log.e(">>",xcorr_idx+"");

            if ((i == numsegs-1 && xcorr_idx > seg.length-preamble.length) ||
                    (xcorr_idx > seg.length-preamble.length)) {
                Log.e("","");
            }
            else {
                double[] status = evalSegv2(filt, corr);
                Log.e(">>",status[0]+","+status[1]);
                if (status[0] != -1) {
                    maxidx = xcorr_idx + counter;
                }
            }
            counter=counter+moveamount;
        }

        return maxidx;
    }

    public static int[] getCandidateLocs(double[] corr) {
        double[] corr2 = Utils.copyArray(corr);
        int MAX_CANDS = 3;
        int[] maxvals=new int[MAX_CANDS];
        for (int i = 0; i < MAX_CANDS; i++) {
            double[] maxs=Utils.max(corr2);
            maxvals[i] = (int)maxs[1];
            int idx1 = Math.max(0,(int)maxs[1]-2400);
            int idx2 = Math.min(corr.length-1,(int)maxs[1]+2400);
            for (int j = idx1; j < idx2; j++) {
                corr2[j]=0;
            }
        }
        return maxvals;
    }

    public static double[] xcorr_helper(double[] preamble, double[] sig) {
        double[][] a = Utils.fftcomplexoutnative_double(preamble, sig.length);
        double[][] b = Utils.fftcomplexoutnative_double(sig, sig.length);
        Utils.conjnative(b);
        double[][] multout = Utils.timesnative(a, b);
        double[] corr = Utils.ifftnative(multout);
        return corr;
    }

    public static double[] max_idx(double[] corr) {
        double maxval=0;
        int maxidx=0;
        for (int i = 0; i < corr.length; i++) {
            if (corr[i]>maxval){
                maxval=corr[i];
                maxidx=i;
            }
        }
        return new double[]{maxidx,maxval};
    }

    public static int transform_idx(int maxidx, int sig_len) {
        return sig_len-(maxidx*2)-1;
    }

    public static double[] abs(double[][] data) {
        double[] out = new double[data[0].length];
        for (int i = 0; i < data[0].length; i++) {
            out[i]=Math.sqrt((data[0][i] * data[0][i]) + (data[1][i] * data[1][i]));
        }
        return out;
    }
    public static double[] filter(double[] sig) {
        return firwrapper(sig);
    }

    public static double[] firwrapper(double[] sig) {
        double[] h=new double[]{0.000182981959336989,0.000281596242979397,0.000278146045925432,0.000175593510303356,-4.62343304809110e-19,-0.000200360419689133,-0.000360951066953434,-0.000412991328953827,-0.000300653514269367,1.50969613210241e-18,0.000465978441137468,0.00102258147190892,0.00155366635073282,0.00192784355834049,0.00203610307379658,0.00183119111593167,0.00135603855040258,0.000749280342023432,0.000220958680427305,-2.30998316092680e-19,0.000264751350126403,0.00107567949517942,0.00233229627684471,0.00377262258405226,0.00502311871694995,0.00569227410155340,0.00548603174290836,0.00431270688583350,0.00234309825736876,0,-0.00213082326080853,-0.00345315860003227,-0.00353962427138790,-0.00228740687362881,3.04497082814396e-18,0.00264042059820580,0.00471756289619728,0.00531631859929438,0.00379212348265709,-1.99178361782373e-17,-0.00558925290045386,-0.0119350370250123,-0.0176440086545525,-0.0213191620435004,-0.0219592383672450,-0.0193021014035209,-0.0140079988312124,-0.00761010753938763,-0.00221480214028708,-3.05997847316821e-18,-0.00261988600696407,-0.0106615919949044,-0.0233019683686087,-0.0382766989959210,-0.0522058307502582,-0.0612344107673335,-0.0618649720104803,-0.0518011604210193,-0.0306049219949348,2.05563094310381e-17,0.0362729247397242,0.0730450660051671,0.104645449260725,0.125975754818137,0.133506082359307,0.125975754818137,0.104645449260725,0.0730450660051671,0.0362729247397242,2.05563094310381e-17,-0.0306049219949348,-0.0518011604210193,-0.0618649720104803,-0.0612344107673335,-0.0522058307502582,-0.0382766989959210,-0.0233019683686087,-0.0106615919949044,-0.00261988600696407,-3.05997847316821e-18,-0.00221480214028708,-0.00761010753938763,-0.0140079988312124,-0.0193021014035209,-0.0219592383672450,-0.0213191620435004,-0.0176440086545525,-0.0119350370250123,-0.00558925290045386,-1.99178361782373e-17,0.00379212348265709,0.00531631859929438,0.00471756289619728,0.00264042059820580,3.04497082814396e-18,-0.00228740687362881,-0.00353962427138790,-0.00345315860003227,-0.00213082326080853,0,0.00234309825736876,0.00431270688583350,0.00548603174290836,0.00569227410155340,0.00502311871694995,0.00377262258405226,0.00233229627684471,0.00107567949517942,0.000264751350126403,-2.30998316092680e-19,0.000220958680427305,0.000749280342023432,0.00135603855040258,0.00183119111593167,0.00203610307379658,0.00192784355834049,0.00155366635073282,0.00102258147190892,0.000465978441137468,1.50969613210241e-18,-0.000300653514269367,-0.000412991328953827,-0.000360951066953434,-0.000200360419689133,-4.62343304809110e-19,0.000175593510303356,0.000278146045925432,0.000281596242979397,0.000182981959336989};
        double[] filtout = fir(sig,h);
        return Utils.segment(filtout,63,filtout.length-1);
    }

    public static int[] arange(int i, int j) {
        int[] out = new int[j-i+1];
        for (int k = 0; k < out.length; k++) {
            out[k]=i+k;
        }
        return out;
    }

    public static double[] copyArray(double[] sig) {
        double[] out = new double[sig.length];
        for (int i = 0; i < sig.length; i++) {
            out[i]=sig[i];
        }
        return out;
    }

    public static double[] div(double[] sig, double val) {
        double[] out = new double[sig.length];
        for (int i = 0; i < sig.length; i++) {
            out[i] = sig[i]/val;
        }
        return out;
    }
    public static int[] linspace(int start, int inc, int end) {
        int num=(end-start)/inc;
        int[] out = new int[num];
        for (int i = 0; i < num; i++) {
            out[i] = start+(inc*i);
        }
        return out;
    }

    public static String getDirName() {
        return Constants.exp_num+"_"+Constants.ts;
    }
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static double sum_multiple(double[] a, double[] b) {
        double out = 0;
        for (int i = 0; i < a.length; i++) {
            out += a[i]*b[i];
        }
        return out;
    }

    public static void sendNotification(Context mContext, String title, String message, int icon) {
        SharedPreferences prefs = mContext.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mContext.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit();
//        if (AppPreferencesHelper.areNotificationsEnabled(mContext, Constants.NOTIFS_ENABLED)) {
            Log.e("notif", "notif");
            NotificationManager mNotificationManager;

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mContext.getApplicationContext(), "channel");
            Intent ii = new Intent(mContext.getApplicationContext(), MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, ii, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(message);
            bigText.setBigContentTitle(title);
//            bigText.setSummaryText(message);

            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setSmallIcon(icon);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(message);
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setStyle(bigText);
//        mBuilder.setBadgeIconType(R.drawable.logo2);

            mNotificationManager =
                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                channel.setShowBadge(true);
                mBuilder.setChannelId(channelId);
            }

            int notifID = prefs.getInt("notif_id_pkey", 0);
            mNotificationManager.notify(notifID, mBuilder.build());
            editor.putInt("notif_id_pkey", notifID + 1);
            editor.commit();
//        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String decode(String bits, int poly1, int poly2, int constraint);
    public static native String encode(String bits, int poly1, int poly2, int constraint);
    public static native double[] fftnative_double(double data[], int N);
    public static native double[] fftnative_short(short data[], int N);
    public static native double[][] fftcomplexinoutnative_double(double data[][], int N);
    public static native double[][] fftcomplexoutnative_double(double data[], int N);
    public static native double[][] fftcomplexoutnative_short(short data[], int N);
    public static native double[] ifftnative(double data[][]);
    public static native double[][] ifftnative2(double data[][]);
    public static native void conjnative(double[][] data);
    public static native double[][] timesnative(double[][] data1,double[][] data2);
    public static native double[][] dividenative(double[][] data1,double[][] data2);
    public static native double[] bandpass(double[] data);
    public static native double[] fir(double[] data, double[] h);
}
