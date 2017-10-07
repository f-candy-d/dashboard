package com.f_candy_d.dashboard.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.LongSparseArray;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.source.local.SqliteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

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
        clearDashboardCache();
    }

    /**
     * CRUD FOR DASHBOARD
     * ----------------------------------------------------------------------------- */

    private LongSparseArray<Dashboard> mDashboardCache;

    @Override
    public void loadDashboard(long id, final ResultCallback<Dashboard> loadCallback, final OperationFailedCallback failedCallback) {
        Dashboard cached = mDashboardCache.get(id);
        if (mDashboardCache != null && cached != null) {
            if (loadCallback != null) {
                loadCallback.onResult(cached);
            }
            return;
        }

        mLocalDataSource.loadDashboard(id, 
                new ResultCallback<Dashboard>() {
                    @Override
                    public void onResult(@NonNull Dashboard data) {
                        cacheDashboard(data);
                        if (loadCallback != null) {
                            loadCallback.onResult(data);
                        }
                    }
                },
                new OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        if (failedCallback != null) {
                            failedCallback.onFailed();
                        }
                    }
                });
    }

    @Override
    public void loadDashboard(long id, ResultCallback<Dashboard> loadCallback) {
        loadDashboard(id, loadCallback, null);
    }
    
    @Override
    public void loadAllDashboards(final ManyResultsCallback<Dashboard> loadCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.loadAllDashboards(
                new ManyResultsCallback<Dashboard>() {
                    @Override
                    public void onManyResults(@NonNull List<Dashboard> data) {
                        cacheDashboards(data);
                        if (loadCallback != null) {
                            loadCallback.onManyResults(data);
                        }
                    }
                },
                new OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        if (failedCallback != null) {
                            failedCallback.onFailed();
                        }
                    }
                });
    }

    @Override
    public void loadAllDashboards(ManyResultsCallback<Dashboard> loadCallback) {
        loadAllDashboards(loadCallback, null);
    }

    @Override
    public void saveDashboard(@NonNull final Dashboard dashboard, final ResultCallback<Dashboard> saveCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.saveDashboard(checkNotNull(dashboard),
                new ResultCallback<Dashboard>() {
                    @Override
                    public void onResult(@NonNull Dashboard data) {
                        cacheDashboard(data);
                        if (saveCallback != null) {
                            saveCallback.onResult(data);
                        }
                    }
                },
                new OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        if (failedCallback != null) {
                            failedCallback.onFailed();
                        }
                    }
                });
    }

    @Override
    public void saveDashboard(@NonNull Dashboard dashboard, ResultCallback<Dashboard> saveCallback) {
        saveDashboard(dashboard, saveCallback, null);
    }

    @Override
    public void saveDashboards(@NonNull final List<Dashboard> dashboards, boolean revertIfError, final ManyResultsCallback<Dashboard> saveCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.saveDashboards(checkNotNull(dashboards), revertIfError,
                new ManyResultsCallback<Dashboard>() {
                    @Override
                    public void onManyResults(@NonNull List<Dashboard> data) {
                        releaseCachedDashboards(data);
                        if (saveCallback != null) {
                            saveCallback.onManyResults(data);
                        }
                    }
                },
                new OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        if (failedCallback != null) {
                            failedCallback.onFailed();
                        }
                    }
                });
    }

    @Override
    public void saveDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, ManyResultsCallback<Dashboard> saveCallback) {
        saveDashboards(dashboards, revertIfError, saveCallback, null);
    }

    @Override
    public void saveDashboards(@NonNull List<Dashboard> dashboards, ManyResultsCallback<Dashboard> saveCallback) {
        saveDashboards(dashboards, true, saveCallback, null);
    }
    
    @Override
    public void deleteDashboard(@NonNull final Dashboard dashboard, final ResultCallback<Dashboard> deleteDataCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.deleteDashboard(checkNotNull(dashboard),
                new ResultCallback<Dashboard>() {
                    @Override
                    public void onResult(@NonNull Dashboard data) {
                        releaseCachedDashboard(dashboard);
                        if (deleteDataCallback != null) {
                            deleteDataCallback.onResult(data);
                        }
                    }
                },
                new OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        if (failedCallback != null) {
                            failedCallback.onFailed();
                        }
                    }
                });
    }

    @Override
    public void deleteDashboard(@NonNull Dashboard dashboard, OperationFailedCallback failedCallback) {
        deleteDashboard(dashboard, null, failedCallback);
    }

    @Override
    public void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, final ManyResultsCallback<Dashboard> deleteCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.deleteDashboards(checkNotNull(dashboards), revertIfError,
                new ManyResultsCallback<Dashboard>() {
                    @Override
                    public void onManyResults(@NonNull List<Dashboard> data) {
                        releaseCachedDashboards(data);
                        if (deleteCallback != null) {
                            deleteCallback.onManyResults(data);
                        }
                    }
                },
                new OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        if (failedCallback != null) {
                            failedCallback.onFailed();
                        }
                    }
                });
    }

    @Override
    public void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, OperationFailedCallback failedCallback) {
        deleteDashboards(dashboards, revertIfError, null, failedCallback);
    }

    @Override
    public void deleteDashboards(@NonNull List<Dashboard> dashboards, OperationFailedCallback failedCallback) {
        deleteDashboards(dashboards, true, null, failedCallback);
    }

    private void cacheDashboard(@NonNull Dashboard dashboard) {
        if (mDashboardCache == null) {
            mDashboardCache = new LongSparseArray<>();
        }
        mDashboardCache.put(dashboard.getId(), dashboard);
    }

    private void cacheDashboards(@NonNull List<Dashboard> dashboards) {
        if (mDashboardCache == null) {
            mDashboardCache = new LongSparseArray<>();
        }

        for (Dashboard dashboard : dashboards) {
            mDashboardCache.put(dashboard.getId(), dashboard);
        }
    }

    private void releaseCachedDashboard(@NonNull Dashboard dashboard) {
        if (mDashboardCache != null) {
            mDashboardCache.remove(dashboard.getId());
            if (mDashboardCache.size() == 0) {
                mDashboardCache = null;
            }
        }
    }

    private void releaseCachedDashboards(@NonNull List<Dashboard> dashboards) {
        if (mDashboardCache != null) {
            for (Dashboard dashboard : dashboards) {
                mDashboardCache.remove(dashboard.getId());
            }
            if (mDashboardCache.size() == 0) {
                mDashboardCache = null;
            }
        }
    }

    private void clearDashboardCache() {
        if (mDashboardCache != null) {
            mDashboardCache.clear();
            mDashboardCache = null;
        }
    }
}
