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
        mSaveResultListener = resultListener;
        Repository.getInstance().loadDashboard(id,
                new DataSource.LoadDataCallback<Dashboard>() {
                    @Override
                    public void onDataLoaded(@NonNull Dashboard data) {
                        mDashboard = data;
                        mDashboardModifier = new Dashboard.Modifier(mDashboard);
                    }
                },
                new DataSource.OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        mDashboard = Dashboard.createAsDefault();
                        mDashboardModifier = new Dashboard.Modifier(mDashboard);
                    }
                });
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

    public void onSave() {
        if (!mDashboard.equals(Dashboard.createAsDefault())) {
            Repository.getInstance().saveDashboard(mDashboard,
                    new DataSource.SaveDataCallback<Dashboard>() {
                        @Override
                        public void onDataSaved(@NonNull Dashboard data) {
                            mSaveResultListener.onSaveSuccessful();
                        }
                    },
                    new DataSource.OperationFailedCallback() {
                        @Override
                        public void onFailed() {
                            mSaveResultListener.onSaveFailed();
                        }
                    });
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
