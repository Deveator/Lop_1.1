package com.example.lop_11;

import org.opencv.core.Mat;

import java.util.Arrays;

public class ZoomStuff {

    public static int y_start, y_stop, x_start, x_stop;

    public static Mat zoomExample(int x, int y, Mat mat) {
        Mat zoomMat = new Mat();
        Mat zoomMat2 = new Mat();

        int h = mat.rows();
        int w = mat.cols();
        y_start = y - (h / 4);
        y_stop = y_start + (h / 2);
        x_start = x - (w / 4);
        x_stop = x_start + (w / 2);

        zoomMat = mat.submat(y_start, y_stop, x_start, x_stop);
        zoomMat2 = x4EveryMatPoint(zoomMat);

        return zoomMat2;
    }

    // every matPoint x4 times
    public static Mat x4EveryMatPoint(Mat mat) {
        Mat x4Mat = new Mat(mat.rows() * 2, mat.cols() * 2, mat.type());
        int sx = 0;
        int sy = 0;
        for (int x = 0; x < mat.cols(); x++) {
            for (int y = 0; y < mat.rows(); y++) {
                double[] clr = mat.get(y, x);
                x4Mat.put(sy, sx, clr);
                x4Mat.put(sy + 1, sx, clr);
                x4Mat.put(sy + 1, sx + 1, clr);
                x4Mat.put(sy, sx + 1, clr);
                sy = sy + 2;
            }
            sy = 0;
            sx = sx + 2;
        }
        return x4Mat;
    }

    public static Mat returnFromX4EveryMatPoint(Mat mat) {
        Mat fromX4Mat = new Mat(mat.rows() / 2, mat.cols() / 2, mat.type());
        for (int x = 0, xOut = 0; x < mat.cols(); x = x + 2, xOut++) {
            for (int y = 0, yOut = 0; y < mat.rows(); y = y + 2, yOut++) {
                double[] clr = getColorFromSquare(mat, y, x);
                fromX4Mat.put(yOut, xOut, clr);
            }
        }
        return fromX4Mat;
    }

    public static double[] getColorFromSquare(Mat mat, int y, int x) {
        double[] clrAvr = new double[0];
        double[] white = {255.0, 255.0, 255.0};
        double[] clr1 = mat.get(y, x);
        double[] clr2 = mat.get(y + 1, x);
        double[] clr3 = mat.get(y + 1, x + 1);
        double[] clr4 = mat.get(y, x + 1);
        if (Arrays.equals(clr1, white) || Arrays.equals(clr2, white) || Arrays.equals(clr3, white) || Arrays.equals(clr4, white)) {
            clrAvr = white;
        } else if (Arrays.equals(clr1, clr2) & Arrays.equals(clr1, clr3)) {
            clrAvr = clr1;
        } else if (Arrays.equals(clr1, clr2) & Arrays.equals(clr1, clr4)) {
            clrAvr = clr1;
        } else if (Arrays.equals(clr2, clr3) & Arrays.equals(clr4, clr3)) {
            clrAvr = clr2;
        } else if (Arrays.equals(clr1, clr2)) {
            clrAvr = clr1;
        } else if (Arrays.equals(clr1, clr3)) {
            clrAvr = clr1;
        } else if (Arrays.equals(clr1, clr4)) {
            clrAvr = clr1;
        } else if (Arrays.equals(clr2, clr3)) {
            clrAvr = clr2;
        } else if (Arrays.equals(clr2, clr4)) {
            clrAvr = clr2;
        } else if (Arrays.equals(clr3, clr4)) {
            clrAvr = clr3;
        }

        return clrAvr;
    }

    public static Mat insertZoomedInOrg(Mat zMat, Mat originalMat, int y1, int y2, int x1, int x2) {

        // Mat zoomMat = originalMat.submat(y1, y2, x1, x2);
        zMat.copyTo(originalMat.submat(y1, y2, x1, x2));


        return originalMat;
    }


}
