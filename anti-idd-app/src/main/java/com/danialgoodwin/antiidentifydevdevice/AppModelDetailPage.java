package com.danialgoodwin.antiidentifydevdevice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import com.danialgoodwin.android.util.PackageUtils;

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
//                .setMessage(String.format("%s%n%s", app.getPackageName(), app.getApkPath()))
                .setView(getView(context, app))
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String packageName = getIntent().getStringExtra(INTENT_EXTRA_PACKAGE_NAME);
        SimpleMessage.showOneToast(this, "packageName=" + packageName);
        ApplicationInfo appInfo = null;
        try {
            appInfo = getPackageManager().getApplicationInfo(packageName, 0);
            setContentView(getView(this, new AppModel(appInfo, PackageUtils.getInstance(this))));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            SimpleMessage.showToast(this, "Error: Package not found");
            finish();
        }
    }

    private static View getView(@NonNull final Context context, @NonNull AppModel app) {
        View rootView = View.inflate(context, R.layout.activity_app_model_detail_page, null);
        TextView packageNameView = (TextView) rootView.findViewById(R.id.package_name_view);
        TextView apkPathView = (TextView) rootView.findViewById(R.id.apk_path_view);
        TextView isInternetView = (TextView) rootView.findViewById(R.id.is_internet_view);
        TextView isRunningView = (TextView) rootView.findViewById(R.id.is_running_view);
        Button showSystemAppPageButton = (Button) rootView.findViewById(R.id.show_system_app_page_button);

        final String packageName = app.getApplicationInfo().packageName;

        packageNameView.setText(packageName);
        apkPathView.setText(app.getApkPath());
        isInternetView.setText("internet: " + app.isInternetPermissionRequested());
        isRunningView.setText("running: " + !app.isAppStopped());

        showSystemAppPageButton.setText("System app info");
        showSystemAppPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSystemAppInfoPage(context, packageName);
            }
        });

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