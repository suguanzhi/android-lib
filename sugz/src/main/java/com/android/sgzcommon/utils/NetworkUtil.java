package com.android.sgzcommon.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

/**
 * Created by sgz on 2019/5/24 0024.
 */
public class NetworkUtil {

    public static boolean isWiFiActive(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
            System.out.println("**** WIFI is on");
            return true;
        } else {
            System.out.println("**** WIFI is off");
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void ssss(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        Network[] networks = cm.getAllNetworks();
        if (networks != null && networks.length > 0){
            Log.d("NetworkUtil", "ssss: sizee = " + networks.length);
            NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(networks[0]);
            boolean has = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            boolean has2 = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P);
            Log.d("NetworkUtil", "ssss: has  =  " + has);
            Log.d("NetworkUtil", "ssss: has2  =  " + has2);
        }
        Log.d("NetworkUtil", "ssss: -----------");
    }
}
