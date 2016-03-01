package com.danialgoodwin.antiidentifydevdevice;

import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.danialgoodwin.android.util.PackageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScanAppsTask extends AsyncTask<Void, ScanAppsTask.AppModelProgress, List<AppModel>> {

    private PackageUtils mPackageUtils;
    private OnProgressListener mListener;
    private volatile int mCountTotalApps;

    public ScanAppsTask(@NonNull PackageUtils packageUtils, @NonNull OnProgressListener listener) {
        mPackageUtils = packageUtils;
        mListener = listener;
        // TODO: Possibly create a AppModelFactory class so that we can set default icon drawable.
    }

    @Override
    protected List<AppModel> doInBackground(Void... params) {
        List<ApplicationInfo> packages = mPackageUtils.getAllPackages();
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
            if (mPackageUtils.isContainPackageMonitoringReceiver(info.packageName)) {
                model = new AppModel(info);
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
