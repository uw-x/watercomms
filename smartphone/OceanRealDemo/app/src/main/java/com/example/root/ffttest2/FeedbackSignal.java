package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class FeedbackSignal {
    public static int xcorr_helper(double[] rec, Constants.SignalType sigType) {
        double[] filt=Utils.copyArray(rec);
        long t1 = System.currentTimeMillis();
        filt = Utils.filter(filt);
        Log.e("asdf","filt run "+(System.currentTimeMillis()-t1)+"");

        double[] preamble = ChirpGen.preamble_d();
        t1 = System.currentTimeMillis();
        int start_point = Utils.xcorr(preamble,filt, rec, filt.length,sigType);
//        if (start_point > Constants.butterworthFiltOffset) {
//            start_point -= Constants.butterworthFiltOffset;
//        }
        if (start_point-Constants.besselFiltOffset>=0) {
            start_point -= Constants.besselFiltOffset;
        }

        Log.e("asdf","xcorr run "+(System.currentTimeMillis()-t1)+"");

        Log.e(LOG, "xcorr "+start_point);

        return start_point;
    }

    public static int[] extractSignal(double[] rec, int m_attempt, Constants.SignalType sigType) {
        Log.e(LOG, "FeedbackSignal_extractSignal");

        int start_point = xcorr_helper(rec,sigType);
        Log.e("start",start_point+"");
        return extractSignalHelper(rec,start_point,m_attempt);
    }

    public static int[] extractSignalHelper(double[] rec, int start_point, int m_attempt) {
        double[] preamble = ChirpGen.preamble_d();
        int end_point = start_point+preamble.length-1;
        Log.e("extract",start_point+","+end_point+","+rec.length);
        if (end_point-1 > rec.length || start_point < 0) {
            Utils.log("Error extracting preamble from feedback signal");
            FileOperations.writetofile(MainActivity.av, new int[]{-1,-1},
                    Utils.genName(Constants.SignalType.FeedbackFreqs,m_attempt)+".txt");
            return new int[]{-1,-1};
        }
        double[] preamble_rx = Utils.segment(rec, start_point, end_point);

        //////////////////////////////////////////////////////////////////////////

        int rec_start = start_point+preamble.length+Constants.ChirpGap+1;
        int rec_end = rec_start+(int)((Constants.fbackTime/1000.0)*Constants.fs)-1;
        int rec_len = (rec_end - rec_start)+1;
        Log.e(LOG, rec.length+","+rec_start+","+rec_end+","+rec_len);

        if (rec_end-1 > rec.length || rec_start < 0) {
            Utils.log("Error extracting feedback from feedback signal");
            FileOperations.writetofile(MainActivity.av, new int[]{-1,-1},
                    Utils.genName(Constants.SignalType.FeedbackFreqs,m_attempt)+".txt");
            return new int[]{-1,-1};
        }
        double[] feedback = Utils.segment(rec, rec_start-1, rec_end-1);

        int[] freqs = parse_signal(preamble_rx, feedback);
        if (freqs.length == 2 && freqs[0] != -1) {
            FileOperations.writetofile(MainActivity.av, freqs,
                    Utils.genName(Constants.SignalType.FeedbackFreqs,m_attempt)+".txt");

            freqs[0]=(int)Math.ceil(freqs[0]/(double)Constants.inc)*Constants.inc;
            freqs[1]=(int)Math.floor(freqs[1]/(double)Constants.inc)*Constants.inc;

            int[] freqs_all = expand_freqs(freqs);

            FileOperations.writetofile(MainActivity.av, freqs_all,
                    Utils.genName(Constants.SignalType.ExactFeedbackFreqs,m_attempt)+".txt");

            int[] bins_all = Utils.freqs2bins(freqs_all);

            return bins_all;
        }
        else {
            FileOperations.writetofile(MainActivity.av, new int[]{-1,-1},
                    Utils.genName(Constants.SignalType.FeedbackFreqs,m_attempt)+".txt");
            return new int[]{-1, -1};
        }
    }

    public static int[] expand_freqs(int[] freqs) {
        int freqSpacing = Constants.fs/Constants.Ns;
        int numbins = (freqs[freqs.length-1]-freqs[0])/freqSpacing;

        int[] out = new int[numbins+1];
        for (int i = 0; i <= numbins; i++) {
            out[i] = freqs[0]+(i*freqSpacing);
        }
        return out;
    }

//    public static int[] extractSignal_nopreamble(double[] rec) {
//        Log.e(LOG,"FeedbackSignal_extractSignal");
//
//        int noise_start = 5000;
//        int noise_end = noise_start+(int)((Constants.preambleTime/1000.0)*Constants.fs)-1;
//        int noise_len = (noise_end-noise_start)+1;
//        Log.e(LOG, "noise "+rec.length+","+noise_start+","+noise_end+","+noise_len);
//
//        if (noise_end-1 > rec.length || noise_start < 0) {
//            Utils.log("Error extracting noise from channel estimate");
//            return Constants.valid_carrier_default;
//        }
//
//        double[] noise = Utils.segment(rec, noise_start, noise_end);
//        double[] noise_spec = Utils.fftnative_double(noise, noise.length);
//        double[] noise_spec_db = Utils.mag2db(noise_spec);
//
//        int moveamount=1000;
//        int numsegs=(rec.length/moveamount)-5;
//        int seglen=(int)(((Constants.preambleTime)/1000.0)*Constants.fs);
//        int cc=0;
//        int freq_spacing = Constants.fs/seglen;
//
//        for (int i = 0; i < numsegs; i++) {
//            Log.e("asdf",i+"/"+numsegs);
//            double[] seg = Utils.segment(rec,cc,cc+seglen-1);
//            cc=cc+moveamount;
//            double[] feedback_spec=Utils.fftnative_double(seg,seg.length);
//            double[] feedback_spec_db = Utils.mag2db(feedback_spec);
//            LinkedList<Bin> bins = new LinkedList<>();
//
//            for (Integer j : Constants.valid_carrier_default) {
//                int freq = Constants.f_seq.get(j);
//                int idx_start=(Constants.f_seq.get(Constants.valid_carrier_default[0]-1) / freq_spacing);
//                int idx_end=(Constants.f_seq.get(Constants.valid_carrier_default[Constants.valid_carrier_default.length-1]+1) / freq_spacing);
//
//                double signal = feedback_spec_db[freq / freq_spacing];
//                double noise2 = noise_spec_db[freq / freq_spacing];
//
//                int snr = (int)(signal-noise2);
//                double prom = getProm(seg, freq, idx_start, idx_end);
//                bins.add(new Bin(freq,snr,signal,noise2,prom));
//            }
//
//            Collections.sort(bins, new Comparator<Bin>() {
//                @Override
//                public int compare(Bin c1, Bin c2) {
//                    if (c1.signal > c2.signal) {return 1;}
//                    else if (c1.signal == c2.signal) {return 0;}
//                    else {return -1;}
//                }
//            });
//
//            int f1 = bins.get(bins.size()-1).freq;
//            int f2 = bins.get(bins.size()-2).freq;
//            int s1 = (int)bins.get(bins.size()-1).signal;
//            int s2 = (int)bins.get(bins.size()-2).signal;
//
//            if (s1 > Constants.feedbackSignalThreshold && s2 > Constants.feedbackSignalThreshold) {
//                Utils.log("feedback freqs "+f1+","+f2);
//                Utils.log("feedback signals "+s1+","+s2);
//
//                MainActivity.av.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (f1 > f2) {
//                            Display.plotSpectrum(Constants.gview, feedback_spec_db, true, MainActivity.av.getResources().getColor(R.color.purple_500),
//                                    "Tx feedback " + f2 + "," + f1);
//                        }
//                        else {
//                            Display.plotSpectrum(Constants.gview, feedback_spec_db, true, MainActivity.av.getResources().getColor(R.color.purple_500),
//                                    "Tx feedback " + f1 + "," + f2);
//                        }
//                        Display.plotSpectrum(Constants.gview, noise_spec_db, false, MainActivity.av.getResources().getColor(R.color.black),"");
//
//                        Display.plotVerticalLine(Constants.gview, Constants.f_seq.get(Constants.nbin1_chanest -2));
//                        Display.plotVerticalLine(Constants.gview, Constants.f_seq.get(Constants.nbin2_chanest +2));
//                    }
//                });
//
//                int[] freqs = new int[]{f1,f2};
//                if (f1 > f2) {
//                    freqs = new int[]{f2, f1};
//                }
//                return Utils.freqs2bins(expand_freqs(freqs));
//            }
//        }
//        Utils.log("FeedbackSignal: No suitable bins found");
//        return null;
//    }

    // len is in ms
    public static short[] multi_freq_signal(int fbegin, int fend, int len_ms, boolean preamble, int m_attempt) {
        Log.e(LOG,"FeedbackSignal_multi_freq_signal");
        int len = (int)((len_ms/1000.0)*Constants.fs);
        if (preamble) {
            len += ((Constants.preambleTime/1000.0)*Constants.fs)+Constants.ChirpGap;
        }
        short[] txsig = new short[len];

        int counter = 0;
        if (preamble) {
            for (Short s : ChirpGen.preamble_s()) {
                txsig[counter++] = s;
            }
            counter += Constants.ChirpGap;
        }

        fbegin=1000+(fbegin*Constants.inc);
        fend=1000+(fend*Constants.inc);

        fbegin=Math.round(fbegin/10)*10;
        fend=Math.round(fend/10)*10;

        int freqs[] = new int[]{fbegin,fend};
        Utils.log("feedback freqs "+fbegin+","+fend);

        int fbackLen=(int)((Constants.fbackTime/1000.0)*Constants.fs);
        int freqSpacing = Constants.fs/fbackLen;
        int[] fseq = Utils.linspace(Constants.f_range[0],freqSpacing,Constants.f_range[1]);

        for (int freq = 0; freq < freqs.length; freq++) {
            int ff = freqs[freq];
            for (int i = counter; i < len; i++) {
                txsig[i] += (Math.sin(2.0 * Math.PI * ff * ((double)i / Constants.fs)))*(32767/2);
            }
        }

        short[] feedback = new short[fbackLen];
        for (int i = 0; i < feedback.length; i++) {
            feedback[i] = txsig[counter++];
        }

        double[] spec_fback = Utils.fftnative_short(feedback, feedback.length);
        double[] spec_fback_db = Utils.mag2db(spec_fback);

        FileOperations.writetofile(MainActivity.av, txsig, Utils.genName(Constants.SignalType.Feedback,m_attempt)+".txt");

        int finalFbegin = fbegin;
        int finalFend = fend;
        MainActivity.av.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Display.plotSpectrum(Constants.gview3, spec_fback_db, true, MainActivity.av.getResources().getColor(R.color.purple_500),
                        "Tx feedback "+finalFbegin+","+finalFend);
                Display.plotVerticalLine(Constants.gview3, Constants.f_seq.get(Constants.nbin1_chanest -2));
                Display.plotVerticalLine(Constants.gview3, Constants.f_seq.get(Constants.nbin2_chanest +2));
            }
        });

        return txsig;
    }

    public static int[] parse_signal(double[] preamble, double[] feedback) {
        Log.e(LOG,"FeedbackSignal_parse_signal");
//        int freq_spacing = Constants.fs/feedback.length;

        double[] preamble_spec = Utils.fftnative_double(preamble, preamble.length);

        long t1 = System.currentTimeMillis();
        double[] feedback_spec = Utils.fftnative_double(feedback, feedback.length);
//        double[] noise_spec = Utils.fftnative_double(noise, noise.length);

        double[] preamble_spec_db = Utils.mag2db(preamble_spec);
        double[] feedback_spec_db = Utils.mag2db(feedback_spec);
//        double[] noise_db = Utils.mag2db(noise_spec);

        int[] freqs= getFreqs(feedback, feedback_spec_db);
        Log.e("time","runtime2 "+(System.currentTimeMillis()-t1)+"");

        if (freqs.length==2) {
            Utils.log("feedback freqs " + freqs[0] + "," + freqs[freqs.length - 1]);
        }
        else {
            Utils.log("no frequencies selected");
        }

        (MainActivity.av).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Constants.gview2.removeAllSeries();
                Constants.gview3.removeAllSeries();
                Constants.gview2.setTitle("");
                Constants.gview3.setTitle("");
                Display.plotSpectrum(Constants.gview, preamble_spec_db, true, MainActivity.av.getResources().getColor(R.color.purple_500),"");
//                Display.plotSpectrum(Constants.gview, noise_db, false, MainActivity.av.getResources().getColor(R.color.black),"Rx preamble");

                Display.plotVerticalLine(Constants.gview, Constants.f_seq.get(Constants.nbin1_chanest));
                Display.plotVerticalLine(Constants.gview, Constants.f_seq.get(Constants.nbin2_chanest));

                if (freqs.length==2) {
                    Display.plotSpectrum(Constants.gview2, feedback_spec_db, true, MainActivity.av.getResources().getColor(R.color.purple_500),
                            "Rx Feedback " + freqs[0] + "," + freqs[freqs.length - 1]);
                }
                else {
                    Display.plotSpectrum(Constants.gview2, feedback_spec_db, true, MainActivity.av.getResources().getColor(R.color.purple_500),
                            "Rx Feedback");
                }
//                if (freqs.length>0) {
//                    Display.plotSpectrum(Constants.gview2, noise_db, false, MainActivity.av.getResources().getColor(R.color.black),
//                            "Rx Feedback " + freqs[0] + "," + freqs[freqs.length - 1]);
//                }
//                else {
//                    Display.plotSpectrum(Constants.gview2, noise_db, false, MainActivity.av.getResources().getColor(R.color.black),
//                            "Rx Feedback");
//                }

                Display.plotVerticalLine(Constants.gview2, Constants.f_seq.get(Constants.nbin1_default -2));
                Display.plotVerticalLine(Constants.gview2, Constants.f_seq.get(Constants.nbin2_default +2));
            }
        });

        return freqs;
    }

    // top 2 bins by snr
    public static int[] getFreqs(double[] feedback, double[] feedback_spec_db) {

        LinkedList<Bin> bins = new LinkedList<>();
//        double[] smooth_noise = Utils.movingaverage(noise_spec_db,10);
//        double[] smooth_sig = Utils.movingaverage(feedback_spec_db,2);
        double[] smooth_sig = feedback_spec_db;

        int feedbackFreqSpacing = Constants.fs/feedback_spec_db.length;
        int startIdx = Constants.f_range[0]/feedbackFreqSpacing;
        int endIdx = Constants.f_range[1]/feedbackFreqSpacing;

        for (int i = startIdx; i < endIdx; i++) {
            int freq = i*feedbackFreqSpacing;
            // -1 for smoothing
//            int idx=(freq / freq_spacing);
//            int idx_start=(Constants.f_seq.get(Constants.valid_carrier_default[0]-2) / freq_spacing);
//            int idx_end=(Constants.f_seq.get(Constants.valid_carrier_default[Constants.valid_carrier_default.length-1]+2) / freq_spacing);

            double signal = smooth_sig[i];
//            double noise = smooth_noise[freq / freq_spacing];
            double[] noise1 = Utils.segment(smooth_sig,i-5,i-2);
            double[] noise2 = Utils.segment(smooth_sig,i+2,i+5);

            double val=0;
            for(Double d : noise1) {
                val+=d;
            }
            for(Double d : noise2) {
                val+=d;
            }
            double noise = val / (noise1.length+noise2.length);

            double snr = signal-noise;
            Log.e("feedback",freq+","+(int)snr+","+(int)signal+","+(int)noise);
            double prom = getProm(smooth_sig,i,i-2,i+2);
//            double gammaVal = gammaValue(feedback, idx);
//            if (Constants.fs%freq == 0 && gammaVal < Constants.GammaThresh) {
//                snr=-100;
//                prom=-100;
//            }
            if (snr >= Constants.FEEDBACK_SNR_THRESH) {
                bins.add(new Bin(freq, snr, signal, noise, prom));
            }
        }

        Collections.sort(bins, new Comparator<Bin>() {
            @Override
            public int compare(Bin c1, Bin c2) {
                double met1 = c1.prom+c1.snr;
                double met2 = c2.prom+c2.snr;
                if (met1 > met2) {return 1;}
                else if (met1 == met2) {return 0;}
                else {return -1;}
            }
        });

        for (Bin bin : bins) {
            Log.e("feedback",bin.freq+","+bin.snr);
        }

        LinkedList<Integer> remove=new LinkedList<>();
        for (int i = bins.size()-1; i >= 1; i--) {
            if (Math.abs(bins.get(i).freq - bins.get(i-1).freq)==feedbackFreqSpacing) {
                if (bins.get(i).snr > bins.get(i-1).snr) {
                    remove.add(i);
                }
                else {
                    remove.add(i-1);
                }
            }
        }
        Log.e("feedback","binsize "+bins.size());
        for (Integer i : remove) {
            Log.e("feedback","remove "+i);
            bins.remove(bins.get(i));
        }
        Log.e("feedback","binsize "+bins.size());

        if (bins.size() >= 2) {
            int f1 = bins.get(bins.size() - 1).freq;
            int f2 = bins.get(bins.size() - 2).freq;
            double s1 = bins.get(bins.size() - 1).snr;
            double s2 = bins.get(bins.size() - 2).snr;

            Bin nf1 = search(bins, f1 / 2);
            Bin nf2 = search(bins, f2 / 2);
            if (nf1 != null && nf1.snr > s1) {
                f1 = nf1.freq;
                s1 = nf1.snr;
            }
            if (nf2 != null && nf2.snr > s2) {
                f2 = nf2.freq;
                s2 = nf2.snr;
            }

//        Utils.log("feedback signals "+s1+","+s2);

//            f1=Math.round(f1/Constants.inc)*Constants.inc;
//            f2=Math.round(f2/Constants.inc)*Constants.inc;
            if (s1 >= Constants.FEEDBACK_SNR_THRESH && s2 >= Constants.FEEDBACK_SNR_THRESH) {
                if (f1 > f2) {
                    return new int[]{f2, f1};
                }
                return new int[]{f1, f2};
            }
//            else if (s1 >= Constants.FEEDBACK_SNR_THRESH) {
//                return new int[]{f1};
//            }
        }
        else if (bins.size() == 1) {
            return new int[]{bins.get(0).freq,bins.get(0).freq};
        }
        return new int[]{-1,-1};
    }

    public static Bin search(LinkedList<Bin> bins, int freq) {
        for (Bin bin : bins) {
            if (bin.freq == freq) {
                return bin;
            }
        }
        return null;
    }

    private static class Bin {
        int freq;
        double snr;
        double signal;
        double noise;
        double prom;
        public Bin(int freq, double snr, double signal, double noise, double prom) {
            this.freq = freq;
            this.snr = snr;
            this.signal = signal;
            this.noise = noise;
            this.prom = prom;
        }
    }

    public static double gammaValue(double[] sig, int freq) {
        int seglen=480;
        int numsegs=sig.length/seglen;
        int cc=0;
        double abscounter=0;
        double[][][] specs = new double[numsegs][][];
        int idx = freq/numsegs;
        for (int i = 0; i < numsegs; i++) {
            double[] seg = Utils.segment(sig,cc,cc+seglen-1);
            double[][] spec = Utils.fftcomplexoutnative_double(seg,seg.length);
            double[] abs = Utils.abs(spec);
            specs[i]=spec;
            abscounter += abs[idx];
            cc+=seglen;
        }
        double meanamp = abscounter / numsegs;
        double[] x = new double[numsegs];
        double[] y = new double[numsegs];
        for (int i = 0; i < numsegs; i++) {
            x[i] = specs[i][0][idx]/meanamp;
            y[i] = specs[i][1][idx]/meanamp;
        }

        double t1 = Math.pow(Utils.mean(x),2) + Math.pow(Utils.mean(y),2);
        double t2 = Utils.sum(Utils.sum(Utils.pow(x,2), Utils.pow(y,2)));
        double gamma = t1*x.length/t2;
        return gamma;
    }

    // bins which exceed threshold
    public static int[] getBins1(double[] feedback_spec_db, int freq_spacing) {
        LinkedList<Integer> valid_bins = new LinkedList<>();
        LinkedList<Integer> snrs = new LinkedList<>();
        for (Integer i : Constants.valid_carrier_preamble) {
            int freq = Constants.f_seq.get(i);

            double signal = feedback_spec_db[freq / freq_spacing];

            int counter=0;
            int summer=0;
            for (int j = 4500; j < 5000; j++) {
                summer += feedback_spec_db[j / freq_spacing];
                counter+=1;
            }
            double noise2 = (summer/counter);

            int snr = (int)(signal-noise2);
            snrs.add(snr);
            if (snr > Constants.feedbackSignalThreshold) {
                valid_bins.add(i);
            }
        }

        String f = "";
        String snr_str="";
        for (int i = 0; i < valid_bins.size(); i++) {
            f += Constants.f_seq.get(valid_bins.get(i))+",";
            snr_str += snrs.get(i)+",";
        }
        Utils.log("feedback freqs "+valid_bins.size()+"\n"+f);
        Utils.log("feedback snrs "+snrs.size()+"\n"+snr_str);

        return Utils.convert(valid_bins);
    }

    public static double getProm(double[] ar, int peakloc, int beginloc, int endloc) {
        double peakval = ar[peakloc];
        int leftmarker = 0;
        int rightmarker = endloc;
        for (int i = peakloc-1; i >= beginloc ; i--) {
            if (ar[i] >= peakval) {
                leftmarker=i;
                break;
            }
        }
        for(int i = peakloc+1; i < endloc; i++) {
            if (ar[i] >= peakval) {
                rightmarker=i;
                break;
            }
        }

        double leftmin=ar[beginloc];
        double rightmin=ar[endloc];
        for(int i = beginloc; i < leftmarker; i++) {
            if (ar[i] < leftmin) {
                leftmin=ar[i];
            }
        }

        for(int i = rightmarker; i<endloc; i++) {
            if (ar[i] < rightmin) {
                rightmin=ar[i];
            }
        }

        double ref = leftmin > rightmin ? leftmin : rightmin;

        double out = peakval - ref;

        return out;
    }
}
