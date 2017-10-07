package com.f_candy_d.dashboard.data.source;

import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.model.Entity;

import java.util.List;

/**
 * Created by daichi on 10/1/17.
 */

public interface DataSource {

    long INVALID_ID = -1;

    interface ResultCallback<T extends Entity<T>> {
        void onResult(@NonNull T data);
    }

    interface ManyResultsCallback<T extends Entity<T>> {
        void onManyResults(@NonNull List<T> data);
    }

    interface OperationFailedCallback {
        void onFailed();
    }

    /**
     * DASHBOARD
     * ----------------------------------------------------------------------------- */

    // General methods
    void loadDashboard(long id, ResultCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback);
    void loadAllDashboards(ManyResultsCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback);
    void saveDashboard(@NonNull Dashboard dashboard, ResultCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback);
    void saveDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, ManyResultsCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback);
    void deleteDashboard(@NonNull Dashboard dashboard, ResultCallback<Dashboard> deleteDataCallback, OperationFailedCallback failedCallback);
    void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, ManyResultsCallback<Dashboard> deleteCallback, OperationFailedCallback failedCallback);

    // Convenience methods
    void loadDashboard(long id, ResultCallback<Dashboard> loadCallback);
    void loadAllDashboards(ManyResultsCallback<Dashboard> loadCallback);
    void saveDashboard(@NonNull Dashboard dashboard, ResultCallback<Dashboard> saveCallback);
    void saveDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, ManyResultsCallback<Dashboard> saveCallback);
    // revertIfError == true
    void saveDashboards(@NonNull List<Dashboard> dashboards, ManyResultsCallback<Dashboard> saveCallback);
    void deleteDashboard(@NonNull Dashboard dashboard, OperationFailedCallback failedCallback);
    void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, OperationFailedCallback failedCallback);
    // revertIfError == true
    void deleteDashboards(@NonNull List<Dashboard> dashboards, OperationFailedCallback failedCallback);
}
