package com.danialgoodwin.identifydevdevice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainPrefs {

    private static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1L);

    private static MainPrefs sMainPrefs;
    private SharedPreferences mDefaultPrefs;

    private MainPrefs(@NonNull Context appContext) {
        mDefaultPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public static MainPrefs getInstance(@NonNull Context context) {
        if (sMainPrefs == null) {
            sMainPrefs = new MainPrefs(context.getApplicationContext());
        }
        return sMainPrefs;
    }

    private static long getDayFromUnixEpoch() {
        return System.currentTimeMillis() / DAY_IN_MILLIS;
    }

    // NOTE: This could be a long linear file without deleting old entries.
    /** Save package name, increase the number of times it has been added for the day.
     * @return number of times package has been added today */
    public int savePackageAdded(@NonNull String packageName) {
        long day = getDayFromUnixEpoch();
        int countPackageAddedToday = mDefaultPrefs.getInt(packageName + "," + day, 0) + 1;
        mDefaultPrefs.edit().putInt(packageName + "," + day, countPackageAddedToday).apply();
        return countPackageAddedToday;
    }

    public Map<String, ?> getAll() {
        return mDefaultPrefs.getAll();
    }

}
