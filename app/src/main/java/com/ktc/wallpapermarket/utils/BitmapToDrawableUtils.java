package com.ktc.wallpapermarket.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by fengjw on 2018/3/17.
 */

public class BitmapToDrawableUtils {

    private int width = 600;
    private int height = 300;
    private Drawable mDrawable;

    public BitmapToDrawableUtils(Drawable drawable, int width, int height){
        this.width = width;
        this.height = height;
        this.mDrawable = drawable;
    }

    public Drawable getInstance(){
        return zoomDrawable(mDrawable, width, height);
    }


    private Bitmap drawableToBitmap(Drawable drawable){
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    private Drawable zoomDrawable(Drawable drawable, int w, int h){
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float)w) / width;
        float scaleheight = ((float)h) / height;
        matrix.postScale(scaleWidth, scaleheight);
        Bitmap newBmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(newBmp);
    }
}
