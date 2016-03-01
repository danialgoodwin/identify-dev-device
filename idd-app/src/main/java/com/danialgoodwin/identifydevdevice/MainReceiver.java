package com.danialgoodwin.identifydevdevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.danialgoodwin.android.SimpleMessage;

public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_PACKAGE_ADDED:
                handleActionPackageAdded(context, intent);
                break;
        }
    }

    public static void handleActionPackageAdded(Context context, Intent intent) {
        Log.d("MainReceiver", "handleActionPackageAdded()");
        int uid = intent.getIntExtra(Intent.EXTRA_UID, -1);
        boolean isReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
        String[] packageNames = context.getPackageManager().getPackagesForUid(uid);
        if (packageNames != null && packageNames.length > 0) {
            String packageName = packageNames[0];
            int countAddedToday = MainPrefs.getInstance(context).savePackageAdded(packageName);
            if (countAddedToday > 3) {
                SimpleMessage.showNotification(context, "ACTION_PACKAGE_ADDED (" + countAddedToday + ")", "app: " + packageName + ", isReplacing=" + isReplacing, R.mipmap.ic_launcher, null);
            } else {
                SimpleMessage.showToast(context, "Not dev device yet, countAddedToday=" + countAddedToday);
            }
        } else {
            SimpleMessage.showNotification(context, "ACTION_PACKAGE_ADDED", "package added, but not found... whaa?", R.mipmap.ic_launcher, null);
        }
    }

}
