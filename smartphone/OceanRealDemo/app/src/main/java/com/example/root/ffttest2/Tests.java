package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;

import android.app.Activity;
import android.util.Log;

import org.ejml.equation.Symbol;
import org.ejml.simple.SimpleMatrix;

import java.util.Arrays;

public class Tests {

    public static void freq_bin_test1() {
        double[] sig = FileOperations.readrawasset(MainActivity.av, R.raw.recv_chirp, 1);
//        double[] noise = FileOperations.readrawasset(MainActivity.av, R.raw.noise, 1);
        Fre_adapt_ret est_preamble = Fre_adaptation.select_fre_bins(sig, sig,1000, 4000, -3);
        String est_preamble_freqs="";
        for (Integer i : est_preamble.freqs) {
            est_preamble_freqs+=Constants.f_seq.get(i)+",";
        }
        Utils.log("preamble freqs "+est_preamble.freqs.length+":"+est_preamble_freqs);
        MainActivity.av.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                double[] spec_preamble = Utils.fftnative_double(sig, sig.length);
                double[] spec_preamble_db = Utils.mag2db(spec_preamble);
                Display.plotSpectrum(Constants.gview, spec_preamble_db, true, MainActivity.av.getResources().getColor(R.color.purple_500),"S/N of preamble");
                Display.plotVerticalLine(Constants.gview, Constants.f_seq.get(Constants.nbin1_chanest));
                Display.plotVerticalLine(Constants.gview, Constants.f_seq.get(Constants.nbin2_chanest));
            }
        });
    }

    public static void naiser_test() {

//        double[] dat = FileOperations.readrawasset(MainActivity.av, R.raw.test_pre,1);
        double[] dat = FileOperations.readfromfile(MainActivity.av, "test","Bob-Sounding-0-0-bottom");
        int numsegs = dat.length/24000;
        int cc=0;
        for (int j = 0; j < 1; j++) {
            double[] seg = Utils.segment(dat, (int) cc, (int) cc+48000-1);
            cc=cc+24000;

            double[] filt = Utils.filter(seg);
            double[] pre = ChirpGen.preamble_d();
            long t1 = System.currentTimeMillis();
            double[] corr = Utils.xcorr_helper(pre, filt);
            int[] cands = Utils.getCandidateLocs(corr);
            for (Integer i : cands) {
                int idx = (Utils.transform_idx(i, seg.length));
//                int out = Naiser.Naiser_check_valid(filt, idx);
//                Log.e("naiser", j+":"+out + "");
            }
            Log.e("asdf",(System.currentTimeMillis()-t1)+"");
        }
    }
    public static void corr_test() {
//        double[] dat = FileOperations.readfromfile(MainActivity.av, "test","Alice-Feedback-13-0-bottom");
        double[] dat = FileOperations.readfromfile(MainActivity.av, "test","Bob-Sounding-1-0-bottom");
//        double[] seg = Utils.segment(dat,0,48000);
//        double[] seg = Utils.segment(dat,(int)48e3, (int)48e3*2);
        double[] seg = Utils.segment(dat,(int)48e3*2, (int)48e3*3);
        double[] filt = Utils.filter(dat);

        double[] pre = ChirpGen.preamble_d();
        double[]corr=Utils.xcorr_helper(pre,seg);
//        double[] maxs=Utils.max_idx(corr);
//        int max_idx = (int)maxs[0];
//        double max_val = maxs[1];
//        int xcorr_idx = (Utils.transform_idx(max_idx, seg.length));
//        Utils.evalSegv2(filt,corr,ChirpGen.preamble_d(),Constants.SignalType.Feedback);
        Utils.evalSegv2(seg,filt,corr,ChirpGen.preamble_d(),Constants.SignalType.Sounding);
    }

    public static void freq_bin_test2() {
        double[] sig = FileOperations.readrawasset(MainActivity.av, R.raw.bob_sounding3, 1);
//        int[] valid_bins = ChannelEstimate.extractSignal_withsymbol(MainActivity.av, sig,0);
    }

    public static void freq_bin_test3() {
        double[] sig = FileOperations.readfromfile(MainActivity.av, "0","Bob-Sounding-0-1642666678664-bottom");
        long t1 = System.currentTimeMillis();
//        int[] valid_bins = ChannelEstimate.extractSignal_withsymbol(MainActivity.av, sig,0);
//        Log.e("asdf",""+(System.currentTimeMillis()-t1));
    }

    public static void filt_test() {
        double[] sig = FileOperations.readfromfile(MainActivity.av, "0","Bob-Sounding-0-1642465432178-bottom");
//        ChannelEstimate.extractSignal_withsymbol(MainActivity.av, sig,0);
    }

    public static void sym_gen() {
//        int[] valid_bins = new int[]{21,22};
//        short[] bits = SymbolGeneration.rand_bits(Constants.DATA_LEN);
//        short[] txsig = SymbolGeneration.generate(bits, valid_bins, Constants.data_symreps, true);

        short[] bits = SymbolGeneration.rand_bits(Constants.valid_carrier_preamble.length);
        short[] out = SymbolGeneration.generate(bits, Constants.valid_carrier_data,
                Constants.chanest_symreps, true, Constants.SignalType.DataAdapt);
        String a = Arrays.toString(out);
    }

    public static void feedback_test() {
//        [23,23] 1642662861353
//        [13,14] 1642662833938
//        [] 1642664816355
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "Alice-Feedback-0-1642662861353-bottom");
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "Alice-Feedback-0-1642662833938-bottom");
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "Alice-Feedback-0-1642664816355-bottom");

        // nothing
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "3_1643075027380","Alice-Feedback-0-0-bottom");
        //3m
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "3_1642989085731","Alice-Feedback-0-0-bottom");
        //20m
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "3_1642991499117","Alice-Feedback-0-0-bottom");

