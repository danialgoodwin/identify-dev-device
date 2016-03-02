package com.danialgoodwin.antiidentifydevdevice;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_MAIN_FRAGMENT = "main"; // Arbitrary unique tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = getFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);
        if (fragment == null) {
            fragment = new MainFragment();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, fragment, TAG_MAIN_FRAGMENT).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // No successful way to do this yet
//        menu.add(Menu.NONE, R.id.menu_action_send_fake_broadcast, Menu.NONE, "Send fake broadcast");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_send_fake_broadcast:
//                sendFakeBroadcast(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Note: This currently has error: `java.lang.SecurityException: Permission Denial: not allowed to send broadcast android.intent.action.PACKAGE_REPLACED`
    // because it's protected by the system. Source: http://developer.android.com/reference/android/content/Intent.html#ACTION_PACKAGE_ADDED
//    private static void sendFakeBroadcast(@NonNull Context context) {
//        Intent intent = new Intent(Intent.ACTION_PACKAGE_REPLACED);
////        intent.setScheme(); // TODO: set scheme through setData(), if needed
//        intent.putExtra(Intent.EXTRA_UID, context.getApplicationInfo().uid);
//        context.sendBroadcast(intent);
//    }

}
