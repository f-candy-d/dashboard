package com.f_candy_d.dashboard.presentation.presenter;

import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.source.DataSource;
import com.f_candy_d.dashboard.data.source.Repository;
import com.f_candy_d.dashboard.presentation.contract.DashboardDetailsContract;

/**
 * Created by daichi on 10/5/17.
 */

public class DashboardDetailsPresenter implements DashboardDetailsContract.Presenter {

    private DashboardDetailsContract.View mView;
    private Dashboard mDashboard;
    private Dashboard.Modifier mDashboardModifier;

    public DashboardDetailsPresenter() {
        initializeAsBlank();
    }

    public DashboardDetailsPresenter(long targetDashboardId) {
        Repository.getInstance().loadDashboard(targetDashboardId,
                new DataSource.ResultCallback<Dashboard>() {
                    @Override
                    public void onResult(@NonNull Dashboard data) {
                        mDashboard = data;
                        mDashboardModifier = new Dashboard.Modifier(mDashboard);
                    }
                },
                new DataSource.OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        initializeAsBlank();
                    }
                });
    }

    private void initializeAsBlank() {
        mDashboard = Dashboard.createAsDefault();
        mDashboardModifier = new Dashboard.Modifier(mDashboard);
    }

    @Override
    public void onStart(DashboardDetailsContract.View view) {
        mView = view;
        if (mView.isAvailable()) {
            mView.showTitle(mDashboard.getTitle());
            mView.showThemeColor(mDashboard.getThemeColor());
        }
    }

    @Override
    public void onInputTitle(String title) {
        if (title == null || title.length() == 0) {
            mDashboardModifier.title(null);
        } else {
            mDashboardModifier.title(title);
        }

        if (mView.isAvailable()) {
            mView.showTitle(title);
        }
    }

    @Override
    public void onInputThemeColor(int themeColor) {
        mDashboardModifier.themeColor(themeColor);

        if (mView.isAvailable()) {
            mView.showThemeColor(themeColor);
        }
    }

    @Override
    public void onSave() {
        if (isDashboardModified()) {
            Repository.getInstance().saveDashboard(mDashboard,
                    new DataSource.ResultCallback<Dashboard>() {
                        @Override
                        public void onResult(@NonNull Dashboard data) {
                            if (mView.isAvailable()) {
                                mView.showSaveSuccessfullyMessage();
                                mView.onCloseUi(null, true);
                            }
                        }
                    },
                    new DataSource.OperationFailedCallback() {
                        @Override
                        public void onFailed() {
                            if (mView.isAvailable()) {
                                mView.showSaveFailedMessage();
                                mView.onCloseUi(null, false);
                            }
                        }
                    });

        } else {
            if (mView.isAvailable()) {
                mView.onCloseUi(null, false);
            }
        }
    }

    @Override
    public void onEditTitle() {
        if (mView.isAvailable()) {
            mView.showTitleEditor(mDashboard.getTitle());
        }
    }

    @Override
    public void onEditThemeColor() {
        if (mView.isAvailable()) {
            mView.showThemeColorEditor(mDashboard.getThemeColor());
        }
    }

    private boolean isDashboardModified() {
        return !mDashboard.equals(Dashboard.createAsDefault());
    }
}