//        double[] sig = FileOperations.readfromfile(MainActivity.av, "test","Alice-Feedback-0-1-bottom");
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "test","Alice-Feedback-0-1-bottom");
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "test2","Alice-Feedback-23-0-bottom");
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "4_1643270168253","Alice-Feedback-0-6-bottom");
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "4_1643271538924","Alice-Feedback-28-0-bottom");
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "4_1643253308181","Alice-Feedback-0-0-bottom");

        for (int i = 0; i < 25; i++) {
            double[] sig = FileOperations.readfromfile(MainActivity.av, "test", "Alice-Feedback-"+i+"-0-bottom");

            int[] valid_bins = FeedbackSignal.extractSignal(sig, 0, Constants.SignalType.Feedback);
        }
//        if (valid_bins[0] != -1) {
//            SendChirpAsyncTask.sendData(valid_bins, 0);
//        } else {
//            SendChirpAsyncTask.sendData(new int[]{20}, 0);
//        }
    }

//    public static void test_decode() {
//        Decoder.test_decode(MainActivity.av);
//    }

    public static void est() {
        short[] bits = Constants.pn20_bits;
        if (Constants.subcarrier_number_default==20) {
            bits=Constants.pn20_bits;
        }
        else if (Constants.subcarrier_number_default==40) {
            bits=Constants.pn40_bits;
        }
        else if (Constants.subcarrier_number_default==60) {
            bits=Constants.pn60_bits;
        }
        short[] rx = SymbolGeneration.generate(bits, Constants.valid_carrier_data,
                Constants.chanest_symreps, false, Constants.SignalType.Sounding);

        int cc=(Constants.Cp*Constants.chanest_symreps)+1;

        double [][][] spec_est = new double[2][Constants.subcarrier_number_default][Constants.chanest_symreps];
        for (int i = 0; i < Constants.chanest_symreps; i++) {
            short[] seg = Utils.segment(rx,cc,cc+Constants.Ns-1);
            double[][] spec = Utils.fftcomplexoutnative_short(seg,seg.length);

            int bin_counter=0;
            for (Integer bin : Constants.valid_carrier_default) {
                double realPart=spec[0][bin];
                double imagPart=spec[1][bin];
                spec_est[0][bin_counter][i] = realPart;
                spec_est[1][bin_counter++][i] = imagPart;
            }

            cc+=Constants.Ns;
        }
    }

    public static void freq_expand() {
        int[] freqs_all = FeedbackSignal.expand_freqs(new int[]{1720,3820});

        int[] bins_all = Utils.freqs2bins(freqs_all);
    }

    public static void sounding_test() {
        double[] test = ChirpGen.sounding_signal_d();
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "2_1642812897864","Bob-Sounding-0-bottom");
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "Bob-Sounding-0-1642631211115-bottom");
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "Bob-Sounding-0-1642631384642-bottom");
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "3_1643091044164","Bob-Sounding-0-0-bottom");
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "4_1643275534549","Bob-Sounding-0-0-bottom");
        for (int i = 0; i < 25; i++) {
            double[] sig = FileOperations.readfromfile(MainActivity.av, "test", "Bob-Sounding-"+i+"-bottom");
//        long t1 = System.currentTimeMillis();
//        double[] filt=Utils.filter(sig);
//        Log.e("asdf",(System.currentTimeMillis()-t1)+"");
//        long t2 = (System.currentTimeMillis()-t1);
//        double[] sig = FileOperations.readfromfile(MainActivity.av, "test","Bob-Sounding-0-0-bottom");
//        double[] filt = Utils.filter(sig);
            Constants.AdaptationMethod = 3;

            double[] filt = Utils.copyArray(sig);

            filt = Utils.filter(filt);
            Log.e(LOG, "finish filtering");

            double[] tx_preamble = ChirpGen.sounding_signal_d();

//        int start_point = Utils.xcorr(tx_preamble, filt, sig, filt.length, Constants.SignalType.Sounding);

//        long t1 = System.currentTimeMillis();
            int[] valid_bins = ChannelEstimate.extractSignal_withsymbol(MainActivity.av, sig, 0, Constants.SignalType.Sounding);
        }
