package com.ktc.wallpapermarket.utils;

import android.content.Context;
import android.text.format.Time;

import com.ktc.wallpapermarket.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by fengjw on 2018/3/26.
 */

public class CurrentTimeUtils {

    private Context mContext;

    public CurrentTimeUtils(Context context){
        mContext = context;
    }

    public String getCurTime(){
        Calendar calendar = Calendar.getInstance();
        Time curTime = new Time();
        curTime.setToNow();
        String tmp = "HH:mm EEEE, MMM" + mContext.getResources().getString(R.string.date_month) + "d"
                + mContext.getResources().getString(R.string.date_day);
        SimpleDateFormat format = new SimpleDateFormat(tmp);
        return format.format(calendar.getTime());
    }


}
