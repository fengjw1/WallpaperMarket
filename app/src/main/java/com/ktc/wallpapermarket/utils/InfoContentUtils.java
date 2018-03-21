package com.ktc.wallpapermarket.utils;

import android.content.Context;

import com.ktc.wallpapermarket.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengjw on 2018/3/21.
 */

public class InfoContentUtils {

    private List<ImageInfoBean> mList;

    private Context mContext;

    public InfoContentUtils(Context context){
        mContext = context;
        mList = new ArrayList<>();
    }

    public List<ImageInfoBean> getList(){
        mList.add(0, getInfo0());
        mList.add(1, getInfo1());
        mList.add(2, getInfo2());
        mList.add(3, getInfo3());
        mList.add(4, getInfo4());
        mList.add(5, getInfo5());
        mList.add(6, getInfo6());
        mList.add(7, getInfo7());
        mList.add(8, getInfo8());
        mList.add(9, getInfo9());
        mList.add(10, getInfo10());
        mList.add(11, getInfo11());
        mList.add(12, getInfo12());
        mList.add(13, getInfo13());
        return mList;

    }

    private ImageInfoBean getInfo0(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_0));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_0));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_0));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_0));
        return imageInfoBean;
    }

    private ImageInfoBean getInfo1(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_1));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_1));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_1));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_1));
        return imageInfoBean;
    }
    private ImageInfoBean getInfo2(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_2));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_2));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_2));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_2));
        return imageInfoBean;
    }
    private ImageInfoBean getInfo3(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_3));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_3));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_3));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_3));
        return imageInfoBean;
    }
    private ImageInfoBean getInfo4(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_4));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_4));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_4));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_4));
        return imageInfoBean;
    }private ImageInfoBean getInfo5(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_5));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_5));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_5));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_5));
        return imageInfoBean;
    }
    private ImageInfoBean getInfo6(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_6));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_6));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_6));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_6));
        return imageInfoBean;
    }
    private ImageInfoBean getInfo7(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_7));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_7));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_7));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_7));
        return imageInfoBean;
    }
    private ImageInfoBean getInfo8(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_8));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_8));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_8));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_8));
        return imageInfoBean;
    }
    private ImageInfoBean getInfo9(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_9));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_9));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_9));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_9));
        return imageInfoBean;
    }
    private ImageInfoBean getInfo10(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_10));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_10));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_10));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_10));
        return imageInfoBean;
    }
    private ImageInfoBean getInfo11(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_11));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_11));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_11));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_11));
        return imageInfoBean;
    }
    private ImageInfoBean getInfo12(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_12));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_12));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_12));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_12));
        return imageInfoBean;
    }
    private ImageInfoBean getInfo13(){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        imageInfoBean.setName(mContext.getResources().getString(R.string.info_wallpaper_13));
        imageInfoBean.setStyle(mContext.getResources().getString(R.string.info_style_13));
        imageInfoBean.setDesigner(mContext.getResources().getString(R.string.info_designer_13));
        imageInfoBean.setDescription(mContext.getResources().getString(R.string.info_description_13));
        return imageInfoBean;
    }

}
