package com.danialgoodwin.antiidentifydevdevice;

import android.content.pm.ApplicationInfo;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.danialgoodwin.android.util.PackageUtils;

import java.util.ArrayList;
import java.util.List;

public class ScanAppsTask extends AsyncTask<Void, AppModel, List<AppModel>> {

    private PackageUtils mPackageUtils;
    private OnProgressListener mListener;

    public ScanAppsTask(@NonNull PackageUtils packageUtils, @NonNull OnProgressListener listener) {
        mPackageUtils = packageUtils;
        mListener = listener;
        // TODO: Possibly create a AppModelFactory class so that we can set default icon drawable.
    }

    @Override
    protected List<AppModel> doInBackground(Void... params) {
        List<ApplicationInfo> packages = mPackageUtils.getAllPackages();
        List<AppModel> models = new ArrayList<>();
        final int countPackages = packages.size();

        for (int i = 0; i < countPackages; i++) {
            ApplicationInfo info = packages.get(i);
            AppModel model = null;
            // TODO add if-condition for interesting stuff
            if (true) {
                model = new AppModel(info);
                models.add(model);
            }
            mListener.onProgress(i + 1, countPackages, model);
        }
        return models;
    }

    public interface OnProgressListener {
        void onProgress(int progress, int max, @Nullable AppModel app);
    }

}
