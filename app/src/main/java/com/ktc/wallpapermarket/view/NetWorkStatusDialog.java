package com.ktc.wallpapermarket.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.RouteInfo;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktc.wallpapermarket.R;
import com.ktc.wallpapermarket.utils.Tools;

/**
 * Created by fengjw on 2018/3/20.
 */

public class NetWorkStatusDialog extends Dialog implements OnClickListener {

    private Context mContext;
    private WifiManager mWifiManager;
    private EthernetManager mEthernetManager ;
    private NetInformation netInfo ;
    private WifiConfiguration wifiConfig = null ;
    private List<ScanResult> results = null;
    private static final int SECURITY_NONE = 0;
    private static final int SECURITY_WEP = 1;
    private static final int SECURITY_PSK = 2;
    private static final int SECURITY_EAP = 3;

    //网络类型
    private String netWorkType = null;
    //IP地址
    private String netWorkIP = null ;
    //设备名称
    private String deviceName = null ;
    //网络模式
    private String netWorkModle = null ;
    //SSID
    private String netWorkSSID = null ;
    //子网掩码
    private String netWorkSubnet = null ;
    //状态
    private String netWorkState = null ;
    //网关
    private String netWorkGateway = null ;
    //速度
    private int netWorkSpeed = 0 ;
    //DNS1
    private String netWorkDNS1 = null ;
    //信号质量
    private int netWorkQuality = 0 ;
    //DNS2
    private String netWorkDNS2 = null ;
    //加密类型
    private String netWorkSecuity = null ;
    //以太网MAC地址
    private String netWorkMAC = null ;
    //无线MAC地址
    private String wirelessMAC = null ;

    //textview
    private Button info_close ;
    private TextView info_type ,info_ip ,info_devicename ,info_modle,info_ssid,
            info_subnet,info_state,info_gateway,info_speed,info_dns1,
            info_dns2,info_security,info_ethMac,info_wifiMac;
    private ImageView info_quality ;
    private RelativeLayout rl_ether_mac, rl_wifi_mac;

    //信号强度
    private static final int[] WIFI_SIGNAL_IMG = {
            R.drawable.wifi_signal_0, R.drawable.wifi_signal_1, R.drawable.wifi_signal_2,
            R.drawable.wifi_signal_3
    };

    private int[] SECURE_TYPE = {
            R.string.none, R.string.secure_wep, R.string.secure_wpa
    };
    private ConnectivityManager connectivityManager;

