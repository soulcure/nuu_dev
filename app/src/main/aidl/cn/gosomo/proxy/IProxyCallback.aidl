package cn.gosomo.proxy;

interface IProxyCallback {
    void onEventSys(int board, int event, int value);
    void onSimcardStateChange(int board, int slot, String imsi);
    void onLocationChange(int board, int slot, String plmn, int lac, int cid, int psc);
    void onServiceStateChange(int board, int slot, int serviceState, int networkType, int rssi);
    void onDataStateChange(int board, int slot, int dataState, int networkType, int rssi);
    void onSignalStrengthChange(int board, int slot, int rssi);
}