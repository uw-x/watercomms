package com.example.root.ffttest2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AutomaticGainControl;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.lang.Thread;
import java.util.concurrent.locks.ReentrantLock;

public class OfflineRecorder extends Thread {
    public boolean recording;
    int samplingfrequency;
    AudioRecord rec;
    int minbuffersize;
    Activity av;
    String filename;
    int channels;
    int read_pointer = 0;
    int write_pointer = 0;
    public short[] temp;

    ArrayList<Short> save_FIFO = new ArrayList<Short>();
    ArrayList<Short> save_FIFO2 = new ArrayList<Short>();
    private final ReentrantLock FIFO_lock = new ReentrantLock();

    public OfflineRecorder(Activity av, int samplingfrequency, String filename) {
        this.filename = filename;
        this.av = av;
        read_pointer = 0;
        write_pointer = 0;
        save_FIFO.clear();
        save_FIFO2.clear();

        this.samplingfrequency = samplingfrequency;
        read_pointer = 0;
//        int channels = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        if (Constants.stereo) {
            channels = AudioFormat.CHANNEL_IN_STEREO;
        }
        else {
            channels = AudioFormat.CHANNEL_IN_MONO;
        }

        minbuffersize = AudioRecord.getMinBufferSize(
                samplingfrequency,
                channels,
                AudioFormat.ENCODING_PCM_16BIT);
        temp = new short[minbuffersize];

