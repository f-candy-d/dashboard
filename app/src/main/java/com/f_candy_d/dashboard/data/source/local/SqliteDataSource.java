package com.f_candy_d.dashboard.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.source.DataSource;
import com.f_candy_d.dashboard.data.source.local.crud.DashboardCrud;
import com.f_candy_d.sqliteutils.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/2/17.
 */

public class SqliteDataSource implements DataSource {

    private SQLiteOpenHelper mOpenHelper;

    public SqliteDataSource(@NonNull Context context) {
        mOpenHelper = new SqliteOpenHelper(context);
    }

    @Override
    public void loadDashboard(long id, LoadDataCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        QueryBuilder queryBuilder = new QueryBuilder()
                .table(DashboardTable.TABLE_NAME)
                .whereColumnEquals(DashboardTable._ID, id);

        List<Dashboard> results = DashboardCrud.read(db, queryBuilder);
        db.close();
        
        if (results.size() == 1 && loadCallback != null) {
            loadCallback.onDataLoaded(results.get(0));
        }
        if (results.size() != 1 && failedCallback != null) {
            failedCallback.onFailed();
        }
    }

    @Override
    public void loadDashboard(long id, LoadDataCallback<Dashboard> loadCallback) {
        loadDashboard(id, loadCallback, null);
    }

    @Override
    public void loadAllDashboards(LoadALotOfDataCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        QueryBuilder queryBuilder = new QueryBuilder()
                .table(DashboardTable.TABLE_NAME);

        List<Dashboard> results = DashboardCrud.read(db, queryBuilder);
        db.close();

        if (results.size() != 0 && loadCallback != null) {
            loadCallback.onDataLoaded(results);
        }
        if (results.size() == 0 && failedCallback != null) {
            failedCallback.onFailed();
        }
    }

    @Override
    public void loadAllDashboards(LoadALotOfDataCallback<Dashboard> loadCallback) {
        loadAllDashboards(loadCallback, null);
    }

    @Override
    public void saveDashboard(@NonNull Dashboard dashboard, SaveDataCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        // Update
        boolean result = DashboardCrud.update(db, dashboard, true);
        if (result) {
            db.close();
            if (saveCallback != null) {
                saveCallback.onDataSaved(dashboard);
            }
            
        } else {
            // Insert
            long id = DashboardCrud.create(db, dashboard);
            db.close();
            if (id != INVALID_ID && saveCallback != null) {
                saveCallback.onDataSaved(new Dashboard.Editor(dashboard).id(id).export());
            }
            if (id == INVALID_ID && failedCallback != null) {
                failedCallback.onFailed();
            }
        }
    }

    @Override
    public void saveDashboard(@NonNull Dashboard dashboard, SaveDataCallback<Dashboard> saveCallback) {
        saveDashboard(dashboard, saveCallback, null);
    }

    @Override
    public void saveDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, SaveALotOfDataCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        ArrayList<Dashboard> saved = new ArrayList<>(dashboards.size());
        boolean isError = false;

        db.beginTransaction();
        try {
            for (Dashboard dashboard : dashboards) {
                // Update
                if (DashboardCrud.update(db, dashboard, false)) {
                    saved.add(dashboard);

                } else {
                    // Insert
                    long id = DashboardCrud.create(db, dashboard);
                    if (id != INVALID_ID) {
                        saved.add(new Dashboard.Editor(dashboard).id(id).export());
                    } else {
                        isError = true;
                        break;
                    }
                }
            }
            
            if (!isError || !revertIfError) {
                db.setTransactionSuccessful();
            }
            
        } finally {
            db.endTransaction();
            db.close();
        }
        
        if (isError && failedCallback != null) {
            failedCallback.onFailed();
        }
        if (!isError && saveCallback != null) {
            saveCallback.onDataSaved(saved);
        }
    }

    @Override
    public void saveDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, SaveALotOfDataCallback<Dashboard> saveCallback) {
        saveDashboards(dashboards, revertIfError, saveCallback, null);
    }

    @Override
    public void saveDashboards(@NonNull List<Dashboard> dashboards, SaveALotOfDataCallback<Dashboard> saveCallback) {
        saveDashboards(dashboards, true, saveCallback, null);
    }

    @Override
    public void deleteDashboard(@NonNull Dashboard dashboard, DeleteDataCallback<Dashboard> deleteDataCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (DashboardCrud.delete(db, dashboard, true)) {
            db.close();
            if (deleteDataCallback != null) {
                deleteDataCallback.onDataDeleted(dashboard);
            }
        } else {
            if (failedCallback != null) {
                failedCallback.onFailed();
            }
        }
    }

    @Override
    public void deleteDashboard(@NonNull Dashboard dashboard, OperationFailedCallback failedCallback) {
        deleteDashboard(dashboard, null, failedCallback);
    }

    @Override
    public void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, DeleteALotOfDataCallback<Dashboard> deleteCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        boolean isError = false;

        db.beginTransaction();
        try {
            for (Dashboard dashboard : dashboards) {
                if (!DashboardCrud.delete(db, dashboard, false)) {
                    isError = true;
                    break;
                }
            }

            if (!isError || !revertIfError) {
                db.setTransactionSuccessful();
            }
            
        } finally {
            db.endTransaction();
            db.close();
        }
        
        if (isError && failedCallback != null) {
            failedCallback.onFailed();
        }
        if (!isError && deleteCallback != null) {
            deleteCallback.onDataDelete(dashboards);
        }
    }

    @Override
    public void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, OperationFailedCallback failedCallback) {
        deleteDashboards(dashboards, revertIfError, null, failedCallback);
    }

    @Override
    public void deleteDashboards(@NonNull List<Dashboard> dashboards, OperationFailedCallback failedCallback) {
        deleteDashboards(dashboards, true, null, failedCallback);
    }
}
