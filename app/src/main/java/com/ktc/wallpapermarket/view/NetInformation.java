package com.ktc.wallpapermarket.view;

/**
 * Created by fengjw on 2018/3/20.
 */

public class NetInformation {
    protected String netType ;
    protected String netIP ;
    protected String deviceName ;
    protected String netModle ;
    protected String netSSID ;
    protected String netSubnet ;
    protected String netState ;
    protected String netGateway ;
    protected int netSpeed ;
    protected String netDNS1 ;
    protected int netQuality ;
    protected String netDNS2 ;
    protected String netSecuity ;
    protected String netMAC ;
    protected String wifiMAC ;

    public void setInfoMation(
            String netType ,String netState,
            String netSecuity,String netIP ,
            String netSubnet ,String netGateway,
            String netDNS1,String netDNS2,
            String netMAC,String wifiMAC,
            String netSSID ,String deviceName ,
            String netModle , int netSpeed,int netQuality){

        this.netType  = netType;
        this.netState  = netState;
        this.netSecuity = netSecuity;
        this.netIP  = netIP;
        this.netSubnet  = netSubnet;
        this.netGateway = netGateway;
        this.netDNS1 = netDNS1;
        this.netDNS2 = netDNS2;
        this.netMAC = netMAC;
        this.wifiMAC = wifiMAC;
        this.netSSID = netSSID;
        this.deviceName = deviceName;
        this.netModle = netModle;
        this.netSpeed = netSpeed;
        this.netQuality = netQuality;
    }
}
