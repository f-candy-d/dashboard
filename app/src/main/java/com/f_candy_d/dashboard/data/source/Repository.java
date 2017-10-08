package com.f_candy_d.dashboard.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.model.Entity;
import com.f_candy_d.dashboard.data.model.TextNote;
import com.f_candy_d.dashboard.data.source.local.SqliteDataSource;
import com.f_candy_d.dashboard.data.source.local.table.DashboardTable;
import com.f_candy_d.dashboard.data.source.local.table.TextNoteTable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daichi on 10/1/17.
 */

public class Repository implements DataSource {

    private static Repository INSTANCE = null;

    private final DataSource mLocalDataSource;

    private Repository(@NonNull Context context) {
        mLocalDataSource = new SqliteDataSource(context);
    }

    public static Repository getInstance() {
        return checkNotNull(INSTANCE,
                "Call Repository#initializeRepository(Context) before call this method");
    }

    public static void initializeRepository(@NonNull Context context) {
        INSTANCE = new Repository(context);
    }

    public void refresh() {
        clearAllCache();
    }

    /**
     * CACHE
     * ----------------------------------------------------------------------------- */

    @NonNull private Map<String, LongSparseArray<Entity<?>>> mCaches = new HashMap<>();

    @Nullable
    @SuppressWarnings("unchecked")
    private <T extends Entity<T>> T findInCache(@NonNull String table, long id) {
        if (!mCaches.containsKey(table)) {
            return null;
        }

        return (T) mCaches.get(table).get(id, null);
    }

    private <T extends Entity<T>> void cache(@NonNull String table, @NonNull T entity) {
        if (!mCaches.containsKey(table)) {
            mCaches.put(table, new LongSparseArray<>());
        }
        mCaches.get(table).put(entity.getId(), entity);
    }

    private <T extends Entity<T>> void cacheAll(@NonNull String table, @NonNull List<T> entities) {
        if (!mCaches.containsKey(table)) {
            mCaches.put(table, new LongSparseArray<>());
        }
        for (T entity : entities) {
            mCaches.get(table).put(entity.getId(), entity);
        }
    }

    private <T extends Entity<T>> void releaseCached(@NonNull String table, @NonNull T entity) {
        if (mCaches.containsKey(table)) {
            mCaches.get(table).remove(entity.getId());
            if (mCaches.get(table).size() == 0) {
                mCaches.remove(table);
            }
        }
    }

    private <T extends Entity<T>> void releaseCachedAll(@NonNull String table, @NonNull List<T> entities) {
        if (mCaches.containsKey(table)) {
            for (T entity : entities) {
                mCaches.get(table).remove(entity.getId());
            }
            if (mCaches.get(table).size() == 0) {
                mCaches.remove(table);
            }
        }
    }

    private void clearCacheOf(@NonNull String table) {
        if (!mCaches.containsKey(table)) {
            mCaches.get(table).clear();
            mCaches.remove(table);
        }
    }

    private void clearAllCache() {
        mCaches.clear();
    }

    /**
     * CRUD FOR DASHBOARD
     * ----------------------------------------------------------------------------- */

