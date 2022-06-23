package com.example.root.ffttest2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.jjoe64.graphview.GraphView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Constants {
    public enum EqMethod {
        Freq,
        Time
    }
    public enum User {
        Alice,
        Bob
    }
    public enum SignalType {
        Sounding,
        Start,
        FreqEsts,
        AXcorr,
        ANaiser,
        ASymCheckSNR,
        ASymCheckFreq,
        Feedback,
        FeedbackFreqs,
        ExactFeedbackFreqs,
        AdaptParams,
        SNRs,
        DataRx,
        Data,
        DataAdapt,
        DataFull_1000_4000,
        DataFull_1000_2500,
        DataFull_1000_1500,
        BitsAdapt,
        BitsFull_1000_4000,
        BitsFull_1000_2500,
        BitsFull_1000_1500,
        BitsAdapt_Padding,
        BitsFull_1000_4000_Padding,
        BitsFull_1000_2500_Padding,
        BitsFull_1000_1500_Padding,
        Bit_Fill_Adapt,
        Bit_Fill_1000_4000,
        Bit_Fill_1000_2500,
        Bit_Fill_1000_1500,
        CodeRate,
        SNRMethod,
        ValidBins,
        FlipSyms,
        Timestamp
    }
    public enum EstSignalType {
        Chirp,
        Symbol
    }
    public enum ExpType {
        BER,
        PER
    }
    public enum CodeRate {
        None,
        C1_2,
        C2_3
    }

    public static double XCORR_MAX_VAL_HEIGHT_FAC = .8;
    public static boolean CODING = true;
    public static int XcorrVersion = 2;
    public static boolean work = false;
    public static int AliceTime = 0;
    public static int BobTime = 0;
    public static boolean FIFO = false;
    public static boolean IO = false;
    public static double SoundingOffset = .5;

    //xcorr
//    public static double MinXcorrVal = 2E13;
    public static double MinXcorrVal = 1E12;
    public static int XcorrAboveThresh = 25;
    public static int VAR_THRESH = 20;
    public static int XcorrAmpDiff = 10;
    public static int xcorr_method=2;

    public static int FEEDBACK_SNR_THRESH = 13;
    public static int CheckSymSNRThresh = 5;
//    public static int FEEDBACK_SNR_THRESH = 5;

    public static int SNR_THRESH1 = 5;
    public static int SNR_THRESH2 = 8;
    public static int SNR_THRESH2_2 = 4;

    static int SyncLag = 2;
    static int WaitForFeedbackTimeDefault = 1;
    static int WaitForSoundingTimeDefault = 1;
    static int WaitForBerTimeDefault = 12;
    static int WaitForPerTimeDefault = 4;

    static int RecorderStepSize = 24000;
    static float NaiserThresh = .5f;
    static double WaitForFeedbackTime;
    static double WaitForSoundingTime;
    static double WaitForBerTime;
    static double WaitForPerTime;
    static int AdaptationMethod = 2;
    static double WaitForDataTime = 0;

    static TextToSpeech tts = null;
    static boolean DIFFERENTIAL=true;
    static boolean INTERLEAVE=true;
    static float FreAdaptScaleFactor;

    static long StartingTimestamp;

    public static double GammaThresh = .8;

    public static int maxbits=5;
    public static int exp_num=5;
    public static int SNR_THRESH = 10; //unused
    public static Spinner spinner,spinner2,spinner3;
    public static CodeRate codeRate = CodeRate.None;
    public static int DATA_LEN = 32;
    public static int mattempts=1;
    public static long ts;
    public static Button startButton,clearButton,stopButton;
    public static float volume=0.2f;
    public static TextView tv1,tv2,tv3,tv4, debugPane,tv5,tv6,tv7,tv8,tv9,tv10,tv13,tv14,tv15,tv16,tv17,tv18,tv19,tv20,tv21,msgview;
    public static NestedScrollView sview;
    public static CountDownTimer timer;
    public static EditText et1,et2,et3,et4,et5,et6,et7,et8,et9,et10,et11,et12,et13,et14,et15,et17,et18,et25,et26,et27;
    public static SendChirpAsyncTask task;
    public static User user;
    public static Switch sw1,sw2,sw3,sw4,sw5,sw6,sw7,sw8,sw9,sw10,sw11,sw12;
    public static EqMethod eqMethod = EqMethod.Freq;
    public static String LOG="log";
    public static int Ns=960;
    public static int Gi=0;
    public static int[] f_range={1000,4000};
    public static int sym_eq_freq = 10;
    public static int fs = 48000;
    public static int Cp = 67;
    public static int Nsyms=100;
    public static int tap_num = 480;
    public static int initSleep=0;
    public static int sync_offset=60;

    public static int nbin1_default, nbin2_default;
    public static int subcarrier_number_default;

    public static int nbin1_chanest, nbin2_chanest;
    public static int subcarrier_number_chanest;

    public static int butterworthFiltOffset = 35;
//    public static int besselFiltOffset = 23;
    public static int besselFiltOffset = 0;

    public static int nbin1_data, nbin2_data;
    public static int subcarrier_number_data;

    public static int inc = fs/Ns;
    public static int snr_method=2;

    public static int sym_len;
    public static int blocklen;

    public static GraphView gview,gview2,gview3;
    public static int ChirpGap = 960;
    public static int[] valid_carrier_preamble;
    public static int[] valid_carrier_data;
    public static int[] valid_carrier_default;
    public static LinkedList<Integer> f_seq;
    public static int[] null_carrier = {};
    public static HashSet<Integer> pilots;

    public static boolean FLIP_SYMBOL = false;
    public static boolean stereo = false;
    public static LinkedList<String>acc;
    public static LinkedList<String>gyro;
    public static boolean sensorFlag=false;
    public static boolean imu=true;
    public static boolean writing=true;
    static Random random;
    static double feedbackSignalThreshold=40;
    static AudioSpeaker sp1;
    static OfflineRecorder _OfflineRecorder;

    static short[] pn600_bits = new short[]{1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1};
    static short[] pn300_bits = new short[]{1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1};
    static short[] pn120_bits = new short[]{1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1};
    static short[] pn60_bits = new short[]{1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0,1,1,0,1,1,1,0,1,1,0,0,1,1,0,1,0,1,0,1,1};
    static short[] pn40_bits = new short[]{1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,0,1,0};
    static short[] pn20_bits = new short[]{1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,0};

    static double[][] pn20_syms = new double[][]
    {{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1}};

    static double[][] pn40_syms = new double[][]
    {{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1}};

    static double[][] pn60_syms = new double[][]
    {{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    };

    static double[][] pn120_syms = new double[][]
    {{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1}};

    static double[][] pn300_syms = new double[][]
    {{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1}};

    static double[][] pn600_syms = new double[][]
    {{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {1,1,1,1,1},{-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},
    {1,1,1,1,1},{1,1,1,1,1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{-1,-1,-1,-1,-1},
    {-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1},{1,1,1,1,1},{1,1,1,1,1},{1,1,1,1,1}};

    static boolean feedbackPreamble=false;
    static int preambleStartFreq;
    static int preambleEndFreq;
    static int chirpPreambleTime = 100; // milliseconds
    static int preambleTime = 160; // milliseconds
    static int fbackTime = 200; // milliseconds
    static boolean DecodeData = false;
    static boolean SEND_DATA = true;
    static int SendPad = 100;
    static int data_symreps = 1;
    static int chanest_symreps = 7;
    static EstSignalType est_sig = EstSignalType.Chirp;
    static boolean TEST = true;
    static short[] data_nocode;
    static short[] data12;
    static short[] data23;
    static boolean NAISER = true;
    static boolean CHECK_SYM = false;
    static int messageID=-1;
    static HashMap<Integer,String>mmap=new HashMap<>();
    static int[] cc = new int[]{7,5,10};
    static boolean SPEECH_IN=false;
    static boolean SPEECH_OUT=false;

    public static void toggleUI(boolean val) {
        Constants.sw1.setEnabled(val);
        Constants.sw2.setEnabled(val);
        Constants.sw3.setEnabled(val);
//        Constants.sw4.setEnabled(val);
//        Constants.sw5.setEnabled(val);
//        Constants.sw6.setEnabled(val);
        Constants.sw7.setEnabled(val);
        Constants.sw8.setEnabled(val);
        Constants.sw9.setEnabled(val);
        Constants.sw10.setEnabled(val);
        Constants.sw11.setEnabled(val);
//        Constants.startButton.setEnabled(val);
        Constants.clearButton.setEnabled(val);
//        Constants.stopButton.setEnabled(!val);
        Constants.et1.setEnabled(val);
        Constants.et2.setEnabled(val);
        Constants.et3.setEnabled(val);
        Constants.et4.setEnabled(val);
        Constants.et5.setEnabled(val);
        Constants.et6.setEnabled(val);
        Constants.et7.setEnabled(val);
        Constants.et8.setEnabled(val);
        Constants.et9.setEnabled(val);
        Constants.et10.setEnabled(val);
        Constants.et11.setEnabled(val);
        Constants.et12.setEnabled(val);
        Constants.et13.setEnabled(val);
        Constants.et14.setEnabled(val);
        Constants.et15.setEnabled(val);
        Constants.et17.setEnabled(val);
        Constants.et18.setEnabled(val);
        Constants.et25.setEnabled(val);
        Constants.et26.setEnabled(val);
        Constants.et27.setEnabled(val);
        Constants.spinner.setEnabled(val);
        Constants.spinner2.setEnabled(val);
        Constants.spinner3.setEnabled(val);
    }

    public static void resetRandom() {random = new Random(1);};
    public static double[] naiser = null;
    static View vv;

    static double[][] preamble_spec = null;
    public static void setup(Context cxt) {
        mmap.put(1,"Ascend");
        mmap.put(2,"Descend");
        mmap.put(3,"Something's wrong");
        mmap.put(4,"Are you okay?");
        mmap.put(5,"Okay");
        mmap.put(6,"Stop!");
        mmap.put(7,"Turn around");
        mmap.put(8,"Which way?");
        mmap.put(9,"Boat");
        mmap.put(10,"Go to buddy");
        mmap.put(11,"Hold on!");
        mmap.put(12,"Who's leading?");
        mmap.put(13,"Level off");
        mmap.put(14,"Relax");
        mmap.put(15,"Give me air!");
        mmap.put(16,"Out of air!");
        mmap.put(17,"Help!");
        mmap.put(18,"I don't know");
        mmap.put(19,"Danger over there");
        mmap.put(20,"I'm cold");
        mmap.put(21,"Look");
        mmap.put(22,"Think");
        mmap.put(23,"Ear is blocked");
        mmap.put(24,"Cut the line");
//        double[] preamble_spec1=null;
//        double[] preamble_spec2=null;

//        preamble_spec = new double[2][];
//        preamble_spec[0] = preamble_spec1;
//        preamble_spec[1] = preamble_spec2;
//        Utils.conjnative(preamble_spec);

        data_nocode = FileOperations.readrawasset_binary(cxt, R.raw.data_nocode);
        data12 = FileOperations.readrawasset_binary(cxt, R.raw.encode_data_1_2);
        data23 = FileOperations.readrawasset_binary(cxt, R.raw.encode_data_2_3);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
        // populate UI elements
        Constants.user=User.valueOf(prefs.getString("user", User.Alice.toString()));
        sw1.setText(Constants.user.toString());
        sw1.setChecked(Constants.user.equals(User.Alice));
        Constants.sw2.setEnabled(!sw1.isChecked());

//        if (Constants.user.equals(User.Alice)) {
//            Constants.gview3.setVisibility(View.GONE);
//        }
//        else if (Constants.user.equals(User.Bob)) {
//            Constants.gview3.setVisibility(View.VISIBLE);
//        }
        Constants.gview.setVisibility(View.GONE);

        Constants.volume=prefs.getFloat("volume",Constants.volume);
        et1.setText(Constants.volume+"");

//        Constants.preambleTime=prefs.getInt("preamble_len",Constants.preambleTime);
//        et2.setText(Constants.preambleTime+"");

        Constants.initSleep=prefs.getInt("init_sleep",Constants.initSleep);
        et3.setText(Constants.initSleep+"");

        Constants.DecodeData=prefs.getBoolean("decode_data", Constants.DecodeData);
        sw2.setChecked(Constants.DecodeData);

        Constants.TEST=prefs.getBoolean("test", Constants.TEST);
        sw3.setChecked(Constants.TEST);

        Constants.stereo=prefs.getBoolean("stereo", Constants.stereo);
        sw4.setChecked(Constants.stereo);

        Constants.imu=prefs.getBoolean("imu", Constants.imu);
        sw5.setChecked(Constants.imu);

        Constants.est_sig=EstSignalType.valueOf(prefs.getString("est_sig", Constants.est_sig.toString()));
        sw6.setChecked(Constants.est_sig.equals(EstSignalType.Chirp));
        sw6.setText(Constants.est_sig.toString());

        Constants.feedbackPreamble=prefs.getBoolean("feed_pre", Constants.feedbackPreamble);
        sw7.setChecked(Constants.feedbackPreamble);

        Constants.SEND_DATA=prefs.getBoolean("send_data", Constants.SEND_DATA);
        sw8.setChecked(Constants.SEND_DATA);

        Constants.FLIP_SYMBOL=prefs.getBoolean("flip_symbol", Constants.FLIP_SYMBOL);
        sw9.setChecked(Constants.FLIP_SYMBOL);

        Constants.NAISER=prefs.getBoolean("naiser", Constants.NAISER);
        sw10.setChecked(Constants.NAISER);

        Constants.CHECK_SYM=prefs.getBoolean("check_sym", Constants.CHECK_SYM);
        sw11.setChecked(Constants.CHECK_SYM);

        updateNaiser(MainActivity.av);

        Constants.Nsyms=prefs.getInt("nsyms",Constants.Nsyms);
        et5.setText(Constants.Nsyms+"");

        Constants.f_range[0]=prefs.getInt("f1",Constants.f_range[0]);
        et6.setText(Constants.f_range[0]+"");
        Constants.f_range[1]=prefs.getInt("f2",Constants.f_range[1]);
        et7.setText(Constants.f_range[1]+"");
        Constants.data_symreps =prefs.getInt("symreps",Constants.data_symreps);
        et8.setText(Constants.data_symreps +"");

        Constants.mattempts=prefs.getInt("mattempts",Constants.mattempts);
        et9.setText(Constants.mattempts+"");

        Constants.exp_num=prefs.getInt("exp_num",Constants.exp_num);
        et10.setText(Constants.exp_num+"");

        Constants.SyncLag=prefs.getInt("sync_lag",Constants.SyncLag);
        et11.setText(Constants.SyncLag+"");

        Constants.FreAdaptScaleFactor=prefs.getFloat("scale_factor",(float)Constants.FreAdaptScaleFactor);
        et12.setText(Constants.FreAdaptScaleFactor+"");

        Constants.SNR_THRESH2_2=(int)prefs.getInt("snr_thresh2_2",Constants.SNR_THRESH2_2);
        et13.setText(Constants.SNR_THRESH2_2+"");

        Constants.MinXcorrVal=prefs.getFloat("xcorr_thresh",(float)Constants.MinXcorrVal);
        et14.setText(Constants.MinXcorrVal+"");

        Constants.XCORR_MAX_VAL_HEIGHT_FAC=prefs.getFloat("xcorr_thresh2",(float)Constants.XCORR_MAX_VAL_HEIGHT_FAC);
        et15.setText(Constants.XCORR_MAX_VAL_HEIGHT_FAC+"");

        Constants.VAR_THRESH=prefs.getInt("var_thresh",Constants.VAR_THRESH);
        et17.setText(Constants.VAR_THRESH+"");

        Constants.XcorrAboveThresh=prefs.getInt("xcorr_above_thresh",Constants.XcorrAboveThresh);
        et18.setText(Constants.XcorrAboveThresh+"");

//        Constants.NaiserThresh=prefs.getFloat("naiser_thresh",Constants.NaiserThresh);
//        et25.setText(Constants.NaiserThresh+"");

        Constants.FEEDBACK_SNR_THRESH=prefs.getInt("feedback_thresh",Constants.FEEDBACK_SNR_THRESH);
        et26.setText(Constants.FEEDBACK_SNR_THRESH+"");

        Constants.CheckSymSNRThresh=prefs.getInt("checksym_snrthresh",Constants.CheckSymSNRThresh);
        et27.setText(Constants.CheckSymSNRThresh+"");

        preambleStartFreq = f_range[0];
        preambleEndFreq = f_range[1];

//        Constants.codeRate=CodeRate.valueOf(prefs.getString("code_rate", Constants.codeRate.toString()));
//        if (Constants.codeRate.equals(CodeRate.None)) {
//            Constants.spinner.setSelection(0);
//        }
//        else if (Constants.codeRate.equals(CodeRate.C1_2)) {
//            Constants.spinner.setSelection(1);
//        }
//        else if (Constants.codeRate.equals(CodeRate.C2_3)) {
//            Constants.spinner.setSelection(2);
//        }

        Constants.snr_method=prefs.getInt("snr_method",Constants.snr_method);
        Log.e("snr",Constants.snr_method+"");
        if (Constants.snr_method==1) {
            Constants.spinner2.setSelection(0);
        }
        else if (Constants.snr_method==2) {
            Constants.spinner2.setSelection(1);
        }

//        if (Constants.snr_method==1) {
//            Constants.SNR_THRESH1 = prefs.getInt("snr_thresh1", Constants.SNR_THRESH1);
//            et4.setText(Constants.SNR_THRESH1 + "");
//        }
//        else if (Constants.snr_method==2) {
            Constants.SNR_THRESH2 = prefs.getInt("snr_thresh2", Constants.SNR_THRESH2);
            et4.setText(Constants.SNR_THRESH2 + "");
//        }

        Constants.Ns=prefs.getInt("ns",Constants.Ns);
        if (Constants.Ns==960) {
            Constants.spinner3.setSelection(0);
        }
        else if (Constants.Ns==1920) {
            Constants.spinner3.setSelection(1);
        }
        else if (Constants.Ns==4800) {
            Constants.spinner3.setSelection(2);
        }
        else if (Constants.Ns==9600) {
            Constants.spinner3.setSelection(3);
        }

        ////////////////////////////////////////////////////////////////////////////////

        updateNbins();

        ////////////////////////////////////////////////////////

//        pilots = new HashSet<>();
//        for (int i = 0; i < Nsyms; i++) {
//            if (i%10==0) {
//                pilots.add(i);
//            }
//        }

        Log.e(LOG, String.format("number of valid carriers %d %d [%d,%d]",
                valid_carrier_default.length, subcarrier_number_default, f_seq.get(nbin1_default), f_seq.get(nbin2_default)));
    }

    public static void updateNaiser(Context cxt) {
        if (NAISER) {
            Constants.preambleTime = 195;
            naiser = FileOperations.readrawasset(cxt, R.raw.naiser3, 1);
//            preamble_spec1 = FileOperations.readrawasset(cxt, R.raw.real_naiser,1);
//            preamble_spec2 = FileOperations.readrawasset(cxt, R.raw.imag_naiser,1);
        }
        else {
//            preamble_spec1 = FileOperations.readrawasset(cxt, R.raw.real_preamble1, 1);
//            preamble_spec2 = FileOperations.readrawasset(cxt, R.raw.imag_preamble1, 1);
            if (Constants.exp_num==5) {
                Constants.preambleTime=200;
            }
            else if (Constants.exp_num==4||Constants.exp_num==3||Constants.exp_num==2||Constants.exp_num==1) {
                Constants.preambleTime=100;
            }
        }
    }

    public static void updateNbins() {
        if (Constants.Ns == 960) {
            Cp = 67;
        }
        else if (Constants.Ns == 1920) {
            Cp = 135;
        }
        else if (Constants.Ns == 4800) {
            Cp = 336;
        }
        else if (Constants.Ns == 9600) {
            Cp = 672;
        }
        inc=fs/Ns;
        sym_len = Ns + Cp + Gi;

        nbin1_data = Math.round(f_range[0] / (inc/data_symreps));
        nbin2_data = Math.round(f_range[1] / (inc/data_symreps))-1;
        subcarrier_number_data = (nbin2_data - nbin1_data + 1)/data_symreps;

        nbin1_chanest = nbin1_data * chanest_symreps;
        nbin2_chanest = nbin1_chanest+(subcarrier_number_data*chanest_symreps);
        subcarrier_number_chanest = (nbin2_chanest - nbin1_chanest + 1)/ chanest_symreps;

        nbin1_default = Math.round(f_range[0] / inc);
        nbin2_default = Math.round(f_range[1] / inc)-1;
        subcarrier_number_default = (nbin2_default - nbin1_default + 1);

        double[] preamble = PreambleGen.preamble_d();
        blocklen = preamble.length + ChirpGap + (sym_len) * Nsyms;

        f_seq = new LinkedList<>();
        for (int i = 0; i < Ns; i++) {
            f_seq.add(inc*i);
        }

        HashSet<Integer> null_carrier_set = new HashSet<>();
        for (Integer i : null_carrier) {
            null_carrier_set.add(i);
        }

        // calculate valid carriers for preamble
        LinkedList<Integer> valid_carrier_list_preamble = new LinkedList<>();
        for (int i = nbin1_chanest; i < nbin2_chanest; i+= chanest_symreps) {
            if (!null_carrier_set.contains(i)) {
                valid_carrier_list_preamble.add(i);
            }
        }
        valid_carrier_preamble = new int[valid_carrier_list_preamble.size()];
        for (int i = 0; i < valid_carrier_list_preamble.size(); i++) {
            valid_carrier_preamble[i] = valid_carrier_list_preamble.get(i);
        }

        // calculate valid carriers for data
        LinkedList<Integer> valid_carrier_list_data = new LinkedList<>();
        for (int i = nbin1_data; i <= nbin2_data; i+=data_symreps) {
            if (!null_carrier_set.contains(i)) {
                valid_carrier_list_data.add(i);
            }
        }
        valid_carrier_data = new int[valid_carrier_list_data.size()];
        for (int i = 0; i < valid_carrier_list_data.size(); i++) {
            valid_carrier_data[i] = valid_carrier_list_data.get(i);
        }

        // calculate valid carriers for default
        LinkedList<Integer> valid_carrier_list_default = new LinkedList<>();
        for (int i = nbin1_default; i <= nbin2_default; i++) {
            if (!null_carrier_set.contains(i)) {
                valid_carrier_list_default.add(i);
            }
        }
        valid_carrier_default = new int[valid_carrier_list_default.size()];
        for (int i = 0; i < valid_carrier_list_default.size(); i++) {
            valid_carrier_default[i] = valid_carrier_list_default.get(i);
        }
    }
}
