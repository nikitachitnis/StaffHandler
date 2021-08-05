package com.miscos.staffhandler.shiftmanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.miscos.staffhandler.shiftmanagement.adapters.WifiAdapter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
/*Developed under Miscos
 * Developed by Karan
 * 25-09-2020
 * */
public class WifiReceiver extends BroadcastReceiver {
    WifiManager wifiManager;
    StringBuilder sb;
    RecyclerView wifiDeviceList;
    WifiAdapter wifiAdapter;
    List<Boolean> checkList;

    public WifiReceiver(WifiManager wifiManager, RecyclerView wifiDeviceList) {
        this.wifiManager = wifiManager;
        this.wifiDeviceList = wifiDeviceList;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
            sb = new StringBuilder();
            List<ScanResult> wifiList = wifiManager.getScanResults();
            List<String> deviceList = new ArrayList<>();
            checkList = new ArrayList<>();

            for (ScanResult scanResult : wifiList) {
                sb.append("\n").append(scanResult.SSID);
                deviceList.add(scanResult.SSID);
                checkList.add(false);
            }
            wifiAdapter = new WifiAdapter(context, deviceList, checkList);
            wifiDeviceList.setAdapter(wifiAdapter);
        }
    }
}