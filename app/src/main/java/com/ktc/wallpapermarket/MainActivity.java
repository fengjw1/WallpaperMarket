package com.ktc.wallpapermarket;

import android.animation.Animator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktc.wallpapermarket.adapter.GridAdapter;
import com.ktc.wallpapermarket.utils.Constants;
import com.ktc.wallpapermarket.utils.ReadPhotoTool;
import com.ktc.wallpapermarket.view.IGridViewHandler;
import com.ktc.wallpapermarket.view.MyGridView;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

    private final String TAG = this.getClass().getSimpleName().toString();
    private static final boolean DEBUG = true;

    private ImageView mMainTopHomeIv1Root;
    private ImageView mMainTopHomeIv2Root;
    private RelativeLayout mMainTopHomeRlRoot;
    private ImageView mMainTopMarketIvRoot;
    private TextView mMainTopMarketTvRoot;
    private RelativeLayout mMainTopMarketRlRoot;
    private RelativeLayout mMainTopBarRlRoot;
    private TextView mMainBottomTimeTvRoot;
    private RelativeLayout mMainBottomRlRoot;
    private RelativeLayout mMainRlRoot;

    private GridAdapter mGridAdapter;
    private MyGridView gridview;

    private IGridViewHandler gridViewHandler;

    private int currentSelectPosition = -1;

    //SharedPreferences
    public static final String EXECUTE_FIRST = "execute_first";
    public static int MODE = Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE;

    private List<File> mList = new ArrayList<>();

    private static final int FIRSTSELECTGRIDVIEW = 0x001;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FIRSTSELECTGRIDVIEW:
                    Constants.debug("FIRSTSELECTGRIDVIEW");
//                    gridview.setAdapter(mGridAdapter);
//                    gridViewHandler = new IGridViewHandler(MainActivity.this, gridview, mList);
                    gridview.setSelection(0);
                    mGridAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.debug("onCreate");
        setContentView(R.layout.activity_main);
        init();
        initView();
        initGridView();
        initClick();
//        new Thread(mRunnable).start();


    }

    private void initGridView() {
        mGridAdapter = new GridAdapter(this, mList);
        gridview.setAdapter(mGridAdapter);
        gridViewHandler = new IGridViewHandler(this, gridview, mList);

//        try {
//            Constants.debug("try");
//            Method fireOnSelected = AdapterView.class.getDeclaredMethod("fireOnSelected ");
//            fireOnSelected.setAccessible(true);
//            fireOnSelected.invoke(gridview); //运行该方法
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        gridview.setFocusable(true);
        gridview.setSelection(0);
        loadSharedPreferences();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentSelectPosition = (int) intent.getExtras().get("position");
            Constants.position = currentSelectPosition;
            Constants.debug("mainActivity currentSelectPosition : " + currentSelectPosition);
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (DEBUG) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                View rootView = MainActivity.this.getWindow().getDecorView();
                View aaa = rootView.findFocus();
                Log.d(TAG, "aaa : " + aaa.toString());
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Constants.debug("onResume");
        gridview.setFocusable(true);
        mGridAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Constants.debug("onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Constants.debug("onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.debug("onDestroy()");
        unregisterReceiver(mBroadcastReceiver);
    }

    private void init() {
        Constants.debug("init()");
        IntentFilter mfilter = new IntentFilter(IGridViewHandler.SELECTACTION);
        registerReceiver(mBroadcastReceiver, mfilter);
        ReadPhotoTool readPhotoTool = new ReadPhotoTool(Constants.photoPath);
        mList = readPhotoTool.readPhotoList();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mList.size(); i++) {
                    Constants.debug("path : " + mList.get(i).getPath());
                }
            }
        }).start();
    }

    private void initClick() {
        Constants.debug("initClick()");
        mMainTopHomeRlRoot.setOnClickListener(this);
        mMainTopHomeRlRoot.setOnFocusChangeListener(this);
        mMainTopMarketRlRoot.setOnClickListener(this);
        mMainTopMarketRlRoot.setOnFocusChangeListener(this);
    }

    private void initView() {
        Constants.debug("initView()");
        mMainTopHomeIv1Root = (ImageView) findViewById(R.id.root_main_top_home_iv1);
        mMainTopHomeIv2Root = (ImageView) findViewById(R.id.root_main_top_home_iv2);
        mMainTopHomeRlRoot = (RelativeLayout) findViewById(R.id.root_main_top_home_rl);
        mMainTopMarketIvRoot = (ImageView) findViewById(R.id.root_main_top_market_iv);
        mMainTopMarketTvRoot = (TextView) findViewById(R.id.root_main_top_market_tv);
        mMainTopMarketRlRoot = (RelativeLayout) findViewById(R.id.root_main_top_market_rl);
        mMainTopBarRlRoot = (RelativeLayout) findViewById(R.id.root_main_top_bar_rl);
        mMainBottomTimeTvRoot = (TextView) findViewById(R.id.root_main_bottom_time_tv);
        mMainBottomRlRoot = (RelativeLayout) findViewById(R.id.root_main_bottom_rl);
        mMainRlRoot = (RelativeLayout) findViewById(R.id.root_main_rl);
        gridview = (MyGridView) findViewById(R.id.root_main_gv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.root_main_top_home_rl:
                // TODO 18/03/13
                break;
            case R.id.root_main_top_market_rl:
                // TODO 18/03/13
                break;
            case R.id.root_main_gv:// TODO 18/03/15
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.root_main_top_home_rl:
                if (hasFocus) {
                    mMainTopHomeIv1Root.setBackgroundResource(R.drawable.top1_circle_highlighted_blue);
                    mMainTopHomeIv2Root.setBackgroundResource(R.drawable.home_dashboard_hl);
                } else {
                    mMainTopHomeIv1Root.setBackgroundResource(R.drawable.top2_circle_normal);
                    mMainTopHomeIv2Root.setBackgroundResource(R.drawable.home_dashboard_d);
                }
                break;
            case R.id.root_main_top_market_rl:
                if (hasFocus) {
                    mMainTopMarketIvRoot.setBackgroundResource(R.drawable.wallpaper_market_focus);
                    mMainTopMarketTvRoot.setTextColor(getResources().getColor(R.color.blue));
                } else {
                    mMainTopMarketIvRoot.setBackgroundResource(R.drawable.wallpaper_market_icon);
                    mMainTopMarketTvRoot.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            default:
                break;
        }
    }


    /**
     * this method 
     */
    private void loadSharedPreferences(){
        Constants.debug("loadSharedPreferences()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences share = getSharedPreferences(EXECUTE_FIRST, MODE);
                String count = share.getString("count", "default");
                Constants.debug("count : " + count);
                if (!count.equals("-2")){
                    mHandler.sendEmptyMessageDelayed(FIRSTSELECTGRIDVIEW, 2000);
                    saveSharedPreferences();
                    Constants.position = -2;
                }
            }
        }).start();
    }

    private void saveSharedPreferences(){
        Constants.debug("saveSharedPreferences()");
        SharedPreferences share = getSharedPreferences(EXECUTE_FIRST, MODE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("count", "-2");
        editor.commit();
    }


}
