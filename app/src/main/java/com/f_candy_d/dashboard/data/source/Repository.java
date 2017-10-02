package com.f_candy_d.dashboard.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.source.local.SqliteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
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
    public void loadDashboard(long id, @NonNull final LoadDashboardCallback callback) {
        Dashboard cached = mDashboardCache.get(id);
        if (mDashboardCache != null && cached != null) {
            checkNotNull(callback).onDashboardLoaded(cached);
            return;
        }

        mLocalDataSource.loadDashboard(id, new LoadDashboardCallback() {
            @Override
            public void onDashboardLoaded(@NonNull Dashboard dashboard) {
                cacheDashboard(dashboard);
                checkNotNull(callback).onDashboardLoaded(dashboard);
            }

            @Override
            public void onDataNotFound() {
                checkNotNull(callback).onDataNotFound();
            }
        });
    }

    @Override
    public void loadAllDashboards(@NonNull final LoadDashboardsCallback callback) {
        mLocalDataSource.loadAllDashboards(new LoadDashboardsCallback() {
            @Override
            public void onDashboardsLoaded(@NonNull List<Dashboard> dashboards) {
                cacheDashboards(dashboards);
                checkNotNull(callback).onDashboardsLoaded(dashboards);
            }

            @Override
            public void onDataNotFound() {
                checkNotNull(callback).onDataNotFound();
            }
        });
    }

    @Override
    public void saveDashboard(@NonNull final Dashboard dashboard, @Nullable final OperationFailedCallback callback) {
        mLocalDataSource.saveDashboard(checkNotNull(dashboard), new OperationFailedCallback() {
            @Override
            public boolean onOperationFailed() {
                releaseCachedDashboard(dashboard);
                return (callback != null && callback.onOperationFailed());
            }
        });

        cacheDashboard(dashboard);
    }

    @Override
    public void saveDashboards(@NonNull final Collection<Dashboard> dashboards, @Nullable final OperationFailedCallback callback) {
        cacheDashboards(checkNotNull(dashboards));
        mLocalDataSource.saveDashboards(dashboards, new OperationFailedCallback() {
            @Override
            public boolean onOperationFailed() {
                releaseCachedDashboards(dashboards);
                return (callback != null && callback.onOperationFailed());
            }
        });
    }

    @Override
    public void deleteDashboard(@NonNull Dashboard dashboard, @Nullable final OperationFailedCallback callback) {
        releaseCachedDashboard(checkNotNull(dashboard));
        mLocalDataSource.deleteDashboard(dashboard, new OperationFailedCallback() {
            @Override
            public boolean onOperationFailed() {
                return (callback != null && callback.onOperationFailed());
            }
        });
    }

    @Override
    public void deleteDashboards(@NonNull Collection<Dashboard> dashboards, @Nullable final OperationFailedCallback callback) {
        releaseCachedDashboards(checkNotNull(dashboards));
        mLocalDataSource.deleteDashboards(dashboards, new OperationFailedCallback() {
            @Override
            public boolean onOperationFailed() {
                return (callback != null && callback.onOperationFailed());
            }
        });
    }

    @Override
    public void deleteAllDashboards(@Nullable final OperationFailedCallback callback) {
        clearDashboardCache();
        mLocalDataSource.deleteAllDashboards(new OperationFailedCallback() {
            @Override
            public boolean onOperationFailed() {
                return (callback != null && callback.onOperationFailed());
            }
        });
    }

    private void cacheDashboard(@NonNull Dashboard dashboard) {
        if (mDashboardCache == null) {
            mDashboardCache = new LongSparseArray<>();
        }
        mDashboardCache.put(dashboard.getId(), dashboard);
    }

    private void cacheDashboards(@NonNull Collection<Dashboard> dashboards) {
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

    private void releaseCachedDashboards(@NonNull Collection<Dashboard> dashboards) {
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
