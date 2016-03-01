package com.danialgoodwin.antiidentifydevdevice;


import android.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.danialgoodwin.android.util.PackageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private Context mContext;
    private PackageUtils mPackageUtils;
    private ScanAppsTask mLoadAppListTask;
    private List<AppModel> mAppModels = new ArrayList<>();

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
                Log.d("MainFragment", "onProgress() called with: " + "progress = [" + progress + "], max = [" + max + "], app = [" + app + "]");
                if (progress == 0) {
                    mProgressBar.setMax(max);
                    mAppModels.clear();
                    mAdapter.notifyDataSetChanged();
                    mProgressTextView.setKeepScreenOn(true);
                } else if (progress < max) {
                    mProgressTextView.setText(progress + "/" + max);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    mProgressTextView.setKeepScreenOn(false);
                    mProgressTextView.setText("Found " + mAppModels .size() + ":");
                }
                if (app != null) {
                    Log.d("MainFragment", "app.getPackageName()=" + app.getPackageName());
                    mAppModels.add(app);
//                    mAdapter.notifyDataSetChanged();
                    mAdapter.notifyItemInserted(mAppModels.size() - 1);
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




//        List<ApplicationInfo> packages = mPackageUtils.getAllPackages();
//        List<AppModel> models = new ArrayList<>();
//        final int countPackages = packages.size();
//        for (int i = 0; i < countPackages; i++) {
//            ApplicationInfo info = packages.get(i);
//            models.add(new AppModel(info));
//        }
//        mAppModels = models;




        mAdapter = new AppItemViewAdapter(mAppModels);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        if (mLoadAppListTask.getStatus() == AsyncTask.Status.PENDING) {
            mLoadAppListTask.execute();
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
