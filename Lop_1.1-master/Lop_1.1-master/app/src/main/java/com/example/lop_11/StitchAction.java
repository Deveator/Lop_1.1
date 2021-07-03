package com.example.lop_11;

import org.opencv.core.Core;
import org.opencv.core.Mat;

// class for stitching two images
public class StitchAction {

    public static Mat rotate(Mat in, int angle) {
        if (angle == 90 || angle == -270) {

            Core.rotate(in, in, Core.ROTATE_90_CLOCKWISE);
        }
        return in;
    }


}
