package com.ktc.wallpapermarket.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktc.wallpapermarket.R;
import com.ktc.wallpapermarket.utils.BitmapToDrawableUtils;
import com.ktc.wallpapermarket.utils.Constants;
import com.ktc.wallpapermarket.utils.ImageCheck;
import com.ktc.wallpapermarket.utils.SDSizeUtils;
import com.ktc.wallpapermarket.utils.SettingPreference;
import com.ktc.wallpapermarket.view.ChangeWallpaperDialog;
import com.ktc.wallpapermarket.view.SettingWallpaperDialog;

import java.io.File;

/**
 * Created by fengjw on 2018/3/16.
 */

public class InfoActivity extends Activity implements View.OnKeyListener, View.OnClickListener, View.OnFocusChangeListener {

    private RelativeLayout infoRl;
    private RelativeLayout infoTopRl;
    private RelativeLayout infoTopHomeRl;
    private RelativeLayout infoTopMarketRl;
    private ImageView infoTopHomeIv1, infoTopHomeIv2;
    private ImageView infoTopMarketIv1, infoTopMarketIv2;
    private TextView infoTopMarketTv;

    private TextView infoContentNameTv;
    private ImageView infoContentIv;
    private Button infoContentBtn;
    private TextView infoContentSizeTv;

    //current Time
    private TextView infoCurTimeTv;

    //wifi
    private Fragment wifiFg;

    //progressBar
    private ProgressBar infoStoragePb;
    private TextView infoStorageTv;

    private int currentPosition = -1;
    private File mFile = null;
    private String path = null;

    private SettingPreference mPreference;

    private ImageCheck mCheck;

    private SettingWallpaperDialog mSettingWallpaperDialog  = null;

    private WallpaperManager mManager = null;

