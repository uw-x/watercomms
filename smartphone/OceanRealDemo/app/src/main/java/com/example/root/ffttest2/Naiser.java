package com.example.root.ffttest2;

import android.util.Log;

public class Naiser {

    public static double[] Naiser_corr(double[] signal, int Nu, int DIVIDE_FACTOR) {
        short[] PN_seq = new short[] {1, -1, -1, -1, -1, -1, 1, -1};
        int total_length = signal.length;
        int L = 8;
        int preamble_L = L * Nu;
        int multi_num = 0;
        int len_corr = (total_length - preamble_L)/DIVIDE_FACTOR + 2;
        double[] naiser_corr = new double[len_corr];
        int num = 0;
        for(int  i = 0; i < total_length - preamble_L -1; i = i+DIVIDE_FACTOR){
            double[] seg = Utils.segment(signal, i, i+preamble_L - 1);
            double Pd = 0;
            for(int k = 0; k < L - 1; ++k){
                multi_num ++;
                int bk = PN_seq[k]*PN_seq[k+1];
                double[] seg1 = Utils.segment(seg, k*Nu, (k+1)*Nu - 1);
                double[] seg2 = Utils.segment(seg, (k + 1)*Nu, (k+2)*Nu - 1);
                Pd += bk*Utils.sum_multiple(seg1, seg2);

            }
            double Rd = Utils.sum_multiple(seg, seg);
            naiser_corr[num] = Pd/Rd;
            num ++;
        }
        double[] max_info = Utils.max(naiser_corr);
        max_info[1] = max_info[1] *DIVIDE_FACTOR;
//        Log.e("multiple times",Integer.toString(multi_num));
        return  max_info;
    }

    public static double[] Naiser_corr2(double[] signal, int Nu, int N0, int DIVIDE_FACTOR) {
        short[] PN_seq = new short[] {1, -1, -1, -1, -1, -1, 1, -1};
        int total_length = signal.length;
        int L = 8;
        int N_both = Nu + N0;
        int preamble_L = L * (Nu + N0);
        int multi_num = 0;
        int len_corr = (total_length - preamble_L)/DIVIDE_FACTOR + 2;
        double[] naiser_corr = new double[len_corr];
        int num = 0;

        for(int  i = 0; i < total_length - preamble_L -1; i = i+DIVIDE_FACTOR){
            double[] seg = Utils.segment(signal, i, i+preamble_L - 1);
            double Pd = 0;
            double Rd = 0;

            for(int k = 0; k < L - 1; ++k){
                multi_num ++;
                int bk = PN_seq[k]*PN_seq[k+1];
                double[] seg1 = Utils.segment(seg, k*N_both + N0, (k+1)*N_both - 1);
                double[] seg2 = Utils.segment(seg, (k + 1)*N_both + N0, (k+2)*N_both - 1);
                Pd += bk*Utils.sum_multiple(seg1, seg2);

                if(k == 0){
                    Rd = Rd + Utils.sum_multiple(seg1, seg1);
                    Rd = Rd + Utils.sum_multiple(seg2, seg2);
                }
                else{
                    Rd = Rd + Utils.sum_multiple(seg2, seg2);
                }
            }
            //double Rd = Utils.sum_multiple(seg, seg);
            naiser_corr[num] = Pd/Rd/(L-1)*L;
            num ++;
        }

        double[] max_info = Utils.max(naiser_corr);
        int use_85 = 1;
        if(use_85 == 0){
            max_info[1] = max_info[1] *DIVIDE_FACTOR;
            return  max_info;
        }

        double max_height_85 = max_info[0]*0.85;
        int max_height_index = (int)max_info[1];
        int right = -1;
        int left = -1;

        for(int i = max_height_index; i < len_corr - 1; ++i){
            if(naiser_corr[i] >= max_height_85 && naiser_corr[i+1] <= max_height_85){
                right = i;
                break;
            }
        }

        for(int i = max_height_index; i > 1; --i){
            if(naiser_corr[i] >= max_height_85 && naiser_corr[i-1] <= max_height_85){
                left = i;
                break;
            }
        }

//        Utils.log("max info: "+((left+right)/2)+","+max_info[1]);
        if(right == -1 || left == -1){
            max_info[1] = max_info[1] *DIVIDE_FACTOR;
            return max_info;
        }
        else{
            max_info[1] = (right + left)/2.0 *DIVIDE_FACTOR;
            return  max_info;
        }
//        Log.e("multiple times",Integer.toString(multi_num));

    }

    public static double[] Naiser_check_valid(double[] signal, int peak_index) {
        int win_size = 1200;
        double naiser_threshold = Constants.NaiserThresh; //adjustable
        int step_size = 8;

//        if(peak_index - win_size < 0 || peak_index + win_size + 9600 > signal.length) return new double[]{-2,-2};
//        double[] preamble_seg = Utils.segment(signal, peak_index - win_size, peak_index + win_size + 9600 -1);

        if(peak_index - win_size < 0 || peak_index + win_size + Constants.naiser.length > signal.length) return new double[]{-2,-2};
        double[] preamble_seg = Utils.segment(signal, peak_index - win_size, peak_index + win_size + Constants.naiser.length -1);

        double[] max_info=null;
//        if (Constants.naiserVersion==1) {
//            max_info = Naiser_corr(preamble_seg, 960,  step_size);
//        }
//        else if (Constants.naiserVersion==2) {
            max_info = Naiser_corr2(preamble_seg, 960, 240, step_size);
//        }
        Log.e("naiser","max info "+max_info[0]);
        if(max_info[0] < naiser_threshold) return new double[]{-1,max_info[0]};
        else{
            return new double[]{peak_index - win_size + max_info[1],max_info[0]};
        }
    }

}