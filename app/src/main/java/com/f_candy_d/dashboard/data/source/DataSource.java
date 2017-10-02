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

    interface LoadDataCallback<T extends Entity> {
        void onDataLoaded(@NonNull T data);
    }

    interface LoadALotOfDataCallback<T extends Entity> {
        void onDataLoaded(@NonNull List<T> data);
    }

    interface SaveDataCallback<T extends Entity> {
        void onDataSaved(@NonNull T data);
    }

    interface SaveALotOfDataCallback<T extends Entity> {
        void onDataSaved(@NonNull List<T> data);
    }

    interface DeleteDataCallback<T extends Entity> {
        void onDataDeleted(@NonNull T data);
    }
    
    interface DeleteALotOfDataCallback<T extends Entity> {
        void onDataDelete(@NonNull List<T> data);
    }

    interface OperationFailedCallback {
        void onFailed();
    }

    /**
     * DASHBOARD
     * ----------------------------------------------------------------------------- */

    void loadDashboard(long id, LoadDataCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback);
    void loadDashboard(long id, LoadDataCallback<Dashboard> loadCallback);

    void loadAllDashboards(LoadALotOfDataCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback);
    void loadAllDashboards(LoadALotOfDataCallback<Dashboard> loadCallback);
    
    void saveDashboard(@NonNull Dashboard dashboard, SaveDataCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback);
    void saveDashboard(@NonNull Dashboard dashboard, SaveDataCallback<Dashboard> saveCallback);

    void saveDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, SaveALotOfDataCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback);
    void saveDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, SaveALotOfDataCallback<Dashboard> saveCallback);
    // revertIfError == true
    void saveDashboards(@NonNull List<Dashboard> dashboards, SaveALotOfDataCallback<Dashboard> saveCallback);

    void deleteDashboard(@NonNull Dashboard dashboard, DeleteDataCallback<Dashboard> deleteDataCallback, OperationFailedCallback failedCallback);
    void deleteDashboard(@NonNull Dashboard dashboard, OperationFailedCallback failedCallback);

    void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, DeleteALotOfDataCallback<Dashboard> deleteCallback, OperationFailedCallback failedCallback);
    void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, OperationFailedCallback failedCallback);
    // revertIfError == true
    void deleteDashboards(@NonNull List<Dashboard> dashboards, OperationFailedCallback failedCallback);
}
