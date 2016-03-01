package com.danialgoodwin.android.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

/** Helper methods related to Intents */
public class IntentUtils {

    private static IntentUtils mIntentUtils;
    @SuppressWarnings("FieldCanBeLocal")
    private Context mAppContext;
    private PackageManager mPackageManager;

    private IntentUtils(@NonNull Context appContext) {
        mAppContext = appContext;
        mPackageManager = appContext.getPackageManager();
    }

    /**
     *
     * @param context the application context will be used
     * @return an instance of this class
     */
    @NonNull
    public static IntentUtils getInstance(@NonNull Context context) {
        if (mIntentUtils == null) {
            mIntentUtils = new IntentUtils(context.getApplicationContext());
        }
        return mIntentUtils;
    }

    // More info: http://developer.android.com/training/basics/intents/sending.html#Verify
    /** Return true if an app can handle this intent, otherwise false. */
    public boolean isMatchingActivity(@NonNull Intent intent) {
        return !mPackageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty();
    }

}