        if (ActivityCompat.checkSelfPermission(av, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.e("asdf","perm error");
        }
        Log.e("recorder","minbuffersize "+minbuffersize);
        rec = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
//                MediaRecorder.AudioSource.UNPROCESSED,
                samplingfrequency, channels,
                AudioFormat.ENCODING_PCM_16BIT,
                minbuffersize);
    }
    /*
    public void halt() {
//        Utils.log("halt");
        if (this.rec.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            this.recording = false;
            this.rec.stop();
            this.rec.release();
            Log.e("asdf","halt "+(samples==null));
            if (channels == AudioFormat.CHANNEL_IN_MONO) {
                FileOperations.writetofile(av, samples, filename+".txt");
            }
            else if (channels == AudioFormat.CHANNEL_IN_STEREO) {
                FileOperations.writetofile(av, samples1, filename+"-top.txt");
                FileOperations.writetofile(av, samples2, filename+"-bottom.txt");

            }

            Constants.sensorFlag=false;
            FileOperations.writeSensors(av,filename+".txt");
        }
    }
    */
    public void halt2() {
        this.recording = false;
        try {
            if (this.rec.getRecordingState() == AudioRecord.STATE_INITIALIZED) {
                rec.stop();
            }
        }
        catch(Exception e) {
            Log.e("asdf","halt1"+e.toString());
        }
        try {
            if (this.rec.getRecordingState() == AudioRecord.STATE_INITIALIZED) {
                rec.release();
            }
        }
        catch(Exception e) {
            Log.e("asdf","halt2"+e.toString());
        }
        Log.e("thread","halting......");
    }

    public void start2() {
        recording = true;
        start();
    }


    public short[] list_to_array(ArrayList<Short> list){
        short[] return_array = new short[list.size()];
        for(int i =0; i < list.size(); i++){
            return_array[i] = (short)  save_FIFO.get(i);
        }
        return return_array;
    }

    public short[] get_FIFO(){
//        Log.e("fifo","get fifo");
        while(read_pointer + Constants.RecorderStepSize > save_FIFO.size()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        Log.e("fifo","get fifo wait over");

        short[] return_array = new short[Constants.RecorderStepSize];
        FIFO_lock.lock();
        try{
            for(int i =read_pointer; i < read_pointer + Constants.RecorderStepSize; i++){
                return_array[i - read_pointer] = (short)  save_FIFO.get(i);
            }
            read_pointer += Constants.RecorderStepSize;
        }
        finally {
            FIFO_lock.unlock();
        }
        return return_array;
    }


    public void run() {
//        if (Constants.FIFO) {
            fifo_read();
//        }
//        else {
//            read();
//        }
    }

    public void fifo_read() {
        Constants.acc = new LinkedList<>();
        Constants.gyro = new LinkedList<>();
        Constants.sensorFlag=true;

        int MAX_SAVE_FIFO = 48000*2;
        int remove_num = 0;
        if (channels == AudioFormat.CHANNEL_IN_MONO) {
            int bytesread;
            rec.startRecording();
            while (recording) {
                bytesread = rec.read(temp, 0, minbuffersize);
                Log.e("Monitor------", String.valueOf(save_FIFO.size()) +"__" + String.valueOf(bytesread) +"__" + String.valueOf(write_pointer)+"__" + String.valueOf(read_pointer));
                if(save_FIFO.size() + bytesread > MAX_SAVE_FIFO){
                    // write part
                    remove_num = save_FIFO.size() + bytesread - MAX_SAVE_FIFO;
                    Log.e("fifo","remove num "+write_pointer+","+remove_num);
                    if(write_pointer - remove_num < 0){
                        int new_length = save_FIFO.size() - write_pointer;
                        short[] save_buffer = new short[new_length];
                        for(int j = write_pointer; j < save_FIFO.size(); ++j){
                            save_buffer[j-write_pointer] = save_FIFO.get(j);
                        }

                        Log.e("fifo","append 1 mono "+new_length+","+(new_length/48000.0));
                        if (Constants.IO) {
                            FileOperations.appendtofile(av, save_buffer, filename + ".txt");
                        }
                        write_pointer = save_FIFO.size() - remove_num;
                    }
                    else write_pointer -= remove_num;
                    // read part
                    FIFO_lock.lock();
                    try{
                        save_FIFO.subList(0, remove_num).clear();
                        read_pointer -= remove_num;
                        Log.e("fifo","read pointer "+read_pointer);
                        if(read_pointer < 0){
                            read_pointer =0;
                            Log.e("asdf","read too slow, some data lost");
                        }
                        for (int i = 0; i < bytesread; i++) {
                            save_FIFO.add(temp[i]);
                        }
                    }
                    finally {
                        FIFO_lock.unlock();
                    }
                }
                else{
                    FIFO_lock.lock();
                    try{
                        for (int i = 0; i < bytesread; i++) {
                            save_FIFO.add(temp[i]);
                        }
                    }
                    finally {
                        FIFO_lock.unlock();
                    }
                    Log.e("fifo","size "+save_FIFO.size()+","+(save_FIFO.size()/48000.0)+","+write_pointer);
                }
                Log.e("Monitor2------", String.valueOf(save_FIFO.size()) +"__" + String.valueOf(bytesread) +"__" + String.valueOf(write_pointer)+"__" + String.valueOf(read_pointer));
            }
            if (rec.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                rec.stop();
                rec.release();
            }
            Constants.sensorFlag=false;
            // save the remaining data to the disk
            int new_length = save_FIFO.size() - write_pointer;
            short[] save_buffer = new short[new_length];
            for(int j = write_pointer; j < save_FIFO.size(); ++j){
                save_buffer[j- write_pointer] = save_FIFO.get(j);
            }
            Log.e("fifo","append 2 mono "+new_length+","+(new_length/48000.0));
            if (Constants.IO) {
                FileOperations.appendtofile(av, save_buffer, filename + ".txt");
            }
        }

        if (channels == AudioFormat.CHANNEL_IN_STEREO) {
            int bytesread;
            if (rec.getRecordingState() == AudioRecord.STATE_INITIALIZED) {
                Log.e("recorder","start "+rec.getRecordingState());
                rec.startRecording();
                while (recording) {
                    bytesread = rec.read(temp, 0, minbuffersize);
                    if (save_FIFO.size() + bytesread / 2 > MAX_SAVE_FIFO) {
                        // write part
                        remove_num = save_FIFO.size() + bytesread / 2 - MAX_SAVE_FIFO;
                        if (write_pointer - remove_num < 0) {
                            int new_length = save_FIFO.size() - write_pointer;
                            short[] save_buffer = new short[new_length];
                            short[] save_buffer2 = new short[new_length];
                            for (int j = write_pointer; j < save_FIFO.size(); ++j) {
                                save_buffer[j - write_pointer] = save_FIFO.get(j);
                                save_buffer2[j - write_pointer] = save_FIFO2.get(j);
                            }

//                            Log.e("fifo", "append 1 " + new_length);
                            if (Constants.IO) {
                                FileOperations.appendtofile(av, save_buffer, filename + "-bottom.txt");
                                FileOperations.appendtofile(av, save_buffer2, filename + "-top.txt");
                            }
                            write_pointer = save_FIFO.size() - remove_num;
                        } else write_pointer -= remove_num;
                        // read part
                        FIFO_lock.lock();
                        try {
                            save_FIFO.subList(0, remove_num).clear();
                            save_FIFO2.subList(0, remove_num).clear();
                            read_pointer -= remove_num;
                            if (read_pointer < 0) {
                                read_pointer = 0;
                                Log.e("asdf", "read too slow, some data lost");
                            }
                            for (int i = 0; i < bytesread; i += 2) {
                                if (android.os.Build.MODEL.equals("SM-N975U1")) {
                                    //s10
                                    save_FIFO.add(temp[i]);
                                    save_FIFO2.add(temp[i + 1]);
                                } else if (android.os.Build.MODEL.equals("SM-G950U") || android.os.Build.MODEL.equals("SM-G960U")) {
                                    //s8
                                    save_FIFO2.add(temp[i]);
                                    save_FIFO.add(temp[i + 1]);
                                } else {
                                    save_FIFO2.add(temp[i]);
                                    save_FIFO.add(temp[i + 1]);
                                }
                            }
                        } finally {
                            FIFO_lock.unlock();
                        }
                    } else {
                        FIFO_lock.lock();
                        try {
                            //                        Log.e("fifo",save_FIFO.size()+","+bytesread);
                            for (int i = 0; i < bytesread; i += 2) {
                                if (android.os.Build.MODEL.equals("SM-N975U1")) {
                                    //s1
                                    save_FIFO.add(temp[i]);
                                    save_FIFO2.add(temp[i + 1]);
                                } else if (android.os.Build.MODEL.equals("SM-G950U") || android.os.Build.MODEL.equals("SM-G960U")) {
                                    //s8
                                    save_FIFO2.add(temp[i]);
                                    save_FIFO.add(temp[i + 1]);
                                } else {
                                    save_FIFO2.add(temp[i]);
                                    save_FIFO.add(temp[i + 1]);
                                }
                            }
                        } finally {
                            FIFO_lock.unlock();
                        }
                    }
                }

                if (rec.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    rec.stop();
                    rec.release();
                }
                int new_length = save_FIFO.size() - write_pointer;
                short[] save_buffer = new short[new_length];
                short[] save_buffer2 = new short[new_length];
                for(int j = write_pointer; j < save_FIFO.size(); ++j){
                    save_buffer[j-write_pointer] = save_FIFO.get(j);
                    save_buffer2[j-write_pointer] = save_FIFO2.get(j);
                }

                Constants.sensorFlag=false;
//                Log.e("fifo","append 2 "+new_length);
                if (Constants.IO) {

                    FileOperations.appendtofile(av, save_buffer, filename + "-bottom.txt");
                    FileOperations.appendtofile(av, save_buffer2, filename + "-top.txt");
                }
            }
            else {
                Log.e("recorder","!!!"+rec.getRecordingState()+"");
            }
        }

        Log.e("thread","recording thread end elegantly......");
    }



//    public void read() {
//        Constants.acc = new LinkedList<>();
//        Constants.gyro = new LinkedList<>();
//        Constants.sensorFlag=true;
//        if (channels == AudioFormat.CHANNEL_IN_MONO) {
//            count = 0;
//            int bytesread;
//            rec.startRecording();
//            recording = true;
//            while (recording) {
//                bytesread = rec.read(temp, 0, minbuffersize);
//                for (int i = 0; i < bytesread; i++) {
////                    Log.e("asdf",count+","+(count/(double)samples.length));
//                    if (count >= samples.length && rec.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
//                        Log.e("asdf","rec stop "+rec.getRecordingState());
//                        rec.stop();
//                        rec.release();
//                        recording = false;
//                        Log.e("asdf","rec stop2 "+rec.getRecordingState());
//                        FileOperations.writetofile(av, samples, filename+".txt");
//
//                        Constants.sensorFlag=false;
//                        FileOperations.writeSensors(av,filename+".txt");
//                        break;
//                    } else if (count < samples.length) {
//                        samples[count] = temp[i];
//                        count++;
//                    } else {
//                        break;
//                    }
//                }
//            }
//        }
//        else if (channels == AudioFormat.CHANNEL_IN_STEREO) {
//            count1 = 0;
//            count2 = 0;
//            int bytesread;
//            rec.startRecording();
//            recording = true;
//            while (recording) {
//                bytesread = rec.read(temp, 0, minbuffersize);
//                if (count1 < samples1.length) {
//                    for (int i = 0; i < bytesread; i+=2) {
//                        if (count1 < samples1.length && count2 < samples2.length) {
//                            if (android.os.Build.MODEL.equals("SM-N975U1")) {
//                                //s10
//                                samples2[count1] = temp[i];
//                                samples1[count2] = temp[i + 1];
//                            }
//                            else if (android.os.Build.MODEL.equals("SM-G950U")) {
//                                //s8
//                                samples1[count1] = temp[i];
//                                samples2[count2] = temp[i + 1];
//                            }
//                            else if (android.os.Build.MODEL.equals("SM-G960U")) {
//                                //s9
//                                samples1[count1] = temp[i];
//                                samples2[count2] = temp[i + 1];
//                            }
//                            else {
//                                samples1[count1] = temp[i];
//                                samples2[count2] = temp[i + 1];
//                            }
//                            count1++;
//                            count2++;
//                        }
//                    }
//                }
//                else if (count1 >= samples1.length && rec.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING){
////                else if (count1 >= samples1.length){
//                    Log.e("asdf","OVER");
//                    rec.stop();
//                    rec.release();
//                    recording = false;
//
//                    Constants.sensorFlag=false;
//                    new Runnable() {
//                        public void run() {
//                            FileOperations.writetofile(av, samples1, filename+"-top.txt");
//                            FileOperations.writetofile(av, samples2, filename+"-bottom.txt");
//                            FileOperations.writeSensors(av,filename+".txt");
//                        }
//                    }.run();
//                    break;
//                }
//            }
//        }
//    }
}
