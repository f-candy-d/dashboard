package com.f_candy_d.dashboard.presentation.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.source.DataSource;
import com.f_candy_d.dashboard.data.source.Repository;
import com.f_candy_d.dashboard.presentation.contract.EditDashboardContract;

/**
 * Created by daichi on 10/5/17.
 */

public class EditDashboardPresenter implements EditDashboardContract.Presenter {

    private EditDashboardContract.View mView;
    private Dashboard mDashboard;
    private Dashboard.Modifier mDashboardModifier;

    public EditDashboardPresenter() {
        initializeAsBlank();
    }

    public EditDashboardPresenter(long targetDashboardId) {
        Repository.getInstance().loadDashboard(targetDashboardId,
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
                        initializeAsBlank();
                    }
                });
    }

    private void initializeAsBlank() {
        mDashboard = Dashboard.createAsDefault();
        mDashboardModifier = new Dashboard.Modifier(mDashboard);
    }

    @Override
    public void onStart(EditDashboardContract.View view) {
        mView = view;
        if (mView.isAvailable()) {
            mView.showDashboardTitle(mDashboard.getTitle());
            mView.showDashboardThemeColor(mDashboard.getThemeColor());
        }
    }

    @Override
    public void onInputDashboardTitle(String title) {
        if (title == null || title.length() == 0) {
            mDashboardModifier.title(null);
        } else {
            mDashboardModifier.title(title);
        }

        if (mView.isAvailable()) {
            mView.showDashboardTitle(title);
        }
    }

    @Override
    public void onInputDashboardThemeColor(int themeColor) {
        mDashboardModifier.themeColor(themeColor);

        if (mView.isAvailable()) {
            mView.showDashboardThemeColor(themeColor);
        }
    }

    @Override
    public void onSave() {
        if (isDashboardModified()) {
            Repository.getInstance().saveDashboard(mDashboard,
                    new DataSource.SaveDataCallback<Dashboard>() {
                        @Override
                        public void onDataSaved(@NonNull Dashboard data) {
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
    public String getDashboardTitle() {
        return mDashboard.getTitle();
    }

    @Override
    public int getDashboardThemeColor() {
        return mDashboard.getThemeColor();
    }

    private boolean isDashboardModified() {
        return !mDashboard.equals(Dashboard.createAsDefault());
    }
}
