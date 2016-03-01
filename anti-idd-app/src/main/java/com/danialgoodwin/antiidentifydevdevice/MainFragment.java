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
    private ScanAppsTask mLoadAppListTask;
    private List<AppModel> mInfectedApps = new ArrayList<>();

    private ProgressBar mProgressBar;
    private TextView mProgressTextView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

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
        mLoadAppListTask = new ScanAppsTask(mPackageUtils, new ScanAppsTask.OnProgressListener() {
            @Override
            public void onProgress(int progress, int max, @Nullable AppModel app) {
                if (progress == 0) {
                    mProgressBar.setMax(max);
                    mInfectedApps.clear();
                    mAdapter.notifyDataSetChanged();
                    mProgressTextView.setKeepScreenOn(true);
                } else if (progress < max) {
                    mProgressTextView.setText("Scanning: " + progress + "/" + max);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    mProgressTextView.setKeepScreenOn(false);
                    mProgressTextView.setText("Found " + mInfectedApps.size() + ":");
                }
                if (app != null) {
                    mInfectedApps.add(app);
                    mAdapter.notifyItemInserted(mInfectedApps.size() - 1);
                }
                mProgressBar.setProgress(progress);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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

        switch (mLoadAppListTask.getStatus()) {
            case PENDING:
//                mProgressTextView.setText("Loading...");
                mLoadAppListTask.execute();
                break;
            case RUNNING:
                break;
            case FINISHED:
                mProgressBar.setVisibility(View.GONE);
                mProgressTextView.setText("Found " + mInfectedApps.size() + ":");
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (mLoadAppListTask.getStatus() != AsyncTask.Status.FINISHED) {
            mLoadAppListTask.cancel(true);
        }
        super.onDestroy();
    }

}
