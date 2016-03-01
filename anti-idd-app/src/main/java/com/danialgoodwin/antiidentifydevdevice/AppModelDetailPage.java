package com.danialgoodwin.antiidentifydevdevice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.danialgoodwin.android.SimpleMessage;
import com.danialgoodwin.android.ViewFactory;

public class AppModelDetailPage extends AppCompatActivity {

    private static final String INTENT_EXTRA_PACKAGE_NAME = "package_name";

    public static Intent getIntentToShow(@NonNull Context context, @NonNull String packageName) {
        Intent intent = new Intent(context, AppModelDetailPage.class);
        intent.putExtra(INTENT_EXTRA_PACKAGE_NAME, packageName);
        return intent;
    }

    public static void showPage(@NonNull Context context, @NonNull AppModel app) {
        Intent intent = new Intent(context, AppModelDetailPage.class);
        intent.putExtra(INTENT_EXTRA_PACKAGE_NAME, app.getPackageName());
        context.startActivity(intent);
    }

    public static void show(@NonNull Context context, @NonNull AppModel app) {
        new AlertDialog.Builder(context)
                .setTitle(app.getTitle())
                .setMessage(String.format("%s%n%s", app.getPackageName(), app.getApkPath()))
                .setView(getView(context, app))
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Open fragment
        // TODO: Dialog theme

        String packageName = getIntent().getStringExtra(INTENT_EXTRA_PACKAGE_NAME);
        SimpleMessage.showOneToast(this, "packageName=" + packageName);
//        setContentView(R.layout.activity_app_model_detail_page);
        finish();
    }



    private static View getView(@NonNull final Context context, @NonNull AppModel app) {
        final String packageName = app.getApplicationInfo().packageName;

        ViewFactory v = new ViewFactory(context);
        Button button = v.button("AppInfo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSystemAppInfoPage(context, packageName);
            }
        });
        TextView isInternetView = v.text("internet: " + app.isInternetPermissionRequested());
        TextView isRunningView = v.text("running: " + !app.isAppStopped());
        LinearLayout rootView = v.col(isInternetView, isRunningView, button);
        return rootView;
    }

    private static void showSystemAppInfoPage(Context context, String packageName) {
        Intent intent;
        try {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);
        } catch (ActivityNotFoundException ignore) {
            // Open the generic Apps page
            intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

//    private static void closeApp(Context context) {
////        activity.finish();
////        activity.finishAndRemoveTask(); // If API 21+
//        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//        homeIntent.addCategory( Intent.CATEGORY_HOME );
//        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(homeIntent);
//    }

}
