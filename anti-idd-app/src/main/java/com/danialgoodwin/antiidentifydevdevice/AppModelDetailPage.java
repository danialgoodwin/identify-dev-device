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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danialgoodwin.android.SimpleMessage;
import com.danialgoodwin.android.SimpleStorage;
import com.danialgoodwin.android.util.IntentUtils;
import com.danialgoodwin.android.util.PackageUtils;
import com.jaredrummler.apkparser.ApkParser;
import com.jaredrummler.apkparser.model.AndroidComponent;
import com.jaredrummler.apkparser.model.IntentFilter;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class AppModelDetailPage extends AppCompatActivity {

    // Note: Using "application/xml", system showed "No apps can perform this action" and the UI looked bad
    private static final String MIME_TYPE_MANIFEST = "text/plain"; // Shows more possible apps to handle viewing compared to "text/xml" and "application/xml"
    private static final String INTENT_EXTRA_PACKAGE_NAME = "package_name";

    public static Intent getIntentToShow(@NonNull Context context, @NonNull String packageName) {
        Intent intent = new Intent(context, AppModelDetailPage.class);
        intent.putExtra(INTENT_EXTRA_PACKAGE_NAME, packageName);
        return intent;
    }

    public static void showFullPage(@NonNull Context context, @NonNull AppModel app) {
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

    private static View getView(@NonNull final Context context, @NonNull final AppModel app) {
        View rootView = View.inflate(context, R.layout.activity_app_model_detail_page, null);
        TextView packageNameView = (TextView) rootView.findViewById(R.id.package_name_view);
        TextView apkPathView = (TextView) rootView.findViewById(R.id.apk_path_view);
        TextView isInternetView = (TextView) rootView.findViewById(R.id.is_internet_view);
        TextView isRunningView = (TextView) rootView.findViewById(R.id.is_running_view);
//        final TextView receiverIntentActionsView = (TextView) rootView.findViewById(R.id.receiver_intent_actions_view);
        Button showAppManifestButton = (Button) rootView.findViewById(R.id.show_app_manifest_button);
        Button showSystemAppPageButton = (Button) rootView.findViewById(R.id.show_system_app_page_button);

        final String packageName = app.getApplicationInfo().packageName;

        packageNameView.setText(packageName);
        apkPathView.setText(app.getApkPath());
        isInternetView.setText("internet: " + app.isInternetPermissionRequested());
        isRunningView.setText("running: " + !app.isAppStopped());

        showAppManifestButton.setText("App Manifest");
        showAppManifestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApkParser apkParser = ApkParser.create(app.getApkPath());
                try {
                    String manifestXml = apkParser.getManifestXml();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_VIEW);
                    File manifestFile = SimpleStorage.newCacheFilePublicReadable(context, "manifest.xml", manifestXml);
                    if (manifestFile == null) {
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "AndroidManifest.xml");
                        sharingIntent.putExtra(Intent.EXTRA_TITLE, "AndroidManifest.xml");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, manifestXml);
                        sharingIntent.setType(MIME_TYPE_MANIFEST);
                    } else {
                        sharingIntent.setDataAndType(Uri.fromFile(manifestFile), MIME_TYPE_MANIFEST);
                    }

                    if (IntentUtils.getInstance(context).isMatchingActivity(sharingIntent)) {
                        // TODOv2: Maybe create preference to allow user to set default app to use
//                    context.startActivity(sharingIntent);
                        context.startActivity(Intent.createChooser(sharingIntent, "View using..."));
                    } else {
                        // TODOv2: Create native way to show the manifest. Meh, it really isn't needed
                        SimpleMessage.showToast(context, "Error: No apps to view manifest");

                        // Just a temp solution
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setDataAndType(Uri.fromFile(manifestFile), MIME_TYPE_MANIFEST);
                        context.startActivity(shareIntent);
                    }
                } catch (IOException e) {
                    SimpleMessage.showToast(context, "Error showing manifest");
                    e.printStackTrace();
                }
            }
        });

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
