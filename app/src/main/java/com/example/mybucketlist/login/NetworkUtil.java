package com.example.mybucketlist.login;

import android.annotation.SuppressLint;
import android.os.StrictMode;

public class NetworkUtil {
    @SuppressLint("NewApi")
    static public void setNetworkPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}