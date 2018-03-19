package com.ktc.wallpapermarket.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fengjw on 2018/3/19.
 */

public class SettingPreference {

    public static final String WALLPAPERSETTING = "wallpaper_setting";
    public static int MODE = Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE;

    private Context mContext;

    public SettingPreference(Context context){
        mContext = context;
    }

    public void loadSharedPreferences(final String key, final String value){
        Constants.debug("loadSharedPreferences()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences share = mContext.getSharedPreferences(WALLPAPERSETTING, MODE);
                String name = share.getString(key, "default");
                Constants.debug(key + " : " + name);
                if (!name.equals(value)){
                    saveSharedPreferences(key, value);
                }
            }
        }).start();
    }

    public int loadSharedPreferences(String key, int value){
        SharedPreferences share = mContext.getSharedPreferences(WALLPAPERSETTING, MODE);
        int position = share.getInt(key, -1);
        Constants.debug("key position : " + position);
        return position;
    }

    public void saveSharedPreferences(String key, int value){
        Constants.debug("saveSharedPreferences()");
        Constants.debug("key : " + key + " value : " + value );
        SharedPreferences share = mContext.getSharedPreferences(WALLPAPERSETTING, MODE);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(key, value);
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
