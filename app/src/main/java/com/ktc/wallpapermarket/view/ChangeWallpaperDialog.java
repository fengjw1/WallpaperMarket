package com.ktc.wallpapermarket.view;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ktc.wallpapermarket.R;
import com.ktc.wallpapermarket.utils.Constants;

/**
 * Created by fengjw on 2018/3/16.
 */

public class ChangeWallpaperDialog extends Dialog implements View.OnClickListener,View.OnFocusChangeListener {

    private TextView titleTv;
    private TextView contentTv;
    private Button cancelBtn;
    private Button sureBtn;

    private Context mContext;


    public ChangeWallpaperDialog(Context context) {
        super(context, R.style.WallpaperDialog);
        mContext = context;
    }

    public ChangeWallpaperDialog(Context context, int theme){
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_wallpaper_dialog);
        initView();
        initEvent();
    }

    private void initView(){
        titleTv = (TextView) findViewById(R.id.dialog_title_tv);
        contentTv = (TextView) findViewById(R.id.dialog_content_tv);
        cancelBtn = (Button) findViewById(R.id.dialog_cancel_btn);
        sureBtn = (Button) findViewById(R.id.dialog_sure_btn);
    }

    private void initEvent(){
        cancelBtn.setOnClickListener(this);
        cancelBtn.setOnFocusChangeListener(this);

        sureBtn.setOnClickListener(this);
        sureBtn.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_cancel_btn:
                dismiss();
                break;
            case R.id.dialog_sure_btn:
                dismiss();
                Intent intent = new Intent(Constants.SETTINGWALLPAPERACTION);
                mContext.sendBroadcast(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.dialog_cancel_btn:
                if (hasFocus){
                    cancelBtn.setBackgroundResource(R.drawable.button_highlight);
                    cancelBtn.setTextColor(mContext.getResources().getColor(R.color.black));
                }else {
                    cancelBtn.setBackgroundResource(R.drawable.cancel_button_normal);
                    cancelBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                }
                break;
            case R.id.dialog_sure_btn:
                if (hasFocus){
                    sureBtn.setBackgroundResource(R.drawable.button_highlight);
                    sureBtn.setTextColor(mContext.getResources().getColor(R.color.black));
                }else {
                    sureBtn.setBackgroundResource(R.drawable.cancel_button_normal);
                    sureBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                }
                break;
            default:
                break;
        }
    }
}