    public NetWorkStatusDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public NetWorkStatusDialog(Context context, int theme){
        super(context, theme);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.network_status_dialog);
        mWifiManager = getWifiManager();
        mEthernetManager = getEthernetManager();
        netWorkState = mContext.getResources().getString(R.string.connect_no) ;
        netInfo = new NetInformation();
        wifiConfig = getWifiConfiguredNetwork() ;
        results = mWifiManager.getScanResults() ;
        if(isWifiConnected()){
            refreshWifiStatus();
        }else{
            refreshEthernetStatus();
        }
        findView();
    }

    private void findView(){

        info_close = (Button)findViewById(R.id.info_close);
        rl_ether_mac = (RelativeLayout)findViewById(R.id.rl_ether_mac);
        rl_wifi_mac = (RelativeLayout)findViewById(R.id.rl_wifi_mac);

        info_type = (TextView)findViewById(R.id.info_type);
        info_ip = (TextView)findViewById(R.id.info_ip);
        info_devicename = (TextView)findViewById(R.id.info_devicename);
        info_modle = (TextView)findViewById(R.id.info_modle);
        info_ssid = (TextView)findViewById(R.id.info_ssid);
        info_subnet = (TextView)findViewById(R.id.info_subnet);
        info_state = (TextView)findViewById(R.id.info_state);
        info_gateway = (TextView)findViewById(R.id.info_gateway);
        info_speed = (TextView)findViewById(R.id.info_speed);
        info_dns1 = (TextView)findViewById(R.id.info_dns1);
        info_quality = (ImageView)findViewById(R.id.info_quality);
        info_dns2 = (TextView)findViewById(R.id.info_dns2);
        info_security = (TextView)findViewById(R.id.info_security);
        info_ethMac = (TextView)findViewById(R.id.info_ethMac);
        info_wifiMac = (TextView)findViewById(R.id.info_wifiMac);

        info_type.setText(netInfo.netType);
        info_ip.setText(netInfo.netIP);
        info_devicename.setText(netInfo.deviceName);
        info_modle.setText(netInfo.netModle);
        info_ssid.setText(netInfo.netSSID);

        info_subnet.setText(netInfo.netSubnet);
        info_state.setText(netInfo.netState);
        info_gateway.setText(netInfo.netGateway);
        info_speed.setText(netInfo.netSpeed+"");
        info_dns1.setText(netInfo.netDNS1);

        info_quality.setImageResource(WIFI_SIGNAL_IMG[netInfo.netQuality>3?3:netInfo.netQuality]);
        info_dns2.setText(netInfo.netDNS2);
        info_security.setText(netInfo.netSecuity);
        info_ethMac.setText(netInfo.netMAC);
        info_wifiMac.setText(netInfo.wifiMAC);

        info_close.setOnClickListener(this);
        if(isWifiConnected()){
            rl_ether_mac.setVisibility(View.GONE);
            rl_wifi_mac.setVisibility(View.VISIBLE);
        }else{
            rl_ether_mac.setVisibility(View.VISIBLE);
            rl_wifi_mac.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        dismiss();
    }

    /*
	     * 有线网络模块
	    */

    //判断有线网络是否连接
    private boolean isEthConnected(){
        if(isEthernetEnabled() && isNetInterfaceAvailable("eth0")){
            return true ;
        }
        return false ;
    }

    private boolean isNetInterfaceAvailable(String ifName) {
        String netInterfaceStatusFile = "/sys/class/net/" + ifName + "/carrier";
        return isStatusAvailable(netInterfaceStatusFile);
    }

    private boolean isStatusAvailable(String statusFile) {
        char st = readStatus(statusFile);
        if (st == '1') {
            return true;
        }

        return false;
    }

    private synchronized char readStatus(String filePath) {
        int tempChar = 0;
        File file = new File(filePath);
        if (file.exists()) {
            Reader reader = null;
            try {
                reader = new InputStreamReader(new FileInputStream(file));
                tempChar = reader.read();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return (char) tempChar;
    }

    //判断有线网络开关状态
    private boolean isEthernetEnabled() {
        EthernetManager ethernet = getEthernetManager();
        return ethernet.isEnabled();
    }

    //获取EthernetManager实例
    private EthernetManager getEthernetManager() {
        return (EthernetManager)mContext.getSystemService(Context.ETHERNET_SERVICE);
    }

    private void refreshEthernetStatus() {
        // show network information.
        if (isEthernetEnabled() && isNetInterfaceAvailable("eth0")) {
            String ip = null;
            String mask = null;
            String gateway = null;
            String dns1 = null;
            String dns2 = null;

            String[] ips = null;
            String[] gateways = null;
            connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            LinkProperties linkProperties = connectivityManager
                    .getLinkProperties(ConnectivityManager.TYPE_ETHERNET);
            Iterator<LinkAddress> iterator = linkProperties.getLinkAddresses().iterator();
            if (iterator.hasNext()) {
                LinkAddress linkAddress = iterator.next();
                ip = linkAddress.getAddress().getHostAddress();
                ips = Tools.resolutionIP(ip);
            }
            // gateway
            for (RouteInfo route : linkProperties.getRoutes()) {
                if (route.isDefaultRoute()) {
                    gateway = route.getGateway().getHostAddress();
                    gateways = Tools.resolutionIP(gateway);
                    break;
                }
            }
            // dns1
            Iterator<InetAddress> dnsIterator = linkProperties.getDnsServers().iterator();
            if (dnsIterator.hasNext()) {
                dns1 = dnsIterator.next().getHostAddress();
                if (!Tools.matchIP(dns1)) {
                    dns1 = null;
                }
            }
            // dns2
            if (dnsIterator.hasNext()) {
                dns2 = dnsIterator.next().getHostAddress();
                if (!Tools.matchIP(dns2)) {
                    dns2 = null;
                }
            }
            // mask
            if (null != ips && null != gateways) {
                if (ips[0].equals(gateways[0])) {
                    mask = "255";
                } else {
                    mask = "0";
                }
                if (ips[1].equals(gateways[1])) {
                    mask += ".255";
                } else {
                    mask += ".0";
                }
                if (ips[2].equals(gateways[2])) {
                    mask += ".255";
                } else {
                    mask += ".0";
                }
                if (ips[3].equals(gateways[3])) {
                    mask += ".255";
                } else {
                    mask += ".0";
                }
            }
            netWorkSSID = "";
            netWorkIP = ip;
            netWorkSubnet = mask;
            netWorkGateway = gateway;
            netWorkDNS1 = dns1;
            netWorkDNS2 = dns2;
            if(mEthernetManager.getConfiguration().getIpAssignment()
                    == IpConfiguration.IpAssignment.DHCP){
                netWorkModle = mContext.getResources().getString(R.string.eth_model_dhcp);
            }else if(mEthernetManager.getConfiguration().getIpAssignment()
                    == IpConfiguration.IpAssignment.STATIC){
                netWorkModle = mContext.getResources().getString(R.string.eth_model_static);
            }else{
                netWorkModle = "";
            }
            netWorkSpeed = 0;
            netWorkQuality = 0;
            netWorkSecuity = "NA";
            netWorkType = mContext.getResources().getString(R.string.ethernet);
            netWorkState = mContext.getResources().getString(R.string.connected);
            deviceName = "eth0";
        } else {// ethernet can not be used.
            netWorkSpeed = 0;
            netWorkQuality = 0;
            netWorkSecuity = "NA";
            netWorkType = mContext.getResources().getString(R.string.ethernet);
            netWorkState = mContext.getResources().getString(R.string.connect_no);
            deviceName = "eth0";
        }
        getAllMacAdress();
        netInfo.setInfoMation(netWorkType, netWorkState, netWorkSecuity,netWorkIP,
                netWorkSubnet, netWorkGateway, netWorkDNS1, netWorkDNS2, netWorkMAC, wirelessMAC,
                netWorkSSID, deviceName, netWorkModle, netWorkSpeed,netWorkQuality);
    }
    //获取有线和无线的mac地址
    private void getAllMacAdress(){
        //eth mac
        EthernetManager ethernetManager = getEthernetManager();
        String mac = ethernetManager.getMacAddress();
        netWorkMAC = mac == null?"NA":mac;
        //wifi mac
        WifiInfo wifiInfo = getWifiManager().getConnectionInfo();
        wirelessMAC = wifiInfo == null ? "NA" : wifiInfo.getMacAddress();
    }
		/*
		 * 无线连接模块
		 * 20151215
		 *
		*/

    //判断wifi是否连接
    private boolean isWifiConnected() {
        WifiManager wifiManager = getWifiManager();
        // wifi is disabled
        if (!wifiManager.isWifiEnabled()) {
            return false;
        }

        // wifi have not connected
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info == null || info.getSSID() == null
                || info.getNetworkId() == WifiConfiguration.INVALID_NETWORK_ID) {
            return false;
        }

        return true;
    }

    private void refreshWifiStatus() {
        // show network information:
        if (mWifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            if (null != wifiInfo && null != wifiInfo.getSSID()
                    && wifiInfo.getNetworkId() != WifiConfiguration.INVALID_NETWORK_ID) {
                String localSsid = wifiInfo.getSSID();
                // get wifi name.
                if (localSsid.contains("\"")) {
                    netWorkSSID = localSsid.substring(1, localSsid.lastIndexOf("\""));
                } else {
                    netWorkSSID = wifiInfo.getSSID();
                }
                // get network information(ip etc.).
                getWifiNetworkInformation();
            } else {// scanning wifi devices.
            }
        } else {// wifi can not be used.
        }
        netInfo.setInfoMation(netWorkType, netWorkState, netWorkSecuity,netWorkIP,
                netWorkSubnet, netWorkGateway, netWorkDNS1, netWorkDNS2, netWorkMAC, wirelessMAC,
                netWorkSSID, deviceName, netWorkModle, netWorkSpeed,netWorkQuality);
    }

    private void getWifiNetworkInformation() {
        WifiConfiguration config = getWifiConfiguredNetwork();
        if (config != null) {
            String ip = null;
            String[] ips = null;
            connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            LinkProperties linkProperties = connectivityManager
                    .getLinkProperties(ConnectivityManager.TYPE_WIFI);
            //yih modify at 2016.10.24 19:20
//	            Iterator<LinkAddress> iterator = linkProperties.getLinkAddresses().iterator();
//	            if (iterator.hasNext()) {
//					LinkAddress linkAddress = iterator.next();
//					String tmp = linkAddress.getAddress().getHostAddress();
//					Toast.makeText(mContext, "tmp = " + tmp, Toast.LENGTH_SHORT).show();
//					if (SettingTools.matchIP(tmp)) {
//						ip = tmp;
//						ips = SettingTools.resolutionIP(ip);
//					}
//				}

            WifiInfo info = mWifiManager.getConnectionInfo();
            int intIp = info.getIpAddress();
            ip = getIpAddress(intIp);
            ips = Tools.resolutionIP(ip);
            //yih end

            String gateway = null;
            String[] gateways = null;
            // gateway
            for (RouteInfo route : linkProperties.getRoutes()) {
                if (route.isDefaultRoute()) {
                    String tmp = route.getGateway().getHostAddress();

                    if (Tools.matchIP(tmp)) {
                        gateway = tmp;
                        gateways = Tools.resolutionIP(gateway);
                    }
                    break;
                }
            }

            String dns1 = null;
            String dns2 = null;
            // dns1
            Iterator<InetAddress> dnsIterator = linkProperties.getDnsServers().iterator();
            if (dnsIterator.hasNext()) {
                String tmp = dnsIterator.next().getHostAddress();

                if (Tools.matchIP(tmp)) {
                    dns1 = tmp;
                }
            }
            // dns2
            if (dnsIterator.hasNext()) {
                String tmp = dnsIterator.next().getHostAddress();

                if (Tools.matchIP(tmp)) {
                    dns2 = tmp;
                }
            }

            String mask = null;
            if (null != ips && null != gateways) {
                if (ips[0].equals(gateways[0])) {
                    mask = "255";
                } else {
                    mask = "0";
                }
                if (ips[1].equals(gateways[1])) {
                    mask += ".255";
                } else {
                    mask += ".0";
                }
                if (ips[2].equals(gateways[2])) {
                    mask += ".255";
                } else {
                    mask += ".0";
                }

                if (ips[3].equals(gateways[3])) {
                    mask += ".255";
                } else {
                    mask += ".0";
                }
            }

            String macAddr = null;
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            if (null != wifiInfo) {
                macAddr = wifiInfo.getMacAddress();
            }
            getAllMacAdress();
            netWorkIP = ip ;
            netWorkSubnet = mask;
            netWorkGateway = gateway;
            netWorkDNS1 = dns1;
            netWorkDNS2 = dns2;
            netWorkModle = "NA";
            netWorkSpeed = wifiInfo.getLinkSpeed();
            netWorkQuality = 0;
            netWorkSecuity = "NA";
            netWorkType = mContext.getResources().getString(R.string.wireless);
            netWorkState = isWifiConnected()?mContext.getResources().getString(R.string.connected):mContext.getResources().getString(R.string.connect_no);
            deviceName = "Wlan0";
            if (results != null && wifiConfig!=null) {
                for (ScanResult result : results) {
                    // Ignore hidden and ad-hoc networks.
                    if (TextUtils.isEmpty(result.SSID) || result.capabilities.contains("[IBSS]")) {
                        continue;
                    }
                    String wSSID = wifiConfig.SSID.substring(1, wifiConfig.SSID.length()-1) ;
                    if(wSSID.equals(result.SSID)){
                        netWorkQuality = getLevel(result.level);
                        netWorkSecuity = mContext.getResources().getString(SECURE_TYPE[getSecurity(result)]);
                        break;
                    }
                }
            }
        }
    }

    private String getIpAddress(int ip) {
        int ip1 = (ip & 0xFF000000) >> 24;
        ip1 = ip1 >= 0 ? ip1 : 256 + ip1;
        // int ip1 = (ip & 0x00FF000000) >> 24 ;
        int ip2 = (ip & 0x00FF0000) >> 16;
        int ip3 = (ip & 0x0000FF00) >> 8;
        int ip4 = ip & 0x000000FF;
        return ip4 + "." + ip3 + "." + ip2 + "." + ip1;
    }

    /**
     * Calculates the level of the signal.
     */
    private int getLevel(int level) {
        if (level == Integer.MAX_VALUE) {
            return -1;
        }

        return WifiManager.calculateSignalLevel(level, 4);
    }

    /**
     * get the security level.
     */
    private int getSecurity(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return SECURITY_WEP;
        } else if (result.capabilities.contains("PSK")) {
            return SECURITY_PSK;
        } else if (result.capabilities.contains("EAP")) {
            return SECURITY_EAP;
        }

        return SECURITY_NONE;
    }

    //获得WifiManager实例
    private WifiManager getWifiManager() {
        if (mWifiManager == null) {
            mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        }

        return mWifiManager;
    }

    public WifiConfiguration getWifiConfiguredNetwork() {
        if (mWifiManager == null) {
            getWifiManager();
        }
        if (mWifiManager.isWifiEnabled()) {
            WifiInfo wifi = mWifiManager.getConnectionInfo();
            List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
            if (configs == null) {
                return null;
            }
            String ssid = wifi.getSSID();
            for (WifiConfiguration config : configs) {
                if (ssid.equals(config.SSID)) {
                    return config;
                }
            }
        }

        return null;
    }

}