//        Log.e("timer2",(System.currentTimeMillis()-t1)+"");
//        if (valid_bins.length==2 && valid_bins[0]!=-1&&valid_bins[1]!=-1) {
//            short[] feedback = FeedbackSignal.multi_freq_signal(valid_bins[0], valid_bins[valid_bins.length - 1],
//                    Constants.preambleTime, true, 0);
//            Log.e("asdf",(System.currentTimeMillis())+"");
//            Constants.sp1 = new AudioSpeaker(MainActivity.av, feedback, Constants.fs, 0, feedback.length, false);
////            appendToLog(Constants.SignalType.Feedback.toString());
////            Constants.sp1.play(Constants.volume);
//        }
        Log.e("asdf",(System.currentTimeMillis())+"");
    }

    public static void datagen() {
        short[] bits = SymbolGeneration.rand_bits(Constants.DATA_LEN);
        int[] freqs2 = FeedbackSignal.expand_freqs(new int[]{2000,3300});
        int[] bins = Utils.freqs2bins(freqs2);
        short[] txsig = SymbolGeneration.generate(bits, bins, Constants.data_symreps, true,
                Constants.SignalType.DataAdapt);
        String a = Arrays.toString(txsig);
    }

//    public static void mat_mult() {
//        SimpleMatrix mat = new SimpleMatrix(40,40);
//        for (int i = 0; i < 40; i++) {
//            double[] row = Utils.random_array(40);
//            mat.setRow(i,0,row);
//        }
//
//        SimpleMatrix mat2 = new SimpleMatrix(40,40);
//        for (int i = 0; i < 40; i++) {
//            double[] row = Utils.random_array(40);
//            mat2.setRow(i,0,row);
//        }
//
//        long t1 = System.currentTimeMillis();
//        mat.mult(mat);
//        Log.e("asdf","runtime "+(System.currentTimeMillis()-t1)+"");
//
//        t1 = System.currentTimeMillis();
//        mat.pseudoInverse();
//        Log.e("asdf","runtime "+(System.currentTimeMillis()-t1)+"");
//    }

    public static void make_data_test() {
//        int bitsPerSymbol = 60;
//        short[] bits = SymbolGeneration.getCodedBits(bitsPerSymbol*Constants.Nsyms);
//        short[] txsig = SymbolGeneration.generate(bits, new int[]{
//                        20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
//                        40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,
//                        60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79}
//                , Constants.data_symreps, true, Constants.SignalType.DataFull);
//        Log.e("asdf","len "+txsig.length);
//        bitsPerSymbol = 40;
//        bits = SymbolGeneration.getCodedBits(bitsPerSymbol*Constants.Nsyms);
//        txsig = SymbolGeneration.generate(bits, new int[]{
//                        20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
//                        40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59}
//                , Constants.data_symreps, true, Constants.SignalType.DataFull);
//        Log.e("asdf","len "+txsig.length);
//        bitsPerSymbol = 20;
//        bits = SymbolGeneration.getCodedBits(bitsPerSymbol*Constants.Nsyms);
//        txsig = SymbolGeneration.generate(bits, new int[]{
//                        20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39}
//                , Constants.data_symreps, true, Constants.SignalType.DataFull);
//        Log.e("asdf","len "+txsig.length);
//
//                bitsPerSymbol = 10;
//        bits = SymbolGeneration.getCodedBits(bitsPerSymbol*Constants.Nsyms);
//        txsig = SymbolGeneration.generate(bits, new int[]{
//                        20, 21, 22, 23, 24, 25, 26, 27, 28, 29}
//                , Constants.data_symreps, true, Constants.SignalType.DataFull);
//        Log.e("asdf","len "+txsig.length);
//        int bitsPerSymbol = 1;
//        short[] bits = SymbolGeneration.getCodedBits(bitsPerSymbol*Constants.Nsyms);
//        short[] txsig = SymbolGeneration.generate(bits, new int[]{43}
//                , Constants.data_symreps, true, Constants.SignalType.DataFull);
//        Log.e("asdf","len "+txsig.length);
    }

    public static void check_est() {
        // both these arrays should return [-1,1]
        double[] snrs=new double[]{-9.1035115369434, -16.73545597619167, -12.964744064079927, -10.187927840850385, -10.805854321679465, -15.2199258685712, -12.449588492329593, -11.019982977578787, -6.196008057824381, -8.91724003590658, -10.21314570223938, -8.193270386160393, -9.726174093183793, -3.2801447755352537, -2.6709370624527833, -5.4481503492338454, -0.7692737907053672, -7.828362760354407, -2.7735981264244476, -7.288979075884992, -6.930300310090764, -8.4435048072231, -11.738297626060593, -14.306521394949902, 3.4038726167964635, -14.221447813087316, -22.697491642811315, -2.9260112844609565, -6.374316120543425, 0.5785948284449418, -9.957110008894556, -4.69881790902196, -3.767783494924398, -12.018954771159127, -5.281021038369777, -20.94746289534377, -10.520736368544165, -16.028440328754098, -5.963896940280865, -12.861140802965904, -14.052106615437474, -5.4056974899628365, -6.144072670304066, -3.280847164441626, -7.218477794578075, -4.32958177718616, -8.719828894527483, -12.779712910121567, -6.7038105316047645, -11.299099496095657, -8.842324308950397, -8.092308086807881, -5.728654146576474, -14.848798304831568, -8.673219579946522, -5.729071003304179, -1.884456133645639, -9.913366636591736, -9.66904110429871, -4.008436745949485};
//        snrs=new double[]{-11,-10,-16,-11,-11,-8,-11,-10,-10,-10,-15,-12,-10,-10,-12,-14,-6,-9,-10,-12,-7,-8,-19,-3,-8,-9,-8,-10,-12,-7,-9,-8,-18,-21,-13,-13,-9,-7,-12,-7,-10,-13,-12,-11,-11,-11,-13,-10,-11,-11,-8,-11,-13,-14,-12,-11,-13,-13,-8};

        int[] fseq=new int[]{1000, 1050, 1100, 1150, 1200, 1250, 1300, 1350, 1400, 1450, 1500, 1550, 1600, 1650, 1700, 1750, 1800, 1850, 1900, 1950, 2000, 2050, 2100, 2150, 2200, 2250, 2300, 2350, 2400, 2450, 2500, 2550, 2600, 2650, 2700, 2750, 2800, 2850, 2900, 2950, 3000, 3050, 3100, 3150, 3200, 3250, 3300, 3350, 3400, 3450, 3500, 3550, 3600, 3650, 3700, 3750, 3800, 3850, 3900, 3950};
        int[] valid_bins = Fre_adaptation.select_fre_bins2(snrs, fseq, Constants.SNR_THRESH2);

        short[] feedback = FeedbackSignal.multi_freq_signal(valid_bins[0], valid_bins[valid_bins.length - 1],
                Constants.preambleTime, Constants.feedbackPreamble, 1);

//        Arrays.toString(feedback);

        valid_bins = FeedbackSignal.extractSignal(Utils.convert(feedback),0,Constants.SignalType.Feedback);
    }

    public static void test_filt() {
        double[] sig=FileOperations.readrawasset(MainActivity.av, R.raw.myseg, 1);
        double[] filt = Utils.filter(sig);
        Arrays.toString(filt);
    }

    public static void shuffle() {
        short[] bits = new short[]{0,1,2,3,4,5,6,7,8,9};
        SymbolGeneration.shuffleArray(bits,1);

        short[] out = SymbolGeneration.unshuffle(bits,1);
        Log.e("asdf","");
    }

    public static void test_mic() {
        Activity av;
        Constants.ts = System.currentTimeMillis();
        String name = Utils.genName(Constants.SignalType.Feedback,1);
//        Log.e("Beging thread1",name);
        Constants.stereo=false;
        Constants._OfflineRecorder = new OfflineRecorder(MainActivity.av, Constants.fs,  name);
        Constants._OfflineRecorder.start2();
        int N = 12;
        short[] save_samples = new short[24000*N];
        int count = 0;
        for (int i = 0; i < N; i++) {
            short[] samples = Constants._OfflineRecorder.get_FIFO();
//            Log.e("fifo","got samples "+i);

            for(int t = 0; t < samples.length; ++t){
                save_samples[count] = samples[t];
                count++;
            }
//            Log.e("thread1","read sucessfully......");
        }

//        FileOperations.writetofile(MainActivity.av, save_samples, "recv.txt");
//        Log.e("thread1","begin halting......" + String.valueOf(count));
        Constants._OfflineRecorder.halt2();
    }

    public static void test_differential() {
//        short[] bits = SymbolGeneration.getCodedBits(10);
//        short[] txsig = SymbolGeneration.generate(bits, new int[]{20,21,22,23},
//                Constants.data_symreps, true, Constants.SignalType.DataFull);
    }

    public static void bin_filling() {
        int msgbits = 52;
        int valid_bins_length = 60;
        int[] valid_bins = new int[valid_bins_length];
        for (int i = 0; i < valid_bins_length; i++) {
            valid_bins[i] = 20+i;
        }
        int numbits = msgbits;
        Constants.ts = System.currentTimeMillis();
//        short[] bits = SymbolGeneration.getCodedBits(numbits);
        short[] bits = new short[numbits];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = 1;
        }
