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

    /**
     * CRUD FOR DASHBOARD
     * ----------------------------------------------------------------------------- */

    private LongSparseArray<Dashboard> mDashboardCache;

    @Override
    public void loadDashboard(long id, final LoadDataCallback<Dashboard> loadCallback, final OperationFailedCallback failedCallback) {
        Dashboard cached = mDashboardCache.get(id);
        if (mDashboardCache != null && cached != null) {
            if (loadCallback != null) {
                loadCallback.onDataLoaded(cached);
            }
            return;
        }

        mLocalDataSource.loadDashboard(id, 
                new LoadDataCallback<Dashboard>() {
                    @Override
                    public void onDataLoaded(@NonNull Dashboard data) {
                        cacheDashboard(data);
                        if (loadCallback != null) {
                            loadCallback.onDataLoaded(data);
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
    public void loadDashboard(long id, LoadDataCallback<Dashboard> loadCallback) {
        loadDashboard(id, loadCallback, null);
    }
    
    @Override
    public void loadAllDashboards(final LoadALotOfDataCallback<Dashboard> loadCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.loadAllDashboards(
                new LoadALotOfDataCallback<Dashboard>() {
                    @Override
                    public void onDataLoaded(@NonNull List<Dashboard> data) {
                        cacheDashboards(data);
                        if (loadCallback != null) {
                            loadCallback.onDataLoaded(data);
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
    public void loadAllDashboards(LoadALotOfDataCallback<Dashboard> loadCallback) {
        loadAllDashboards(loadCallback, null);
    }

    @Override
    public void saveDashboard(@NonNull final Dashboard dashboard, final SaveDataCallback<Dashboard> saveCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.saveDashboard(checkNotNull(dashboard),
                new SaveDataCallback<Dashboard>() {
                    @Override
                    public void onDataSaved(@NonNull Dashboard data) {
                        cacheDashboard(data);
                        if (saveCallback != null) {
                            saveCallback.onDataSaved(data);
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
    public void saveDashboard(@NonNull Dashboard dashboard, SaveDataCallback<Dashboard> saveCallback) {
        saveDashboard(dashboard, saveCallback, null);
    }

    @Override
    public void saveDashboards(@NonNull final List<Dashboard> dashboards, boolean revertIfError, final SaveALotOfDataCallback<Dashboard> saveCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.saveDashboards(checkNotNull(dashboards), revertIfError,
                new SaveALotOfDataCallback<Dashboard>() {
                    @Override
                    public void onDataSaved(@NonNull List<Dashboard> data) {
                        releaseCachedDashboards(data);
                        if (saveCallback != null) {
                            saveCallback.onDataSaved(data);
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
    public void saveDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, SaveALotOfDataCallback<Dashboard> saveCallback) {
        saveDashboards(dashboards, revertIfError, saveCallback, null);
    }

    @Override
    public void saveDashboards(@NonNull List<Dashboard> dashboards, SaveALotOfDataCallback<Dashboard> saveCallback) {
        saveDashboards(dashboards, true, saveCallback, null);
    }
    
    @Override
    public void deleteDashboard(@NonNull final Dashboard dashboard, final DeleteDataCallback<Dashboard> deleteDataCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.deleteDashboard(checkNotNull(dashboard),
                new DeleteDataCallback<Dashboard>() {
                    @Override
                    public void onDataDeleted(@NonNull Dashboard data) {
                        releaseCachedDashboard(dashboard);
                        if (deleteDataCallback != null) {
                            deleteDataCallback.onDataDeleted(data);
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
    public void deleteDashboards(@NonNull List<Dashboard> dashboards, boolean revertIfError, final DeleteALotOfDataCallback<Dashboard> deleteCallback, final OperationFailedCallback failedCallback) {
        mLocalDataSource.deleteDashboards(checkNotNull(dashboards), revertIfError,
                new DeleteALotOfDataCallback<Dashboard>() {
                    @Override
                    public void onDataDelete(@NonNull List<Dashboard> data) {
                        releaseCachedDashboards(data);
                        if (deleteCallback != null) {
                            deleteCallback.onDataDelete(data);
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