    @Override
    public void loadDashboard(long id, final ResultCallback<Dashboard> loadCallback, final OperationFailedCallback failedCallback) {
        Dashboard cached = findInCache(DashboardTable.TABLE_NAME, id);
        if (cached != null) {
            if (loadCallback != null) {
                loadCallback.onResult(cached);
            }
            return;
        }

        mLocalDataSource.loadDashboard(id,
                (data) -> {
                    cache(DashboardTable.TABLE_NAME, data);
                    if (loadCallback != null) {
                        loadCallback.onResult(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }

    @Override
    public void loadAllDashboards(final ManyResultsCallback<Dashboard> loadCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.loadAllDashboards(
                (data) -> {
                    cacheAll(DashboardTable.TABLE_NAME, data);
                    if (loadCallback != null) {
                        loadCallback.onManyResults(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }

    @Override
    public void saveDashboard(@NonNull final Dashboard dashboard, final ResultCallback<Dashboard> saveCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.saveDashboard(checkNotNull(dashboard),
                (data) -> {
                    cache(DashboardTable.TABLE_NAME, data);
                    if (saveCallback != null) {
                        saveCallback.onResult(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }

    @Override
    public void saveDashboards(@NonNull final List<Dashboard> dashboards, boolean revertIfError, final ManyResultsCallback<Dashboard> saveCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.saveDashboards(checkNotNull(dashboards), revertIfError,
                (data) -> {
                    releaseCachedAll(DashboardTable.TABLE_NAME, data);
                    if (saveCallback != null) {
                        saveCallback.onManyResults(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }

    @Override
    public void deleteDashboard(@NonNull final Dashboard dashboard, final ResultCallback<Dashboard> deleteDataCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.deleteDashboard(checkNotNull(dashboard),
                (data) -> {
                    releaseCached(DashboardTable.TABLE_NAME, dashboard);
                    if (deleteDataCallback != null) {
                        deleteDataCallback.onResult(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }

    @Override
    public void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, final ManyResultsCallback<Dashboard> deleteCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.deleteDashboards(checkNotNull(dashboards), revertIfError,
                (data) -> {
                    releaseCachedAll(DashboardTable.TABLE_NAME, data);
                    if (deleteCallback != null) {
                        deleteCallback.onManyResults(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }

    /**
     * CRUD FOR TEXT_NOTE
     * ----------------------------------------------------------------------------- */

    @Override
    public void loadTextNote(long id, final ResultCallback<TextNote> loadCallback, final OperationFailedCallback failedCallback) {
        TextNote cached = findInCache(TextNoteTable.TABLE_NAME, id);
        if (cached != null) {
            if (loadCallback != null) {
                loadCallback.onResult(cached);
            }
            return;
        }

        mLocalDataSource.loadTextNote(id,
                (data) -> {
                    cache(TextNoteTable.TABLE_NAME, data);
                    if (loadCallback != null) {
                        loadCallback.onResult(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }

    @Override
    public void loadAllTextNotes(final ManyResultsCallback<TextNote> loadCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.loadAllTextNotes(
                (data) -> {
                    cacheAll(TextNoteTable.TABLE_NAME, data);
                    if (loadCallback != null) {
                        loadCallback.onManyResults(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }

    @Override
    public void saveTextNote(@NonNull final TextNote textNote, final ResultCallback<TextNote> saveCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.saveTextNote(checkNotNull(textNote),
                (data) -> {
                    cache(TextNoteTable.TABLE_NAME, data);
                    if (saveCallback != null) {
                        saveCallback.onResult(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }

    @Override
    public void saveTextNotes(@NonNull final List<TextNote> textNotes, boolean revertIfError, final ManyResultsCallback<TextNote> saveCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.saveTextNotes(checkNotNull(textNotes), revertIfError,
                (data) -> {
                    releaseCachedAll(TextNoteTable.TABLE_NAME, data);
                    if (saveCallback != null) {
                        saveCallback.onManyResults(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }

    @Override
    public void deleteTextNote(@NonNull final TextNote textNote, final ResultCallback<TextNote> deleteDataCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.deleteTextNote(checkNotNull(textNote),
                (data) -> {
                    releaseCached(TextNoteTable.TABLE_NAME, textNote);
                    if (deleteDataCallback != null) {
                        deleteDataCallback.onResult(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }

    @Override
    public void deleteTextNotes(@NonNull List<TextNote> textNotes, boolean revertIfError, final ManyResultsCallback<TextNote> deleteCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.deleteTextNotes(checkNotNull(textNotes), revertIfError,
                (data) -> {
                    releaseCachedAll(TextNoteTable.TABLE_NAME, data);
                    if (deleteCallback != null) {
                        deleteCallback.onManyResults(data);
                    }
                },
                () -> {
                    if (failedCallback != null) {
                        failedCallback.onFailed();
                    }
                });
    }
}
