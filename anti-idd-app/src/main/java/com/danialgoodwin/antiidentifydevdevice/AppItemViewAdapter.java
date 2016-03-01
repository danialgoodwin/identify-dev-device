package com.danialgoodwin.antiidentifydevdevice;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppItemViewAdapter extends RecyclerView.Adapter<AppItemViewAdapter.ViewHolder> {

    private List<AppModel> mAppModels;

    public AppItemViewAdapter(List<AppModel> items) {
        mAppModels = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_model_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppModel app = mAppModels.get(position);
//        holder.mImageView = ; // TODO
        holder.mTitleView.setText(app.getPackageName()); // TODO add user-facing name in parenthesis here
        holder.mDescriptionView.setText(app.getPackageName()); // TODO add notable flags here

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AppItemViewAdapter", "onBindViewHolder()");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTitleView;
        public final TextView mDescriptionView;
        public AppModel mItem;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.list_item_image);
            mTitleView = (TextView) view.findViewById(R.id.list_item_title);
            mDescriptionView = (TextView) view.findViewById(R.id.list_item_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

}
