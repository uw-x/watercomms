package com.example.root.ffttest2;

public class Lora {
    public static double[] cumtrapz(double x[], double y[]) {
        int n = y.length;
        double[] cumtrap = new double[n];

        cumtrap[0] = 0.0;
        for (int i = 1; i < n; i++) {
            cumtrap[i] = cumtrap[i - 1] + 0.5 * (y[i] + y[i - 1])*(x[i] - x[i-1]);
        }
        return cumtrap;
    }

    public static double[] loramod(int x,int SF, int BW, int fs, int f_center, int Inv){
        double M = Math.pow(2, SF) ;
        double Ts      = M/BW ;
        int Ns      = (int)(fs*M/BW) ;

        double gamma   = x/Ts ;
        double beta    = BW/Ts ;
        double[] times = new double[Ns];
        double[] freqs = new double[Ns];


        for(int i = 0; i < Ns; ++i){
            times[i] = ((double)i)/((double)fs);
            freqs[i] = (BW + gamma + Inv*beta*i/fs)%BW - BW/2 + f_center;
        }

        double[] phases = cumtrapz(times, freqs);
        double[] symbol = new double[Ns];

        for(int i = 0; i < Ns; ++i){
            symbol[i] = Math.cos(2*Math.PI*phases[i]);
        }
        return symbol;
    }

    public static double[][] loramod_complex(int x,int SF, int BW, int fs, int f_center, int Inv){
        double M = Math.pow(2, SF) ;
        double Ts      = M/BW ;
        int Ns      = (int)(fs*M/BW) ;

        double gamma   = x/Ts ;
        double beta    = BW/Ts ;
        double[] times = new double[Ns];
        double[] freqs = new double[Ns];


        for(int i = 0; i < Ns; ++i){
            times[i] = ((double)i)/((double)fs);
            freqs[i] = (BW + gamma + Inv*beta*i/fs)%BW - (BW/2) + f_center;
        }

        double[] phases = cumtrapz(times, freqs);
        double[][] symbol = new double[2][Ns];

        for(int i = 0; i < Ns; ++i){
            symbol[0][i]= Math.cos(2*Math.PI*phases[i]);
            symbol[1][i] = Math.sin(2*Math.PI*phases[i]);
        }
        return symbol;
    }


    public static int lorademod(double[] recv, int SF, int BW, int fs){
        double M = Math.pow(2, SF) ;
        int Ns      = (int)(fs*M/BW) ;

        double[][] decode_chirp = loramod_complex(0,SF,BW,fs,0, -1);
        double[][] dechirp = Utils.multiple(recv, decode_chirp);
        double[][] dechirp_fft = Utils.fftcomplexinoutnative_double(dechirp, dechirp[0].length);
        double [] dechirp_fft2 = Utils.abs(dechirp_fft);
        double[] part1 = Utils.segment(dechirp_fft2, 768, 767 + (int)M);
        double[] part2 = Utils.segment2(dechirp_fft2, 768 - (int)M,767);
        double[] seg_fft = Utils.add(part1, part2);
        double[] max_info = Utils.max(seg_fft);
        return (int)max_info[1];
    }

}
