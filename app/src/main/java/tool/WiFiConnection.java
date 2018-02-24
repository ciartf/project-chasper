package tool;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by danu on 6/11/17.
 */

public class WiFiConnection {
    private Context context;
    private static WifiManager wifiManager;
    private static String networkSSID;
    private static String networkPass;
    private static WifiConfiguration wifiConfiguration;

    public static WifiManager getWifiManager() {
        return wifiManager;
    }

    public static void setWifiManager(WifiManager wifiManager) {
        WiFiConnection.wifiManager = wifiManager;
    }

    public static String getNetworkSSID() {
        return networkSSID;
    }

    public static void setNetworkSSID(String networkSSID) {
        WiFiConnection.networkSSID = networkSSID;
    }

    public static String getNetworkPass() {
        return networkPass;
    }

    public static void setNetworkPass(String networkPass) {
        WiFiConnection.networkPass = networkPass;
    }

    public static WifiConfiguration getWifiConfiguration() {
        return wifiConfiguration;
    }

    public static void setWifiConfiguration(WifiConfiguration wifiConfiguration) {
        WiFiConnection.wifiConfiguration = wifiConfiguration;
    }

    public WiFiConnection(Context context, String ssid, String pass) {
        this.context = context;
        this.wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        this.networkSSID = ssid;
        this.networkPass = pass;
        this.wifiConfiguration = new WifiConfiguration();
        this.wifiConfiguration.SSID = String.format("\"%s\"", ssid);
        this.wifiConfiguration.preSharedKey = String.format("\"%s\"", pass);
    }

    public static void connect(){
        if (!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.addNetwork(wifiConfiguration);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
//                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }
    }

    public static boolean isConnected(){
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                if (wifiInfo != null) {
                    NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                    if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean disconnect(){
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                if (wifiInfo != null) {
                    NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                    if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                        wifiManager.setWifiEnabled(false);
                    }
                }
            }
        }
        return false;
    }

}
