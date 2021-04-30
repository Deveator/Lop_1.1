package com.example.lop_11;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageResize {

    // method to get image in Mat format resized down 4 times from path
    public static Mat GetResizedImage(String path) {
        Mat orImage = new Mat();
        Mat originImg = Imgcodecs.imread(path);// image is BGR format , try to get format
        Size sz = new Size(750, 1000);

        System.out.println(originImg.size());

        Imgproc.resize(originImg, orImage, sz);
        //  orImage = originImg;
        Imgproc.cvtColor(orImage, orImage, Imgproc.COLOR_BGR2RGB);
        return orImage;
    }

    public static int getXforRect(int x, int imageWidth, int step) {
        int rectX = 0;
        int helpX;
        switch (step) {
            case 1:
                rectX = x - (imageWidth / 4);
                if ((rectX + (imageWidth / 2)) > imageWidth) {
                    helpX = (rectX + (imageWidth / 2)) - imageWidth;
                    rectX = rectX - helpX;
                } else if (rectX < 0) {
                    rectX = 0;
                }
                break;
            case 2:
                imageWidth = imageWidth / 2;
                rectX = x - (imageWidth / 4);
                if ((rectX + (imageWidth / 2)) > imageWidth) {
                    helpX = (rectX + (imageWidth / 2)) - imageWidth;
                    rectX = rectX - helpX;
                } else if (rectX < 0) {
                    rectX = 0;
                }
                break;
        }
        return rectX;
    }

    public static int getYforRect(int y, int imageHeight, int step) {
        int rectY = 0;
        int helpY;
        switch (step) {
            case 1:
                rectY = y - (imageHeight / 4);
                if ((rectY + (imageHeight / 2)) > imageHeight) {
                    helpY = (rectY + (imageHeight / 2)) - imageHeight;
                    rectY = rectY - helpY;
                } else if (rectY < 0) {
                    rectY = 0;
                }
                break;
            case 2:
                imageHeight = imageHeight / 2;
                rectY = y - (imageHeight / 4);
                if ((rectY + (imageHeight / 2)) > imageHeight) {
                    helpY = (rectY + (imageHeight / 2)) - imageHeight;
                    rectY = rectY - helpY;
                } else if (rectY < 0) {
                    rectY = 0;
                }
                break;
        }
        return rectY;
    }


}
