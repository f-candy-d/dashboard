package com.f_candy_d.dashboard.data.source;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.model.Entity;
import com.f_candy_d.dashboard.data.source.local.crud.Crud;
import com.f_candy_d.sqliteutils.BaseTable;
import com.f_candy_d.sqliteutils.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/1/17.
 */

public abstract class DataSource {

    public static final long INVALID_ID = -1;

    public interface ResultCallback<T extends Entity<T>> {
        void onResult(@NonNull T data);
    }

    public interface ManyResultsCallback<T extends Entity<T>> {
        void onManyResults(@NonNull List<T> data);
    }

    public interface OperationFailedCallback {
        void onFailed();
    }

    /**
     * GENERAL METHODS TO DO CRUD
     * ----------------------------------------------------------------------------- */

    protected static <T extends Entity<T>> void load(SQLiteOpenHelper openHelper,
                                           long id, String table,
                                            Crud.ModelCreator<T> creator,
                                            ResultCallback<T> loadCallback,
                                            OperationFailedCallback failedCallback) {

        SQLiteDatabase db = openHelper.getReadableDatabase();
        QueryBuilder queryBuilder = new QueryBuilder()
                .table(table)
                .whereColumnEquals(BaseTable._ID, id);

        List<T> results = Crud.read(db, queryBuilder, creator);
        db.close();

        if (results.size() == 1 && loadCallback != null) {
            loadCallback.onResult(results.get(0));
        }
        if (results.size() != 1 && failedCallback != null) {
            failedCallback.onFailed();
        }
    }

    protected static <T extends Entity<T>> void loadAll(SQLiteOpenHelper openHelper, String table,
                                               Crud.ModelCreator<T> creator,
                                               ManyResultsCallback<T> loadCallback,
                                               OperationFailedCallback failedCallback) {

        SQLiteDatabase db = openHelper.getReadableDatabase();
        QueryBuilder queryBuilder = new QueryBuilder().table(table);

        List<T> results = Crud.read(db, queryBuilder, creator);
        db.close();

        if (results.size() != 0 && loadCallback != null) {
            loadCallback.onManyResults(results);
        }
        if (results.size() == 0 && failedCallback != null) {
            failedCallback.onFailed();
        }
    }

    protected static <T extends Entity<T>> void save(SQLiteOpenHelper openHelper,
                                           T entity, String table,
                                            Crud.ValueMapper<T> mapper,
                                            ResultCallback<T> saveCallback,
                                            OperationFailedCallback failedCallback) {

        SQLiteDatabase db = openHelper.getWritableDatabase();
        // Update
        boolean result = Crud.update(db, entity, table, true, mapper);
        if (result) {
            db.close();
            if (saveCallback != null) {
                saveCallback.onResult(entity);
            }

        } else {
            // Insert
            long id = Crud.create(db, entity, table, mapper);
            db.close();
            if (id != INVALID_ID && saveCallback != null) {
                saveCallback.onResult(new Entity.Modifier<>(entity).id(id).releaseTarget());
            }
            if (id == INVALID_ID && failedCallback != null) {
                failedCallback.onFailed();
            }
        }
    }

    protected static <T extends Entity<T>> void saveAll(SQLiteOpenHelper openHelper,
                                              List<T> entities, String table,
                                               boolean revertIfError,
                                               Crud.ValueMapper<T> mapper,
                                               ManyResultsCallback<T> saveCallback,
                                               OperationFailedCallback failedCallback) {

        SQLiteDatabase db = openHelper.getWritableDatabase();
        ArrayList<T> saved = new ArrayList<>(entities.size());
        boolean isError = false;

        db.beginTransaction();
        try {
            for (T entity : entities) {
                // Update
                if (Crud.update(db, entity, table, revertIfError, mapper)) {
                    saved.add(entity);

                } else {
                    // Insert
                    long id = Crud.create(db, entity, table, mapper);
                    if (id != INVALID_ID) {
                        saved.add(new Entity.Modifier<>(entity).id(id).releaseTarget());
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
            saveCallback.onManyResults(saved);
        }
    }

    protected static <T extends Entity<T>> void delete(SQLiteOpenHelper openHelper,
                                             T entity, String table,
                                             ResultCallback<T> deleteDataCallback,
                                             OperationFailedCallback failedCallback) {

        SQLiteDatabase db = openHelper.getWritableDatabase();
        if (Crud.delete(db, entity, table, true)) {
            db.close();
            if (deleteDataCallback != null) {
                deleteDataCallback.onResult(entity);
            }
        } else {
            if (failedCallback != null) {
                failedCallback.onFailed();
            }
        }
    }

    protected static <T extends Entity<T>> void deleteAll(SQLiteOpenHelper openHelper,
                                                List<T> entities, String table,
                                                 boolean revertIfError,
                                                 ManyResultsCallback<T> deleteCallback,
                                                 OperationFailedCallback failedCallback) {

        SQLiteDatabase db = openHelper.getWritableDatabase();
        boolean isError = false;

        db.beginTransaction();
        try {
            for (T entity : entities) {
                if (!Crud.delete(db, entity, table, false)) {
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
            deleteCallback.onManyResults(entities);
        }
    }

    /**
     * DASHBOARD
     * ----------------------------------------------------------------------------- */

    // General methods
    abstract public void loadDashboard(long id, ResultCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback);
    abstract public void loadAllDashboards(ManyResultsCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback);
    abstract public void saveDashboard(@NonNull Dashboard dashboard, ResultCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback);
    abstract public void saveDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, ManyResultsCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback);
    abstract public void deleteDashboard(@NonNull Dashboard dashboard, ResultCallback<Dashboard> deleteDataCallback, OperationFailedCallback failedCallback);
    abstract public void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, ManyResultsCallback<Dashboard> deleteCallback, OperationFailedCallback failedCallback);
}
