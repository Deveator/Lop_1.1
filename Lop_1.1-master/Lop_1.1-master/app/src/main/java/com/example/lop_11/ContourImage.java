package com.example.lop_11;

import com.example.lop_11.CustomView.DrawRect;

import org.opencv.core.Mat;

import java.util.ArrayList;

import static com.example.lop_11.CustomView.DrawRect.xOrg;
import static com.example.lop_11.CustomView.DrawRect.xRed;
import static com.example.lop_11.CustomView.DrawRect.yGreen;
import static com.example.lop_11.CustomView.DrawRect.yRed;

public class ContourImage {

    public static int xRed_add;
    public static int yRed_add;
    public static int xOrg_add;
    public static int yGreen_add;
    public static ArrayList<String> rgb_string_array = new ArrayList<>();

    public static Mat getMatFromROI(Mat inputImage) {

        DrawRect.getCoord();
        Mat sMat = inputImage.submat(yRed, yGreen, xRed, xOrg);

        yRed_add = yRed;
        yGreen_add = yGreen;
        xRed_add = xRed;
        xOrg_add = xOrg;

        return sMat;
    }

    public static Mat getColorLines(Mat inputImage) {

        ///  DrawRect.getCoord();

        Mat sMat = inputImage.submat(yRed_add, yGreen_add, xRed_add, xOrg_add);
        System.out.println("yRed - " + yRed_add);
        System.out.println("yGreen - " + yGreen_add);
        System.out.println("xRed - " + xRed_add);
        System.out.println("xOrg - " + xOrg_add);

        MainActivity._yFromROI = yRed_add;
        MainActivity._xFromROI = xRed_add;

        getNumOfColors(sMat);

        return sMat;

    }

     private static void getNumOfColors(Mat iImage) {

        double[] colValue = iImage.get(0, 0);

        int r = (int) colValue[0];
        int g = (int) colValue[1];
        int b = (int) colValue[2];

        String rgb_String = r + ";" + g + ";" + b;

        rgb_string_array.add(rgb_String);

        for (int x = 0; x < iImage.cols(); x++) {

            for (int y = 0; y < iImage.rows(); y++) {

                colValue = iImage.get(y, x);
                int r2 = (int) colValue[0];
                int g2 = (int) colValue[1];
                int b2 = (int) colValue[2];

                String v = r2 + ";" + g2 + ";" + b2;

                if (!rgb_string_array.contains(v)) {
                    rgb_string_array.add(v);
                }
            }
        }
        System.out.println("Number of colors - " + rgb_string_array.size());
        for (int j = 0; j < rgb_string_array.size(); j++) {
            System.out.println("colors - " + rgb_string_array.get(j));
        }
    }
}
