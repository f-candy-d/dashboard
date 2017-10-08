package com.f_candy_d.dashboard.data.source;

import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.model.Entity;
import com.f_candy_d.dashboard.data.model.TextNote;

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

    void loadDashboard(long id, ResultCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback);
    void loadAllDashboards(ManyResultsCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback);
    void saveDashboard(@NonNull Dashboard dashboard, ResultCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback);
    void saveDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, ManyResultsCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback);
    void deleteDashboard(@NonNull Dashboard dashboard, ResultCallback<Dashboard> deleteDataCallback, OperationFailedCallback failedCallback);
    void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, ManyResultsCallback<Dashboard> deleteCallback, OperationFailedCallback failedCallback);

    /**
     * TEXT_NOTE
     * ----------------------------------------------------------------------------- */

    void loadTextNote(long id, ResultCallback<TextNote> loadCallback, OperationFailedCallback failedCallback);
    void loadAllTextNotes(ManyResultsCallback<TextNote> loadCallback, OperationFailedCallback failedCallback);
    void saveTextNote(@NonNull TextNote dashboard, ResultCallback<TextNote> saveCallback, OperationFailedCallback failedCallback);
    void saveTextNotes(@NonNull List<TextNote> dashboards, boolean revertIfError, ManyResultsCallback<TextNote> saveCallback, OperationFailedCallback failedCallback);
    void deleteTextNote(@NonNull TextNote dashboard, ResultCallback<TextNote> deleteDataCallback, OperationFailedCallback failedCallback);
    void deleteTextNotes(@NonNull List<TextNote> dashboards, boolean revertIfError, ManyResultsCallback<TextNote> deleteCallback, OperationFailedCallback failedCallback);
}
