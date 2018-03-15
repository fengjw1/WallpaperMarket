package com.ktc.wallpapermarket.utils;

import android.util.Log;

/**
 * Created by fengjw on 2018/3/14.
 */

public class Constants {

    public static int position;
    public static String photoPath = "/system/etc/wallpaper";


    public static boolean DEBUG = true;
    public static void debug(String str){
        if (DEBUG){
            Log.d("fengjw", str);
        }
    }

}
