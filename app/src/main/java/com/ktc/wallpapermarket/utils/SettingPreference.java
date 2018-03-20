package com.ktc.wallpapermarket.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fengjw on 2018/3/19.
 */

public class SettingPreference {

    public static final String WALLPAPERSETTING = "wallpaper_setting";
    public static int MODE = Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE
            + Context.MODE_MULTI_PROCESS;

    private Context mContext;

    public SettingPreference(Context context){
        mContext = context;
    }

    public String loadSharedPreferences(String key, String value){
        SharedPreferences share = mContext.getSharedPreferences(WALLPAPERSETTING, MODE);
        String tmp = share.getString(key, "default");
        return tmp;
    }

    public int loadSharedPreferences(String key, int value){
        SharedPreferences share = mContext.getSharedPreferences(WALLPAPERSETTING, MODE);
        int tmp = share.getInt(key, -2);
        return tmp;
    }

    public void saveSharedPreferences(String key, int value){
        Constants.debug("saveSharedPreferences()");
        Constants.debug("key : " + key + " value : " + value );
        SharedPreferences share = mContext.getSharedPreferences(WALLPAPERSETTING, MODE);
        SharedPreferences.Editor editor = share.edit();
        editor.clear().putInt(key, value);
        editor.commit();
    }

    public void saveSharedPreferences(String key, String value){
        Constants.debug("saveSharedPreferences()");
        SharedPreferences share = mContext.getSharedPreferences(WALLPAPERSETTING, MODE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
