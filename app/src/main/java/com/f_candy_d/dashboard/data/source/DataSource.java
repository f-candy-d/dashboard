package com.f_candy_d.dashboard.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.dashboard.data.model.Dashboard;

import java.util.Collection;
import java.util.List;

/**
 * Created by daichi on 10/1/17.
 */

public interface DataSource {

    long INVALID_ID = -1;

    interface OperationFailedCallback {
        // Return true if you want to do rollback
        boolean onOperationFailed();
    }

    /**
     * DASHBOARD
     * ----------------------------------------------------------------------------- */

    interface LoadDashboardCallback {
        void onDashboardLoaded(@NonNull Dashboard dashboard);
        void onDataNotFound();
    }

    interface LoadDashboardsCallback {
        void onDashboardsLoaded(@NonNull List<Dashboard> dashboards);
        void onDataNotFound();
    }

    void loadDashboard(long id, @NonNull LoadDashboardCallback callback);
    void loadAllDashboards(@NonNull LoadDashboardsCallback callback);
    void saveDashboard(@NonNull Dashboard dashboard, @Nullable OperationFailedCallback callback);
    void saveDashboards(@NonNull Collection<Dashboard> dashboards, @Nullable OperationFailedCallback callback);
    void deleteDashboard(@NonNull Dashboard dashboard, @Nullable OperationFailedCallback callback);
    void deleteDashboards(@NonNull Collection<Dashboard> dashboards, @Nullable OperationFailedCallback callback);
    void deleteAllDashboards(@Nullable OperationFailedCallback callback);
}
