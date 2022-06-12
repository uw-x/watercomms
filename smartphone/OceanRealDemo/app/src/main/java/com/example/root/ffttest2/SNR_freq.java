package com.example.root.ffttest2;

public class SNR_freq {
    public static double[] calculate_snr(double[][][] rx_spectrum, double[][] gt_symbol, int sym_start, int sym_end) {
        int bin_num = gt_symbol.length;
        double[] SNR_list = new double[bin_num];
        int sym_count = sym_end-sym_start;

        for(int i = 0; i < bin_num; ++i){
            double[] H = new double[2];
            H[0] = 0;
            H[1] = 0;
            for(int j = sym_start; j < sym_end; ++j) {
                H[0] += rx_spectrum[0][i][j]*gt_symbol[i][0];
                H[1] += rx_spectrum[1][i][j]*gt_symbol[i][0];
            }
            H[0] = H[0]/sym_count;
            H[1] = H[1]/sym_count;
            double noise_level = 0;
            for(int j = sym_start; j < sym_end; ++j) {
                noise_level += Math.pow(rx_spectrum[0][i][j]*gt_symbol[i][0] - H[0],2 )+
                        Math.pow(H[1] - rx_spectrum[1][i][j]*gt_symbol[i][0], 2);
            }
            noise_level = noise_level/sym_count;
            double signal_level = Math.pow(H[0], 2) + Math.pow(H[1], 2);
            SNR_list[i] = Utils.mag2db(signal_level/noise_level)/2;
        }
        return SNR_list;
    }
}
