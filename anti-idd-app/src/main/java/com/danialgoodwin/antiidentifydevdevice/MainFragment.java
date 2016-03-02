package com.danialgoodwin.antiidentifydevdevice;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.danialgoodwin.android.util.PackageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The page displaying list of apps that could potentially identify developer devices.
 */
public class MainFragment extends Fragment {

    private Context mContext;
    private PackageUtils mPackageUtils;
    private AiddPrefs mAiddPrefs;
    @Nullable private ScanAppsTask mLoadAppListTask;
    private List<AppModel> mInfectedApps = new ArrayList<>();

    private Button mScanAppsButton;
    private ProgressBar mProgressBar;
    private TextView mProgressTextView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private ScanAppsTask.OnProgressListener mOnProgressListener = new ScanAppsTask.OnProgressListener() {
        @Override
        public void onProgress(int progress, int max, @Nullable AppModel app) {
            if (progress == 0) {
//                mScanAppsButton.setVisibility(View.VISIBLE); // Maybe not?
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setMax(max);
                mInfectedApps.clear();
                mAdapter.notifyDataSetChanged();
                mAiddPrefs.clearInfectedApps();
                mProgressTextView.setKeepScreenOn(true);
            } else if (progress < max) {
                mProgressTextView.setText("Scanning: " + progress + "/" + max);
            } else {
//                mScanAppsButton.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mProgressTextView.setKeepScreenOn(false);
                mProgressTextView.setText("Found " + mInfectedApps.size() + ":");
            }
            if (app != null) {
                mInfectedApps.add(app);
                mAiddPrefs.addInfectedApp(app.getPackageName());
                mAdapter.notifyItemInserted(mInfectedApps.size() - 1);
            }
            mProgressBar.setProgress(progress);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPackageUtils = PackageUtils.getInstance(mContext);
        mAiddPrefs = AiddPrefs.getInstance(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mScanAppsButton = (Button) rootView.findViewById(R.id.scan_apps_button);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressTextView = (TextView) rootView.findViewById(R.id.progress_text);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.app_list);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new AppItemViewAdapter(mInfectedApps, new AppItemViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(AppModel app, View view, int position) {
                AppModelDetailPage.show(view.getContext(), app);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mScanAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mLoadAppListTask == null) {
                    mLoadAppListTask = new ScanAppsTask(mPackageUtils, mOnProgressListener, true);
                    mLoadAppListTask.execute();
//                }
            }
        });

        if (mLoadAppListTask == null) {
            List<String> savedInfectedApps = mAiddPrefs.getInfectedApps();
            if (!savedInfectedApps.isEmpty()) {
                mLoadAppListTask = new ScanAppsTask(mPackageUtils, mOnProgressListener, false);
                String[] infectedAppsArray = new String[savedInfectedApps.size()];
                infectedAppsArray = savedInfectedApps.toArray(infectedAppsArray);
                mLoadAppListTask.execute(infectedAppsArray);
            }
        } else {
            switch (mLoadAppListTask.getStatus()) {
                case PENDING:
//                mProgressTextView.setText("Loading...");
//                mLoadAppListTask.execute();
                    break;
                case RUNNING:
                    break;
                case FINISHED:
                    mProgressBar.setVisibility(View.GONE);
                    mProgressTextView.setText("Found " + mInfectedApps.size() + ":");
                    break;
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (mLoadAppListTask != null && mLoadAppListTask.getStatus() != AsyncTask.Status.FINISHED) {
            mLoadAppListTask.cancel(true);
        }
        super.onDestroy();
    }

}
