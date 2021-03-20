package com.example.lop_11;

import org.opencv.core.Mat;

import static com.example.lop_11.CustomView.DrawRect.xOrg;
import static com.example.lop_11.CustomView.DrawRect.xRed;
import static com.example.lop_11.CustomView.DrawRect.yGreen;
import static com.example.lop_11.CustomView.DrawRect.yRed;

public class ZoomStuff {

    public static Mat zoomExample(int x, int y, Mat mat) {
        Mat zoomMat = new Mat();
        Mat zoomMat2 = new Mat();

        int h = mat.rows();
        int w = mat.cols();
        int y_start = y - (h / 4);
        int y_stop = y_start + (h / 2);
        int x_start = x - (w / 4);
        int x_stop = x_start + (w / 2);

        zoomMat = mat.submat(y_start, y_stop, x_start, x_stop);
        zoomMat2 = x4EveryMatPoint(zoomMat);

        return zoomMat2;
    }

    // every matPoint x4 times
    public static Mat x4EveryMatPoint(Mat mat) {
        Mat x4Mat = new Mat(mat.rows() * 2, mat.cols() * 2, mat.type());
        int sx = 0;
        int sy = 0;
        for (int x = 0; x < mat.cols() ; x++) {
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


}
