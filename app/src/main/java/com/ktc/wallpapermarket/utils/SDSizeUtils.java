package com.ktc.wallpapermarket.utils;

import android.content.Context;
import android.os.StatFs;
import android.text.format.Formatter;

import java.text.NumberFormat;

/**
 * Created by fengjw on 2018/3/21.
 */

public class SDSizeUtils {

    private final String INTERNAL_STORAGE_PATH = "/storage/emulated/0/";

    private Context mContext;

    public SDSizeUtils(Context context){
        mContext = context;
    }

    /**
     * SD total size
     */
    public String getSDTotalSize(){
        StatFs fs = new StatFs(INTERNAL_STORAGE_PATH);
        long blockSize = fs.getBlockSize();
        long totalBlocks = fs.getBlockCount();
        return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
    }

    /**
     * SD available size
     */
    public String getSDAvailableSize(){
        StatFs fs = new StatFs(INTERNAL_STORAGE_PATH);
        long blockSize = fs.getBlockSize();
        long availableBlocks = fs.getAvailableBlocks();
        return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
    }

    /**
     * SD available size percent
     */
    public String getSizePercent(){
        StatFs fs = new StatFs(INTERNAL_STORAGE_PATH);
        long totalBlocks = fs.getBlockCount();
        long availableBlocks = fs.getAvailableBlocks();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String percentSize = numberFormat.format(((float)availableBlocks / (float)totalBlocks) * 100);
        Constants.debug("percent : " + percentSize);
        return percentSize;
    }

    public float getSizePercent(int value){
        StatFs fs = new StatFs(INTERNAL_STORAGE_PATH);
        long totalBlocks = fs.getBlockCount();
        Constants.debug("totalBlocks : " + totalBlocks);
        long availableBlocks = fs.getAvailableBlocks();
        Constants.debug("availableBlocks : " + availableBlocks);
        float percentSize =  ((float)availableBlocks / (float)totalBlocks) * 100;
        Constants.debug("percent : " + percentSize);
        return percentSize;
    }


}
