package com.adolfo.flexiologistics.utils;

import android.app.Application;
import android.content.Context;
import android.device.ScanDevice;
import android.net.ConnectivityManager;

public class CoreApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppConstants.sm = new ScanDevice();
        AppConstants.sm.setOutScanMode(0);
    }

    public static boolean isNetworkConnection(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
}
