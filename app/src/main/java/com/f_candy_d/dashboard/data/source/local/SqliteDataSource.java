package com.f_candy_d.dashboard.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.model.Entity;
import com.f_candy_d.dashboard.data.model.TextNote;
import com.f_candy_d.dashboard.data.source.DataSource;
import com.f_candy_d.dashboard.data.source.local.table.DashboardTable;
import com.f_candy_d.dashboard.data.source.local.table.TextNoteTable;
import com.f_candy_d.sqliteutils.BaseTable;
import com.f_candy_d.sqliteutils.DataConverter;
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

    /**
     * GENERAL METHODS TO DO CRUD
     * ----------------------------------------------------------------------------- */

    private <T extends Entity<T>> void load(long id, String table, Crud.ModelCreator<T> creator, ResultCallback<T> loadCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
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

    private <T extends Entity<T>> void loadAll(String table, Crud.ModelCreator<T> creator, ManyResultsCallback<T> loadCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
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

    private <T extends Entity<T>> void save(T entity, String table, Crud.ValueMapper<T> mapper, ResultCallback<T> saveCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
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

    private <T extends Entity<T>> void saveAll(List<T> entities, String table, boolean revertIfError, Crud.ValueMapper<T> mapper, ManyResultsCallback<T> saveCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
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

    private <T extends Entity<T>> void delete(T entity, String table, ResultCallback<T> deleteDataCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
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

    private <T extends Entity<T>> void deleteAll(List<T> entities, String table, boolean revertIfError, ManyResultsCallback<T> deleteCallback, OperationFailedCallback failedCallback) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
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
     * CRUD DASHBOARD
     * ----------------------------------------------------------------------------- */

    @Override
    public void loadDashboard(long id, ResultCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback) {
        load(id, DashboardTable.TABLE_NAME, DASHBOARD_MODEL_CREATOR, loadCallback, failedCallback);
    }

    @Override
    public void loadAllDashboards(ManyResultsCallback<Dashboard> loadCallback, OperationFailedCallback failedCallback) {
        loadAll(DashboardTable.TABLE_NAME, DASHBOARD_MODEL_CREATOR, loadCallback, failedCallback);
    }

    @Override
    public void saveDashboard(@NonNull Dashboard dashboard, ResultCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback) {
        save(dashboard, DashboardTable.TABLE_NAME, DASHBOARD_VALUE_MAPPER, saveCallback, failedCallback);
    }

    @Override
    public void saveDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, ManyResultsCallback<Dashboard> saveCallback, OperationFailedCallback failedCallback) {
        saveAll(dashboards, DashboardTable.TABLE_NAME, revertIfError, DASHBOARD_VALUE_MAPPER, saveCallback, failedCallback);
    }

    @Override
    public void deleteDashboard(@NonNull Dashboard dashboard, ResultCallback<Dashboard> deleteDataCallback, OperationFailedCallback failedCallback) {
        delete(dashboard, DashboardTable.TABLE_NAME, deleteDataCallback, failedCallback);
    }

    @Override
    public void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, ManyResultsCallback<Dashboard> deleteCallback, OperationFailedCallback failedCallback) {
        deleteAll(dashboards, DashboardTable.TABLE_NAME, revertIfError, deleteCallback, failedCallback);
    }

    /**
     * CRUD TEXT_NOTE
     * ----------------------------------------------------------------------------- */

    @Override
    public void loadTextNote(long id, ResultCallback<TextNote> loadCallback, OperationFailedCallback failedCallback) {
        load(id, TextNoteTable.TABLE_NAME, TEXT_NOTE_MODEL_CREATOR, loadCallback, failedCallback);
    }

    @Override
    public void loadAllTextNotes(ManyResultsCallback<TextNote> loadCallback, OperationFailedCallback failedCallback) {
        loadAll(TextNoteTable.TABLE_NAME, TEXT_NOTE_MODEL_CREATOR, loadCallback, failedCallback);
    }

    @Override
    public void saveTextNote(@NonNull TextNote dashboard, ResultCallback<TextNote> saveCallback, OperationFailedCallback failedCallback) {
        save(dashboard, TextNoteTable.TABLE_NAME, TEXT_NOTE_VALUE_MAPPER, saveCallback, failedCallback);
    }

    @Override
    public void saveTextNotes(@NonNull List<TextNote> dashboards, boolean revertIfError, ManyResultsCallback<TextNote> saveCallback, OperationFailedCallback failedCallback) {
        saveAll(dashboards, TextNoteTable.TABLE_NAME, revertIfError, TEXT_NOTE_VALUE_MAPPER, saveCallback, failedCallback);
    }

    @Override
    public void deleteTextNote(@NonNull TextNote dashboard, ResultCallback<TextNote> deleteDataCallback, OperationFailedCallback failedCallback) {
        delete(dashboard, TextNoteTable.TABLE_NAME, deleteDataCallback, failedCallback);
    }

    @Override
    public void deleteTextNotes(@NonNull List<TextNote> dashboards, boolean revertIfError, ManyResultsCallback<TextNote> deleteCallback, OperationFailedCallback failedCallback) {
        deleteAll(dashboards, TextNoteTable.TABLE_NAME, revertIfError, deleteCallback, failedCallback);
    }

    /**
     * CRUD.MODEL_CREATOR & CRUD.VALUE_MAPPER FOR DASHBOARD
     * ----------------------------------------------------------------------------- */

    private static final Crud.ModelCreator<Dashboard> DASHBOARD_MODEL_CREATOR =
            contentValues ->
                    new Dashboard.Modifier()
                            .setTarget(Dashboard.createAsDefault())
                            .id(contentValues.getAsLong(DashboardTable._ID))
                            .title(contentValues.getAsString(DashboardTable._TITLE))
                            .themeColor(contentValues.getAsInteger(DashboardTable._THEME_COLOR))
                            .isArchived(DataConverter.toJavaBool(contentValues.getAsInteger(DashboardTable._IS_ARCHIVED)))
                            .releaseTarget();

    private static final Crud.ValueMapper<Dashboard> DASHBOARD_VALUE_MAPPER =
            (entity, containId) -> {
                ContentValues contentValues = new ContentValues();

                if (containId) {
                    contentValues.put(DashboardTable._ID, entity.getId());
                }

                contentValues.put(DashboardTable._TITLE, entity.getTitle());
                contentValues.put(DashboardTable._IS_ARCHIVED, DataConverter.toSqliteBool(entity.isArchived()));
                contentValues.put(DashboardTable._THEME_COLOR, entity.getThemeColor());

                return contentValues;
            };

    /**
     * CRUD.MODEL_CREATOR & CRUD.VALUE_MAPPER FOR TEXT_NOTE
     * ----------------------------------------------------------------------------- */

    private static final Crud.ModelCreator<TextNote> TEXT_NOTE_MODEL_CREATOR =
            contentValues ->
                    new TextNote.Modifier()
                            .setTarget(TextNote.createAsDefault())
                            .id(contentValues.getAsLong(TextNoteTable._ID))
                            .title(contentValues.getAsString(TextNoteTable._TITLE))
                            .body(contentValues.getAsString(TextNoteTable._BODY))
                            .parentBoardId(contentValues.getAsLong(TextNoteTable._PARENT_BOARD_ID))
                            .releaseTarget();

    private static final Crud.ValueMapper<TextNote> TEXT_NOTE_VALUE_MAPPER =
            (entity, containId) -> {
                ContentValues contentValues = new ContentValues();

                if (containId) {
                    contentValues.put(TextNoteTable._ID, entity.getId());
                }

                contentValues.put(TextNoteTable._TITLE, entity.getTitle());
                contentValues.put(TextNoteTable._BODY, entity.getBody());
                contentValues.put(TextNoteTable._PARENT_BOARD_ID, entity.getParentBoardId());

                return contentValues;
            };
}
