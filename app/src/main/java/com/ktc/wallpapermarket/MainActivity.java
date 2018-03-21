package com.ktc.wallpapermarket;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktc.wallpapermarket.adapter.GridAdapter;
import com.ktc.wallpapermarket.ui.InfoActivity;
import com.ktc.wallpapermarket.utils.Constants;
import com.ktc.wallpapermarket.utils.ReadPhotoTool;
import com.ktc.wallpapermarket.utils.SettingPreference;
import com.ktc.wallpapermarket.view.MyGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private final String TAG = this.getClass().getSimpleName().toString();
    private static final boolean DEBUG = true;

    private ImageView mMainTopHomeIv1Root;
    private RelativeLayout mMainTopHomeRlRoot;
    private ImageView mMainTopMarketIv1Root, mMainTopMarketIv2Root;
    private TextView mMainTopMarketTvRoot;
    private RelativeLayout mMainTopMarketRlRoot;
    private RelativeLayout mMainTopBarRlRoot;
    private TextView mMainBottomTimeTvRoot;
    private RelativeLayout mMainRlRoot;

    //arrow
    private RelativeLayout mMainArrowUpRlRoot;
    private RelativeLayout mMainArrowDownRlRoot;
    private ImageView mMainArrowUpIvRoot;
    private ImageView mMainArrowDownIvRoot;

    //wifi
    private Fragment wifiFg;

    private GridAdapter mGridAdapter;
    private MyGridView gridview;

    private int currentSelectPosition = -1;

    private SettingPreference mSettingPreference;
    private int settingPosition;

    private List<File> mList = new ArrayList<>();

    private static final int FIRSTSELECTGRIDVIEW = 0x001;
    private static final int FRESHANIMATION = 0x002;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FIRSTSELECTGRIDVIEW:
                    Constants.debug("FIRSTSELECTGRIDVIEW");
                    mGridAdapter.notifyDataSetChanged();
                    mGridAdapter.setSelection(0);
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
        //new Thread(mRunnable).start();
        mMainTopMarketRlRoot.setFocusable(true);
        Constants.position = 0;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (DEBUG) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                View rootView = MainActivity.this.getWindow().getDecorView();
                View aaa = rootView.findFocus();
                Log.d(TAG, "aaa : " + aaa.toString());
            }
        }
    };

    private void initGridView() {
        mGridAdapter = new GridAdapter(this, mList, gridview);
        gridview.setAdapter(mGridAdapter);
        gridview.setOnKeyListener(new GridViewOnKeyListener());
        gridview.setOnItemClickListener(new GridViewOnItemClickListener());
        gridview.setOnItemSelectedListener(new GridViewOnItemSelectedListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.debug("onResume");

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
        settingPosition = mSettingPreference.loadSharedPreferences("settingPosition", -2);
        currentSelectPosition = Constants.position;
        mGridAdapter.setSelection(Constants.position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.debug("onDestroy()");

    }

    private void init() {
        Constants.debug("init()");
        ReadPhotoTool readPhotoTool = new ReadPhotoTool(Constants.photoPath);
        mList = readPhotoTool.readPhotoList();
        Constants.sList = mList;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mList.size(); i++) {
                    Constants.debug("path : " + mList.get(i).getPath());
                }
            }
        }).start();

        mSettingPreference = new SettingPreference(this);
        settingPosition = mSettingPreference.loadSharedPreferences("settingPosition", -2);

    }

    private void initClick() {
        Constants.debug("initClick()");
        mMainTopHomeRlRoot.setOnClickListener(this);
        mMainTopHomeRlRoot.setOnFocusChangeListener(this);
        mMainTopHomeRlRoot.setOnKeyListener(this);
        mMainTopMarketRlRoot.setOnClickListener(this);
        mMainTopMarketRlRoot.setOnKeyListener(this);
        mMainTopMarketRlRoot.setOnFocusChangeListener(this);
    }

    private void initView() {
        Constants.debug("initView()");
        mMainTopHomeIv1Root = (ImageView) findViewById(R.id.root_main_top_home_iv1);
        mMainTopHomeRlRoot = (RelativeLayout) findViewById(R.id.root_main_top_home_rl);
        mMainTopMarketIv1Root = (ImageView) findViewById(R.id.root_main_top_market_iv1);
        mMainTopMarketIv2Root = (ImageView) findViewById(R.id.root_main_top_market_iv2);
        mMainTopMarketTvRoot = (TextView) findViewById(R.id.root_main_top_market_tv);
        mMainTopMarketRlRoot = (RelativeLayout) findViewById(R.id.root_main_top_market_rl);
        mMainTopBarRlRoot = (RelativeLayout) findViewById(R.id.root_main_top_bar_rl);
        mMainBottomTimeTvRoot = (TextView) findViewById(R.id.root_main_bottom_time_tv);
        mMainRlRoot = (RelativeLayout) findViewById(R.id.root_main_rl);
        gridview = (MyGridView) findViewById(R.id.root_main_gv);

        //arrow
        mMainArrowUpRlRoot = (RelativeLayout) findViewById(R.id.root_main_arrow_up_rl);
        mMainArrowUpIvRoot = (ImageView) findViewById(R.id.root_main_arrow_up_iv);
        mMainArrowDownRlRoot = (RelativeLayout) findViewById(R.id.root_main_arrow_down_rl);
        mMainArrowDownIvRoot = (ImageView) findViewById(R.id.root_main_arrow_down_iv);

        //curTime
        mMainBottomTimeTvRoot.setText(Constants.getCurTime());

        //wifi
        wifiFg = getFragmentManager().findFragmentById(R.id.root_main_top_wifi_fg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.root_main_top_home_rl:
                // TODO 18/03/13
                finish();
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
                    mMainTopHomeIv1Root.setBackgroundResource(R.drawable.top2_circle_highlighted_blue);
                } else {
                    mMainTopHomeIv1Root.setBackgroundResource(R.drawable.top2_circle_normal);
                }
                break;
            case R.id.root_main_top_market_rl:
                if (hasFocus) {
                    mMainTopMarketIv1Root.setBackgroundResource(R.drawable.top_circle_highlighted);
                    mMainTopMarketIv2Root.setBackgroundResource(R.drawable.theme_market_hl);
                    mMainTopMarketTvRoot.setTextColor(getResources().getColor(R.color.blue));
                } else {
                    mMainTopMarketIv1Root.setBackgroundResource(R.drawable.top_circle_white);
                    mMainTopMarketIv2Root.setBackgroundResource(R.drawable.theme_market_a);
                    mMainTopMarketTvRoot.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()){
            case R.id.root_main_top_market_rl:
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                    mMainTopHomeRlRoot.setFocusable(true);
                    mMainTopMarketRlRoot.setFocusable(false);
                    gridview.setFocusable(false);
                    wifiFg.getView().setFocusable(false);
                }
                //wifi
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                    mMainTopHomeRlRoot.setFocusable(false);
                    mMainTopMarketRlRoot.setFocusable(false);
                    gridview.setFocusable(false);
                    wifiFg.getView().setFocusable(true);
                }
                //gridview
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                    mMainTopHomeRlRoot.setFocusable(false);
                    mMainTopMarketRlRoot.setFocusable(false);
                    wifiFg.getView().setFocusable(false);
                    gridview.setFocusable(true);
                    gridview.requestFocus();
                    currentSelectPosition = Constants.position;
                    mGridAdapter.setSelection(Constants.position);
                    mMainArrowUpRlRoot.setVisibility(View.INVISIBLE);
                    mMainArrowDownRlRoot.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.root_main_top_home_rl:
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                    wifiFg.getView().setFocusable(false);
                    mMainTopHomeRlRoot.setFocusable(false);
                    mMainTopMarketRlRoot.setFocusable(true);
                    gridview.setFocusable(false);
                }

                //gridview
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                    mMainTopHomeRlRoot.setFocusable(false);
                    mMainTopMarketRlRoot.setFocusable(false);
                    gridview.setFocusable(true);
                    gridview.requestFocus();
                    currentSelectPosition = Constants.position;
                    mGridAdapter.setSelection(Constants.position);
                    mMainArrowUpRlRoot.setVisibility(View.INVISIBLE);
                    mMainArrowDownRlRoot.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Constants.debug("onKeyDown");
        View rootview = this.getWindow().getDecorView();
        int focusId = rootview.findFocus().getId();
        Constants.debug("focusId : " + focusId);
        if (focusId == R.id.root_main_top_wifi_fg){
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                wifiFg.getView().setFocusable(false);
                mMainTopHomeRlRoot.setFocusable(false);
                mMainTopMarketRlRoot.setFocusable(true);
                gridview.setFocusable(false);
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                wifiFg.getView().setFocusable(false);
                mMainTopHomeRlRoot.setFocusable(false);
                mMainTopMarketRlRoot.setFocusable(false);
                gridview.setFocusable(true);
                gridview.requestFocus();
                currentSelectPosition = Constants.position;
                mGridAdapter.setSelection(Constants.position);
                mMainArrowUpRlRoot.setVisibility(View.INVISIBLE);
                mMainArrowDownRlRoot.setVisibility(View.VISIBLE);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class GridViewOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Constants.debug("onItemClick() position : " + position);
            if (settingPosition == -1){
                if (position == 0){
                    Toast.makeText(MainActivity.this, R.string.toast_str, Toast.LENGTH_SHORT)
                            .show();
                }else {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    intent.putExtra("position", position - 1);
                    startActivity(intent);
                }
            }else {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }

        }
    }

    private class GridViewOnKeyListener implements View.OnKeyListener{

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Constants.debug("GridViewOnKeyListener onKey()");
            if (event.getAction() == KeyEvent.ACTION_DOWN){
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP ){
                    if (currentSelectPosition == 3){
                        mGridAdapter.setSelection(-2);
                        mMainTopMarketRlRoot.setFocusable(false);
                        mMainTopHomeRlRoot.setFocusable(false);
                        mMainArrowUpRlRoot.setVisibility(View.INVISIBLE);
                        mMainArrowDownRlRoot.setVisibility(View.INVISIBLE);
                        wifiFg.getView().setFocusable(true);
                    }
                    if (currentSelectPosition >= 0 && currentSelectPosition <= 2){
                        Constants.debug(".........");
//                        Constants.position = -2;
//                        currentSelectPosition = -2;
                        mGridAdapter.setSelection(-2);
                        mMainTopMarketRlRoot.setFocusable(true);
                        mMainTopHomeRlRoot.setFocusable(false);
                        mMainArrowUpRlRoot.setVisibility(View.INVISIBLE);
                        mMainArrowDownRlRoot.setVisibility(View.INVISIBLE);
                    }else if (currentSelectPosition > 11){
                        mMainArrowUpRlRoot.setVisibility(View.VISIBLE);
                        mMainArrowDownRlRoot.setVisibility(View.INVISIBLE);
                    }else if (currentSelectPosition >= 8 && currentSelectPosition <=11){
                        mMainArrowUpRlRoot.setVisibility(View.VISIBLE);
                        mMainArrowDownRlRoot.setVisibility(View.VISIBLE);
                    }else if (currentSelectPosition >= 4 && currentSelectPosition <=7){
                        mMainArrowUpRlRoot.setVisibility(View.INVISIBLE);
                        mMainArrowDownRlRoot.setVisibility(View.VISIBLE);
                    }

                }

                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                    if (currentSelectPosition >= 8 && currentSelectPosition <= 11){
                        mMainArrowUpRlRoot.setVisibility(View.VISIBLE);
                        mMainArrowDownRlRoot.setVisibility(View.INVISIBLE);
                    }else if (currentSelectPosition >= 4 && currentSelectPosition <= 7){
                        mMainArrowUpRlRoot.setVisibility(View.VISIBLE);
                        mMainArrowDownRlRoot.setVisibility(View.VISIBLE);
                    }
                }

            }
            return false;
        }
    }

    private class GridViewOnItemSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Constants.debug("onItemSelected position : " + position);
            Constants.position = position;
            currentSelectPosition = position;
            mGridAdapter.setSelection(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Constants.debug("onNothingSelected");
            mGridAdapter.setSelection(-2);
        }
    }

}
