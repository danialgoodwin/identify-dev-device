package com.danialgoodwin.antiidentifydevdevice;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.danialgoodwin.android.SimpleMessage;
import com.danialgoodwin.android.util.PackageUtils;

// More info: http://developer.android.com/reference/android/content/Intent.html#ACTION_PACKAGE_ADDED
public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_PACKAGE_ADDED:
                handleActionPackageAdded(context, intent);
                break;
        }
    }

    public static void handleActionPackageAdded(@NonNull Context context, @NonNull Intent intent) {
        int uid = intent.getIntExtra(Intent.EXTRA_UID, -1);
        String[] packageNames = context.getPackageManager().getPackagesForUid(uid);
        if (packageNames == null || packageNames.length == 0) { return; }
        PackageUtils packageUtils = PackageUtils.getInstance(context);
        for (String packageName : packageNames) {
            boolean isContainPackageMonitoring = packageUtils.isContainPackageMonitoringReceiver(packageName);
            if (isContainPackageMonitoring) {
                AiddPrefs.getInstance(context).addInfectedApp(packageName);
                Intent activityIntent = AppModelDetailPage.getIntentToShow(context, packageName);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, activityIntent, 0);
                SimpleMessage.showNotification(context, "WARNING", packageName, R.drawable.ic_stat_app, pendingIntent);
            }
        }
    }

}
