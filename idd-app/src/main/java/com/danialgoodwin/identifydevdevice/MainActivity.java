package com.danialgoodwin.identifydevdevice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.danialgoodwin.android.ViewFactory;
import com.danialgoodwin.android.util.PackageUtils;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final boolean CONFIG_IS_USE_IDENTIFY_SERVICE = true;
    private static final boolean CONFIG_IS_USE_IDENTIFY_RECEIVER = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewFactory vf = new ViewFactory(this);
        Map<String, ?> allPrefs = MainPrefs.getInstance(this).getAll();

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> entry : allPrefs.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        TextView summaryView = vf.text("Entries for packages added: " + allPrefs.size());
        TextView entriesView = vf.text(sb.toString());
        setContentView(vf.scroll(vf.col(summaryView, entriesView)));

        if (CONFIG_IS_USE_IDENTIFY_SERVICE) {
            PackageUtils.getInstance(this).enableComponent(MainService.class, CONFIG_IS_USE_IDENTIFY_SERVICE);
            startService(new Intent(this, MainService.class));
        }
        if (CONFIG_IS_USE_IDENTIFY_RECEIVER) {
            PackageUtils.getInstance(this).enableComponent(MainReceiver.class, CONFIG_IS_USE_IDENTIFY_RECEIVER);
        }
    }

}
