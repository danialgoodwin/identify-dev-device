package com.danialgoodwin.antiidentifydevdevice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppItemViewAdapter extends RecyclerView.Adapter<AppItemViewAdapter.ViewHolder> {

    private List<AppModel> mAppModels;
    private OnItemClickListener mOnClickListener;

    public AppItemViewAdapter(List<AppModel> items, OnItemClickListener listener) {
        mAppModels = items;
        mOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_model_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AppModel app = mAppModels.get(position);
        holder.mImageView.setImageDrawable(app.getIcon());
        holder.mTitleView.setText(app.getPackageName() + " (" + app.getTitle() + ")");
        holder.mDescriptionView.setText(
                (app.isInternetPermissionRequested() ? "    internet" : "") +
                (app.isAppStopped() ? "" : "    not stopped")
        );

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onClick(app, v, holder.getAdapterPosition()); // Probably don't need the third argument
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

    public interface OnItemClickListener {
        void onClick(AppModel app, View view, int position);
    }

}
