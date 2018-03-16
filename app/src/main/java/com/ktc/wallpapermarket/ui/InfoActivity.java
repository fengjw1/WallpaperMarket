package com.ktc.wallpapermarket.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktc.wallpapermarket.R;
import com.ktc.wallpapermarket.utils.Constants;

/**
 * Created by fengjw on 2018/3/16.
 */

public class InfoActivity extends Activity {

    private RelativeLayout infoRl;
    private RelativeLayout infoTopRl;
    private RelativeLayout infoTopHomeRl;
    private RelativeLayout infoTopMarketRl;
    private ImageView infoTopHomeIv;
    private ImageView infoTopMarketIv1, infoTopMarketIv2;
    private TextView infoTopMarketTv;

    private TextView infoContentNameTv;
    private ImageView infoContentIv;
    private Button infoContentBtn;
    private TextView infoContentSizeTv;

    private int currentPosition = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initView();
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("position", -1);
        Constants.debug("currentPosition : " + currentPosition);
    }

    private void initView(){
        infoRl = (RelativeLayout) findViewById(R.id.info_rl);
        infoTopRl = (RelativeLayout) findViewById(R.id.info_top_bar_rl);
        infoTopHomeRl = (RelativeLayout) findViewById(R.id.info_top_home_rl);
        infoTopMarketRl = (RelativeLayout) findViewById(R.id.info_top_market_rl);
        infoTopHomeIv = (ImageView) findViewById(R.id.info_top_home_iv);
        infoTopMarketIv1 = (ImageView) findViewById(R.id.info_top_market_iv1);
        infoTopMarketIv2 = (ImageView) findViewById(R.id.info_top_market_iv2);
        infoTopMarketTv = (TextView) findViewById(R.id.info_top_market_tv);

        infoContentNameTv = (TextView) findViewById(R.id.info_content_name_tv);
        infoContentIv = (ImageView) findViewById(R.id.info_content_iv);
        infoContentBtn = (Button) findViewById(R.id.info_content_btn);
        infoContentSizeTv = (TextView) findViewById(R.id.info_content_size_tv);
    }

}
