package com.f_candy_d.dashboard.presentation.presenter;

import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.source.DataSource;
import com.f_candy_d.dashboard.data.source.Repository;
import com.f_candy_d.dashboard.presentation.contract.HomeContract;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daichi on 10/4/17.
 */

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mView;

    @Override
    public void onStart(HomeContract.View view) {
        mView = checkNotNull(view);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        Repository.getInstance().loadAllDashboards(
                new DataSource.ManyResultsCallback<Dashboard>() {
                    @Override
                    public void onManyResults(@NonNull List<Dashboard> data) {
                        if (mView.isAvailable()) {
                            mView.replaceDashboards(data);
                        }
                    }
                },
                new DataSource.OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        if (mView.isAvailable()) {
                            mView.showLoadingDashboardsError();
                        }
                    }
                });
    }

    @Override
    public void onOpenDashboardDetails(Dashboard dashboard) {
        if (mView.isAvailable()) {
            mView.showDashboardDetailsUi(checkNotNull(dashboard).getId());
        }
    }

    @Override
    public void onCreateNewDashboard() {
        if (mView.isAvailable()) {
            mView.showCreateNewDashboardUi();
        }
    }

    @Override
    public void onArchiveDashboard(Dashboard dashboard) {
        dashboard = new Dashboard.Modifier()
                .setTarget(checkNotNull(dashboard))
                .isArchived(true)
                .releaseTarget();

        Repository.getInstance().saveDashboard(dashboard,
                new DataSource.ResultCallback<Dashboard>() {
                    @Override
                    public void onResult(@NonNull Dashboard data) {
                        if (mView.isAvailable()) {
                            mView.onDashboardArchived(data);
                        }
                    }
                }, new DataSource.OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        if (mView.isAvailable()) {
                            mView.showArchivingDashboardError();
                        }
                    }
                });
    }
}
