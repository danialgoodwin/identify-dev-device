package com.danialgoodwin.antiidentifydevdevice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Quick storage and preferences.
 */
public class AiddPrefs {

    private static final String PREFS_FILE_INFECTED_APPS = "infected_apps";

    private static AiddPrefs mAiddPrefs;
    private SharedPreferences mDefaultPrefs;
    private SharedPreferences mInfectedAppsPrefs;

    private AiddPrefs(@NonNull Context appContext) {
        mDefaultPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        mInfectedAppsPrefs = appContext.getSharedPreferences(PREFS_FILE_INFECTED_APPS, Context.MODE_PRIVATE);
    }

    /**
     * @param context the application context will be used
     * @return instance of this class
     */
    public static AiddPrefs getInstance(@NonNull Context context) {
        if (mAiddPrefs == null) {
            mAiddPrefs = new AiddPrefs(context.getApplicationContext());
        }
        return mAiddPrefs;
    }

    /** Add infected app to be saved. */
    public void addInfectedApp(@NonNull String packageName) {
        mInfectedAppsPrefs.edit().putBoolean(packageName, true).apply();
    }

    /** Return all saved infected apps. */
    @NonNull
    public List<String> getInfectedApps() {
        Map<String, ?> all = mInfectedAppsPrefs.getAll();
        List<String> infectedApps = new ArrayList<>(all.size());
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            infectedApps.add(entry.getKey());
        }
        return infectedApps;
    }

    /** Remove all saved infected apps. */
    public void clearInfectedApps() {
        mInfectedAppsPrefs.edit().clear().apply();
    }

}
