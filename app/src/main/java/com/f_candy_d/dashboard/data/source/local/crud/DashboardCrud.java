package com.f_candy_d.dashboard.data.source.local.crud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.source.DataSource;
import com.f_candy_d.dashboard.data.source.local.DashboardTable;
import com.f_candy_d.sqliteutils.DataConverter;
import com.f_candy_d.sqliteutils.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/2/17.
 */

public final class DashboardCrud {

    private DashboardCrud() {}

    public static long create(SQLiteDatabase db, Dashboard dashboard) {
        long id = db.insert(DashboardTable.TABLE_NAME, null, toContentValues(dashboard, false));
        // SQLiteDatabase#insert() method returns the row ID of the newly inserted row, or -1 if an error occurred.
        // See document -> https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#insert(java.lang.String, java.lang.String, android.content.ContentValues)
        return (id != -1) ? id : DataSource.INVALID_ID;
    }

    @NonNull
    public static List<Dashboard> read(SQLiteDatabase db, QueryBuilder queryBuilder) {
        Cursor cursor = queryBuilder.query(db);
        ContentValues valueMap = new ContentValues();
        Dashboard.Modifier modifier = new Dashboard.Modifier();
        ArrayList<Dashboard> dashboards = new ArrayList<>(cursor.getCount());
        boolean hasNext = cursor.moveToFirst();

        while (hasNext) {
            valueMap.clear();
            modifier.setTarget(Dashboard.createAsDefault());
            DatabaseUtils.cursorRowToContentValues(cursor, valueMap);
            dashboards.add(modifier
                    .id(valueMap.getAsLong(DashboardTable._ID))
                    .title(valueMap.getAsString(DashboardTable._TITLE))
                    .themeColor(valueMap.getAsInteger(DashboardTable._THEME_COLOR))
                    .isArchived(DataConverter.toJavaBool(valueMap.getAsInteger(DashboardTable._IS_ARCHIVED)))
                    .releaseTarget());
            hasNext = cursor.moveToNext();
        }

        return dashboards;
    }

    public static boolean update(SQLiteDatabase db, Dashboard dashboard, boolean doRollbackIfError) {
        if (dashboard.getId() == DataSource.INVALID_ID) {
            return false;
        }

        String where = new QueryBuilder()
                .whereColumnEquals(DashboardTable._ID, dashboard.getId())
                .getWhereClause();

        boolean isError = true;
        db.beginTransaction();
        try {
            final int affected = db.update(DashboardTable.TABLE_NAME, toContentValues(dashboard, true), where, null);
            // Return true if the number of affected rows is 1, otherwise rollback and return false.
            if (affected == 1 || !doRollbackIfError) {
                db.setTransactionSuccessful();
                isError = false;
            }

        } finally {
            db.endTransaction();
        }

        return !isError;
    }

    public static boolean delete(SQLiteDatabase db, Dashboard dashboard, boolean doRollbackIfError) {
        return delete(db, dashboard.getId(), doRollbackIfError);
    }

    public static boolean delete(SQLiteDatabase db, long id, boolean doRollbackIfError) {
        if (id == DataSource.INVALID_ID) {
            return false;
        }

        String where = new QueryBuilder()
                .whereColumnEquals(DashboardTable._ID, id)
                .getWhereClause();

        boolean isError = true;
        db.beginTransaction();
        try {
            // Return true if the number of affected rows is 1, otherwise rollback and return false.
            final int affected = db.delete(DashboardTable.TABLE_NAME, where, null);
            if (affected == 1 || !doRollbackIfError) {
                db.setTransactionSuccessful();
                isError = false;
            }
        } finally {
            db.endTransaction();
        }

        return !isError;
    }

    private static ContentValues toContentValues(Dashboard dashboard, boolean containId) {
        ContentValues contentValues = new ContentValues();

        if (containId) {
            contentValues.put(DashboardTable._ID, dashboard.getId());
        }

        contentValues.put(DashboardTable._TITLE, dashboard.getTitle());
        contentValues.put(DashboardTable._IS_ARCHIVED, DataConverter.toSqliteBool(dashboard.isArchived()));
        contentValues.put(DashboardTable._THEME_COLOR, dashboard.getThemeColor());

        return contentValues;
    }
}
