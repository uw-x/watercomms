package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;

import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class FeedbackSignal {
    public static int[] extractSignalHelper(double[] rec, int start_point, int m_attempt) {
        double[] preamble = PreambleGen.preamble_d();
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

    public static short[] encodeFeedbackSignal(int fbegin, int fend, int len_ms, boolean preamble, int m_attempt) {
        int len = (int)((len_ms/1000.0)*Constants.fs);
        if (preamble) {
            len += ((Constants.preambleTime/1000.0)*Constants.fs)+Constants.ChirpGap;
        }
        short[] txsig = new short[len];

        int counter = 0;
        if (preamble) {
            for (Short s : PreambleGen.preamble_s()) {
                txsig[counter++] = s;
            }
            counter += Constants.ChirpGap;
        }

        fbegin=Constants.f_range[0]+(fbegin*Constants.inc);
        fend=Constants.f_range[0]+(fend*Constants.inc);

        fbegin=Math.round(fbegin/10)*10;
        fend=Math.round(fend/10)*10;

        // encode the feedback frequencies
        int freqs[] = new int[]{fbegin,fend};
        int fbackLen=(int)((Constants.fbackTime/1000.0)*Constants.fs);
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

        // plot transmitted frequencies
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

        double[] preamble_spec = Utils.fftnative_double(preamble, preamble.length);

        double[] feedback_spec = Utils.fftnative_double(feedback, feedback.length);

        double[] preamble_spec_db = Utils.mag2db(preamble_spec);
        double[] feedback_spec_db = Utils.mag2db(feedback_spec);

        int[] freqs= decodeFeedbackSignal(feedback_spec_db);

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

                Display.plotVerticalLine(Constants.gview2, Constants.f_seq.get(Constants.nbin1_default -2));
                Display.plotVerticalLine(Constants.gview2, Constants.f_seq.get(Constants.nbin2_default +2));
            }
        });

        return freqs;
    }

    public static int[] decodeFeedbackSignal(double[] feedback_spec_db) {

        LinkedList<Bin> bins = new LinkedList<>();
        double[] smooth_sig = feedback_spec_db;

        int feedbackFreqSpacing = Constants.fs/feedback_spec_db.length;
        int startIdx = Constants.f_range[0]/feedbackFreqSpacing;
        int endIdx = Constants.f_range[1]/feedbackFreqSpacing;

        for (int i = startIdx; i < endIdx; i++) {
            int freq = i*feedbackFreqSpacing;

            double signal = smooth_sig[i];
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
            double prom = getProm(smooth_sig,i,i-2,i+2);

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

        for (Integer i : remove) {
            bins.remove(bins.get(i));
        }

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

            if (s1 >= Constants.FEEDBACK_SNR_THRESH && s2 >= Constants.FEEDBACK_SNR_THRESH) {
                if (f1 > f2) {
                    return new int[]{f2, f1};
                }
                return new int[]{f1, f2};
            }
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
