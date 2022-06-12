package com.example.root.ffttest2;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Display {
    static double currentMin=100000;
    static double currentMax=-100000;

    public static void plotVerticalLine(GraphView gview, int x) {
        DataPoint[] dp = new DataPoint[2];
        dp[0] = new DataPoint(x, currentMin);
        dp[1] = new DataPoint(x, currentMax);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));

        series.setDrawAsPath(true);
        series.setCustomPaint(paint);

        gview.addSeries(series);
    }

    public static void plotSpectrum(GraphView gview, double[] spectrum, boolean clear, int c, String title) {
        if (clear) {
            gview.removeAllSeries();
            currentMin = 100000;
            currentMax = -100000;
        }
        double tempMin = Utils.min(spectrum, 0, spectrum.length)-10;
        double tempMax = Utils.max(spectrum, 0, spectrum.length)+10;
        if (tempMin < -100) {
            tempMin=-100;
        }
        currentMin = tempMin < currentMin ? tempMin : currentMin;
        currentMax = tempMax > currentMax ? tempMax : currentMax;
        Log.e("graph", currentMin+","+currentMax);

        LineGraphSeries<DataPoint> series = convert(spectrum);

        series.setColor(c);
        gview.addSeries(series);
        gview.getViewport().setMinY(currentMin);
        gview.getViewport().setMaxY(currentMax);
        gview.getViewport().setYAxisBoundsManual(true);
        gview.getViewport().setXAxisBoundsManual(true);
        gview.getViewport().setMinX(500);
        gview.getViewport().setMaxX(Constants.f_seq.get(Constants.nbin2_default)+1000);
        GridLabelRenderer r = gview.getGridLabelRenderer();
        gview.setTitle(title);
    }

    public static LineGraphSeries<DataPoint> convert(double[] sig) {
        DataPoint[] dp = new DataPoint[sig.length];

        int freqSpacing = Constants.fs/sig.length;
        for (int i = 0; i < sig.length; i++) {
            dp[i] = new DataPoint(i*freqSpacing, sig[i]);
        }

        return new LineGraphSeries<>(dp);
    }
}
