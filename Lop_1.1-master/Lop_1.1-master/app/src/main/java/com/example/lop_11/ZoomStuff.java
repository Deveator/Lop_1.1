package com.example.lop_11;

import org.opencv.core.Mat;

import static com.example.lop_11.CustomView.DrawRect.xOrg;
import static com.example.lop_11.CustomView.DrawRect.xRed;
import static com.example.lop_11.CustomView.DrawRect.yGreen;
import static com.example.lop_11.CustomView.DrawRect.yRed;

public class ZoomStuff {

    public static Mat zoomExample(int x, int y, Mat mat){
        Mat zoomMat = new Mat();

        int h = mat.rows();
        int w = mat.cols();
        int y_start = y - (h/4);
        int y_stop = y + (h/4);
        int x_start = x - (w/4);
        int x_stop = x + (w/4);
        zoomMat = mat.submat(y_start, y_stop, x_start, x_stop);

        System.out.println("*****");
        System.out.println(zoomMat.cols());
        System.out.println(zoomMat.rows());
        System.out.println("*****");




        return zoomMat;


    }
}
