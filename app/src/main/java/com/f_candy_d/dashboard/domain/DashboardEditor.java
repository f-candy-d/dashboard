package com.f_candy_d.dashboard.domain;

import android.support.annotation.NonNull;

/**
 * Created by daichi on 9/30/17.
 */

public class DashboardEditor {

    private Dashboard mDashboard;
    @NonNull private SaveResultListener mSaveResultListener;

    public DashboardEditor(@NonNull SaveResultListener resultListener) {
        mSaveResultListener = resultListener;
        mDashboard = new Dashboard();
    }

    public DashboardEditor(@NonNull SaveResultListener resultListener, long id) {
        mSaveResultListener = resultListener;
        mDashboard = Dashboard.createIfPossible(id);
        if (mDashboard == null) {
            mDashboard = new Dashboard();
        }
    }

    public void onInputTitle(String title) {
        if (title != null && title.length() == 0) {
            mDashboard.setTitle(null);
        } else {
            mDashboard.setTitle(title);
        }
    }

    public String getTitle() {
        return mDashboard.getTitle();
    }

    public void onInputThemeColor(int color) {
        mDashboard.setThemeColor(color);
    }

    public int getThemeColor() {
        return mDashboard.getThemeColor();
    }

    public void onSave() {
        if (!mDashboard.isDefaultState()) {
            if (mDashboard.commit()) {
                mSaveResultListener.onSaveSuccessful();
            } else {
                mSaveResultListener.onSaveFailed();
            }
        }
    }

    /**
     * SAVE RESULT LISTENER
     * ----------------------------------------------------------------------------- */

    public interface SaveResultListener {
        void onSaveSuccessful();
        void onSaveFailed();
    }
}
