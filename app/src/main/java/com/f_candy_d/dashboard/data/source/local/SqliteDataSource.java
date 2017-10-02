package com.f_candy_d.dashboard.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.source.DataSource;

import java.util.Collection;

/**
 * Created by daichi on 10/2/17.
 */

public class SqliteDataSource implements DataSource {

    private SQLiteOpenHelper mOpenHelper;

    public SqliteDataSource(@NonNull Context context) {
        mOpenHelper = new SqliteOpenHelper(context);
    }

    @Override
    public void loadDashboard(long id, @NonNull LoadDashboardCallback callback) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
    }

    @Override
    public void loadAllDashboards(@NonNull LoadDashboardsCallback callback) {

    }

    @Override
    public void saveDashboard(@NonNull Dashboard dashboard, @Nullable OperationFailedCallback callback) {

    }

    @Override
    public void saveDashboards(@NonNull Collection<Dashboard> dashboards, @Nullable OperationFailedCallback callback) {

    }

    @Override
    public void deleteDashboard(@NonNull Dashboard dashboard, @Nullable OperationFailedCallback callback) {

    }

    @Override
    public void deleteDashboards(@NonNull Collection<Dashboard> dashboards, @Nullable OperationFailedCallback callback) {

    }

    @Override
    public void deleteAllDashboards(@Nullable OperationFailedCallback callback) {

    }
}
