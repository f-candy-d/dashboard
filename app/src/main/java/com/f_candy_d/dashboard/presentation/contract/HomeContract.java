package com.f_candy_d.dashboard.presentation.contract;

import android.os.Bundle;

import com.f_candy_d.dashboard.data.model.Dashboard;

import java.util.List;

/**
 * Created by daichi on 10/4/17.
 */

public interface HomeContract {

    interface View extends BaseView {
        void appendDashboards(List<Dashboard> dashboards);
        void replaceDashboards(List<Dashboard> newDashboards);
        void onDashboardArchived(Dashboard dashboard);
        void showArchivingDashboardError();
        void removeDashboard(Dashboard dashboard);
        void showLoadingDashboardsError();
        void showDashboardDetailsUi(long targetDashboardId);
        void showCreateNewDashboardUi();
    }

    interface Presenter extends BasePresenter<HomeContract.View> {
        void onRefresh();
        void onOpenDashboardDetails(Dashboard dashboard);
        void onCreateNewDashboard();
        void onArchiveDashboard(Dashboard dashboard);
    }
}
