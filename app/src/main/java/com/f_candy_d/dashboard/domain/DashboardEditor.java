package com.f_candy_d.dashboard.domain;

import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.source.DataSource;
import com.f_candy_d.dashboard.data.source.Repository;

/**
 * Created by daichi on 9/30/17.
 */

public class DashboardEditor {

    private Dashboard mDashboard;
    private Dashboard.Modifier mDashboardModifier;
    @NonNull private SaveResultListener mSaveResultListener;

    public DashboardEditor(@NonNull SaveResultListener resultListener) {
        mSaveResultListener = resultListener;
        mDashboard = Dashboard.createAsDefault();
        mDashboardModifier = new Dashboard.Modifier(mDashboard);
    }

    public DashboardEditor(@NonNull SaveResultListener resultListener, long id) {

    }

    public void onInputTitle(String title) {
        if (title != null && title.length() == 0) {
            mDashboardModifier.title(null);
        } else {
            mDashboardModifier.title(title);
        }
    }

    public String getTitle() {
        return mDashboard.getTitle();
    }

    public void onInputThemeColor(int color) {
        mDashboardModifier.themeColor(color);
    }

    public int getThemeColor() {
        return mDashboard.getThemeColor();
    }


    /**
     * SAVE RESULT LISTENER
     * ----------------------------------------------------------------------------- */

    public interface SaveResultListener {
        void onSaveSuccessful();
        void onSaveFailed();
    }
}
