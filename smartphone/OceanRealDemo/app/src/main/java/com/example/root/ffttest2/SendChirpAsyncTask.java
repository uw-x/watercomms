package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.tv4;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Arrays;

public class SendChirpAsyncTask extends AsyncTask<Void, Void, Void> {
    Activity av;
    int num_measurements = 0;
    public SendChirpAsyncTask(Activity activity, int num_measurements) {
        this.av = activity;
        this.num_measurements = num_measurements;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public void setupTimer() {
        av.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                double totalTime = 0;
                if (Constants.user.equals(Constants.User.Alice)) {
                    double soundingTimeTx = 1;
                    double extractionFeedbackTime = 1;
                    totalTime += (soundingTimeTx + Constants.WaitForFeedbackTime +
                            extractionFeedbackTime);
                    if (Constants.SEND_DATA) {
                        totalTime+=Constants.WaitForDataTime;
                    }
                    Constants.AliceTime = (int)totalTime;
                    totalTime *= 1000;
                    totalTime *= num_measurements;

                    totalTime += 1000+(Constants.initSleep*1000);
                }
                else if (Constants.user.equals(Constants.User.Bob)) {
                    int extractSoundingTime = 1;
                    int sendFeedbackTime = 1;
                    totalTime += Constants.WaitForSoundingTime+
                            extractSoundingTime+sendFeedbackTime;
                    totalTime += Constants.SoundingOffset;
                    if (Constants.SEND_DATA) {
                        totalTime+=Constants.WaitForDataTime;
                    }
                    Constants.BobTime = (int)totalTime;
                    totalTime *= 1000;
                    totalTime *= num_measurements;
                    totalTime += 1000+(Constants.initSleep*1000);
                }
            }
        });
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);

        MainActivity.unreg(av);

        if (Constants.timer!=null) {
            Constants.timer.cancel();
            tv4.setText("0");
        }

        Constants.sp1=null;
        Constants._OfflineRecorder = null;
        Constants.user  = Constants.User.Bob;
        MainActivity.startMethod(av);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Constants.WaitForFeedbackTime = Constants.WaitForFeedbackTimeDefault + Constants.SyncLag;
        Constants.WaitForSoundingTime = Constants.WaitForSoundingTimeDefault + Constants.SyncLag - Constants.SoundingOffset;
        Constants.WaitForBerTime = Constants.WaitForBerTimeDefault + Constants.SyncLag;
        Constants.WaitForPerTime = Constants.WaitForPerTimeDefault + Constants.SyncLag;

        Constants.SEND_DATA=true;
        Constants.WaitForDataTime = Constants.WaitForPerTime;
        Constants.AdaptationMethod = 3;

        FileOperations.writetofile(MainActivity.av, Constants.SNR_THRESH2+"\n"+Constants.FreAdaptScaleFactor+"\n"+Constants.SNR_THRESH2_2,
                Utils.genName(Constants.SignalType.AdaptParams,0)+".txt");

        setupTimer();

        sleep(Constants.initSleep * 1000);

        Constants.StartingTimestamp = System.currentTimeMillis();
        appendToLog(Constants.SignalType.Start.toString());

        if (Constants.user.equals(Constants.User.Alice)) {
            FileOperations.writetofile(MainActivity.av, Constants.FLIP_SYMBOL + "",
                    Utils.genName(Constants.SignalType.FlipSyms, 0) + ".txt");
        }

        for (int i = 0; i < num_measurements; i++) {
            Log.e("timer","work "+i);
            int flag = work(i);
            updateTimer((i+1)+"");
            if (flag == -1) {
                updateTimer("-1");
                break;
            }
        }
        return null;
    }

    public static void appendToLog(String s) {
        if (s.equals(Constants.SignalType.Start.toString())) {
            if (Constants.user.equals(Constants.User.Alice)) {
                String ts = System.currentTimeMillis()+"";
                String filename = Constants.user.toString() + "-" + Constants.SignalType.Sounding + "-" + "log";
                FileOperations.appendtofile(MainActivity.av, ts + "\n", filename + ".txt");
                filename = Constants.user.toString() + "-" + Constants.SignalType.Data + "-" + "log";
                FileOperations.appendtofile(MainActivity.av, ts + "\n", filename + ".txt");
            }
            else {
                String filename = Constants.user.toString() + "-" + Constants.SignalType.Feedback + "-" + "log";
                FileOperations.appendtofile(MainActivity.av, System.currentTimeMillis() + "\n", filename + ".txt");
            }
        }
        else {
            String filename = Constants.user.toString() + "-" + s + "-" + "log";
            FileOperations.appendtofile(MainActivity.av, System.currentTimeMillis() + "\n", filename + ".txt");
        }
    }

    public void updateTimer(String ss) {
        MainActivity.av.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv4.setText(ss);
            }
        });
    }

    public int work(int m_attempt) {
        double[] tx_preamble = PreambleGen.preamble_d();
        if (Constants.user.equals(Constants.User.Alice)) {
            int chirpLoopNumber = 0;
            double[] feedback_signal = null;
            do {
                short[] sig = PreambleGen.sounding_signal_s();
                FileOperations.writetofile(MainActivity.av, sig, Utils.genName(Constants.SignalType.Sounding, m_attempt) + ".txt");

                Constants.sp1 = new AudioSpeaker(av, sig, Constants.fs, 0, sig.length, false);
                appendToLog(Constants.SignalType.Sounding.toString());
                Constants.sp1.play(Constants.volume);

                int sig_len = (int)(((double)sig.length/Constants.fs)*1000);
                sleep(sig_len+Constants.SendPad);

                feedback_signal = Utils.waitForChirp(Constants.SignalType.Feedback, m_attempt, chirpLoopNumber);
                chirpLoopNumber++;
                if (chirpLoopNumber >= 3 || !Constants.work) {
                    return -1;
                }
            } while (feedback_signal == null);

            double[] seg = Utils.segment(feedback_signal,0,24000-1);
            double[] xcorr_out = Utils.xcorr_online(tx_preamble, seg);

            int[] valid_bins = FeedbackSignal.extractSignalHelper(feedback_signal, (int)xcorr_out[1], m_attempt);

            if (Constants.SEND_DATA) {
                appendToLog(Constants.SignalType.Data.toString());
                if (valid_bins.length >= 1 && valid_bins[0] != -1) {
                    sendData(valid_bins, m_attempt);
                }
                try {
                    Thread.sleep(3000);
                }
                catch(Exception e){
                    Log.e("asdf",e.toString());
                }
            }
            return 0;
        }
        else if (Constants.user.equals(Constants.User.Bob)) {
            int chirpLoopNumber = 0;
            int[] valid_bins = null;
            double[] sounding_signal = null;
            do {
                sounding_signal = Utils.waitForChirp(Constants.SignalType.Sounding, m_attempt, chirpLoopNumber);
                if (sounding_signal == null) {
                    return -1;
                }

                double[] seg = Utils.segment(sounding_signal,0,24000-1);
                double[] xcorr_out = Utils.xcorr_online(tx_preamble, seg);

                valid_bins = ChannelEstimate.extractSignal_withsymbol_helper(av, sounding_signal, (int)xcorr_out[1], m_attempt);
                chirpLoopNumber++;

                if (!Constants.work) {
                    return -1;
                }
            } while (valid_bins == null || valid_bins.length == 0 || valid_bins[0] == -1);

            short[] feedback = FeedbackSignal.encodeFeedbackSignal(valid_bins[0], valid_bins[valid_bins.length - 1],
                    Constants.fbackTime, true, m_attempt);

            Constants.sp1 = new AudioSpeaker(av, feedback, Constants.fs, 0, feedback.length, false);
            appendToLog(Constants.SignalType.Feedback.toString());
            Constants.sp1.play(Constants.volume);

            int stime = (int) ((feedback.length / (double) Constants.fs) * 1000);
            sleep(stime+Constants.SendPad);

            double[] data_signal = null;
            if (Constants.SEND_DATA) {
                data_signal = Utils.waitForChirp(Constants.SignalType.DataRx, m_attempt, 0);
            }
            if (data_signal!=null) {
                Decoder.decode_helper(av, data_signal, valid_bins);
            }
            return 0;
        }
        return 0;
    }

    public static void sendData(int[] valid_bins, int m_attempt) {
        send_data_per(valid_bins,m_attempt);
    }

    public static void send_data_helper(int numbits, int[] valid_bins, int m_attempt,
                                 Constants.SignalType sigType,Constants.ExpType expType) {
        short[] bits = SymbolGeneration.getCodedBits();

        String out="";
        for (int i = 0; i < bits.length; i++) {
            out+=bits[i]+"";
        }

        short[] txsig=SymbolGeneration.generateDataSymbols(bits, valid_bins, Constants.data_symreps, true, sigType,m_attempt);

        FileOperations.writetofile(MainActivity.av, txsig,
                Utils.genName(Constants.SignalType.DataAdapt, m_attempt) + ".txt");

        Constants.sp1 = new AudioSpeaker(MainActivity.av, txsig, Constants.fs, 0, txsig.length, false);
        Constants.sp1.play(Constants.volume);

        int sleepTime = (int) (((double) txsig.length / Constants.fs) * 1000);
        sleep(sleepTime + Constants.SendPad);
    }

    public static void send_data_ber(int[] valid_bins, int m_attempt) {
        FileOperations.writetofile(MainActivity.av, Constants.codeRate.toString(),
                Utils.genName(Constants.SignalType.CodeRate,m_attempt)+".txt");
        FileOperations.writetofile(MainActivity.av, Utils.trim(Arrays.toString(valid_bins)),
                Utils.genName(Constants.SignalType.ValidBins, m_attempt) + ".txt");

        // adaptive  //////////////////////////////////////////////
        send_data_helper(valid_bins.length*Constants.Nsyms,
                valid_bins, m_attempt,
                Constants.SignalType.DataAdapt,
                Constants.ExpType.BER);
        // full bandwidth//////////////////////////////////////////////
        int[] end_bins = new int[]{79,49,29};
        Constants.SignalType[] sigTypes = new Constants.SignalType[]{
                Constants.SignalType.DataFull_1000_4000,
                Constants.SignalType.DataFull_1000_2500,
                Constants.SignalType.DataFull_1000_1500,
        };
        for (int i = 0; i < end_bins.length; i++) {
            int[] bins = generateBins(20, end_bins[i]);
            send_data_helper(bins.length * Constants.Nsyms, bins, m_attempt,
                    sigTypes[i],Constants.ExpType.BER);
        }
        //////////////////////////////////////////////
    }

    public static int[] generateBins(int bin1, int bin2) {
        int[] bins = new int[bin2-bin1+1];
        int counter=0;
        for (int i = bin1; i <= bin2; i++) {
            bins[counter++]=i;
        }
        return bins;
    }

    public static void send_data_per(int[] valid_bins, int m_attempt) {
        FileOperations.writetofile(MainActivity.av, Constants.codeRate.toString(),
                Utils.genName(Constants.SignalType.CodeRate,m_attempt)+".txt");
        FileOperations.writetofile(MainActivity.av, Utils.trim(Arrays.toString(valid_bins)),
                Utils.genName(Constants.SignalType.ValidBins, m_attempt) + ".txt");

        // calc bits//////////////////////////////////////////////
        int msgbits = 16;
        int traceDepth = 0;
        msgbits += traceDepth;

        // adapt//////////////////////////////////////////////
        send_data_helper(msgbits,
                valid_bins, m_attempt,
                Constants.SignalType.DataAdapt, Constants.ExpType.PER);
        Log.e("numbits","adapt "+msgbits);
        // full bandwidth//////////////////////////////////////////////
    }

    public static void sleep(int s) {
        try {
            Thread.sleep(s);
        }
        catch (Exception e) {
            Utils.log(e.getMessage());
        }
    }
}
