package com.example.root.ffttest2;

import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Fre_adaptation {
    public static int[] select_fre_bins(double[] SNR, double threshold) {
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
                break;
            }
        }

        if(select_idx[0] == -1){
            fre_range[0] = -1;
            fre_range[1] = -1;
        }
        else{
            fre_range[0] = select_idx[0];
            fre_range[1] = select_idx[1];
        }

        return fre_range;
    }
}