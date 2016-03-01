package com.danialgoodwin.antiidentifydevdevice;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

}
