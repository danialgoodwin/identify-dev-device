package com.danialgoodwin.android.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.List;

/** Helper methods related to packages. */
public class PackageUtils {

    private static PackageUtils mPackageUtils;
    private Context mAppContext;
    private PackageManager mPackageManager;

    private PackageUtils(@NonNull Context appContext) {
        mAppContext = appContext;
        mPackageManager = appContext.getPackageManager();
    }

    /**
     *
     * @param context the application context will be used
     * @return an instance of this class
     */
    public static PackageUtils getInstance(@NonNull Context context) {
        if (mPackageUtils == null) {
            mPackageUtils = new PackageUtils(context.getApplicationContext());
        }
        return mPackageUtils;
    }

    @NonNull
    public List<ApplicationInfo> getAllPackages() {
        return mPackageManager.getInstalledApplications(0);
    }

    /**
     * Enable or disable an app component.
     * @param componentClass the app component to enable or disable
     * @param isEnable set true to enable component, set false to disable component
     */
    public void enableComponent(@NonNull Class<?> componentClass, boolean isEnable) {
        ComponentName component = new ComponentName(mAppContext, componentClass);
        mPackageManager.setComponentEnabledSetting(component,
                isEnable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


}