//        short[] bits, int[] valid_carrier, int symreps, Constants.SignalType sigType
//        SymbolGeneration.generate_helper(bits, valid_bins, 1, com.example.root.ffttest2.Constants.SignalType.DataAdapt);
//        short[] txsig = SymbolGeneration.generate2(bits, valid_bins,
//                Constants.data_symreps, true,
//                Constants.SignalType.DataAdapt,0);
//        Arrays.toString(txsig);
    }

    public static void test_freq() {
        int[] selected = new int[]{-1,-1};
        int[] fseq = Utils.linspace(Constants.f_range[0],50,Constants.f_range[1]);

        if (selected.length==2&&selected[0] != -1 && selected[1] != -1) {
            int[] freqs = new int[selected.length];
            for (int i = 0; i < selected.length; i++) {
                freqs[i] = fseq[selected[i]];
            }
//            Utils.log(Arrays.toString(Utils.convert(snrs)));
            Utils.log(Arrays.toString(freqs));

//            FileOperations.writetofile(MainActivity.av, Utils.trim(Arrays.toString(snrs)),
//                    Utils.genName(Constants.SignalType.SNRs, m_attempt) + ".txt");
//            FileOperations.writetofile(MainActivity.av, freqs,
//                    Utils.genName(Constants.SignalType.FreqEsts, 0) + ".txt");
//            FileOperations.writetofile(MainActivity.av, Constants.snr_method + "",
//                    Utils.genName(Constants.SignalType.SNRMethod, 0) + ".txt");
        }
        else {

        }
    }
}