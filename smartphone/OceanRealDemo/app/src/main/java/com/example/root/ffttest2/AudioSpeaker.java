package com.example.root.ffttest2;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class AudioSpeaker extends Thread {

    AudioTrack track1;
    Context mycontext;
    short[] samples;
    int speakerType;
    AudioManager man;
    int loops;
    int samplingFreq;

    int[] streams = new int[]{AudioManager.STREAM_MUSIC,
            AudioManager.STREAM_ACCESSIBILITY, AudioManager.STREAM_ALARM,
            AudioManager.STREAM_DTMF, AudioManager.STREAM_NOTIFICATION,
            AudioManager.STREAM_RING, AudioManager.STREAM_SYSTEM,
            AudioManager.STREAM_VOICE_CALL};

    int preamble_length;
    public AudioSpeaker(Context mycontext, short[] samples, int samplingFreq, int loops, int preamble_length, boolean top) {
        this.loops = loops;
        this.preamble_length=preamble_length;
        this.samplingFreq= samplingFreq;

        this.speakerType = AudioManager.STREAM_SYSTEM; // streamType – the type of the audio stream

        this.mycontext = mycontext;
        man = (AudioManager)mycontext.getSystemService(Context.AUDIO_SERVICE);
        for (Integer i : streams) {
            man.setStreamMute(i, true);
        }
        man.setStreamMute(AudioManager.STREAM_MUSIC,false);
        man.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(man.getStreamMaxVolume(speakerType)),0);

        man.setStreamMute(speakerType, false);
        man.setStreamVolume(speakerType,(int)(man.getStreamMaxVolume(speakerType)),0);

        write(samples);
        man.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    public void write(short[] samples) {
        this.samples = samples;
        track1 = new AudioTrack(speakerType,
                samplingFreq,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, // PCM 16 bit per sample. Guaranteed to be supported by devices.
                samples.length * 2,  // Each sample is 2 8-bit bytes, so the buffer length is samples * 2.
                AudioTrack.MODE_STATIC // mode – streaming or static buffer.
        );
        track1.write(samples,0,samples.length);
    }

    public void play(double vol) {
        try {
            track1.setLoopPoints(preamble_length, samples.length, loops);
            track1.setVolume((float)vol);
            track1.play();
        }catch(Exception e) {
            Log.e("asdf",e.toString());
        }
    }

    public void reset() {
        track1.stop();
        track1.reloadStaticData();
    }

    public void run() {
        Log.e("asdf","set loop points ");
    }

    public void pause() {
        track1.pause();
    }
}
