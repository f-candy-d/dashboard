package com.f_candy_d.dashboard.domain;

import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.source.DataSource;
import com.f_candy_d.dashboard.data.source.Repository;

/**
 * Created by daichi on 9/30/17.
 */

public class DashboardEditor {

    private Dashboard.Editor mDashboardEditor;
    @NonNull private SaveResultListener mSaveResultListener;

    public DashboardEditor(@NonNull SaveResultListener resultListener) {
        mSaveResultListener = resultListener;
        mDashboardEditor = new Dashboard.Editor();
    }

    public DashboardEditor(@NonNull SaveResultListener resultListener, long id) {
        mSaveResultListener = resultListener;
        Repository.getInstance().loadDashboard(id,
                new DataSource.LoadDataCallback<Dashboard>() {
                    @Override
                    public void onDataLoaded(@NonNull Dashboard data) {
                        mDashboardEditor = new Dashboard.Editor(data);
                    }
                },
                new DataSource.OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        mDashboardEditor = new Dashboard.Editor();
                    }
                });
    }

    public void onInputTitle(String title) {
        if (title != null && title.length() == 0) {
            mDashboardEditor.title(null);
        } else {
            mDashboardEditor.title(title);
        }
    }

    public String getTitle() {
        return mDashboardEditor.title();
    }

    public void onInputThemeColor(int color) {
        mDashboardEditor.themeColor(color);
    }

    public int getThemeColor() {
        return mDashboardEditor.themeColor();
    }

    public void onSave() {
        if (!mDashboardEditor.export().equals(Dashboard.createAsDefault())) {
            Repository.getInstance().saveDashboard(mDashboardEditor.export(),
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
