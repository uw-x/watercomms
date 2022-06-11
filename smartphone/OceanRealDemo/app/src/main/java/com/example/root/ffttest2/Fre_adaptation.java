package com.example.root.ffttest2;

import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Fre_adapt_ret {
    int[] freqs;
    boolean[] selected_bin;
    public Fre_adapt_ret(int[] freqs, boolean[] selected_bin) {
        this.freqs = freqs;
        this.selected_bin = selected_bin;
    }
}

public class Fre_adaptation {
    public static int[] select_fre_bins2(double[] SNR, int[] f_seq, double threshold) {
        int total_bins = SNR.length;
        int[] select_idx = new int[2];
        select_idx[0] = -1;
        select_idx[1] = -1;
        int[] fre_range = new int[2];

        for(int L = total_bins; L >0 ; L--){
            double incre = Utils.mag2db((double)(total_bins)/(double)(L))/2*Constants.FreAdaptScaleFactor;
            int best_idx = -1;
            double best_valley = -1;
            for(int i = 0; i < total_bins - L + 1 ; ++i){
                double valley = Utils.min(SNR, i, i+L) + incre;
                if(valley < threshold){
                    continue;
                }
                else {
                    if(valley > best_valley) {
                        best_idx = i;
                        best_valley = valley;
                    }
                }
            }
            if(best_idx >= 0){
                select_idx[0] = best_idx;
                select_idx[1] = best_idx + L -1;
//                select_idx[1] = best_idx + L;
                break;
            }
        }

        if(select_idx[0] == -1){
            fre_range[0] = -1;
            fre_range[1] = -1;
        }
        else{
//            fre_range[0] = f_seq[select_idx[0]];
//            fre_range[1] = f_seq[select_idx[1]];
            fre_range[0] = select_idx[0];
            fre_range[1] = select_idx[1];
        }

        return fre_range;
    }

    public static int[] select_fre_bins3(double[] SNR, int[] f_seq, double threshold) {
        double threshold2 = (double)Constants.SNR_THRESH2_2;
        int total_bins = SNR.length;
        int[] select_idx = new int[2];
        select_idx[0] = -1;
        select_idx[1] = -1;
        int[] fre_range = new int[2];

        for(int L = total_bins; L >0 ; L--){
            double incre = Utils.mag2db((double)(total_bins)/(double)(L))/2*Constants.FreAdaptScaleFactor;
            int best_idx = -1;
            double best_valley = -1;
            for(int i = 0; i < total_bins - L + 1 ; ++i){
                double valley = Utils.min(SNR, i, i+L) + incre;
                double[] seg = Utils.segment(SNR, i, i+L-1);
                Arrays.sort(seg);

                if(seg.length >= 2) {
                    if(seg[0]+incre < threshold2 || seg[1]+incre < threshold){
                        continue;
                    }
                    else {
                        if (valley > best_valley) {
                            best_idx = i;
                            best_valley = valley;
                        }
                    }
                }
                else{
                    if(seg[0]+incre < threshold){
                        continue;
                    }
                    else {
                        if (valley > best_valley) {
                            best_idx = i;
                            best_valley = valley;
                        }
                    }
                }
            }
            if(best_idx >= 0){
                select_idx[0] = best_idx;
                select_idx[1] = best_idx + L -1;
//                select_idx[1] = best_idx + L;
                break;
            }
        }

        if(select_idx[0] == -1){
            fre_range[0] = -1;
            fre_range[1] = -1;
        }
        else{
//            fre_range[0] = f_seq[select_idx[0]];
//            fre_range[1] = f_seq[select_idx[1]];
            fre_range[0] = select_idx[0];
            fre_range[1] = select_idx[1];
        }

        return fre_range;
    }


