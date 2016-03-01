package com.danialgoodwin.android.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.jaredrummler.apkparser.ApkParser;
import com.jaredrummler.apkparser.model.AndroidComponent;
import com.jaredrummler.apkparser.model.AndroidManifest;
import com.jaredrummler.apkparser.model.IntentFilter;

import java.io.IOException;
import java.text.ParseException;
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

    public boolean isContainPackageMonitoringReceiver(String packageName) {
        if (packageName == null || packageName.isEmpty()) { return false; }
        try {
            ApkParser parser = ApkParser.create(mPackageManager, packageName);
            AndroidManifest androidManifest = parser.getAndroidManifest();
            for (AndroidComponent component : androidManifest.getComponents()) {
                if (!component.intentFilters.isEmpty()) {
                    for (IntentFilter intentFilter : component.intentFilters) {
                        List<String> actions = intentFilter.actions;
                        for (String action : actions) {
                            switch (action) {
                                case Intent.ACTION_PACKAGE_ADDED:
                                case Intent.ACTION_PACKAGE_DATA_CLEARED:
                                case Intent.ACTION_PACKAGE_FIRST_LAUNCH:
                                case Intent.ACTION_PACKAGE_FULLY_REMOVED:
                                case Intent.ACTION_PACKAGE_REMOVED:
                                case Intent.ACTION_PACKAGE_REPLACED:
                                case Intent.ACTION_PACKAGE_RESTARTED:
                                    return true;
                            }
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Return true if the internet permission is declared in manifest, otherwise false. */
    public boolean isInternetPermissionRequested(String packageName) {
        try {
            PackageInfo app = mPackageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            if (app.requestedPermissions != null) {
                for (String permission : app.requestedPermissions) {
                    if (permission.equals(android.Manifest.permission.INTERNET)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException ignore) {}
        return false;
    }

    public boolean isAppStopped(String packageName) {
        try {
            PackageInfo packageInfo = mPackageManager.getPackageInfo(packageName, 0);
            boolean isStopped = (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_STOPPED) != 0;
            return isStopped;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return true;
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


    public PackageManager getPackageManager() {
        return mPackageManager;
    }

}
