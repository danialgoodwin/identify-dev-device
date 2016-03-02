package com.danialgoodwin.antiidentifydevdevice;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.danialgoodwin.android.util.PackageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScanAppsTask extends AsyncTask<String, ScanAppsTask.AppModelProgress, List<AppModel>> {

    private final PackageUtils mPackageUtils;
    private final OnProgressListener mListener;
    private volatile int mCountTotalApps;
    private final boolean mIsCheckForPackageMonitoringReceiver;

    public ScanAppsTask(@NonNull PackageUtils packageUtils, @NonNull OnProgressListener listener, boolean isCheckForPackageMonitoringReceiver) {
        mPackageUtils = packageUtils;
        mListener = listener;
        mIsCheckForPackageMonitoringReceiver = isCheckForPackageMonitoringReceiver;
    }

    @Override
    protected List<AppModel> doInBackground(String... savedPackages) {
        List<ApplicationInfo> packages;
        if (savedPackages == null || savedPackages.length == 0) {
            packages = mPackageUtils.getAllPackages();
        } else {
            packages = new ArrayList<>(savedPackages.length);
            for (String savedPackage : savedPackages) {
                try {
                    packages.add(mPackageUtils.getPackageManager().getApplicationInfo(savedPackage, 0));
                } catch (PackageManager.NameNotFoundException ignore) {}
            }
        }
        mCountTotalApps = packages.size();
        publishProgress(new AppModelProgress(null, 0)); // Update listener with max
        Collections.sort(packages, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
                return lhs.packageName.compareTo(rhs.packageName);
            }
        });

        List<AppModel> infectedApps = new ArrayList<>();
        for (int i = 0; i < mCountTotalApps; i++) {
            if (isCancelled()) { return infectedApps; }
            ApplicationInfo info = packages.get(i);
            AppModel model = null;
            if (!mIsCheckForPackageMonitoringReceiver || mPackageUtils.isContainPackageMonitoringReceiver(info.packageName)) {
                model = new AppModel(info, mPackageUtils);
                infectedApps.add(model);
            }
            publishProgress(new AppModelProgress(model, i + 1));
        }
        return infectedApps;
    }

    @Override
    protected void onProgressUpdate(ScanAppsTask.AppModelProgress... values) {
        AppModelProgress appModelProgress = values[0];
        mListener.onProgress(appModelProgress.progress, mCountTotalApps, appModelProgress.app);
    }

    public interface OnProgressListener {
        void onProgress(int progress, int max, @Nullable AppModel app);
    }

    static class AppModelProgress {
        public AppModel app;
        public int progress;
        public AppModelProgress(AppModel app, int progress) {
            this.app = app;
            this.progress = progress;
        }
    }

}