    private final int LOAD_SUC_FIUNISH = 0x003;
    private final int LOAD_FAIL_FINISH = 0x004;
    private Handler settingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_FAIL_FINISH:
                    mSettingWallpaperDialog.dismissFail();
                    break;
                case LOAD_SUC_FIUNISH:
                    mSettingWallpaperDialog.dismissSuc();
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        Constants.debug("onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("position", -1);
        mFile = Constants.sList.get(currentPosition);
        Constants.debug("currentPosition : " + currentPosition);

        path = mFile.getPath();

        mPreference = new SettingPreference(this);

        initView();
        initClick();

        mCheck = new ImageCheck();
        mManager = WallpaperManager.getInstance(InfoActivity.this);

        IntentFilter mfilter = new IntentFilter(Constants.SETTINGWALLPAPERACTION);
        registerReceiver(mBroadcastReceiver, mfilter);

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Constants.debug("mBroadcastReceiver");
            startSettingWp();
        }
    };

    private void startSettingWp(){
        Constants.debug("startSettingWp()");
        mSettingWallpaperDialog = new SettingWallpaperDialog(this);
        mSettingWallpaperDialog.show();
        if (!path.isEmpty() && mCheck.isImage(path)){
            try {
                new WallpaperThread().start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            mSettingWallpaperDialog.dismissFail();
        }
    }

    class WallpaperThread extends Thread{
        @Override
        public void run() {
            setWallPaper();
        }
    }

    private void setWallPaper(){
        Constants.debug("setWallPaper()");
        try {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(path);
            Bitmap bitmap = bitmapDrawable.getBitmap();
            mManager.setBitmap(bitmap);
            mPreference.saveSharedPreferences("settingPosition",Constants.position);
            settingHandler.sendEmptyMessage(LOAD_SUC_FIUNISH);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.debug("onDestroy()");
        unregisterReceiver(mBroadcastReceiver);
    }

    private void initView(){
        Constants.debug("initView()");
        infoRl = (RelativeLayout) findViewById(R.id.info_rl);
        infoTopRl = (RelativeLayout) findViewById(R.id.info_top_bar_rl);
        infoTopHomeRl = (RelativeLayout) findViewById(R.id.info_top_home_rl);
        infoTopMarketRl = (RelativeLayout) findViewById(R.id.info_top_market_rl);
        infoTopHomeIv1 = (ImageView) findViewById(R.id.info_top_home_iv1);
        infoTopHomeIv2 = (ImageView) findViewById(R.id.info_top_home_iv2);
        infoTopMarketIv1 = (ImageView) findViewById(R.id.info_top_market_iv1);
        infoTopMarketIv2 = (ImageView) findViewById(R.id.info_top_market_iv2);
        infoTopMarketTv = (TextView) findViewById(R.id.info_top_market_tv);

        infoContentNameTv = (TextView) findViewById(R.id.info_content_name_tv);
        infoContentIv = (ImageView) findViewById(R.id.info_content_iv);
        infoContentBtn = (Button) findViewById(R.id.info_content_btn);
        infoContentSizeTv = (TextView) findViewById(R.id.info_content_size_tv);

        //curTime
        infoCurTimeTv = (TextView) findViewById(R.id.info_curtime_tv);
        infoCurTimeTv.setText(Constants.getCurTime());

        //wifi
        wifiFg = getFragmentManager().findFragmentById(R.id.info_top_wifi_fg);

        //ProgressBar
        infoStoragePb = (ProgressBar) findViewById(R.id.info_storage_pb);
        infoStorageTv = (TextView) findViewById(R.id.info_storage_tv);

        SDSizeUtils sdSizeUtils = new SDSizeUtils(this);
        String percentSize = sdSizeUtils.getSizePercent() +
                getResources().getString(R.string.info_storage_percent);
        float tmpSize = sdSizeUtils.getSizePercent(0);
        Constants.debug("tmpSize : " + tmpSize);
        infoStorageTv.setText(percentSize);
        infoStoragePb.setProgress((int)tmpSize);
    }

    private void initClick(){
        Constants.debug("initClick");

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
        Drawable drawable = Constants.mDList.get(currentPosition);
        int width = 1600;
        int height = 800;
        BitmapToDrawableUtils drawableUtils = new BitmapToDrawableUtils(drawable, width, height);
        infoContentIv.setImageDrawable(drawableUtils.getInstance());

        infoContentNameTv.setText(mFile.getName());

        String tmpSize = getResources().getString(R.string.wallpaper_info_size)
                + Constants.formatSize(this, mFile.length());
        infoContentSizeTv.setText(tmpSize);

        infoTopHomeRl.setOnKeyListener(this);
        infoTopHomeRl.setOnFocusChangeListener(this);
        infoTopHomeRl.setOnClickListener(this);

        infoTopMarketRl.setOnKeyListener(this);
        infoTopMarketRl.setOnFocusChangeListener(this);
        infoTopMarketRl.setOnClickListener(this);

        infoContentBtn.setOnKeyListener(this);
        infoContentBtn.setOnFocusChangeListener(this);
        infoContentBtn.setOnClickListener(this);
        infoContentBtn.requestFocus();
        infoContentBtn.setFocusable(true);
    }

    @Override
    public void onClick(View v) {
        Constants.debug("onClick()");
        switch (v.getId()){
            case R.id.info_top_home_rl:
                finish();
                break;
            case R.id.info_top_market_rl:
                break;
            case R.id.info_content_btn:
                String name = mFile.getName();
                Constants.debug("path : " + name);
                ChangeWallpaperDialog changeWallpaperDialog = new ChangeWallpaperDialog(this, name);
                changeWallpaperDialog.show();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        View rootView = this.getWindow().getDecorView();
        int focusId = rootView.findFocus().getId();
        if (focusId == R.id.info_top_wifi_fg){
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                wifiFg.getView().setFocusable(false);
                infoTopHomeRl.setFocusable(false);
                infoContentBtn.setFocusable(false);
                infoTopMarketRl.setFocusable(true);
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                wifiFg.getView().setFocusable(false);
                infoTopHomeRl.setFocusable(false);
                infoContentBtn.setFocusable(true);
                infoTopMarketRl.setFocusable(false);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Constants.debug("onKey()");
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (v.getId()){
                case R.id.info_top_home_rl:
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                        infoContentBtn.setFocusable(true);
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        infoTopMarketRl.setFocusable(true);
                    }
                    break;
                case R.id.info_top_market_rl:
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                        infoContentBtn.setFocusable(true);
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                        infoTopHomeRl.setFocusable(true);
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        wifiFg.getView().setFocusable(true);
                        infoTopHomeRl.setFocusable(false);
                        infoContentBtn.setFocusable(false);
                        infoTopMarketRl.setFocusable(false);
                    }
                    break;
                case R.id.info_content_btn:
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP){
                        infoTopHomeRl.setFocusable(true);
                    }
                    break;
                default:
                    break;
            }
        }

        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Constants.debug("onFocusChange()");
        switch (v.getId()){
            case R.id.info_top_home_rl:
                if (hasFocus){
                    infoTopHomeIv1.setBackgroundResource(R.drawable.top_circle_highlighted);
                    infoTopHomeIv2.setBackgroundResource(R.drawable.theme_market_hl);
                }else {
                    infoTopHomeIv1.setBackgroundResource(R.drawable.top_circle_white);
                    infoTopHomeIv2.setBackgroundResource(R.drawable.theme_market_a);
                }
                break;
            case R.id.info_top_market_rl:
                if (hasFocus){
                    infoTopMarketIv1.setBackgroundResource(R.drawable.top_circle_highlighted);
                    infoTopMarketIv2.setBackgroundResource(R.drawable.mediacenter_hl);
                    infoTopMarketTv.setTextColor(getResources().getColor(R.color.blue));
                }else {
                    infoTopMarketIv1.setBackgroundResource(R.drawable.top_circle_white);
                    infoTopMarketIv2.setBackgroundResource(R.drawable.mediacenter_white);
                    infoTopMarketTv.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.info_content_btn:
                if (hasFocus){
                    infoContentBtn.setBackgroundResource(R.drawable.button_highlight);
                    infoContentBtn.setTextColor(getResources().getColor(R.color.black));
                }else {
                    infoContentBtn.setBackgroundResource(R.drawable.cancel_button_normal);
                    infoContentBtn.setTextColor(getResources().getColor(R.color.white));
                }
                break;
        }
    }

}
