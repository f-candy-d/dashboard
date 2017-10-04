package com.f_candy_d.dashboard.presentation.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by daichi on 9/30/17.
 */

public class ItemClickHelper<T extends RecyclerView.ViewHolder>
        implements RecyclerView.OnChildAttachStateChangeListener {

    public interface Callback<T2 extends RecyclerView.ViewHolder> {
        void onItemClick(T2 viewHolder);
        void onItemLongClick(T2 viewHolder);
    }

    private RecyclerView mRecyclerView;
    @NonNull private Callback<T> mCallback;

    public ItemClickHelper(@NonNull Callback<T> callback) {
        mCallback = callback;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        if (mRecyclerView == recyclerView) {
            return;
        }
        if (mRecyclerView != null) {
            detachFromRecyclerView();
        }
        if (recyclerView != null) {
            recyclerView.addOnChildAttachStateChangeListener(this);
        }

        mRecyclerView = recyclerView;
    }

    public void detachFromRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.removeOnChildAttachStateChangeListener(this);
            mRecyclerView = null;
        }
    }

    /**
     * RecyclerView.OnChildAttachStateChangeListener implementation
     */

    @Override
    public void onChildViewAttachedToWindow(View view) {
        view.setOnClickListener(ON_CLICK_LISTENER);
        view.setOnLongClickListener(ON_LONG_CLICK_LISTENER);
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        view.setOnClickListener(null);
        view.setOnLongClickListener(null);
    }

    @SuppressWarnings("unchecked")
    private final View.OnClickListener ON_CLICK_LISTENER = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mRecyclerView != null) {
                RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(view);
                if (vh != null) {
                    mCallback.onItemClick((T) vh);
                }
            }
        }
    };

    @SuppressWarnings("unchecked")
    private final View.OnLongClickListener ON_LONG_CLICK_LISTENER = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (mRecyclerView != null) {
                RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(view);
                if (vh != null) {
                    mCallback.onItemLongClick((T) vh);
                }
            }

            return true;
        }
    };
}