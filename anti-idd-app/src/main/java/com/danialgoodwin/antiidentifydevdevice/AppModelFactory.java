package com.danialgoodwin.antiidentifydevdevice;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.danialgoodwin.android.util.PackageUtils;

public class AppModelFactory {

    private static AppModelFactory mAppModelFactory;
    private Context mAppContext;
    private PackageUtils mPackageUtils;
    private PackageManager mPackageManager;

    private AppModelFactory(@NonNull Context appContext) {
        mAppContext = appContext;
        mPackageUtils = PackageUtils.getInstance(appContext);
        mPackageManager = appContext.getPackageManager();

//        mPackageManager.getApplicationIcon();
//        mPackageManager.getApplicationLabel()

    }

    public static AppModelFactory getInstance(@NonNull Context context) {
        if (mAppModelFactory == null) {
            mAppModelFactory = new AppModelFactory(context.getApplicationContext());
        }
        return mAppModelFactory;
    }

    public AppModel newAppModel() {
        return null;
    }


}
