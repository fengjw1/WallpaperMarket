package com.ktc.wallpapermarket.utils;

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

    public static boolean DEBUG = true;
    public static void debug(String str){
        if (DEBUG){
            Log.d("fengjw", str);
        }
    }

}
