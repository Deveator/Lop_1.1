package com.example.lop_11;

import android.widget.Toast;

import com.example.lop_11.CustomView.DrawRect;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.TermCriteria;

import java.util.ArrayList;

import static com.example.lop_11.CustomView.DrawRect.xOrg;
import static com.example.lop_11.CustomView.DrawRect.xRed;
import static com.example.lop_11.CustomView.DrawRect.yGreen;
import static com.example.lop_11.CustomView.DrawRect.yRed;
import static com.example.lop_11.MainActivity.add_oImage;
import static com.example.lop_11.MainActivity.getListColor;
import static com.example.lop_11.MainActivity.numClust;
import static com.example.lop_11.MainActivity.oImageClusterColored;

public class KmeansStuff {

    public static int xRed_km;
    public static int yRed_km;
    public static int xOrg_km;
    public static int yGreen_km;

    public static int count = 0;
    public static int preCount;

    public static ArrayList<Double> _0 = new ArrayList<Double>();
    public static ArrayList<Double> _1 = new ArrayList<Double>();
    public static ArrayList<Double> _2 = new ArrayList<Double>();

    public static ArrayList<double[]> _f = new ArrayList<double[]>();

    public static ArrayList<ArrayList<Integer>> _X = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<ArrayList<Integer>> _Y = new ArrayList<ArrayList<Integer>>();

    public static Mat getMatFromROI_km(Mat inputImage) {

        DrawRect.getCoord();
        Mat sMat = inputImage.submat(yRed, yGreen, xRed, xOrg);

        yRed_km = yRed;
        yGreen_km = yGreen;
        xRed_km = xRed;
        xOrg_km = xOrg;

        return sMat;
    }

    public static Mat getKMeanImage(Mat img) {

        img.convertTo(img, CvType.CV_32F);
        Mat data = img.reshape(1, (int) img.total());
        int K = numClust;
        System.out.println("----");
        System.out.println(numClust);
        Mat bestLabels = new Mat();
        TermCriteria criteria = new TermCriteria();
        int attempts = 5;
        int flags = Core.KMEANS_PP_CENTERS;
        Mat centers = new Mat();
        double compactness = Core.kmeans(data, K, bestLabels, criteria, attempts, flags, centers);

        Mat draw = new Mat((int) img.total(), 1, CvType.CV_32FC3);
        Mat colors = centers.reshape(3, K);
        for (int i = 0; i < K; i++) {
            Mat mask = new Mat(); // a mask for each cluster label
            Core.compare(bestLabels, new Scalar(i), mask, Core.CMP_EQ);
            Mat col = colors.row(i); // can't use the Mat directly with setTo() (see #19100)
            // double d[] = col.get(0, 0); // can't create Scalar directly from get(), 3 vs 4 elements
            double d[] = getListColor();
            draw.setTo(new Scalar(d[0], d[1], d[2]), mask);
            _f.add(d);
            // _0.add(d[0]);
            // _1.add(d[1]);
            // _2.add(d[2]);
        }
        draw = draw.reshape(3, img.rows());
        draw.convertTo(draw, CvType.CV_8U);
        System.out.println(draw.type());
        return draw;
    }

    public static void changeRoiInKmeans(Mat inputM, Mat outputM) {

        Mat m = outputM.clone();

        double[] dVal;
        for (int yStart = yRed_km, y = 0; yStart < yGreen_km; yStart++, y++) {
            for (int xStart = xRed_km, x = 0; xStart < xOrg_km; xStart++, x++) {
                dVal = inputM.get(y, x);
                m.put(yStart, xStart, dVal);
            }
        }
    }

    public static void createColorArrays(Mat matFromRoi, int numOfArrays) {

        for (int i = 0; i < numOfArrays; i++) {
            ArrayList<Integer> x = new ArrayList<Integer>();
            ArrayList<Integer> y = new ArrayList<Integer>();
            _X.add(x);
            _Y.add(y);
        }

        for (int y = 0; y < matFromRoi.rows(); y++) {
            for (int x = 0; x < matFromRoi.cols(); x++) {
                double[] sA = matFromRoi.get(y, x);
                for (int i = 0; i < _f.size(); i++) {
                    double[] sR = _f.get(i);
                    if (sA[0] == sR[0] && sA[1] == sR[1] && sA[2] == sR[2]) {
                        _Y.get(i).add(y);
                        _X.get(i).add(x);
                    }
                }
            }
        }
    }

    public static void showClusters(Mat outputImage) {
        if (count >= numClust) {
            System.out.println("No count");
        } else {
            preCount = count;
            ArrayList<Integer> y = _Y.get(count);
            ArrayList<Integer> x = _X.get(count);
            double[] sR = _f.get(count);

            for (int j = 0; j < y.size(); j++) {
                int yO = y.get(j) + yRed_km;
                int xO = x.get(j) + xRed_km;
                outputImage.put(yO, xO, sR);
            }
            count++;
        }
    }

    public static void returnToOriginal(Mat inputImage, Mat outputImage) {

        ArrayList<Integer> y = _Y.get(preCount);
        ArrayList<Integer> x = _X.get(preCount);

        for (int j = 0; j < y.size(); j++) {
            int yO = y.get(j) + yRed_km;
            int xO = x.get(j) + xRed_km;
            double[] orV = inputImage.get(yO, xO);
            outputImage.put(yO, xO, orV);
        }
    }

    public static void colorInWhite(Mat outputImage) {

        ArrayList<Integer> y = _Y.get(preCount);
        ArrayList<Integer> x = _X.get(preCount);

        double[] orV = {255.0, 255.0, 255.0};
        for (int j = 0; j < y.size(); j++) {
            int yO = y.get(j) + yRed_km;
            int xO = x.get(j) + xRed_km;
            outputImage.put(yO, xO, orV);
        }

    }

    public static void viceVersaColorInWhite(Mat outputImage) {

        for (int n = 0; n < numClust; n++) {

            if (n != preCount) {
                ArrayList<Integer> y = _Y.get(n);
                ArrayList<Integer> x = _X.get(n);

                double[] orV = {255.0, 255.0, 255.0};
                for (int j = 0; j < y.size(); j++) {
                    int yO = y.get(j) + yRed_km;
                    int xO = x.get(j) + xRed_km;
                    outputImage.put(yO, xO, orV);
                }
            }
        }


    }


}
