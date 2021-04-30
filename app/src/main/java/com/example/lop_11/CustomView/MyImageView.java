package com.example.lop_11.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import static com.example.lop_11.ImageResize.getXforRect;
import static com.example.lop_11.ImageResize.getYforRect;
import static com.example.lop_11.MainActivity.doubleTapCount;
import static com.example.lop_11.MainActivity.idealWidth;
import static com.example.lop_11.MainActivity.oImage;
import static com.example.lop_11.MainActivity.originalHeight;
import static com.example.lop_11.MainActivity.originalWidth;
import static com.example.lop_11.MainActivity.screenWidth;

public class MyImageView extends AppCompatImageView {

    private Context context;
    private GestureListener mGestureListener;
    private GestureDetector mGestureDetector;
    public Bitmap bitmapS3;
    public static Mat zImage, doubleZoomedImage;
    public static int xRR, xR2;
    public static int yRR, yR2;


    public MyImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sharedConstructing(context);
    }

    private void setBtm(Bitmap btm) {
        super.setImageBitmap(btm);
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.context = context;
        mGestureListener = new GestureListener();

        Log.e("Adding", "Listener:::");
        mGestureDetector = new GestureDetector(context, mGestureListener, null, true);

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);

                //..my other code logic
                // invalidate();
                return true; // indicate event was handled
            }
        });
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            System.out.println("onDoubleTap");
            int y = (int) e.getY();
            int ad = (screenWidth - idealWidth) / 2;
            int x = (int) e.getX() - ad;
            // coordinates for first zoom
            int x1 = x * 4;
            int y1 = y * 4;
            // coordinates for second zoom
            int x2 = x * 2;
            int y2 = y * 2;
            // variable for method coordinates
            int _x = 0;
            int _y = 0;
            // variable for better understanding what stage of zoom is it
            doubleTapCount = doubleTapCount + 1;

            if (doubleTapCount % 3 == 1) {
                _x = x1;
                _y = y1;
            } else if (doubleTapCount % 3 == 2) {
                _x = x2;
                _y = y2;
            }

            ///setZoomedImage(_x, _y, originalWidth, originalHeight, oImage3, 1);

            setZoomedImage(_x, _y, originalWidth, originalHeight, oImage, 1);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.e("onLongPress", "onLongPress");
            int y = (int) e.getY();
            int ad = (screenWidth - idealWidth) / 2;
            int x = (int) e.getX() - ad;
            System.out.println(x);
            System.out.println(y);

        }
    }

    public void setZoomedImage(int x, int y, int w, int h, Mat mat, int step) {

        int i = doubleTapCount % 3;
        switch (i) {
            case 1:
                int xR = getXforRect(x, w, 1);
                int yR = getYforRect(y, h, 1);
                xRR = getXforRect(x, w, 1);
                yRR = getYforRect(y, h, 1);
                Rect rectCrop = new Rect(xR, yR, w / 2, h / 2);
                zImage = mat.submat(rectCrop);
                bitmapS3 = Bitmap.createBitmap(zImage.cols(), zImage.rows(), Bitmap.Config.RGB_565);
                Utils.matToBitmap(zImage, bitmapS3);
                setBtm(bitmapS3);
                break;
            case 2:
                // plus xRR & yRR because image is zoomed one time
                xR2 = getXforRect(x, w, 2) + xRR;
                yR2 = getYforRect(y, h, 2) + yRR;
                Rect rectCrop2 = new Rect(xR2, yR2, w / 4, h / 4);
                // need to make static
                Mat imageROI2 = mat.submat(rectCrop2);
                doubleZoomedImage = mat.submat(rectCrop2);
                bitmapS3 = Bitmap.createBitmap(imageROI2.cols(), imageROI2.rows(), Bitmap.Config.RGB_565);
                Utils.matToBitmap(imageROI2, bitmapS3);
                setBtm(bitmapS3);
                break;
            default:
                bitmapS3 = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.RGB_565);
                Utils.matToBitmap(mat, bitmapS3);
                setBtm(bitmapS3);
                break;
        }

    }


}
