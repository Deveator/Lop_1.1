package com.example.lop_11.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ColorBall {


    Bitmap bitmap;
    Context mContext;
    Point point;
    int id;
    static int count = 0;

    public ColorBall(Context context, int resourceId, Point point, int id) {
        this.id = id;
        bitmap = drawableToBitmap(context.getResources().getDrawable(resourceId));
        // bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        mContext = context;
        this.point = point;
    }

    public ColorBall(Context context, int resourceId, Point point) {

        bitmap = BitmapFactory.decodeResource(context.getResources(),
                resourceId);
        mContext = context;
        this.point = point;
    }
    // method to convert 'XML' file to Bitmap
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public int getWidthOfBall() {
        return bitmap.getWidth();
    }

    public int getHeightOfBall() {
        return bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    public int getID() {
        return id;
    }

    public void setX(int x) {
        point.x = x;
    }

    public void setY(int y) {
        point.y = y;
    }

    public void addY(int y) {
        point.y = point.y + y;
    }

    public void addX(int x) {
        point.x = point.x + x;
    }
}
