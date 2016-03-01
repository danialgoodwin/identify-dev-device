package com.danialgoodwin.antiidentifydevdevice;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.danialgoodwin.android.util.PackageUtils;

import java.io.File;

public class AppModel {

    private ApplicationInfo mApplicationInfo;
    private File mApkFile;
    private String mPackageName;
    private CharSequence mTitle;
    private Drawable mIcon;
    private boolean mIsInternetPermissionRequested;
    private boolean mIsAppStopped;

    public AppModel(@NonNull ApplicationInfo info, @NonNull PackageUtils packageUtils) {
        mApplicationInfo = info;
        mApkFile = new File(info.sourceDir);
        mPackageName = info.packageName;
        mIcon = info.loadIcon(packageUtils.getPackageManager());
        mTitle = info.loadLabel(packageUtils.getPackageManager());

        mIsAppStopped = packageUtils.isAppStopped(mPackageName);
        mIsInternetPermissionRequested = packageUtils.isInternetPermissionRequested(mPackageName);
    }

    public String getPackageName() {
        return mPackageName;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public String getApkPath() {
        return mApkFile.getPath();
    }

    public ApplicationInfo getApplicationInfo() {
        return mApplicationInfo;
    }

    public boolean isInternetPermissionRequested() {
        return mIsInternetPermissionRequested;
    }

    public boolean isAppStopped() {
        return mIsAppStopped;
    }


    // Another method, from Android sample app
//    public Drawable getIcon() {
//        if (mIcon == null) {
//            if (mApkFile.exists()) {
//                mIcon = mInfo.loadIcon(mPackageManager);
//                return mIcon;
//            } else {
//                mMounted = false;
//            }
//        } else if (!mMounted) {
//            // If the app wasn't mounted but is now mounted, reload
//            // its icon.
//            if (mApkFile.exists()) {
//                mMounted = true;
//                mIcon = mInfo.loadIcon(mPackageManager);
//                return mIcon;
//            }
//        } else {
//            return mIcon;
//        }
//
//        return mContext.getResources().getDrawable(
//                android.R.drawable.sym_def_app_icon);
//    }
//
//    void loadLabel(Context context) {
//        if (mLabel == null || !mMounted) {
//            if (!mApkFile.exists()) {
//                mLabel = mInfo.packageName;
//                mMounted = false;
//            } else {
//                mMounted = true;
//                CharSequence label = mInfo.loadLabel(context.getPackageManager());
//                mLabel = label != null ? label.toString() : mInfo.packageName;
//            }
//        }
//    }

}