    public static Fre_adapt_ret select_fre_bins(double[] data, double[] noise, double f_begin, double f_end, int threshold) {
        int preamble_len = data.length;
        for(int i =0; i< preamble_len;++i){
            data[i] = data[i]/30000;
        }
//        for(int i =0; i< noise.length;++i){
//            noise[i] = noise[i]/30000;
//        }
        int nbin1 = (int)Math.ceil((f_begin*preamble_len)/Constants.fs);
        int nbin2 = (int)Math.floor((f_end*preamble_len)/Constants.fs);

        double[] sig_spectrum = Utils.fftnative_double(data, preamble_len);
        double[] noise_spectrum = Utils.fftnative_double(noise, preamble_len);

        double[] sig_seg = (Utils.segment(sig_spectrum, nbin1, nbin2-1));
        double[] noise_seg =  (Utils.segment(noise_spectrum, nbin1, nbin2-1));
        double[] snr_seg = sig_seg;//Utils.subtract(sig_seg, noise_seg);

        double delta_f = 50;//Constants.fs/preamble_len *groups_bins;
        double groups_bins = (delta_f*preamble_len)/Constants.fs;
        delta_f = Constants.fs/preamble_len *groups_bins;

        int group_numbers = (int)Math.floor(snr_seg.length/groups_bins);
        double[] group_fft = new double[group_numbers];
        double[] group_fre = new double[group_numbers];
        for(int i = 0; i < group_numbers; ++i){
            double sum = 0;
            for(int j = 0; j < groups_bins; ++j){
//                Log.e("asdf",i+","+(i*groups_bins + j)+"");
                sum += snr_seg[(int)(i*groups_bins + j)];
            }
            group_fft[i] = sum/groups_bins;
            group_fre[i] = f_begin + i*delta_f;
        }

        double[] new_snr = Utils.subtract(Utils.mag2db(group_fft), threshold);

        List<Integer> cross_zero = new ArrayList<Integer>();
        List<Integer> polarity = new ArrayList<Integer>();

        for(int i = 0; i < group_numbers - 1; ++i){
            if(new_snr[i] < 0 && new_snr[i + 1] > 0){
                cross_zero.add(i);
                polarity.add(1);
            }
            if(new_snr[i] > 0 && new_snr[i + 1] < 0){
                if(cross_zero.isEmpty()) {
                    cross_zero.add(0);
                    polarity.add(1);
                    cross_zero.add(i);
                    polarity.add(0);
                }
                else{
                    cross_zero.add(i);
                    polarity.add(0);
                }
            }
        }
        if (cross_zero.size() == 0) {
            cross_zero.add(0);
            cross_zero.add(group_numbers - 1);
            polarity.add(0);
        }
        else {
            if (polarity.get(polarity.size() - 1) == 1) {
                cross_zero.add(group_numbers - 1);
                polarity.add(0);
            }
        }

        String s1=Arrays.toString(Utils.convert(cross_zero));
        String s2=Arrays.toString(Utils.convert(polarity));

        // enforce minimum spacing of min_interval between marked zero-crossings
        int idx = 0;
        int min_interval = 2;
        List<Integer> delete_index = new ArrayList<Integer>();
        for(int i = 0; i < cross_zero.size() - 1; ++i){
            if(idx == cross_zero.size() - 1){
                break;
            }
            if(cross_zero.get(idx+1) -  cross_zero.get(idx)< min_interval){
                delete_index.add(idx+1);
                delete_index.add(idx);
                idx ++;
            }
            idx ++;
        }

        int valid_num = (cross_zero.size() - delete_index.size());
        double[] selected_fre = new double[valid_num];
        int [] valid_bins = new int[valid_num];
        idx = 0;
        for(int i = 0; i < cross_zero.size(); ++i){
            if(!delete_index.contains(i)){
                int bound_idx = cross_zero.get(i);
                valid_bins[idx] = bound_idx;
                selected_fre[idx] = group_fre[bound_idx];
                idx +=1;
            }
        }

        LinkedList<Integer> freqs = new LinkedList<>();
        boolean[] selected_bin = new boolean[group_numbers];
        for(int i = 0; i < valid_num; i += 2){
            for(int j = valid_bins[i]; j <= valid_bins[i+1]; ++j){
                selected_bin[j] = true;
                freqs.add(j+Constants.nbin1_default);
            }
        }

        return new Fre_adapt_ret(Utils.convert(freqs), selected_bin);
    }
}