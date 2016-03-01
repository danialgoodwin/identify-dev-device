package com.danialgoodwin.antiidentifydevdevice;

import android.content.pm.ApplicationInfo;
import android.support.annotation.NonNull;
import android.widget.TextView;

public class AppModel {

    private ApplicationInfo mApplicationInfo;
    private String mPackageName;

    public AppModel(@NonNull ApplicationInfo info) {
        mApplicationInfo = info;
        mPackageName = mApplicationInfo.packageName;
    }

    public String getPackageName() {
        return mPackageName;
    }
}
