package com.example.root.ffttest2;

import static com.example.root.ffttest2.Constants.LOG;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class FileOperations {
    public static double[] readrawasset(Context context, int id, int normalizer) {
        Scanner inp = new Scanner(context.getResources().openRawResource(id));
        LinkedList<Double> ll = new LinkedList<>();
        while (inp.hasNextLine()) {
            ll.add((Double.parseDouble(inp.nextLine())/normalizer));
        }
        inp.close();
        double[] ar = new double[ll.size()];
        int counter = 0;
        for (Double d : ll) {
            ar[counter++] = d;
        }
        ll.clear();

        return ar;
    }

    public static double[] readrawasset2(Context context, int id, int normalizer) {
        Scanner inp = new Scanner(context.getResources().openRawResource(id));
//        LinkedList<Double> ll = new LinkedList<>();
//        while (inp.hasNextLine()) {
//            ll.add((Double.parseDouble(inp.nextLine())/normalizer));
//        }
        String[] ll = inp.nextLine().split(",");
        inp.close();
        double[] ar = new double[ll.length];
        int counter = 0;
        for (int i = 0; i < ar.length; i++) {
            ar[i] = Double.parseDouble(ll[i]);
        }
//        ll.clear();

        return ar;
    }

    public static void list(Activity av) {
        String dir = av.getExternalFilesDir(null).toString();
        File file = new File(dir);
        for(File f2:file.listFiles()) {
            Log.e("asdf",f2.getAbsolutePath());
        }
    }

    public static double[] readfromfile(Activity av, String dd, String filename) {
        LinkedList<Double> ll = new LinkedList<Double>();

        try {
            String dir = av.getExternalFilesDir(null).toString()+"/"+dd;
            File file = new File(dir + File.separator + filename+".txt");
            BufferedReader buf = new BufferedReader(new FileReader(file));

            String line;
            while ((line = buf.readLine()) != null && line.length() != 0) {
                ll.add(Double.parseDouble(line));
            }

            buf.close();
        } catch (Exception e) {
            Log.e("ble",e.getMessage());
        }

        double[] ar = new double[ll.size()];
        int counter = 0;
        for (Double d : ll) {
            ar[counter++] = d;
        }
        ll.clear();
        return ar;
    }

    public static void writetofile(String _ExternalFilesDir, short[] buff, String filename) {
        Constants.writing=true;
        Log.e(LOG,"writetofile " + _ExternalFilesDir + "," + filename + " "+(buff==null));
        long ts = System.currentTimeMillis();

        try {
            String dir = _ExternalFilesDir;
            File path = new File(dir);
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(dir, filename);

            BufferedWriter buf = new BufferedWriter(new FileWriter(file,false));
            for (int i = 0; i < buff.length; i++) {
                buf.append(""+buff[i]);
                buf.newLine();
            }
            buf.flush();
            buf.close();
        } catch(Exception e) {
            Log.e("asdf","write to file exception " +e.toString());
        }
        if (filename.contains("Bob")&&filename.contains("Sounding")&&filename.contains("bottom")||
                filename.contains("Bob")&&filename.contains("Data")&&filename.contains("bottom")||
                filename.contains("Alice")&&filename.contains("Feedback")&&filename.contains("bottom")) {
            Utils.log("finish writing " + filename);
        }
    }

    public static void appendtofile(String _ExternalFilesDir, short[] buff, String filename) {
        Constants.writing=true;
        Log.e(LOG,"appendtofile " + filename + " "+(buff==null));
        long ts = System.currentTimeMillis();

        try {
            String dir = _ExternalFilesDir;
            File path = new File(dir);
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(dir, filename);

            BufferedWriter buf = new BufferedWriter(new FileWriter(file,true));
            for (int i = 0; i < buff.length; i++) {
                buf.append(""+buff[i]);
                buf.newLine();
            }
            buf.flush();
            buf.close();
        } catch(Exception e) {
            Log.e("asdf","write to file exception " +e.toString());
        }
        if (filename.contains("Bob")&&filename.contains("Sounding")&&filename.contains("bottom")||
                filename.contains("Bob")&&filename.contains("Data")&&filename.contains("bottom")||
                filename.contains("Alice")&&filename.contains("Feedback")&&filename.contains("bottom")) {
            Utils.log("finish writing " + filename);
        }
    }

    public static void appendtofile(String _ExternalFilesDir, String buff, String filename) {
        Constants.writing=true;
        Log.e(LOG,"appendtofile " + filename + " "+(buff==null));
        long ts = System.currentTimeMillis();

        try {
            String dir = _ExternalFilesDir;
            File path = new File(dir);
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(dir, filename);

            BufferedWriter buf = new BufferedWriter(new FileWriter(file,true));
            buf.append(""+buff);
            buf.flush();
            buf.close();
        } catch(Exception e) {
            Log.e("asdf","write to file exception " +e.toString());
        }
        if (filename.contains("Bob")&&filename.contains("Sounding")&&filename.contains("bottom")||
                filename.contains("Bob")&&filename.contains("Data")&&filename.contains("bottom")||
                filename.contains("Alice")&&filename.contains("Feedback")&&filename.contains("bottom")) {
            Utils.log("finish writing " + filename);
        }
    }

    public static void writetofile(String _ExternalFilesDir, Short[] buff, String filename) {
        Constants.writing=true;
        Log.e(LOG,"writetofile " + _ExternalFilesDir + "," + filename + " "+(buff==null));
        long ts = System.currentTimeMillis();

        try {
            String dir = _ExternalFilesDir;
            File path = new File(dir);
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(dir, filename);

            BufferedWriter buf = new BufferedWriter(new FileWriter(file,false));
            for (int i = 0; i < buff.length; i++) {
                buf.append(""+buff[i]);
                buf.newLine();
            }
            buf.flush();
            buf.close();
        } catch(Exception e) {
            Log.e("asdf","write to file exception " +e.toString());
        }
        if (filename.contains("Bob")&&filename.contains("Sounding")&&filename.contains("bottom")||
                filename.contains("Bob")&&filename.contains("Data")&&filename.contains("bottom")||
                filename.contains("Alice")&&filename.contains("Feedback")&&filename.contains("bottom")) {
            Utils.log("finish writing " + filename);
        }
    }

    public static void writetofile(String _ExternalFilesDir, int[] buff, String filename) {
        Constants.writing=true;
        Log.e(LOG,"writetofile " + _ExternalFilesDir + "," + filename + " "+(buff==null));
        long ts = System.currentTimeMillis();

        try {
            String dir = _ExternalFilesDir;
            File path = new File(dir);
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(dir, filename);

            BufferedWriter buf = new BufferedWriter(new FileWriter(file,false));
            for (int i = 0; i < buff.length; i++) {
                buf.append(""+buff[i]);
                buf.newLine();
            }
            buf.flush();
            buf.close();
        } catch(Exception e) {
            Log.e("asdf","write to file exception " +e.toString());
        }
        if (filename.contains("Bob")&&filename.contains("Sounding")&&filename.contains("bottom")||
                filename.contains("Bob")&&filename.contains("Data")&&filename.contains("bottom")||
                filename.contains("Alice")&&filename.contains("Feedback")&&filename.contains("bottom")) {
            Utils.log( "finish writing " + filename);
        }
    }

    public static void writetofile(String _ExternalFilesDir, String buff, String filename) {
        Constants.writing=true;
        Log.e(LOG,"writetofile " + _ExternalFilesDir + " " + filename + " "+(buff==null));
        long ts = System.currentTimeMillis();

        try {
            String dir = _ExternalFilesDir;
            File path = new File(dir);
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(dir, filename);

            BufferedWriter buf = new BufferedWriter(new FileWriter(file,false));
            buf.append(buff);
            buf.flush();
            buf.close();
        } catch(Exception e) {
            Log.e("asdf","write to file exception " +e.toString());
        }
        if (filename.contains("Bob")&&filename.contains("Sounding")&&filename.contains("bottom")||
                filename.contains("Bob")&&filename.contains("Data")&&filename.contains("bottom")||
                filename.contains("Alice")&&filename.contains("Feedback")&&filename.contains("bottom")) {
            Utils.log( "finish writing " + filename);
        }
    }

    public static void writetofile(String _ExternalFilesDir, double[] buff, String filename) {
        Constants.writing=true;
        Log.e(LOG,"writetofile " + filename + " "+(buff==null));
        long ts = System.currentTimeMillis();

        try {
            String dir = _ExternalFilesDir;
            File path = new File(dir);
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(dir, filename);
            Log.e("asdf","write "+file.getAbsolutePath());
            BufferedWriter buf = new BufferedWriter(new FileWriter(file,false));
            for (int i = 0; i < buff.length; i++) {
                buf.append(""+buff[i]);
                buf.newLine();
            }
            buf.flush();
            buf.close();
        } catch(Exception e) {
            Log.e("asdf","write to file exception " +e.toString());
        }
        if (filename.contains("Bob")&&filename.contains("Sounding")&&filename.contains("bottom")||
            filename.contains("Bob")&&filename.contains("Data")&&filename.contains("bottom")||
            filename.contains("Alice")&&filename.contains("Feedback")&&filename.contains("bottom")) {
            Utils.log("finish writing " + filename);
        }
    }

    public static void writeSensors(Activity av, String filename) {
        Constants.writing=true;
        long ts = System.currentTimeMillis();
        Log.e("asdf","writing sensors "+Constants.acc.size()+","+Constants.gyro.size()+","+filename);

        try {
            String dir = av.getExternalFilesDir(null).toString()+"/"+Utils.getDirName();

            File path = new File(dir);
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(dir, "acc-" + filename);
            BufferedWriter buf = new BufferedWriter(new FileWriter(file, false));
            for (String s : Constants.acc) {
                buf.write(s);
            }
            buf.flush();
            buf.close();

            File file2 = new File(dir, "gyro-" + filename);
            BufferedWriter buf2 = new BufferedWriter(new FileWriter(file2, false));
            for (String s : Constants.gyro) {
                buf2.write(s);
            }
            buf2.flush();
            buf2.close();
        } catch (Exception e) {
            Log.e("asdf", e.toString());
        }
        Log.e("asdf","finish writing sensors "+(System.currentTimeMillis()-ts));

        Constants.writing=false;
    }

    public static short[] readrawasset_binary(Context context, int id) {
        InputStream inp = context.getResources().openRawResource(id);
        ArrayList<Integer> ll = new ArrayList<>();
        int counter=0;
        int byteRead=0;
        try {
            while ((byteRead = inp.read()) != -1) {
                ll.add(byteRead);
                counter += 1;
//                if (counter % 1000 == 0) {
//                    Log.e("asdf", counter + "");
//                }
            }
            inp.close();
        }
        catch(Exception e) {
            Log.e("asdf",e.getMessage());
        }
        short[] ar = new short[ll.size()/2];

        counter=0;
        for (int i = 0; i < ll.size(); i+=2) {
            int out=ll.get(i)+ll.get(i+1)*256;
            if (out > 32767) {
                out=out-65536;
            }
            ar[counter++]=(short)out;
        }

        return ar;
    }

    public static double[] readrawasset_binary_d(Context context, int id) {
        InputStream inp = context.getResources().openRawResource(id);
        ArrayList<Integer> ll = new ArrayList<>();
        int counter=0;
        int byteRead=0;
        try {
            while ((byteRead = inp.read()) != -1) {
                ll.add(byteRead);
                counter += 1;
//                if (counter % 1000 == 0) {
//                    Log.e("asdf", counter + "");
//                }
            }
            inp.close();
        }
        catch(Exception e) {
            Log.e("asdf",e.getMessage());
        }
        double[] ar = new double[ll.size()/3];

        counter=0;
        for (int i = 0; i < ll.size(); i+=3) {
            double out=ll.get(i)+ll.get(i+1)*256+ll.get(i+1)*Math.pow(256,2);
            if (out > 32767) {
                out=out-65536;
            }
            ar[counter++]=out;
        }

        return ar;
    }

    public static void writetofile(Activity av, short[] buff, String filename) {
        new Thread() {
            public void run() {
                try {
                    String dir = av.getExternalFilesDir(null).toString();
                    writetofile(dir+"/"+Utils.getDirName(), buff, filename);

                } catch (Exception e) {
                    Log.e("asdf", e.toString());
                }
            }
        }.run();
    }

    public static void appendtofile(Activity av, short[] buff, String filename) {
//        Log.e("fifo","append to "+filename+","+Constants.ts);
        new Thread() {
            public void run() {
                try {
                    String dir = av.getExternalFilesDir(null).toString();
                    appendtofile(dir+"/"+Utils.getDirName(), buff, filename);

                } catch (Exception e) {
                    Log.e("asdf", e.toString());
                }
            }
        }.run();
    }

    public static void appendtofile(Activity av, String buff, String filename) {
//        Log.e("fifo","append to "+filename+","+Constants.ts);
        new Thread() {
            public void run() {
                try {
                    String dir = av.getExternalFilesDir(null).toString();
                    appendtofile(dir+"/"+Utils.getDirName(), buff, filename);

                } catch (Exception e) {
                    Log.e("asdf", e.toString());
                }
            }
        }.run();
    }

    public static void writetofile(Activity av, Short[] buff, String filename) {
        new Thread() {
            public void run() {
                try {
                    String dir = av.getExternalFilesDir(null).toString();
                    writetofile(dir+"/"+Utils.getDirName(), buff, filename);

                } catch (Exception e) {
                    Log.e("asdf", e.toString());
                }
            }
        }.run();
    }

    public static void writetofile(Activity av, double[] buff, String filename) {
        new Thread() {
            public void run() {
                try {
                    String dir = av.getExternalFilesDir(null).toString();
                    writetofile(dir+"/"+Utils.getDirName(), buff, filename);

                } catch (Exception e) {
                    Log.e("asdf", e.toString());
                }
            }
        }.run();
    }

    public static void writetofile(Activity av, int[] buff, String filename) {
        new Thread() {
            public void run() {
                try {
                    String dir = av.getExternalFilesDir(null).toString();
                    writetofile(dir+"/"+Utils.getDirName(), buff, filename);

                } catch (Exception e) {
                    Log.e("asdf", e.toString());
                }
            }
        }.run();
    }

    public static void writetofile(Activity av, String buff, String filename) {
        new Thread() {
            public void run() {
                try {
                    String dir = av.getExternalFilesDir(null).toString();
                    writetofile(dir+"/"+Utils.getDirName(), buff, filename);

                } catch (Exception e) {
                    Log.e("asdf", e.toString());
                }
            }
        }.run();
    }

    public static void mkdir(Activity av, String dd) {
        String dir = av.getExternalFilesDir(null).toString();
        File path = new File(dir+"/"+dd);
        path.mkdir();
    }

}
