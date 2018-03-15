package com.ktc.wallpapermarket.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengjw on 2018/3/13.
 */

public class ReadPhotoTool {

    private String path = null;
    private ArrayList<BaseData> mData = new ArrayList<>();
    private  List<File> list = new ArrayList<>();
    public ReadPhotoTool(String path){
        this.path = path;
    }

    public List<File> readPhotoList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(path);
                File[] files = file.listFiles();
                for (File file1 : files){
                    list.add(file1);
                }
            }
        }).start();
        return list;
    }

}
