package com.ktc.wallpapermarket.ui;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.NetworkInfo;

import com.ktc.wallpapermarket.R;
import com.ktc.wallpapermarket.utils.NetTool;
import com.ktc.wallpapermarket.view.NetWorkStatusDialog;


/**
 * Created by fengjw on 2018/3/20.
 */

public class NetIconFragment extends Fragment {

    private NetTool mNetTool;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mNetTool = new NetTool(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imageView =new ImageView(getActivity());
        imageView.setFocusable(false);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(mNetTool.isNetworkConnected(getActivity())){
                    NetWorkStatusDialog statusDialog = new NetWorkStatusDialog(getActivity(), R.style.MyDialog);
                    statusDialog.show();
                }else{
                    ComponentName componentName = new ComponentName("com.mstar.tv.tvplayer.ui",
                            "com.mstar.philips.network.NetSettingActivity");
                    Intent intent = new Intent();
                    intent.setComponent(componentName);
                    startActivity(intent);
                }
            }
        });
        return imageView;
    }

    @Override
    public View getView() {
        // TODO Auto-generated method stub
        return imageView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(mNetTool.isNetworkConnected(getActivity())){
            if(mNetTool.isWifiConnected()){
                imageView.setImageResource(R.drawable.net_wifi_bt);
            }else{
                imageView.setImageResource(R.drawable.net_ethernet_bt);
            }
        }else{
            imageView.setImageResource(R.drawable.net_wifi_disable_bt);
        }
        registNetReceiver();

    }
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try{
            getActivity().unregisterReceiver(mNetReceiver);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void registNetReceiver(){
        //ethernet status changed
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //wifi status changed
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        //pppoe status changed
        getActivity().registerReceiver(mNetReceiver, intentFilter);
    }

    private BroadcastReceiver mNetReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                final NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                final boolean Connected = networkInfo != null && networkInfo.isConnected();
                if (Connected && (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET)) { //ethernet connected
                    imageView.setImageResource(R.drawable.net_ethernet_bt);
                } else { // ethernet disconnected
                    imageView.setImageResource(R.drawable.net_ethernet_disable_bt);
                }
            }else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)
                    ||action.equals(WifiManager.WIFI_STATE_ENABLED))
            {
                final NetworkInfo networkInfo = (NetworkInfo)
                        intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                boolean mWifiConnected = networkInfo != null && networkInfo.isConnected();
                if(mWifiConnected){
                    imageView.setImageResource(R.drawable.net_wifi_bt);
                }else{
                    if(mNetTool.isNetworkConnected(getActivity())){
                        imageView.setImageResource(R.drawable.net_ethernet_bt);
                    }else{
                        imageView.setImageResource(R.drawable.net_ethernet_disable_bt);
                    }
                }

            }else if(action.equals(WifiManager.WIFI_STATE_DISABLED)){
                imageView.setImageResource(R.drawable.net_ethernet_disable_bt);
            }


        }
    };


}
