package com.example.lop_11.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.lop_11.R;

import java.util.ArrayList;

import static com.example.lop_11.ImageResize.newW;

public class DrawRect extends View {

    Point point1, point3;
    Point point2, point4;
    Point startMovePoint;
    private static boolean moved = false;


    /**
     * point1 and point3 are of same group and same as point 2 and point4
     */
    int groupId = 2;
    public ArrayList<ColorBall> colorballs;
    // array that holds the balls
    private int balID = 0;
    // variable to know what ball is being dragged
    Paint paint;
    Canvas canvas;
    // variables help to getX&Ycoordinates
    public static int xRed;
    public static int yRed;
    public static int xOrg = -1;
    public static int yOrg = -1;
    public static int xYell = -1;
    public static int yYell = -1;
    public static int xGreen = -1;
    public static int yGreen = -1;

    public DrawRect(Context context) {
        super(context);
        init(context);
    }

    public DrawRect(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawRect(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void createScreen() {
        point1 = new Point();

        point1.x = 250;
        point1.y = 170;
       // point1.x = 50;
      //  point1.y = 20;

        point2 = new Point();
        point2.x = 300;
        point2.y = 170;

        //point2.x = 150;
       // point2.y = 20;

        point3 = new Point();
        point3.x = 300;
        point3.y = 220;

       // point3.x = 150;
      //  point3.y = 120;

        point4 = new Point();

        point4.x = 250;
        point4.y = 220;
      //  point4.x = 50;
      //  point4.y = 120;

        // declare each ball with the ColorBall class
        colorballs = new ArrayList<ColorBall>();
        colorballs.add(0, new ColorBall(getContext(), R.drawable.first_dot, point1, 0));
        colorballs.add(1, new ColorBall(getContext(), R.drawable.second_dot, point2, 1));
        colorballs.add(2, new ColorBall(getContext(), R.drawable.third_dot, point3, 2));
        colorballs.add(3, new ColorBall(getContext(), R.drawable.forth_dot, point4, 3));
    }


    private void init(Context context) {
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        // setting the start point for the balls

        createScreen();


    /*
        point1 = new Point();
        point1.x = 50;
        point1.y = 20;

        point2 = new Point();
        point2.x = 150;
        point2.y = 20;

        point3 = new Point();
        point3.x = 150;
        point3.y = 120;

        point4 = new Point();
        point4.x = 50;
        point4.y = 120;

        // declare each ball with the ColorBall class
        colorballs = new ArrayList<ColorBall>();
        colorballs.add(0, new ColorBall(context, R.drawable.gray_circle, point1, 0));
        colorballs.add(1, new ColorBall(context, R.drawable.gray_circle, point2, 1));
        colorballs.add(2, new ColorBall(context, R.drawable.gray_circle, point3, 2));
        colorballs.add(3, new ColorBall(context, R.drawable.gray_circle, point4, 3));
        */
    }

    // method to get ROI's corner XY_coordinates
    public static void getCoord() {

        if (moved) {
            int matWidth = newW;
            int screenWidth = 1080;
            xRed = xRed - ((screenWidth - matWidth) / 2);
            xOrg = xOrg - ((screenWidth - matWidth) / 2);
            xYell = xYell - ((screenWidth - matWidth) / 2);
            xGreen = xGreen - ((screenWidth - matWidth) / 2);
            System.out.println("Red " + xRed + "::" + yRed);
            System.out.println("Orange " + xOrg + "::" + yOrg);
            System.out.println("Yellow " + xYell + "::" + yYell);
            System.out.println("Green " + xGreen + "::" + yGreen);
            moved = false;

        } else {
            System.out.println("Red " + xRed + "::" + yRed);
            System.out.println("Orange " + xOrg + "::" + yOrg);
            System.out.println("Yellow " + xYell + "::" + yYell);
            System.out.println("Green " + xGreen + "::" + yGreen);
        }
    }

    // the method that draws the balls
    @Override
    protected void onDraw(Canvas canvas) {
        //  canvas.drawColor(0); //if you want another background color

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#00000000"));// set full transparent
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        // mPaint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);

        canvas.drawPaint(paint);
        paint.setColor(Color.parseColor("#55FFFFFF"));

        if (groupId == 1) {
            canvas.drawRect(point1.x + colorballs.get(0).getWidthOfBall() / 2,
                    point3.y + colorballs.get(2).getWidthOfBall() / 2, point3.x
                            + colorballs.get(2).getWidthOfBall() / 2, point1.y
                            + colorballs.get(0).getWidthOfBall() / 2, paint);
        } else {
            canvas.drawRect(point2.x + colorballs.get(1).getWidthOfBall() / 2,
                    point4.y + colorballs.get(3).getWidthOfBall() / 2, point4.x
                            + colorballs.get(3).getWidthOfBall() / 2, point2.y
                            + colorballs.get(1).getWidthOfBall() / 2, paint);
        }
        BitmapDrawable mBitmap;
        mBitmap = new BitmapDrawable();

        // draw the balls on the canvas
        for (ColorBall ball : colorballs) {
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                    new Paint());
        }
    }

    // events when touching the screen
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventaction) {

            case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on
                // a ball
                balID = -1;
                startMovePoint = new Point(X, Y);
                for (ColorBall ball : colorballs) {
                    // check if inside the bounds of the ball (circle)
                    // get the center for the ball
                    int centerX = ball.getX() + ball.getWidthOfBall();
                    int centerY = ball.getY() + ball.getHeightOfBall();
                    //    System.out.println("getHeightOfBall " + ball.getHeightOfBall());

                    paint.setColor(Color.CYAN);
                    // calculate the radius from the touch to the center of the ball
                    double radCircle = Math
                            .sqrt((double) (((centerX - X) * (centerX - X)) + (centerY - Y)
                                    * (centerY - Y)));

                    if (radCircle < ball.getWidthOfBall()) {

                        balID = ball.getID();
                        if (balID == 1 || balID == 3) {
                            groupId = 2;
                            canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                    paint);
                        } else {
                            groupId = 1;
                            canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                    paint);
                        }
                        invalidate();
                        break;
                    }
                    invalidate();
                }

                break;

            case MotionEvent.ACTION_MOVE: // touch drag with the ball
                // move the balls the same as the finger
                if (balID > -1) {
                    colorballs.get(balID).setX(X);
                    colorballs.get(balID).setY(Y);

                    paint.setColor(Color.CYAN);

                    if (groupId == 1) {
                        // comment - green & orange change palces
                       /// colorballs.get(1).setX(colorballs.get(0).getX());
                       /// colorballs.get(1).setY(colorballs.get(2).getY());
                       /// colorballs.get(3).setX(colorballs.get(2).getX());
                       /// colorballs.get(3).setY(colorballs.get(0).getY());

                        colorballs.get(1).setX(colorballs.get(2).getX());
                        colorballs.get(1).setY(colorballs.get(0).getY());
                        colorballs.get(3).setX(colorballs.get(0).getX());
                        colorballs.get(3).setY(colorballs.get(2).getY());
                        canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                paint);
                    } else {
                        colorballs.get(0).setX(colorballs.get(1).getX());
                        colorballs.get(0).setY(colorballs.get(3).getY());
                        colorballs.get(2).setX(colorballs.get(3).getX());
                        colorballs.get(2).setY(colorballs.get(1).getY());
                        canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                paint);
                    }
                    invalidate();
                } else {
                    if (startMovePoint != null) {
                        paint.setColor(Color.CYAN);
                        int diffX = X - startMovePoint.x;
                        int diffY = Y - startMovePoint.y;
                        startMovePoint.x = X;
                        startMovePoint.y = Y;
                        colorballs.get(0).addX(diffX);
                        colorballs.get(1).addX(diffX);
                        colorballs.get(2).addX(diffX);
                        colorballs.get(3).addX(diffX);
                        colorballs.get(0).addY(diffY);
                        colorballs.get(1).addY(diffY);
                        colorballs.get(2).addY(diffY);
                        colorballs.get(3).addY(diffY);
                        if (groupId == 1)

                            canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                    paint);
                        else
                            canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                    paint);
                        invalidate();
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                // touch drop - just do things here after dropping

                break;
        }
        // redraw the canvas
        invalidate();
        xRed = point1.x;
        yRed = point1.y;
        xOrg = point2.x;
        yOrg = point2.y;
        xYell = point3.x;
        yYell = point3.y;
        xGreen = point4.x;
        yGreen = point4.y;
        moved = true;
        return true;
    }

    public void shade_region_between_points() {
        canvas.drawRect(point1.x, point3.y, point3.x, point1.y, paint);
    }
}
