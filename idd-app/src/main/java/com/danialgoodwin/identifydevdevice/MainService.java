package com.danialgoodwin.identifydevdevice;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.danialgoodwin.android.SimpleMessage;

public class MainService extends Service {

    private BroadcastReceiver mReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");
//        mReceiver = new MainReceiver();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case Intent.ACTION_PACKAGE_ADDED:
                        MainReceiver.handleActionPackageAdded(context, intent);
                        break;
                }
            }
        };
        registerReceiver(mReceiver, intentFilter);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Watching, waiting")
                .setContentText("hmmm")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopForeground(false);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
