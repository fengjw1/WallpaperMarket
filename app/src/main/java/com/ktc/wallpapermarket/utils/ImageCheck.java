package com.ktc.wallpapermarket.utils;

/**
 * Created by fengjw on 2018/3/16.
 */

public class ImageCheck {
    public ImageCheck(){
    }

    private String[] imgeArray = {"bmp","dib","gif", "jfif", "jpe", "jpeg", "jpg", "png", "tif", "tiff", "ico"};

    public boolean isImage(String path){
        if (path.length() > 0) {
            String tmpName = path.substring(path.lastIndexOf(".") + 1,
                    path.length());
            for (int i = 0; i < imgeArray.length; i++) {
                if (imgeArray[i].equals(tmpName.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
}
