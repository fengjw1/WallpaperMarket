package com.ktc.wallpapermarket.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ktc.wallpapermarket.R;
import com.ktc.wallpapermarket.utils.Constants;

/**
 * Created by fengjw on 2018/3/16.
 */

public class SettingWallpaperDialog extends Dialog {

    private Context mContext;

    private ImageView settingResultIv;
    private TextView settingTv;
    private ProgressBar settingPb;
    private final int LOAD_SUCC = 0x001;
    private final int LOAD_FAIL = 0x002;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_SUCC:
                    dismiss();
                    break;
                case LOAD_FAIL:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public SettingWallpaperDialog(Context context) {
        super(context, R.style.circleloading);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        View view = LayoutInflater.from(mContext).inflate(R.layout.setting_wallpaper_dialog, null);
        view.setMinimumWidth(display.getWidth());
        view.setMinimumHeight(display.getHeight());
        setContentView(view);
        initView();
    }

    private void initView(){
        settingResultIv = (ImageView) findViewById(R.id.setting_loading_result_iv);
        settingPb = (ProgressBar) findViewById(R.id.setting_loading_pb);
        settingTv = (TextView) findViewById(R.id.setting_loading_tv);
    }

    public void dismissSuc(){
        Constants.debug("dismissSuc()");
        settingPb.setVisibility(View.GONE);
        settingResultIv.setVisibility(View.VISIBLE);
        settingResultIv.setImageResource(R.drawable.load_suc_icon);
        settingTv.setText(R.string.setting_loading_succeed);
        mHandler.sendEmptyMessageDelayed(LOAD_SUCC, 1500);
    }

    public void dismissFail(){
        Constants.debug("dismissFail()");
        settingPb.setVisibility(View.GONE);
        settingResultIv.setVisibility(View.VISIBLE);
        settingResultIv.setImageResource(R.drawable.load_fail_icon);
        settingTv.setText(R.string.setting_loading_failed);
        mHandler.sendEmptyMessageDelayed(LOAD_FAIL, 1500);
    }


}

