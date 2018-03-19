package com.ktc.wallpapermarket.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengjw on 2018/3/14.
 */

public class Constants {

    public static int position;
    public static String photoPath = "/system/etc/wallpaper";

    public static List<File> sList = new ArrayList<>();
    public static List<Drawable> mDList = new ArrayList<>();

    public static final String SETTINGWALLPAPERACTION = "com.ktc.wallpapermarket.setting";


    public static boolean DEBUG = true;
    public static void debug(String str){
        if (DEBUG){
            Log.d("fengjw", str);
        }
    }

    public static String formatSize(Context context, long target_size){
        String tmpSize = String.valueOf(target_size);
        return Formatter.formatFileSize(context, Long.valueOf(tmpSize));
    }





}
